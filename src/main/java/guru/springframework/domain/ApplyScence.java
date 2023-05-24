package guru.springframework.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ApplyScence {
    private List<Map<String,List<PreRule>>> productCodeList;
    private List<Map<String,List<PreRule>>> channelIdList;
    private List<Map<String,List<PreRule>>> merchantIdList;
}
