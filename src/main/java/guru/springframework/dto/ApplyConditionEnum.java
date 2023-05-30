package guru.springframework.dto;

public enum ApplyConditionEnum{
    ruleName(null, "规则名称"),

    merchantId(ruleName, "商户号"),
    channelId(merchantId, "渠道号"),
    productCode(channelId,  "产品号"),
    businessType(productCode,   "业务类型");

    private ApplyConditionEnum next;
    private String description;
    public ApplyConditionEnum getNext() {
        return next;
    }
    public String getDescription() {
        return description;
    }
    ApplyConditionEnum(ApplyConditionEnum next, String description) {
        this.next = next;
        this.description = description;
    }



}
