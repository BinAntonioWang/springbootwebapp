package guru.springframework.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PreviewScence {
    private List<Map<String,List<PreRule>>> marketTypeList;
}
