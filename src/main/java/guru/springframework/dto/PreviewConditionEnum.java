package guru.springframework.dto;

public enum PreviewConditionEnum{
    marketType(null),
    businessType(marketType);
    private PreviewConditionEnum next;
    public PreviewConditionEnum getNext() {
        return next;
    }
    PreviewConditionEnum(PreviewConditionEnum next){
        this.next = next;
    }
}
