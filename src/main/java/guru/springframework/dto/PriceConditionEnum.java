package guru.springframework.dto;

public enum PriceConditionEnum {
    ruleName(null, "规则名称"),

    productCode(ruleName, "产品号"),

    businessType(productCode,"业务类型");

    public String getDescription() {
        return description;
    }
    private String description;
    private PriceConditionEnum next;
    public PriceConditionEnum getNext() {
        return next;
    }
    PriceConditionEnum(PriceConditionEnum next, String description){
        this.next = next;
        this.description = description;
    }
}
