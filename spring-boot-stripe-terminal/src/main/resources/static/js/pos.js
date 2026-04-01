(function () {
    'use strict';

    var API_BASE = '/api/terminal';
    var selectedReaderId = null;
    var pollingTimer = null;

    // ==================== 工具函数 ====================

    function formatTime() {
        var d = new Date();
        return d.toLocaleTimeString('en-US', {hour12: false}) + '.' + String(d.getMilliseconds()).padStart(3, '0');
    }

    function appendLog(msg, level) {
        var container = document.getElementById('logContainer');
        var entry = document.createElement('div');
        entry.className = 'log-entry log-' + (level || 'info');
        entry.innerHTML = '<span class="log-time">[' + formatTime() + ']</span> ' + msg;
        container.appendChild(entry);
        container.scrollTop = container.scrollHeight;
    }

    function apiCall(method, path, body) {
        var opts = {
            method: method,
            headers: {'Content-Type': 'application/json'}
        };
        if (body) {
            opts.body = JSON.stringify(body);
        }
        return fetch(API_BASE + path, opts)
            .then(function (res) { return res.json(); })
            .then(function (data) {
                if (data.code !== 200) {
                    throw new Error(data.message || 'API Error');
                }
                return data.data;
            });
    }

    function setPaymentStatus(text, statusClass) {
        var el = document.getElementById('paymentStatus');
        el.className = 'payment-status' + (statusClass ? ' ' + statusClass : '');
        el.querySelector('.status-info').textContent = text;
    }

    // ==================== Location管理 ====================

    function loadLocations() {
        appendLog('加载Location列表...');
        apiCall('GET', '/locations')
            .then(function (data) {
                var list = data.data || [];
                var container = document.getElementById('locationList');
                container.innerHTML = '';
                if (list.length === 0) {
                    container.innerHTML = '<div class="list-item"><span class="item-detail">暂无Location, 请先创建</span></div>';
                    return;
                }
                list.forEach(function (loc) {
                    var item = document.createElement('div');
                    item.className = 'list-item';
                    item.innerHTML = '<div class="item-title">' + (loc.displayName || loc.display_name || loc.id) + '</div>'
                        + '<div class="item-detail">ID: ' + loc.id + '</div>';
                    item.onclick = function () {
                        document.getElementById('regLocationId').value = loc.id;
                        appendLog('已选择Location: ' + loc.id);
                    };
                    container.appendChild(item);
                });
                appendLog('Location列表加载完成, 共 ' + list.length + ' 个', 'success');
            })
            .catch(function (e) { appendLog('加载Location失败: ' + e.message, 'error'); });
    }

    function createLocation() {
        var body = {
            displayName: document.getElementById('locDisplayName').value,
            addressLine1: document.getElementById('locLine1').value,
            city: document.getElementById('locCity').value,
            state: document.getElementById('locState').value,
            country: document.getElementById('locCountry').value,
            postalCode: document.getElementById('locPostalCode').value
        };
        appendLog('创建Location: ' + body.displayName);
        apiCall('POST', '/locations', body)
            .then(function (loc) {
                appendLog('Location创建成功: ' + loc.id, 'success');
                loadLocations();
            })
            .catch(function (e) { appendLog('创建Location失败: ' + e.message, 'error'); });
    }

    // ==================== Reader管理 ====================

    function loadReaders() {
        appendLog('加载读卡器列表...');
        apiCall('GET', '/readers')
            .then(function (data) {
                var list = data.data || [];
                var container = document.getElementById('readerList');
                container.innerHTML = '';
                if (list.length === 0) {
                    container.innerHTML = '<div class="list-item"><span class="item-detail">暂无读卡器</span></div>';
                    return;
                }
                list.forEach(function (reader) {
                    var item = document.createElement('div');
                    var isOnline = reader.status === 'online';
                    var isSelected = reader.id === selectedReaderId;
                    item.className = 'list-item' + (isSelected ? ' selected' : '');
                    item.innerHTML = '<div class="item-title">' + (reader.label || reader.id) +
                        ' <span class="item-status ' + (isOnline ? 'online' : 'offline') + '">' +
                        reader.status + '</span></div>' +
                        '<div class="item-detail">ID: ' + reader.id + '</div>' +
                        '<div class="item-detail">类型: ' + (reader.deviceType || reader.device_type || 'unknown') + '</div>';
                    item.onclick = function () { selectReader(reader); };
                    container.appendChild(item);
                });
                appendLog('读卡器列表加载完成, 共 ' + list.length + ' 个', 'success');
            })
            .catch(function (e) { appendLog('加载读卡器失败: ' + e.message, 'error'); });
    }

    function selectReader(reader) {
        selectedReaderId = reader.id;
        var el = document.getElementById('selectedReader');
        el.className = 'selected-reader active';
        el.innerHTML = '<div><strong>' + (reader.label || reader.id) + '</strong>' +
            '<br/><span style="font-size:12px;color:#888">ID: ' + reader.id +
            ' | 类型: ' + (reader.deviceType || reader.device_type || 'unknown') +
            ' | 状态: ' + reader.status + '</span></div>';

        var isSimulated = (reader.deviceType || reader.device_type || '').indexOf('simulated') >= 0;
        document.getElementById('simulateSection').style.display = isSimulated ? 'block' : 'none';

        appendLog('已选择读卡器: ' + reader.id + ' (' + (reader.label || '') + ')', 'success');
        loadReaders();
    }

    function registerReader() {
        var body = {
            registrationCode: document.getElementById('regCode').value,
            label: document.getElementById('regLabel').value,
            locationId: document.getElementById('regLocationId').value
        };
        if (!body.registrationCode) {
            appendLog('请输入配对码', 'warn');
            return;
        }
        appendLog('注册读卡器: ' + body.label);
        apiCall('POST', '/readers', body)
            .then(function (reader) {
                appendLog('读卡器注册成功: ' + reader.id, 'success');
                loadReaders();
            })
            .catch(function (e) { appendLog('注册读卡器失败: ' + e.message, 'error'); });
    }

    function createSimulatedReader() {
        var locationId = document.getElementById('regLocationId').value;
        appendLog('创建模拟读卡器...');
        apiCall('POST', '/readers/simulated?locationId=' + encodeURIComponent(locationId || ''))
            .then(function (reader) {
                appendLog('模拟读卡器创建成功: ' + reader.id, 'success');
                loadReaders();
            })
            .catch(function (e) { appendLog('创建模拟读卡器失败: ' + e.message, 'error'); });
    }

    // ==================== 支付操作 ====================

    function createPayment() {
        if (!selectedReaderId) {
            appendLog('请先选择读卡器', 'warn');
            return;
        }
        var amountDollars = parseFloat(document.getElementById('payAmount').value);
        if (isNaN(amountDollars) || amountDollars < 0.5) {
            appendLog('请输入有效金额(至少$0.50)', 'warn');
            return;
        }
        var body = {
            amount: Math.round(amountDollars * 100),
            currency: 'usd',
            description: document.getElementById('payDescription').value,
            referenceId: document.getElementById('payReferenceId').value,
            readerId: selectedReaderId,
            captureMethod: document.getElementById('payCaptureMethod').value
        };

        appendLog('发起支付: $' + amountDollars.toFixed(2) + ' 到读卡器 ' + selectedReaderId);
        setPaymentStatus('正在发送支付指令到读卡器...', 'processing');

        apiCall('POST', '/payments', body)
            .then(function (reader) {
                var action = reader.action || {};
                var piId = '';
                if (action.processPaymentIntent || action.process_payment_intent) {
                    var ppi = action.processPaymentIntent || action.process_payment_intent || {};
                    piId = ppi.paymentIntent || ppi.payment_intent || '';
                }
                appendLog('支付指令已发送, 等待客户刷卡... (actionStatus: ' + (action.status || 'unknown') + ')', 'success');
                setPaymentStatus('等待客户在S710上刷卡/插卡/贴卡...', 'processing');

                if (piId) {
                    document.getElementById('capturePaymentIntentId').value = piId;
                }

                startPollingReaderStatus();
            })
            .catch(function (e) {
                appendLog('发起支付失败: ' + e.message, 'error');
                setPaymentStatus('支付失败: ' + e.message, 'failed');
            });
    }

    function capturePayment() {
        var piId = document.getElementById('capturePaymentIntentId').value;
        if (!piId) {
            appendLog('请输入PaymentIntent ID', 'warn');
            return;
        }
        appendLog('手动Capture支付: ' + piId);
        apiCall('POST', '/payments/capture', {paymentIntentId: piId})
            .then(function (pi) {
                appendLog('Capture成功! 状态: ' + pi.status, 'success');
                setPaymentStatus('支付完成! PaymentIntent: ' + pi.id + ' 状态: ' + pi.status, 'success');
            })
            .catch(function (e) {
                appendLog('Capture失败: ' + e.message, 'error');
                setPaymentStatus('Capture失败: ' + e.message, 'failed');
            });
    }

    function checkPaymentStatus() {
        var piId = document.getElementById('capturePaymentIntentId').value;
        if (!piId) {
            appendLog('请输入PaymentIntent ID', 'warn');
            return;
        }
        appendLog('查询PaymentIntent状态: ' + piId);
        apiCall('GET', '/payments/' + piId)
            .then(function (pi) {
                appendLog('PaymentIntent状态: ' + pi.status + ' (amount: ' + pi.amount + ')', 'info');
                if (pi.status === 'requires_capture') {
                    setPaymentStatus('已授权, 待Capture. PI: ' + pi.id, 'processing');
                } else if (pi.status === 'succeeded') {
                    setPaymentStatus('支付已完成! PI: ' + pi.id, 'success');
                } else {
                    setPaymentStatus('状态: ' + pi.status + ' PI: ' + pi.id, '');
                }
            })
            .catch(function (e) { appendLog('查询状态失败: ' + e.message, 'error'); });
    }

    function cancelAction() {
        if (!selectedReaderId) {
            appendLog('请先选择读卡器', 'warn');
            return;
        }
        appendLog('取消读卡器当前操作: ' + selectedReaderId);
        apiCall('POST', '/readers/cancel-action', {readerId: selectedReaderId})
            .then(function () {
                appendLog('操作已取消', 'success');
                setPaymentStatus('操作已取消', '');
                stopPolling();
            })
            .catch(function (e) { appendLog('取消操作失败: ' + e.message, 'error'); });
    }

    function simulatePayment() {
        if (!selectedReaderId) {
            appendLog('请先选择读卡器', 'warn');
            return;
        }
        appendLog('模拟刷卡: ' + selectedReaderId);
        apiCall('POST', '/readers/simulate-payment', {readerId: selectedReaderId})
            .then(function () {
                appendLog('模拟刷卡成功, 支付处理中...', 'success');
            })
            .catch(function (e) { appendLog('模拟刷卡失败: ' + e.message, 'error'); });
    }

    // ==================== 轮询读卡器状态 ====================

    function startPollingReaderStatus() {
        stopPolling();
        var count = 0;
        pollingTimer = setInterval(function () {
            count++;
            if (!selectedReaderId || count > 60) {
                stopPolling();
                return;
            }
            apiCall('GET', '/readers/' + selectedReaderId)
                .then(function (reader) {
                    var action = reader.action || {};
                    var status = action.status || '';
                    appendLog('轮询读卡器状态: ' + status + ' (第' + count + '次)');

                    if (status === 'succeeded') {
                        setPaymentStatus('授权成功! 请进行Capture操作', 'success');
                        appendLog('读卡器操作成功!', 'success');
                        stopPolling();
                    } else if (status === 'failed') {
                        var failMsg = action.failureMessage || action.failure_message || '未知错误';
                        setPaymentStatus('支付失败: ' + failMsg, 'failed');
                        appendLog('读卡器操作失败: ' + failMsg, 'error');
                        stopPolling();
                    }
                })
                .catch(function () {});
        }, 2000);
    }

    function stopPolling() {
        if (pollingTimer) {
            clearInterval(pollingTimer);
            pollingTimer = null;
        }
    }

    // ==================== 初始化 ====================

    function init() {
        document.getElementById('btnListLocations').onclick = loadLocations;
        document.getElementById('btnCreateLocation').onclick = createLocation;
        document.getElementById('btnListReaders').onclick = loadReaders;
        document.getElementById('btnRegisterReader').onclick = registerReader;
        document.getElementById('btnCreateSimulated').onclick = createSimulatedReader;
        document.getElementById('btnCreatePayment').onclick = createPayment;
        document.getElementById('btnCapturePayment').onclick = capturePayment;
        document.getElementById('btnCheckStatus').onclick = checkPaymentStatus;
        document.getElementById('btnCancelAction').onclick = cancelAction;
        document.getElementById('btnSimulatePayment').onclick = simulatePayment;
        document.getElementById('btnClearLog').onclick = function () {
            document.getElementById('logContainer').innerHTML = '';
        };

        appendLog('POS系统初始化完成, Stripe Terminal S710 Server-Driven集成', 'success');

        var dot = document.querySelector('.status-dot');
        var statusText = document.getElementById('statusText');
        dot.className = 'status-dot online';
        statusText.textContent = '系统就绪';
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

})();
