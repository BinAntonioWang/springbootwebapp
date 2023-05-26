package guru.springframework.dto;

public enum AdjConditionEnum {
    adjRange(null),
    fileType(adjRange),
    orgAppType(fileType),
    adjType(orgAppType),
    subType(adjType),
    batchFrom(subType),
    productCode(batchFrom),

    businessType(productCode);







    private AdjConditionEnum next;
    public AdjConditionEnum getNext() {
        return next;
    }
    AdjConditionEnum(AdjConditionEnum next){
        this.next = next;
    }
}
