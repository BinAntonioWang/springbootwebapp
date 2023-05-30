package guru.springframework.dto;

public enum LoanConditionEnum {
    ruleName(null, "规则名称"),

    entryType(ruleName, "入口类型"),
    merchantId(entryType, "商户号"),
    channelId(merchantId, "渠道号"),
    productCode(channelId,  "产品号"),
    businessType(productCode,   "业务类型");
    private LoanConditionEnum next;
    public String getDescription() {
        return description;
    }
    private String description;
    public LoanConditionEnum getNext() {
        return next;
    }
    LoanConditionEnum(LoanConditionEnum next,String description){
        this.next = next;
        this.description = description;
    }
}
