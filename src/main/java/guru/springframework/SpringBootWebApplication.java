package guru.springframework;

import com.lark.oapi.Client;
import com.lark.oapi.card.CardActionHandler;
import com.lark.oapi.card.enums.MessageCardButtonTypeEnum;
import com.lark.oapi.card.model.*;
import com.lark.oapi.core.request.RequestOptions;
import com.lark.oapi.core.utils.Jsons;
import com.lark.oapi.event.EventDispatcher;
import com.lark.oapi.sdk.servlet.ext.ServletAdapter;
import com.lark.oapi.service.im.v1.ImService;
import com.lark.oapi.service.im.v1.enums.CreateMessageReceiveIdTypeEnum;
import com.lark.oapi.service.im.v1.enums.ReceiveIdTypeEnum;
import com.lark.oapi.service.im.v1.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
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
                    public Object handle(CardAction cardAction) {
                        // 1.1 处理卡片行为
                        System.out.println(Jsons.DEFAULT.toJson(cardAction));
                        System.out.println(cardAction.getRequestId());

                        return getCard();
                    }
                }).build();
    }
    private MessageCard getCard() {
        // 配置
        MessageCardConfig config = MessageCardConfig.newBuilder()
                .enableForward(true)
                .wideScreenMode(true)
                .updateMulti(true)
                .build();

        // cardUrl
        MessageCardURL cardURL = MessageCardURL.newBuilder()
                .pcUrl("http://www.baidu.com")
                .iosUrl("http://www.google.com")
                .url("http://open.feishu.com")
                .androidUrl("http://www.jianshu.com")
                .build();

        // header
        MessageCardHeader header = MessageCardHeader.newBuilder()
                .template("red")
                .title(MessageCardPlainText.newBuilder()
                        .content("1 级报警 - 数据平台")
                        .build())
                .build();

        //elements
        MessageCardDiv div1 = MessageCardDiv.newBuilder()
                .fields(new MessageCardField[]{
                        MessageCardField.newBuilder()
                                .isShort(true)
                                .text(MessageCardLarkMd.newBuilder()
                                        .content("**🕐 时间：**2021-02-23 20:17:51")
                                        .build())
                                .build(),
                        MessageCardField.newBuilder()
                                .isShort(true)
                                .text(MessageCardLarkMd.newBuilder()
                                        .content("**🔢 事件 ID：：**336720")
                                        .build())
                                .build(),
                        MessageCardField.newBuilder()
                                .isShort(false)
                                .text(MessageCardLarkMd.newBuilder()
                                        .content("")
                                        .build())
                                .build(),
                        MessageCardField.newBuilder()
                                .isShort(true)
                                .text(MessageCardLarkMd.newBuilder()
                                        .content("**📋 项目：**\nQA 7")
                                        .build())
                                .build(),
                        MessageCardField.newBuilder()
                                .isShort(true)
                                .text(MessageCardLarkMd.newBuilder()
                                        .content("**👤 一级值班：**\n<at id=ou_c245b0a7dff2725cfa2fb104f8b48b9d>加多</at>")
                                        .build())
                                .build(),
                        MessageCardField.newBuilder()
                                .isShort(false)
                                .text(MessageCardLarkMd.newBuilder()
                                        .content("")
                                        .build())
                                .build(),
                        MessageCardField.newBuilder()
                                .isShort(true)
                                .text(MessageCardLarkMd.newBuilder()
                                        .content("**👤 二级值班：**\n<at id=ou_c245b0a7dff2725cfa2fb104f8b48b9d>加多</at>")
                                        .build())
                                .build()
                })
                .build();

        MessageCardImage image = MessageCardImage.newBuilder()
                .alt(MessageCardPlainText.newBuilder()
                        .content("")
                        .build())
                .imgKey("img_v2_8b2ebeaf-c97c-411d-a4dc-4323e8cba10g")
                .title(MessageCardLarkMd.newBuilder()
                        .content("支付方式 支付成功率低于 50%：")
                        .build())
                .build();

        MessageCardNote note = MessageCardNote.newBuilder()
                .elements(new IMessageCardNoteElement[]{MessageCardPlainText.newBuilder()
                        .content("🔴 支付失败数  🔵 支付成功数")
                        .build()})
                .build();

        Map<String, Object> value = new HashMap<>();
        value.put("key1", "value1");
        MessageCardAction cardAction = MessageCardAction.newBuilder()
                .actions(new IMessageCardActionElement[]{
                        MessageCardEmbedButton.newBuilder()
                                .buttonType(MessageCardButtonTypeEnum.PRIMARY)
                                .value(value)
                                .text(MessageCardPlainText.newBuilder().content("跟进处理").build())
                                .build(),
                        MessageCardEmbedSelectMenuStatic.newBuilder()
                                .options(new MessageCardEmbedSelectOption[]{
                                        MessageCardEmbedSelectOption.newBuilder()
                                                .value("1")
                                                .text(MessageCardPlainText.newBuilder()
                                                        .content("屏蔽10分钟")
                                                        .build())
                                                .build(),
                                        MessageCardEmbedSelectOption.newBuilder()
                                                .value("2")
                                                .text(MessageCardPlainText.newBuilder()
                                                        .content("屏蔽30分钟")
                                                        .build())
                                                .build(),
                                        MessageCardEmbedSelectOption.newBuilder()
                                                .value("3")
                                                .text(MessageCardPlainText.newBuilder()
                                                        .content("屏蔽1小时")
                                                        .build())
                                                .build(),
                                        MessageCardEmbedSelectOption.newBuilder()
                                                .value("4")
                                                .text(MessageCardPlainText.newBuilder()
                                                        .content("屏蔽24小时")
                                                        .build())
                                                .build()
                                })
                                .placeholder(MessageCardPlainText.newBuilder()
                                        .content("暂时屏蔽报警")
                                        .build())
                                .value(value)
                                .build()
                })
                .build();

        MessageCardHr hr = MessageCardHr.newBuilder().build();

        MessageCardDiv div2 = MessageCardDiv.newBuilder()
                .text(MessageCardLarkMd.newBuilder()
                        .content(
                                "🙋🏼 [我要反馈误报](https://open.feishu.cn/) | 📝 [录入报警处理过程](https://open.feishu.cn/)")
                        .build())
                .build();

        MessageCard card = MessageCard.newBuilder()
                .cardLink(cardURL)
                .config(config)
                .header(header)
                .elements(new MessageCardElement[]{div1, note, image, cardAction, hr, div2})
                .build();

        return card;
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
