package test.xinghuo;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: kejiefu
 * @create: 2024-08-16 16:46
 **/
public class XfyunService {

    public static void main(String[] args) {
        String url = "https://spark-api-open.xf-yun.com/v1/chat/completions";
        RequestData requestData = new RequestData();
        List<RequestData.Message> messages = new ArrayList<>();
        RequestData.Message message = new RequestData.Message();
        message.setContent("存款二三十万后上班心态会有变化吗？以这个话题为开头写300字短文");
        message.setRole("user");
        messages.add(message);
        requestData.setMessages(messages);
        requestData.setModel("4.0Ultra");
        requestData.setStream(Boolean.FALSE);
        String body = JSONUtil.toJsonStr(requestData);
        HttpResponse httpResponse = HttpUtil.createPost(url).auth("Bearer XXXX").contentType(ContentType.JSON.getValue()).body(body).execute();
        System.out.println(httpResponse.body());
    }




}
