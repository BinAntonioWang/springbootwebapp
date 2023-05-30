package guru.springframework.dto;

public enum PreviewConditionEnum{
    ruleName(null, "规则名称"),
    marketType(ruleName,"营销类型"),
    businessType(marketType,"业务类型");

    public String getDescription() {
        return description;
    }
    private String description;
    private PreviewConditionEnum next;
    public PreviewConditionEnum getNext() {
        return next;
    }
    PreviewConditionEnum(PreviewConditionEnum next, String description){
        this.next = next;
        this.description = description;
    }
}
