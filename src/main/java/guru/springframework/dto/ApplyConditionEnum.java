package guru.springframework.dto;

public enum ApplyConditionEnum{
    merchantId(null),
    channelId(merchantId),
    productCode(channelId),

    businessType(productCode);

    private ApplyConditionEnum next;
    public ApplyConditionEnum getNext() {
        return next;
    }
    ApplyConditionEnum(ApplyConditionEnum next){
        this.next = next;
    }


}
