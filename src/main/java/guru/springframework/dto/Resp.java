package guru.springframework.dto;

import lombok.Data;

@Data
public class Resp {
    private VariableDto data;
    private String type;
}
