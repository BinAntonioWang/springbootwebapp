package guru.springframework;

import com.lark.oapi.Client;
import com.lark.oapi.card.CardActionHandler;
import com.lark.oapi.card.model.CardAction;
import com.lark.oapi.card.model.CustomResponse;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.event.EventDispatcher;
import com.lark.oapi.sdk.servlet.ext.ServletAdapter;
import com.lark.oapi.service.im.v1.ImService;
import com.lark.oapi.service.im.v1.enums.CreateMessageReceiveIdTypeEnum;
import com.lark.oapi.service.im.v1.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SpringBootWebApplication {


    @Value("${feishu.app.appId}")
    private String appId;
    @Value("${feishu.app.appSecret}")
    private String appSecret;
    @Value("${feishu.app.verificationToken}")
    private String verificationToken;
    @Value("${feishu.app.encryptKey}")
    private String encryptKey;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebApplication.class, args);
    }
    @Bean
    public Client getClient() {
        return Client.newBuilder(appId, appSecret).build();
    }
    @Bean
    public ServletAdapter getServletAdapter() {
        return new ServletAdapter();
    }

    @Bean
    public CardActionHandler getCardActionHandler() {
        return CardActionHandler.newBuilder(verificationToken, encryptKey,
                new CardActionHandler.ICardHandler() {
                    @Override
                    public Object handle(CardAction cardAction) throws Exception {
                        // 1.1 处理卡片行为
                        System.out.println(Jsons.DEFAULT.toJson(cardAction));
                        System.out.println(cardAction.getRequestId());

                        if(cardAction.getAction().getValue()!=null
                                && cardAction.getAction().getValue().containsKey("cardType") && cardAction.getAction().getValue().get("cardType")!=null) {
                            String cardType = (String)cardAction.getAction().getValue().get("cardType");
                            // 返回业务类型主页选择
                            if (cardType.equals("businessSelect")) {
                                String openId = cardAction.getOpenId();
                                Client client = Client.newBuilder(appId, appSecret).build();
                                CreateMessageResp resp = null;
                                String option = cardAction.getAction().getOption();

                                resp = client.im().message().create(
                                        CreateMessageReq.newBuilder()
                                                .receiveIdType(CreateMessageReceiveIdTypeEnum.OPEN_ID)
                                                .createMessageReqBody(
                                                        CreateMessageReqBody.newBuilder()
                                                                .content("{\"type\": \"template\", \"data\": { \"template_id\": \"" + option + "\",\"template_variable\":{\"serProductList\":[{\"text\":\"授信1111\",\"value\":\"101\"},{\"text\":\"用信2222\",\"value\":\"201\"}],\"serChannelList\":[{\"text\":\"Option\",\"value\":\"option\"}],\"serMerchantList\":[{\"text\":\"Option\",\"value\":\"option\"}]}}}")
                                                                .msgType("interactive")
                                                                .receiveId(openId).build())
                                                .build());
                                if (!resp.success()) {
                                    System.out.println(String.format("code:%s,msg:%s,reqId:%s"
                                            , resp.getCode(), resp.getMsg(), resp.getRequestId()));
                                }
                                System.out.println(Jsons.DEFAULT.toJson(resp.getData()));
                            }else if (cardType.equals("business10")) {

                            }else if (cardType.equals("business20")) {

                            }else if (cardType.equals("business30")) {

                            }else if (cardType.equals("business40")) {

                            }else if (cardType.equals("business60")) {

                            }else if (cardType.equals("business22")) {

                            }
                        }
                        return getCustomResp();
                    }
                }).build();
    }
    // 构建自定义响应
    private CustomResponse getCustomResp() {
        Map<String, Object> map = new HashMap<>();
        map.put("serProductList", Arrays.asList(
                new HashMap<String, String>() {{
                    put("text", "授信123123");
                    put("value", "101");
                }},
                new HashMap<String, String>() {{
                    put("text", "用信123213213");
                    put("value", "201");
                }}
        ));
        map.put("serChannelList", Arrays.asList(
                new HashMap<String, String>() {{
                    put("text111111", "Option");
                    put("value", "option");
                }}
        ));
        map.put("serMerchantList", Arrays.asList(
                new HashMap<String, String>() {{
                    put("text1111111111", "Option");
                    put("value", "option");
                }}
        ));
        CustomResponse customResponse = new CustomResponse();
        customResponse.setStatusCode(200);
        customResponse.setBody(map);
        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put("key1", Arrays.asList("a", "b"));
        headers.put("key2", Arrays.asList("c", "d"));
        customResponse.setHeaders(headers);
        return customResponse;
    }
    @Bean
    public EventDispatcher getEventDispatcher() {
        return EventDispatcher.newBuilder(verificationToken,
                        encryptKey)
                .onP2MessageReceiveV1(new ImService.P2MessageReceiveV1Handler() {
                    @Override
                    public void handle(P2MessageReceiveV1 event) throws Exception {
                        System.out.println(Jsons.DEFAULT.toJson(event));
                        System.out.println(event.getRequestId());
                        if (event.getEvent().getMessage().getContent().contains("\\前置规则查询")) {
                            // 获取租户 key
                            String messageId = event.getEvent().getMessage().getMessageId();
                            String openId = event.getEvent().getSender().getSenderId().getOpenId();

                            Client client = Client.newBuilder(appId, appSecret).build();
                            // 发送请求
                            CreateMessageResp resp = client.im().message().create(
                                    CreateMessageReq.newBuilder()
                                            .receiveIdType(CreateMessageReceiveIdTypeEnum.OPEN_ID)
                                            .createMessageReqBody(
                                                    CreateMessageReqBody.newBuilder()
                                                            .content("{\"type\": \"template\", \"data\": { \"template_id\": \"ctp_AAuhmBbkpa9R\",\"template_variable\":{\"serBusinessTypeList\":[{\"text\":\"授信\",\"value\":\"10\"},{\"text\":\"用信\",\"value\":\"20\"}]}}}")
                                                            .msgType("interactive")
                                                            .receiveId(openId).build())
                                            .build());


                            // 处理服务端错误
                            if (!resp.success()) {
                                System.out.println(String.format("code:%s,msg:%s,reqId:%s"
                                        , resp.getCode(), resp.getMsg(), resp.getRequestId()));
                                return;
                            }

                            // 业务数据处理
                            System.out.println(Jsons.DEFAULT.toJson(resp.getData()));
                        }
                    }
                }).onP2MessageReadV1(new ImService.P2MessageReadV1Handler() {
                    @Override
                    public void handle(P2MessageReadV1 event) throws Exception {

                    }
                }).onP2MessageReactionCreatedV1(new ImService.P2MessageReactionCreatedV1Handler() {
                    @Override
                    public void handle(P2MessageReactionCreatedV1 event) throws Exception {

                    }
                }).onP2MessageRecalledV1(new ImService.P2MessageRecalledV1Handler() {
                    @Override
                    public void handle(P2MessageRecalledV1 event) throws Exception {

                    }
                }).onP2MessageReactionDeletedV1(new ImService.P2MessageReactionDeletedV1Handler() {
                    @Override
                    public void handle(P2MessageReactionDeletedV1 event) throws Exception {

                    }
                }).build();
    }
}
