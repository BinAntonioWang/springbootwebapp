package guru.springframework.dto;

public enum ActiveConditionEnum{
    businessType(null);

    private ActiveConditionEnum next;
    public ActiveConditionEnum getNext() {
        return next;
    }
    ActiveConditionEnum(ActiveConditionEnum next){
        this.next = next;
    }

}
