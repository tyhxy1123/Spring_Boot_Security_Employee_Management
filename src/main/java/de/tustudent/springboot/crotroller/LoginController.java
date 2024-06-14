package de.tustudent.springboot.crotroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/employees")
    public String employees() {
        return "employees";
    }

    @GetMapping("/showMyLoginPage")
    public String loginPage(){
        return "fancy-login";
    }

    @GetMapping("/leaders")
    public String leaderMeeting() {
        return "leaders";
    }

    @GetMapping("/systems")
    public String systemMeeting(){
        return "systems";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }
}
