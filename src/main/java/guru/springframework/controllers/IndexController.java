package guru.springframework.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @Value("${feishu.app.verificationToken}")
    private String verificationToken;
    @Value("${feishu.app.encryptKey}")
    private String encryptKey;
    @RequestMapping("/")
    String index(){
        System.out.println("verificationToken: " + verificationToken);
        System.out.println("encryptKey: " + encryptKey);
        return "index";
    }
}
