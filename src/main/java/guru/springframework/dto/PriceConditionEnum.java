package guru.springframework.dto;

public enum PriceConditionEnum {
    productCode(null),

    businessType(productCode);

    private PriceConditionEnum next;
    public PriceConditionEnum getNext() {
        return next;
    }
    PriceConditionEnum(PriceConditionEnum next){
        this.next = next;
    }
}
