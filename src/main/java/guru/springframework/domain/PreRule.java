package guru.springframework.domain;

import lombok.Data;

//{ "id": "600006", "ruleName": "申请人年龄是否大于18岁", "businessTypeId": "业务类型_60|产品_全部|渠道_全部|商户_全部|营销类型_线上转线下|", "logicType": "通用", "businessTypeId_1": "2-以下场景走通用规则组和对应场景下特定规则组", "businessType": "60", "productCode": "全部", "channelId": "全部", "merchantId": "全部", "batchFrom": "不涉及", "subType": "不涉及", "adjType": "不涉及", "entryType": "", "orgAppType": "", "fileType": "", "marketType": "线上转线下", "adjRange": "", "resultType": "拒绝" } 根据以上json格式，生成对应的实体类
@Data
public class PreRule {
    private String id;
    private String ruleName;
    private String businessTypeId;
    private String logicType;
    private String businessTypeId_1;

    private String channelId;
    private String merchantId;
    private String entryType;

    private String businessType;
    private String productCode;
    private String batchFrom;
    private String subType;
    private String adjType;
    private String orgAppType;
    private String fileType;
    private String adjRange;

    private String marketType;


    private String resultType;
}
