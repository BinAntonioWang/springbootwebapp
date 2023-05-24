package guru.springframework.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PriceScence {
    private List<Map<String,List<PreRule>>> productCodeList;
}
