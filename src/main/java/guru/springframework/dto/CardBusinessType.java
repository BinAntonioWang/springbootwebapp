package guru.springframework.dto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum CardBusinessType {
    C10(new Businessable(){
        @Override
        public String getNextBusinessAttr(String key) {
            return ApplyConditionEnum.valueOf(key).getNext().name();
        }

        @Override
        public String getTemplateId() {
            return "ctp_AAgb4GZKkP3n";
        }

        @Override
        public String getNextBusinessText(String key) {
            return ApplyConditionEnum.valueOf(key).getNext().getDescription();
        }

        @Override
        public List<String> allEnums() {
            return Arrays.stream(ApplyConditionEnum.values()).map(ApplyConditionEnum::name).collect(Collectors.toList());
        }
    }),
    C20(new Businessable(){
        @Override
        public String getNextBusinessAttr(String key) {
            return LoanConditionEnum.valueOf(key).getNext().name();
        }

        @Override
        public String getTemplateId() {
            return "ctp_AAgeT0ozR6tF";
        }

        @Override
        public String getNextBusinessText(String key) {
            return LoanConditionEnum.valueOf(key).getNext().getDescription();
        }
    }),
    C30(new Businessable(){
        @Override
        public String getNextBusinessAttr(String key) {
            return AdjConditionEnum.valueOf(key).getNext().name();
        }

        @Override
        public String getTemplateId() {
            return "ctp_AAgXWjFf1nWd";
        }

        @Override
        public String getNextBusinessText(String key) {
            return AdjConditionEnum.valueOf(key).getNext().getDescription();
        }
    }),
    C40(new Businessable(){
        @Override
        public String getNextBusinessAttr(String key) {
            return PriceConditionEnum.valueOf(key).getNext().name();
        }

        @Override
        public String getTemplateId() {
            return "ctp_AAgXTG83XGOD";
        }

        @Override
        public String getNextBusinessText(String key) {
            return PriceConditionEnum.valueOf(key).getNext().getDescription();
        }
    }),
    C22(new Businessable(){
        @Override
        public String getNextBusinessAttr(String key) {
            return ActiveConditionEnum.valueOf(key).getNext().name();
        }

        @Override
        public String getTemplateId() {
            return "ctp_AAg2dpK7feP7";
        }

        @Override
        public String getNextBusinessText(String key) {
            return ActiveConditionEnum.valueOf(key).getNext().getDescription();
        }
    }),
    C60(new Businessable(){
        @Override
        public String getNextBusinessAttr(String key) {
            return PreviewConditionEnum.valueOf(key).getNext().name();
        }

        @Override
        public String getTemplateId() {
            return "ctp_AAgXnZLxvcrm";
        }

        @Override
        public String getNextBusinessText(String key) {
            return PreviewConditionEnum.valueOf(key).getNext().getDescription();
        }
    });
    private Businessable able;
    public Businessable getAble() {
        return able;
    }
    CardBusinessType(Businessable able){
        this.able = able;
    }
}
