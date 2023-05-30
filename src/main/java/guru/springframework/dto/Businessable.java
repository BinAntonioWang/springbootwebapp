package guru.springframework.dto;

import java.util.List;

public abstract class Businessable {
//    ApplyConditionEnum nextQueryEnum1 = ApplyConditionEnum.valueOf(optionSelected.getCurQueryKey()).getNext();
//    String templateId = "ctp_AAg2dpK7feP7";
    public abstract String getNextBusinessAttr(String key);
    public abstract String getTemplateId();
    public abstract String getNextBusinessText(String key);

    public List<String> allEnums() {

        return null;
    }
}
