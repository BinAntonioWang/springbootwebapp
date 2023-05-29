package guru.springframework;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import com.lark.oapi.Client;
import com.lark.oapi.card.CardActionHandler;
import com.lark.oapi.card.model.CardAction;
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
        private Map<String,List<VariableDto.A>> template_variable;
        public C(Date date,Map<String,List<VariableDto.A>> template_variable) {
            this.date = date;
            this.template_variable = template_variable;
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
                        System.out.println(Jsons.DEFAULT.toJson(cardAction));
                        System.out.println(cardAction.getRequestId());

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
                                TypeRef<List<PreRule>> typeRef = new TypeRef<>() {
                                };




                                Resp respDto=new Resp();
                                VariableDto dto = new VariableDto();
                                respDto.setType("template");
                                respDto.setData(dto);
                                dto.setTemplate_variable(new HashMap<>());
                                String a = "[{\"id\":\"100029\",\"ruleName\":\"京东快贷18岁校验\",\"businessTypeId\":\"业务类型_10|产品_10120092-邮你贷-京东金融|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"10120092-邮你贷-京东金融\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100018\",\"ruleName\":\"中邮白名单产品\",\"businessTypeId\":\"业务类型_10|产品_10120097-邮你贷-受邀白名单|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"10120097-邮你贷-受邀白名单\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"赋值\"},{\"id\":\"100025\",\"ruleName\":\"白名单管控\",\"businessTypeId\":\"业务类型_10|产品_10120097-邮你贷-受邀白名单|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"10120097-邮你贷-受邀白名单\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100004\",\"ruleName\":\"有未结清贷款\",\"businessTypeId\":\"业务类型_10|产品_10120099-邮你贷|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"10120099-邮你贷\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100040\",\"ruleName\":\"产品地区管控\",\"businessTypeId\":\"业务类型_10|产品_10220096-邮你花-支付宝|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"10220096-邮你花-支付宝\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100041\",\"ruleName\":\"产品地区管控\",\"businessTypeId\":\"业务类型_10|产品_10220097-邮你花-线下|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"10220097-邮你花-线下\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100042\",\"ruleName\":\"产品地区管控\",\"businessTypeId\":\"业务类型_10|产品_10220098-邮你花-受邀白名单|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"10220098-邮你花-受邀白名单\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100043\",\"ruleName\":\"产品地区管控\",\"businessTypeId\":\"业务类型_10|产品_10220099-邮你花-线上|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"10220099-邮你花-线上\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100001\",\"ruleName\":\"循环产品在途判断\",\"businessTypeId\":\"业务类型_10|产品_10280088-循环贷|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"10280088-循环贷\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100019\",\"ruleName\":\"中邮白名单产品\",\"businessTypeId\":\"业务类型_10|产品_10280088-循环贷|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"10280088-循环贷\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"赋值\"},{\"id\":\"100024\",\"ruleName\":\"白名单管控\",\"businessTypeId\":\"业务类型_10|产品_10280088-循环贷|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"10280088-循环贷\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100005\",\"ruleName\":\"小于18周岁不允许申请\",\"businessTypeId\":\"业务类型_10|产品_10280089-邮你贷-通用循环贷|渠道_8200992880000001-哈啰|商户_82009928-哈啰|\",\"logicType\":\"仅\",\"businessTypeId_1\":\"1-以下场景下只走特定规则组\",\"channelId\":\"8200992880000001-哈啰\",\"merchantId\":\"82009928-哈啰\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"10280089-邮你贷-通用循环贷\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100006\",\"ruleName\":\"附件是否齐全\",\"businessTypeId\":\"业务类型_10|产品_10280089-邮你贷-通用循环贷|渠道_8200992880000001-哈啰|商户_82009928-哈啰|\",\"logicType\":\"仅\",\"businessTypeId_1\":\"1-以下场景下只走特定规则组\",\"channelId\":\"8200992880000001-哈啰\",\"merchantId\":\"82009928-哈啰\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"10280089-邮你贷-通用循环贷\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100007\",\"ruleName\":\"在途判断\",\"businessTypeId\":\"业务类型_10|产品_10280089-邮你贷-通用循环贷|渠道_8200992880000001-哈啰|商户_82009928-哈啰|\",\"logicType\":\"仅\",\"businessTypeId_1\":\"1-以下场景下只走特定规则组\",\"channelId\":\"8200992880000001-哈啰\",\"merchantId\":\"82009928-哈啰\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"10280089-邮你贷-通用循环贷\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100008\",\"ruleName\":\"小于18周岁不允许申请\",\"businessTypeId\":\"业务类型_10|产品_10280089-邮你贷-通用循环贷|渠道_8200995680000001-银联云闪贷|商户_82009956-银联云闪贷|\",\"logicType\":\"仅\",\"businessTypeId_1\":\"1-以下场景下只走特定规则组\",\"channelId\":\"8200995680000001-银联云闪贷\",\"merchantId\":\"82009956-银联云闪贷\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"10280089-邮你贷-通用循环贷\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100010\",\"ruleName\":\"附件是否齐全\",\"businessTypeId\":\"业务类型_10|产品_10280089-邮你贷-通用循环贷|渠道_8200995680000001-银联云闪贷|商户_82009956-银联云闪贷|\",\"logicType\":\"仅\",\"businessTypeId_1\":\"1-以下场景下只走特定规则组\",\"channelId\":\"8200995680000001-银联云闪贷\",\"merchantId\":\"82009956-银联云闪贷\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"10280089-邮你贷-通用循环贷\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100011\",\"ruleName\":\"在途判断\",\"businessTypeId\":\"业务类型_10|产品_10280089-邮你贷-通用循环贷|渠道_8200995680000001-银联云闪贷|商户_82009956-银联云闪贷|\",\"logicType\":\"仅\",\"businessTypeId_1\":\"1-以下场景下只走特定规则组\",\"channelId\":\"8200995680000001-银联云闪贷\",\"merchantId\":\"82009956-银联云闪贷\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"10280089-邮你贷-通用循环贷\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100002\",\"ruleName\":\"循环产品在途判断\",\"businessTypeId\":\"业务类型_10|产品_10280089-邮你贷-通用循环贷|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"10280089-邮你贷-通用循环贷\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100009\",\"ruleName\":\"小于18周岁不允许申请\",\"businessTypeId\":\"业务类型_10|产品_11110001-众邦联合贷|渠道_9900972680000001-众邦联合贷|商户_99009726-众邦联合贷|\",\"logicType\":\"仅\",\"businessTypeId_1\":\"1-以下场景下只走特定规则组\",\"channelId\":\"9900972680000001-众邦联合贷\",\"merchantId\":\"99009726-众邦联合贷\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"11110001-众邦联合贷\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100030\",\"ruleName\":\"AA9在途判断\",\"businessTypeId\":\"业务类型_10|产品_11110001-众邦联合贷|渠道_9900972680000001-众邦联合贷|商户_99009726-众邦联合贷|\",\"logicType\":\"仅\",\"businessTypeId_1\":\"1-以下场景下只走特定规则组\",\"channelId\":\"9900972680000001-众邦联合贷\",\"merchantId\":\"99009726-众邦联合贷\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"11110001-众邦联合贷\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100031\",\"ruleName\":\"在途判断\",\"businessTypeId\":\"业务类型_10|产品_11110001-众邦联合贷|渠道_9900972680000001-众邦联合贷|商户_99009726-众邦联合贷|\",\"logicType\":\"仅\",\"businessTypeId_1\":\"1-以下场景下只走特定规则组\",\"channelId\":\"9900972680000001-众邦联合贷\",\"merchantId\":\"99009726-众邦联合贷\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"11110001-众邦联合贷\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100032\",\"ruleName\":\"AA9规则管控\",\"businessTypeId\":\"业务类型_10|产品_11110001-众邦联合贷|渠道_9900972680000001-众邦联合贷|商户_99009726-众邦联合贷|\",\"logicType\":\"仅\",\"businessTypeId_1\":\"1-以下场景下只走特定规则组\",\"channelId\":\"9900972680000001-众邦联合贷\",\"merchantId\":\"99009726-众邦联合贷\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"11110001-众邦联合贷\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100033\",\"ruleName\":\"三个月拒绝记录\",\"businessTypeId\":\"业务类型_10|产品_11110001-众邦联合贷|渠道_9900972680000001-众邦联合贷|商户_99009726-众邦联合贷|\",\"logicType\":\"仅\",\"businessTypeId_1\":\"1-以下场景下只走特定规则组\",\"channelId\":\"9900972680000001-众邦联合贷\",\"merchantId\":\"99009726-众邦联合贷\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"11110001-众邦联合贷\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100034\",\"ruleName\":\"小于18周岁不允许申请\",\"businessTypeId\":\"业务类型_10|产品_20220066-邮你付|渠道_全部|商户_全部|\",\"logicType\":\"仅\",\"businessTypeId_1\":\"1-以下场景下只走特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"20220066-邮你付\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100035\",\"ruleName\":\"AA9在途判断\",\"businessTypeId\":\"业务类型_10|产品_20220066-邮你付|渠道_全部|商户_全部|\",\"logicType\":\"仅\",\"businessTypeId_1\":\"1-以下场景下只走特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"20220066-邮你付\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100036\",\"ruleName\":\"在途判断\",\"businessTypeId\":\"业务类型_10|产品_20220066-邮你付|渠道_全部|商户_全部|\",\"logicType\":\"仅\",\"businessTypeId_1\":\"1-以下场景下只走特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"20220066-邮你付\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100037\",\"ruleName\":\"AA9规则管控\",\"businessTypeId\":\"业务类型_10|产品_20220066-邮你付|渠道_全部|商户_全部|\",\"logicType\":\"仅\",\"businessTypeId_1\":\"1-以下场景下只走特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"20220066-邮你付\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100038\",\"ruleName\":\"产品地区管控\",\"businessTypeId\":\"业务类型_10|产品_20220066-邮你付|渠道_全部|商户_全部|\",\"logicType\":\"仅\",\"businessTypeId_1\":\"1-以下场景下只走特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"20220066-邮你付\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100003\",\"ruleName\":\"循环产品在途判断\",\"businessTypeId\":\"业务类型_10|产品_20220099-邮你花(类白条)|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"20220099-邮你花(类白条)\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100044\",\"ruleName\":\"产品地区管控\",\"businessTypeId\":\"业务类型_10|产品_20220099-邮你花（类白条）|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"20220099-邮你花（类白条）\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100017\",\"ruleName\":\"贷款金额超限校验\",\"businessTypeId\":\"业务类型_10|产品_21130182-邮你购-合约机分期|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"21130182-邮你购-合约机分期\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100016\",\"ruleName\":\"贷款金额超限校验\",\"businessTypeId\":\"业务类型_10|产品_21130184-邮你购-祝博士教育|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"21130184-邮你购-祝博士教育\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100026\",\"ruleName\":\"驾校分呗限流\",\"businessTypeId\":\"业务类型_10|产品_21130200-邮你购-学车分期|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"21130200-邮你购-学车分期\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100027\",\"ruleName\":\"驾校分呗限流\",\"businessTypeId\":\"业务类型_10|产品_21130201-邮你购-学车分期付息|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"21130201-邮你购-学车分期付息\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100039\",\"ruleName\":\"产品地区管控\",\"businessTypeId\":\"业务类型_10|产品_21220099-邮你花（京东白条）|渠道_全部|商户_全部|\",\"logicType\":\"特定\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"21220099-邮你花（京东白条）\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100012\",\"ruleName\":\"小于18周岁不允许申请\",\"businessTypeId\":\"业务类型_10|产品_全部|渠道_全部|商户_全部|\",\"logicType\":\"通用\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"全部\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100013\",\"ruleName\":\"附件是否齐全\",\"businessTypeId\":\"业务类型_10|产品_全部|渠道_全部|商户_全部|\",\"logicType\":\"通用\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"全部\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100014\",\"ruleName\":\"在途判断\",\"businessTypeId\":\"业务类型_10|产品_全部|渠道_全部|商户_全部|\",\"logicType\":\"通用\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"全部\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100015\",\"ruleName\":\"实验室白名单\",\"businessTypeId\":\"业务类型_10|产品_全部|渠道_全部|商户_全部|\",\"logicType\":\"通用\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"全部\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100021\",\"ruleName\":\"AA9在途判断\",\"businessTypeId\":\"业务类型_10|产品_全部|渠道_全部|商户_全部|\",\"logicType\":\"通用\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"全部\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100022\",\"ruleName\":\"AA9规则管控\",\"businessTypeId\":\"业务类型_10|产品_全部|渠道_全部|商户_全部|\",\"logicType\":\"通用\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"全部\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"},{\"id\":\"100023\",\"ruleName\":\"三个月拒绝记录\",\"businessTypeId\":\"业务类型_10|产品_全部|渠道_全部|商户_全部|\",\"logicType\":\"通用\",\"businessTypeId_1\":\"2-以下场景走通用规则组和对应场景下特定规则组\",\"channelId\":\"全部\",\"merchantId\":\"全部\",\"entryType\":\"\",\"businessType\":\"10\",\"productCode\":\"全部\",\"batchFrom\":\"不涉及\",\"subType\":\"不涉及\",\"adjType\":\"不涉及\",\"orgAppType\":\"\",\"fileType\":\"\",\"adjRange\":\"\",\"marketType\":\"\",\"resultType\":\"拒绝\"}]";
                                String currentKey = ApplyConditionEnum.businessType.name();


                                List<PreRule> userSelectedValueData1 = JsonPath.using(config).parse(a).read( "$", typeRef);
                                ApplyConditionEnum nextQueryEnum1 = ApplyConditionEnum.valueOf(currentKey).getNext();
                                String nextKey = nextQueryEnum1.name();
                                String uuid = UUID.randomUUID().toString();
                                List<VariableDto.A> preRules= jsonRepository.getRuleOptionList(userSelectedValueData1, nextKey,uuid);
                                //缓存更新
                                C cacheObject =cache.get(uuid);
                                if(cacheObject==null){
                                    cacheObject = new C(null,null);
                                }
                                if (cacheObject.getTemplate_variable() == null){
                                    cacheObject.setTemplate_variable(new ConcurrentHashMap<>());
                                }
                                cacheObject.setDate(new Date());
                                cacheObject.getTemplate_variable().put(nextKey,preRules);
                                //返回一致结果给飞邮
                                dto.getTemplate_variable().putAll(cacheObject.getTemplate_variable());
                                cache.put(uuid, cacheObject);
                                //保存缓存
                                dto.setTemplate_id("ctp_AAgbFBFGthrB");
                                for (ApplyConditionEnum key : ApplyConditionEnum.values()) {
                                    if (key.name().equals(nextKey)) {
                                        continue;
                                    }
                                    dto.getTemplate_variable().put(key.name(), Collections.singletonList(new VariableDto.A("sds", "asd")));
                                }
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
                            String openId = cardAction.getOpenId();
                            Client client = Client.newBuilder(appId, appSecret).build();
                            CreateMessageResp resp = null;
                            String option = cardAction.getAction().getOption();
                            Configuration config = Configuration.defaultConfiguration()
                                    .jsonProvider(new GsonJsonProvider())
                                    .mappingProvider(new GsonMappingProvider());
                            TypeRef<VariableDto.B> typeRef = new TypeRef<>() {
                            };
                            Resp respDto=new Resp();
                            VariableDto dto = new VariableDto();
                            respDto.setType("template");
                            respDto.setData(dto);
                            dto.setTemplate_variable(new HashMap<>());

                            VariableDto.B optionSelected = JsonPath.using(config).parse(option).read( "$", typeRef);
                            String uuid = optionSelected.getSessionToken();
                            Businessable able = CardBusinessType.valueOf("C"+optionSelected.getCardType()).getAble();
                            String nextKey = able.getNextBusinessAttr(optionSelected.getCurQueryKey());
                            List<PreRule> userSelectedValueData = optionSelected.getData();
                            List<VariableDto.A> preRules = jsonRepository.getRuleOptionList(userSelectedValueData, nextKey,uuid);

                            C cacheObject =cache.get(uuid);
                            if(cacheObject==null){
                                cacheObject = new C(null,null);
                            }
                            if (cacheObject.getTemplate_variable() == null){
                                cacheObject.setTemplate_variable(new ConcurrentHashMap<>());
                            }
                            cacheObject.setDate(new Date());
                            cacheObject.getTemplate_variable().put(nextKey,preRules);
                            //返回一致结果给飞邮
                            dto.getTemplate_variable().putAll(cacheObject.getTemplate_variable());
                            cache.put(uuid, cacheObject);
                            //保存缓存
                            for (String key : able.allEnums()) {
                                if (key.equals(nextKey) || dto.getTemplate_variable().containsKey(key)) {
                                    continue;
                                }
                                dto.getTemplate_variable().put(key, Collections.singletonList(new VariableDto.A("sds", "asd")));
                            }


//                            cache.put(uuid, new C(new Date(),dto.getTemplate_variable()));
//
//                            for (String key : able.allEnums()) {
//                                if (key.equals(nextKey)) {
//                                    continue;
//                                }
//                                dto.getTemplate_variable().put(key, preRules);
//                            }
//                            dto.getTemplate_variable().put(nextKey, preRules);

                            String templateId = able.getTemplateId();
                            dto.setTemplate_id(templateId);

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
