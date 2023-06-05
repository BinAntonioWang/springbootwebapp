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
        public String getBusinessText(String key) {
            try {
                return ApplyConditionEnum.valueOf(key).getDescription();
            } catch (Exception e) {
                return "EDD";
            }

        }

        @Override
        public List<String> allEnums() {
            return Arrays.stream(ApplyConditionEnum.values()).map(ApplyConditionEnum::name).collect(Collectors.toList());
        }
    },"授信"),
    C20(new Businessable(){
        @Override
        public String getNextBusinessAttr(String key) {
            return LoanConditionEnum.valueOf(key).getNext().name();
        }

        @Override
        public String getTemplateId() {
            return "ctp_AAgb4GZKkP3n";
        }

        @Override
        public String getBusinessText(String key) {
            try {
                return LoanConditionEnum.valueOf(key).getDescription();
            } catch (Exception e) {
                return "EDD";
            }
        }
        @Override
        public List<String> allEnums() {
            return Arrays.stream(LoanConditionEnum.values()).map(LoanConditionEnum::name).collect(Collectors.toList());
        }
    },"用信"),
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
        public String getBusinessText(String key) {
            try {
                return AdjConditionEnum.valueOf(key).getDescription();
            } catch (Exception e) {
                return "EDD";
            }
        }
        @Override
        public List<String> allEnums() {
            return Arrays.stream(AdjConditionEnum.values()).map(AdjConditionEnum::name).collect(Collectors.toList());
        }
    },"调额"),
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
        public String getBusinessText(String key) {
            try {
                return PriceConditionEnum.valueOf(key).getDescription();
            } catch (Exception e) {
                return "EDD";
            }
        }
        @Override
        public List<String> allEnums() {
            return Arrays.stream(PriceConditionEnum.values()).map(PriceConditionEnum::name).collect(Collectors.toList());
        }
    },"重检"),
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
        public String getBusinessText(String key) {
            try {
                return ActiveConditionEnum.valueOf(key).getDescription();
            } catch (Exception e) {
                return "EDD";
            }
        }
        @Override
        public List<String> allEnums() {
            return Arrays.stream(ActiveConditionEnum.values()).map(ActiveConditionEnum::name).collect(Collectors.toList());
        }
    },"激活"),
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
        public String getBusinessText(String key) {
            try {
                return PreviewConditionEnum.valueOf(key).getDescription();
            } catch (Exception e) {
                return "EDD";
            }
        }
        @Override
        public List<String> allEnums() {
            return Arrays.stream(PreviewConditionEnum.values()).map(PreviewConditionEnum::name).collect(Collectors.toList());
        }
    },"营销重检");
    private Businessable able;
    private String description;
    public Businessable getAble() {
        return able;
    }

    public String getDescription() {
        return description;
    }


    CardBusinessType(Businessable able,String description){
        this.description = description;
        this.able = able;
    }
}
