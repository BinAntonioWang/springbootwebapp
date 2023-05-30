package guru.springframework.dto;

public enum AdjConditionEnum {
    ruleName(null, "规则名称"),

    adjRange(ruleName,"调额范围"),
    fileType(adjRange,"调额文件类型"),
    orgAppType(fileType,"原授信类型"),
    adjType(orgAppType,"调额类型"),
    subType(adjType,"调额子类型"),
    batchFrom(subType,"调额来源"),
    productCode(batchFrom,"产品号"),

    businessType(productCode,"业务类型");




    public String getDescription() {
        return description;
    }
    private String description;


    private AdjConditionEnum next;
    public AdjConditionEnum getNext() {
        return next;
    }
    AdjConditionEnum(AdjConditionEnum next, String description){
        this.next = next;
        this.description = description;
    }
}
