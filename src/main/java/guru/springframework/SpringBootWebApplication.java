package guru.springframework;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
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
import guru.springframework.domain.PreRule;
import guru.springframework.dto.*;
import guru.springframework.repositories.json.JsonRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableScheduling
public class SpringBootWebApplication {


    @Value("${feishu.app.appId}")
    private String appId;
    @Value("${feishu.app.appSecret}")
    private String appSecret;
    @Value("${feishu.app.verificationToken}")
    private String verificationToken;
    @Value("${feishu.app.encryptKey}")
    private String encryptKey;
    private static final Map<String,C> cache= new ConcurrentHashMap<>();



    @Data
    public static class C{
        private Date date;
        private Map<String,Object> template_variable;
        public C(Date date,Map<String,Object> template_variable) {
            this.date = date;
            this.template_variable = template_variable;
        }
    }
    @Data
    public static class D{
        private String sceneKey;
        private String sceneValue;

        public D(String sceneKey,String sceneValue) {
            this.sceneKey = sceneKey;
            this.sceneValue = sceneValue;
        }
    }
    @Autowired
    JsonRepository jsonRepository;
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


    @Scheduled(cron = "0 0/5 * * * ?")
    public void test() {
        for (Map.Entry<String,C> entry:cache.entrySet()) {
            if(new Date().getTime()-entry.getValue().getDate().getTime()>1000*60*5){
                cache.remove(entry.getKey());
            }
        }
    }


    @Bean
    public CardActionHandler getCardActionHandler() {
        return CardActionHandler.newBuilder(verificationToken, encryptKey,
                new CardActionHandler.ICardHandler() {
                    @Override
                    public Object handle(CardAction cardAction) throws Exception {
                        // 1.1 处理卡片行为
                        if(cardAction.getAction().getValue()!=null
                                && cardAction.getAction().getValue().containsKey("cardType")
                                && cardAction.getAction().getValue().get("cardType")!=null) {
                            String cardType = (String)cardAction.getAction().getValue().get("cardType");
                            // 返回业务类型主页选择
                            if (cardType.equals("businessSelect")) {
                                String openId = cardAction.getOpenId();
                                Client client = Client.newBuilder(appId, appSecret).build();
                                CreateMessageResp resp = null;
                                String option = cardAction.getAction().getOption();
                                Configuration config = Configuration.defaultConfiguration()
                                        .jsonProvider(new GsonJsonProvider())
                                        .mappingProvider(new GsonMappingProvider());
                                TypeRef<VariableDto.B> typeRef = new TypeRef<>() {
                                };
                                CardBusinessType cbt = CardBusinessType.valueOf("C"+option);
                                Businessable able = cbt.getAble();

                                Resp respDto=new Resp();
                                respDto.setType("template");
                                String uuid = UUID.randomUUID().toString();
                                String currentKey = "businessType";

                                List<VariableDto.A> rootPreRules= jsonRepository.getRuleOptionList("", currentKey,uuid);

                                List<String> rootPreRuleStrList = rootPreRules.stream().filter(ac -> option!=null && option.equals(ac.getText())).map(ad -> ad.getValue()).collect(Collectors.toList());
                                String a = rootPreRuleStrList.get(0);
                                //模拟前端筛选了数据。


                                List<PreRule> userSelectedValueData = JsonPath.using(config).parse(a).read( "$", typeRef).getData();
                                String nextKey = able.getNextBusinessAttr(currentKey);;
                                List<VariableDto.A> preRules= jsonRepository.getRuleOptionList(userSelectedValueData, nextKey,uuid);






                                // init cache
                                C cacheObject =cache.get(uuid);
                                if(cacheObject==null){
                                    cacheObject = new C(null,null);
                                }
                                if (cacheObject.getTemplate_variable() == null){
                                    cacheObject.setTemplate_variable(new TreeMap<>());
                                }
                                // init dto
                                VariableDto dto = new VariableDto();
                                respDto.setData(dto);
                                dto.setTemplate_variable(new TreeMap<>());

                                // update cache
                                cacheObject.setDate(new Date());
                                cacheObject.getTemplate_variable().put("optionList",preRules);//计算出下次前端可选列表 businessType 代表业务场景下的可选列表
                                cacheObject.getTemplate_variable().put("businessType",cbt.getDescription());//本次选择，下次前端反显
                                cacheObject.getTemplate_variable().put("selectedKey","请选择业务场景-"+able.getBusinessText(nextKey));//计算出下次可选列表领域
                                cacheObject.getTemplate_variable().put("sceneList",cacheObject.getTemplate_variable()
                                        .entrySet().stream()
                                        .map(c -> new D(able.getBusinessText(c.getKey()),c.getValue().toString()))
                                        .filter(c -> !"EDD".equals(c.getSceneKey())).
                                        collect(Collectors.toList()));

                                List<VariableDto.A> logicTypePreRules= jsonRepository.getRuleOptionList(userSelectedValueData, "logicType",uuid);
                                List<VariableDto.A> logicTypeCommonPreRulesList = logicTypePreRules.stream().filter(ac-> "通用".equals(ac.getText())).collect(Collectors.toList());
                                List<VariableDto.A> logicTypeNotPreRulesList = logicTypePreRules.stream().filter(ac-> "非".equals(ac.getText())).collect(Collectors.toList());
                                String commonRuleStr  = logicTypeCommonPreRulesList.isEmpty()?null:logicTypeCommonPreRulesList.get(0).getValue();
                                if (commonRuleStr != null){
                                    List<PreRule> preRuleCommons = JsonPath.using(config).parse(commonRuleStr).read( "$", typeRef).getData();
                                    cacheObject.getTemplate_variable().put("commonRuleList",preRuleCommons);//计算出下次可选列表领域
                                }else{
                                    cacheObject.getTemplate_variable().put("commonRuleList",new ArrayList<>());
                                }
                                String notRuleStr  = logicTypeNotPreRulesList.isEmpty()?null:logicTypeNotPreRulesList.get(0).getValue();
                                if(notRuleStr   != null){
                                    List<PreRule> preRuleNot = JsonPath.using(config).parse(notRuleStr).read( "$", typeRef).getData();
                                    cacheObject.getTemplate_variable().put("notRuleList",preRuleNot);//计算出下次可选列表领域
                                }else {
                                    cacheObject.getTemplate_variable().put("notRuleList",new ArrayList<>());
                                }


                                for (String key : able.allEnums()) {
                                    if(!cacheObject.getTemplate_variable().containsKey(key)){
                                        dto.getTemplate_variable().put(key,"");
                                    }
                                }

                                dto.getTemplate_variable().putAll(cacheObject.getTemplate_variable());
                                cache.put(uuid, cacheObject);
                                //保存缓存
                                dto.setTemplate_id("ctp_AAgb4GZKkP3n");


                                String respDtoJson = JsonPath.using(config).parse(respDto).jsonString();

                                CreateMessageReq req = CreateMessageReq.newBuilder()
                                        .receiveIdType(CreateMessageReceiveIdTypeEnum.OPEN_ID)
                                        .createMessageReqBody(
                                                CreateMessageReqBody.newBuilder()
                                                        .content(respDtoJson)
                                                        .msgType("interactive")
                                                        .receiveId(openId).build())
                                        .build();
                                resp = client.im().message().create(req);
                                if (!resp.success()) {
                                    System.out.println(String.format("code:%s,msg:%s,reqId:%s"
                                            , resp.getCode(), resp.getMsg(), resp.getRequestId()));
                                }
                                System.out.println(Jsons.DEFAULT.toJson(resp.getData()));

                            }
                        }else {
                            String option = cardAction.getAction().getOption();
                            Configuration config = Configuration.defaultConfiguration()
                                    .jsonProvider(new GsonJsonProvider())
                                    .mappingProvider(new GsonMappingProvider());
                            TypeRef<VariableDto.B> typeRef = new TypeRef<>() {
                            };
                            Resp respDto = new Resp();

                            respDto.setType("template");


                            respDto.setData(new VariableDto());


                            VariableDto.B optionSelected = JsonPath.using(config).parse(option).read("$", typeRef);
                            Businessable able = CardBusinessType.valueOf("C" + optionSelected.getCardType()).getAble();
                            String nextKey = able.getNextBusinessAttr(optionSelected.getCurQueryKey());;

                            String uuid = optionSelected.getSessionToken();
                            if (uuid == null) {
                                uuid = UUID.randomUUID().toString();
                            }
                            // init cache
                            C cacheObject = cache.get(uuid);
                            if (cacheObject == null) {
                                cacheObject = new C(null, null);
                            }
                            if (cacheObject.getTemplate_variable() == null) {
                                cacheObject.setTemplate_variable(new TreeMap<>());
                            }
                            // init dto

                            VariableDto dto = new VariableDto();
                            respDto.setData(dto);
                            dto.setTemplate_variable(new TreeMap<>());
                            for (String key : able.allEnums()) {
                                if (cacheObject.getTemplate_variable().containsKey(key)) {
                                    //优先初始化使用缓存
                                    dto.getTemplate_variable().put(key, cacheObject.getTemplate_variable().get(key));
                                } else {
                                    dto.getTemplate_variable().put(key, "");
                                }

                            }
                            List<PreRule> userSelectedValueData = optionSelected.getData();
                            cacheObject.setDate(new Date());
                            cacheObject.getTemplate_variable().put(optionSelected.getCurQueryKey(), optionSelected.getCurQuerySelectedValue());//本次选择，下次前端反显

                            if ("ruleName".equals(nextKey)) {
                                cacheObject.getTemplate_variable().put("sceneList",cacheObject.getTemplate_variable()
                                        .entrySet().stream()
                                        .map(c -> new D(able.getBusinessText(c.getKey()),c.getValue().toString()))
                                        .filter(c -> !"EDD".equals(c.getSceneKey())).
                                        collect(Collectors.toList()));
                                cacheObject.getTemplate_variable().put("ruleList", userSelectedValueData);

                                String logicType = userSelectedValueData.stream().map(c -> c.getLogicType()).distinct().findFirst().get();
                                if("仅".equals(logicType)){
                                    cacheObject.getTemplate_variable().remove("commonRuleList");//计算出下次可选列表领域
                                    dto.getTemplate_variable().remove("commonRuleList");//计算出下次可选列表领域
                                    cacheObject.getTemplate_variable().put("commonRuleList",new ArrayList<>());
                                }

                                cacheObject.getTemplate_variable().remove("ruleName");
                                dto.getTemplate_variable().remove("ruleName");

                                dto.getTemplate_variable().putAll(cacheObject.getTemplate_variable());
                                cache.remove(uuid);
                                //保存缓存
                                dto.setTemplate_id("ctp_AAglFhVj7dIZ");

                                String respDtoJson = JsonPath.using(config).parse(respDto).jsonString();
                                CustomResponse cr = new CustomResponse();
                                cr.setBody(Jsons.DEFAULT.fromJson(respDtoJson, Map.class));
                                cr.setStatusCode(200);
                                return respDtoJson;


                            } else {
                                List<VariableDto.A> preRules = jsonRepository.getRuleOptionList(userSelectedValueData, nextKey, uuid);
                                // update cache
                                cacheObject.getTemplate_variable().put("optionList", preRules);//计算出下次前端可选列表 businessType 代表业务场景下的可选列表
                                cacheObject.getTemplate_variable().put("selectedKey", "请选择业务场景-" + able.getBusinessText(nextKey));//计算出下次可选列表领域
                                //返回一致结果给飞邮
                                cacheObject.getTemplate_variable().put("sceneList",cacheObject.getTemplate_variable()
                                        .entrySet().stream()
                                        .map(c -> new D(able.getBusinessText(c.getKey()),c.getValue().toString()))
                                        .filter(c -> !"EDD".equals(c.getSceneKey())).
                                        collect(Collectors.toList()));
                                dto.getTemplate_variable().putAll(cacheObject.getTemplate_variable());
                                cache.put(uuid, cacheObject);
                                //保存缓存
                                dto.setTemplate_id("ctp_AAgb4GZKkP3n");


                                String respDtoJson = JsonPath.using(config).parse(respDto).jsonString();

                                CustomResponse cr = new CustomResponse();
                                cr.setBody(Jsons.DEFAULT.fromJson(respDtoJson, Map.class));
                                cr.setStatusCode(200);
                                //\"config\": { \"wide_screen_mode\": true, \"enable_forward\": true, \"update_multi\": false }
                                 return respDtoJson;
                            }
                        }

                        return null;
                    }
                }).build();
    }
    // 构建自定义响应

    @Bean
    public EventDispatcher getEventDispatcher() {
        return EventDispatcher.newBuilder(verificationToken,
                        encryptKey)
                .onP2MessageReceiveV1(new ImService.P2MessageReceiveV1Handler() {
                    @Override
                    public void handle(P2MessageReceiveV1 event) throws Exception {
                        System.out.println("c"+Jsons.DEFAULT.toJson(event));
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
                                                            .content("{\"type\": \"template\", \"data\": { \"template_id\": \"ctp_AAgbyZqa1KCy\",\"template_variable\":{\"serBusinessTypeList\":[{\"text\":\"授信\",\"value\":\"10\"},{\"text\":\"用信\",\"value\":\"20\"}]}}}")
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
                            System.out.println("ccccc"+Jsons.DEFAULT.toJson(resp.getData()));
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
