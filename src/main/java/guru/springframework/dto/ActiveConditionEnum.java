package guru.springframework.dto;

public enum ActiveConditionEnum{
    ruleName(null, "规则名称"),
    businessType(ruleName, "业务类型"),;
    public String getDescription() {
        return description;
    }
    private String description;
    private ActiveConditionEnum next;
    public ActiveConditionEnum getNext() {
        return next;
    }
    ActiveConditionEnum(ActiveConditionEnum next, String description){
        this.next = next;
        this.description = description;
    }

}
