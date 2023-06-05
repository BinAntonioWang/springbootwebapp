package guru.springframework.repositories.json;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;
import guru.springframework.domain.PreRule;
import guru.springframework.dto.VariableDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JsonRepository {
    private static String jsonData;

    @Autowired
    private ResourceLoader resourceLoader;



    @PostConstruct
    public void init() throws IOException {
        // 初始化文件
        // 读取JSON文件
        // 读取JSON文件成UTF-8字符串
        Resource resource = resourceLoader.getResource("classpath:scence.json");
        try (Reader reader = new InputStreamReader(resource.getInputStream(),  StandardCharsets.UTF_8)) {
            jsonData = FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    /**
     * 通过关键字获取规则列表
     * @param jsonDataFilter
     * @param keyword
     * @return
     */
    public List<VariableDto.A> getRuleOptionList(String jsonDataFilter, String keyword,String uuid) {
        String jsonDataFiltered = StringUtils.isEmpty(jsonDataFilter) ? jsonData:jsonDataFilter;
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
            b.setCurQuerySelectedValue(text);
            results.add(new VariableDto.A(text,JsonPath.using(config).parse(b).jsonString()));
        }
        return results;

    }
    public List<VariableDto.A> getRuleOptionList(List<PreRule> preRules, String keyword,String uuid) {
        String jsonRuleJson = JsonPath.parse(preRules).jsonString();
        return  getRuleOptionList(jsonRuleJson, keyword,uuid);

    }
}
