package guru.springframework.dto;

public enum LoanConditionEnum {
    entryType(null),
    merchantId(entryType),
    channelId(merchantId),
    productCode(channelId),

    businessType(productCode);
    private LoanConditionEnum next;
    public LoanConditionEnum getNext() {
        return next;
    }
    LoanConditionEnum(LoanConditionEnum next){
        this.next = next;
    }
}
