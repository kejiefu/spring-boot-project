(function () {
    'use strict';

    var API_BASE = '/api/terminal';
    var terminal = null;
    /** SDK discoverReaders 返回的读卡器列表（不请求后端 /readers） */
    var discoveredReaders = [];

    function log(msg, level) {
        var c = document.getElementById('sdkLogContainer');
        var d = new Date();
        var e = document.createElement('div');
        e.className = 'log-entry log-' + (level || 'info');
        e.innerHTML = '<span class="log-time">[' + d.toLocaleTimeString('en-US', {hour12: false}) + ']</span> ' + msg;
        c.appendChild(e);
        c.scrollTop = c.scrollHeight;
    }

    function setStatus(text, online) {
        document.getElementById('sdkStatusText').textContent = text;
        document.getElementById('sdkStatusDot').className = 'status-dot ' + (online ? 'online' : 'offline');
    }

    function setPayStatus(text, klass) {
        var el = document.getElementById('sdkPayStatus');
        el.className = 'payment-status' + (klass ? ' ' + klass : '');
        el.querySelector('.status-info').textContent = text;
    }

    function apiCall(method, path, body) {
        var opts = {method: method, headers: {'Content-Type': 'application/json'}};
        if (body) {
            opts.body = JSON.stringify(body);
        }
        return fetch(API_BASE + path, opts)
            .then(function (r) { return r.json(); })
            .then(function (res) {
                if (res.code !== 200) {
                    throw new Error(res.message || 'API error');
                }
                return res.data;
            });
    }

    function fetchConnectionToken() {
        return apiCall('POST', '/connection-token').then(function (res) {
            return res.secret;
        });
    }

    function loadSdkConfig() {
        return apiCall('GET', '/sdk/config').then(function (cfg) {
            if (!cfg) {
                return;
            }
            var locationEl = document.getElementById('sdkLocationId');
            var simulatedEl = document.getElementById('sdkSimulatedMode');
            if (locationEl && !locationEl.value && cfg.defaultLocationId) {
                locationEl.value = cfg.defaultLocationId;
            }
            if (simulatedEl && typeof cfg.simulated === 'boolean') {
                simulatedEl.checked = cfg.simulated;
            }
            log('已加载SDK默认配置: simulated=' + simulatedEl.checked
                + ', location=' + (locationEl.value || '(empty)'));
        }).catch(function (e) {
            log('加载SDK默认配置失败: ' + e.message, 'warn');
        });
    }

    function initSdk() {
        if (!window.StripeTerminal) {
            log('Stripe Terminal JS SDK 未加载', 'error');
            return;
        }
        terminal = window.StripeTerminal.create({
            onFetchConnectionToken: fetchConnectionToken,
            onUnexpectedReaderDisconnect: function () {
                setStatus('读卡器断开', false);
                log('读卡器意外断开', 'warn');
                renderConnectedReader(null);
            }
        });
        setStatus('SDK 已初始化', true);
        log('SDK 初始化成功', 'success');
    }

    function discoverReaders() {
        if (!terminal) {
            log('请先初始化SDK', 'warn');
            return;
        }
        var simulated = document.getElementById('sdkSimulatedMode').checked;
        var locationId = (document.getElementById('sdkLocationId').value || '').trim();
        var discoverConfig = {simulated: simulated};
        if (locationId) {
            discoverConfig.location = locationId;
        }
        log('开始发现读卡器... simulated=' + simulated + (locationId ? (', location=' + locationId) : ', location=(empty)'));
        terminal.discoverReaders(discoverConfig)
            .then(function (result) {
                if (result.error) {
                    throw new Error(result.error.message);
                }
                var list = result.discoveredReaders || result.readers || [];
                discoveredReaders = Array.isArray(list) ? list : [];
                if (discoveredReaders.length > 0) {
                    discoveredReaders.forEach(function (r) {
                        log('发现读卡器: id=' + r.id + ', label=' + (r.label || '-') + ', status=' + (r.status || '-'));
                    });
                    log('共 ' + discoveredReaders.length + ' 台', 'success');
                } else {
                    log('SDK discoverReaders 未返回读卡器（可试模拟模式或填写 Location ID）', 'warn');
                }
                renderReaderList();
            })
            .catch(function (e) { log('发现读卡器失败: ' + e.message, 'error'); });
    }

    function connectReader(reader) {
        terminal.connectReader(reader).then(function (result) {
            if (result.error) {
                throw new Error(result.error.message);
            }
            renderConnectedReader(result.reader);
            setStatus('读卡器已连接', true);
            log('读卡器连接成功: ' + (result.reader.label || result.reader.id), 'success');
        }).catch(function (e) {
            log('连接读卡器失败: ' + e.message, 'error');
        });
    }

    function disconnectReader() {
        if (!terminal) {
            return;
        }
        terminal.disconnectReader().then(function () {
            renderConnectedReader(null);
            setStatus('读卡器已断开', false);
            log('读卡器已断开', 'warn');
        });
    }

    function renderReaderList() {
        var c = document.getElementById('sdkReaderList');
        c.innerHTML = '';
        if (!discoveredReaders.length) {
            c.innerHTML = '<div class="list-item"><span class="item-detail">未发现读卡器。可勾选「模拟」测试，或为实体机填写 Location ID 后重试。</span></div>';
            return;
        }
        discoveredReaders.forEach(function (reader) {
            var item = document.createElement('div');
            item.className = 'list-item';
            var title = reader.label || reader.serial_number || reader.serialNumber || reader.id || 'reader';
            var status = reader.status ? ('<div class="item-detail">状态: ' + reader.status + '</div>') : '';
            item.innerHTML = '<div class="item-title">' + title + '</div>'
                + '<div class="item-detail">ID: ' + reader.id + '</div>'
                + status;
            item.onclick = function () { connectReader(reader); };
            c.appendChild(item);
        });
    }

    function renderConnectedReader(reader) {
        var c = document.getElementById('sdkConnectedReader');
        if (!reader) {
            c.className = 'selected-reader';
            c.innerHTML = '<span class="placeholder">暂无</span>';
            return;
        }
        c.className = 'selected-reader active';
        c.innerHTML = '<div><strong>' + (reader.label || reader.id) + '</strong><br/><span style="font-size:12px;color:#888">ID: '
            + reader.id + '</span></div>';
    }

    function sdkPay() {
        if (!terminal || terminal.getConnectionStatus() !== 'connected') {
            log('请先连接读卡器', 'warn');
            return;
        }
        var amount = Math.round(parseFloat(document.getElementById('sdkAmount').value || '0') * 100);
        var body = {
            amount: amount,
            currency: 'usd',
            description: document.getElementById('sdkDescription').value,
            referenceId: document.getElementById('sdkReferenceId').value,
            captureMethod: document.getElementById('sdkCaptureMethod').value
        };
        setPayStatus('创建 PaymentIntent...', 'processing');
        apiCall('POST', '/payments/intents', body).then(function (pi) {
            var clientSecret = pi.client_secret;
            document.getElementById('sdkPaymentIntentId').value = pi.id;
            log('已创建 PaymentIntent: ' + pi.id);
            setPayStatus('请在读卡器上刷卡...', 'processing');
            return terminal.collectPaymentMethod(clientSecret);
        }).then(function (result) {
            if (result.error) {
                throw new Error(result.error.message);
            }
            log('已收集付款方式，处理中...');
            return terminal.processPayment(result.paymentIntent);
        }).then(function (result) {
            if (result.error) {
                throw new Error(result.error.message);
            }
            var status = result.paymentIntent.status;
            log('处理完成, PaymentIntent 状态: ' + status, 'success');
            if (status === 'requires_capture') {
                setPayStatus('授权成功，待Capture', 'success');
            } else if (status === 'succeeded') {
                setPayStatus('支付成功', 'success');
            } else {
                setPayStatus('状态: ' + status, '');
            }
        }).catch(function (e) {
            log('SDK支付失败: ' + e.message, 'error');
            setPayStatus('支付失败: ' + e.message, 'failed');
        });
    }

    function capture() {
        var id = document.getElementById('sdkPaymentIntentId').value;
        if (!id) {
            log('请输入 PaymentIntent ID', 'warn');
            return;
        }
        apiCall('POST', '/payments/capture', {paymentIntentId: id})
            .then(function (pi) {
                log('Capture成功: ' + pi.id, 'success');
                setPayStatus('Capture成功: ' + pi.status, 'success');
            })
            .catch(function (e) { log('Capture失败: ' + e.message, 'error'); });
    }

    function query() {
        var id = document.getElementById('sdkPaymentIntentId').value;
        if (!id) {
            log('请输入 PaymentIntent ID', 'warn');
            return;
        }
        apiCall('GET', '/payments/' + id)
            .then(function (pi) {
                log('查询状态: ' + pi.status);
                setPayStatus('PaymentIntent: ' + pi.status, pi.status === 'succeeded' ? 'success' : '');
            })
            .catch(function (e) { log('查询失败: ' + e.message, 'error'); });
    }

    function bind() {
        document.getElementById('btnInitSdk').onclick = initSdk;
        document.getElementById('btnDiscoverReaders').onclick = discoverReaders;
        document.getElementById('btnDisconnect').onclick = disconnectReader;
        document.getElementById('btnSdkPay').onclick = sdkPay;
        document.getElementById('btnSdkCapture').onclick = capture;
        document.getElementById('btnSdkQuery').onclick = query;
        document.getElementById('btnSdkClearLog').onclick = function () {
            document.getElementById('sdkLogContainer').innerHTML = '';
        };
        log('SDK 页面已加载');
        loadSdkConfig();
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', bind);
    } else {
        bind();
    }
})();
