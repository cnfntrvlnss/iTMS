package backend.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



//TODO 由于放在WEB-INF所以在对主页跳转不能直接链接访问,等程序写多后要善于总结比较后改正，这样写太坑
@Controller
@RequestMapping("main.do")
public class MainController {

    @RequestMapping("admin")
    public String adminMainView() {
        return "/admin/main";
    }

   
}
