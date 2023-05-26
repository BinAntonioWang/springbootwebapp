package guru.springframework.dto;

import guru.springframework.domain.PreRule;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class VariableDto {
    private Map<String, List<A>> template_variable = new HashMap<>();
    private String template_id;
    @Data
    public static class A{
        private String text;
        private String value;

        public A(String text, String value) {
            this.text = text;
            this.value = value;
        }
    }
    @Data
    public static class B{
        private List<PreRule> data;
        private String curQueryKey;
        private String cardType;
        private String sessionToken;
    }
}
