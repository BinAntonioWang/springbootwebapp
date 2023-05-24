package guru.springframework.repositories.json;

import com.jayway.jsonpath.JsonPath;
import guru.springframework.domain.PreRule;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
@Component
public class JsonRepository {
    private String jsonData;

    @PostConstruct
    public void init() throws IOException {
        // 读取JSON文件
        // 读取JSON文件成UTF-8字符串
        jsonData = new String(Files.readAllBytes(Paths.get(
                "C:\\Users\\zyxf\\app\\springbootwebapp\\src\\main\\resources\\scence.json")), StandardCharsets.UTF_8);

    }
    public List<PreRule> getResult() {
        List<PreRule> listresult = JsonPath.parse(jsonData).read( "$[?(@.businessType == '40')]", List.class);
        return listresult;

    }
//
//    public static void main(String[] args) throws IOException {
//        JsonRepository jsonRepository = new JsonRepository();
//        jsonRepository.init();
//        System.out.println(jsonRepository.getResult().get(0));
//    }

}
