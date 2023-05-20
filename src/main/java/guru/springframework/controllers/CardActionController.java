package guru.springframework.controllers;

import com.lark.oapi.card.CardActionHandler;
import com.lark.oapi.card.enums.MessageCardButtonTypeEnum;
import com.lark.oapi.card.model.*;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.event.EventDispatcher;
import com.lark.oapi.sdk.servlet.ext.ServletAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CardActionController {
    //1. 注册卡片处理器
    @Autowired
    private CardActionHandler cardActionHandler;
    // 2. 注入 ServletAdapter 示例
    @Autowired
    private ServletAdapter servletAdapter;

    //3. 注册服务路由
    @RequestMapping("/webhook/card")
    public void card(HttpServletRequest request, HttpServletResponse response)
            throws Throwable {
        //3.1 回调扩展包卡片行为处理回调
        servletAdapter.handleCardAction(request, response, cardActionHandler);
    }


    // 构建卡片响应


    // 构建自定义响应
    private CustomResponse getCustomResp() {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("ke2", "value2");
        CustomResponse customResponse = new CustomResponse();
        customResponse.setStatusCode(0);
        customResponse.setBody(map);
        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put("key1", Arrays.asList("a", "b"));
        headers.put("key2", Arrays.asList("c", "d"));
        customResponse.setHeaders(headers);
        return customResponse;
    }
}