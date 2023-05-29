package guru.springframework.repositories.json;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import guru.springframework.domain.PreRule;
import guru.springframework.dto.VariableDto;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JsonRepository {
    private String jsonData;

    @PostConstruct
    public void init() throws IOException {
        // 初始化文件
        // 读取JSON文件
        // 读取JSON文件成UTF-8字符串

        jsonData = new String(Files.readAllBytes(Paths.get(
                "C:\\Users\\zyxf\\app\\springbootwebapp\\src\\main\\resources\\scence.json")), StandardCharsets.UTF_8);

    }
    /**
     * 通过关键字获取规则列表
     * @param jsonDataFilter
     * @param keyword
     * @return
     */
    public List<VariableDto.A> getRuleOptionList(String jsonDataFilter, String keyword,String uuid) {
        String jsonDataFiltered = jsonDataFilter==null ? jsonData:jsonDataFilter;
        Configuration config = Configuration.defaultConfiguration()
                .jsonProvider(new GsonJsonProvider())
                .mappingProvider(new GsonMappingProvider());
        TypeRef<List<PreRule>> typeRef = new TypeRef<>() {
        };
        List<String> listResult = JsonPath.parse(jsonDataFiltered).read( "$.."+keyword+"", List.class);
        List<String> listResult2 =listResult.stream().distinct().collect(Collectors.toList());
        List<VariableDto.A> results = new java.util.ArrayList<>();
        for (String text : listResult2) {
            List<PreRule> preRules = JsonPath.using(config).parse(jsonDataFiltered).read( "$[?(@."+keyword+" == '"+text+"')]", typeRef);
            VariableDto.B b = new VariableDto.B();
            b.setData(preRules);
            b.setCardType(preRules.isEmpty()?null:preRules.get(0).getBusinessType());
            b.setCurQueryKey(keyword);
            b.setSessionToken(uuid);
            results.add(new VariableDto.A(text,JsonPath.using(config).parse(b).jsonString()));
        }
        return results;

    }
    public List<VariableDto.A> getRuleOptionList(List<PreRule> preRules, String keyword,String uuid) {
        String jsonRuleJson = JsonPath.parse(preRules).jsonString();
        return  getRuleOptionList(jsonRuleJson, keyword,uuid);

    }
}
