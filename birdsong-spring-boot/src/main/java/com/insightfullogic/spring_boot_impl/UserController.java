package com.insightfullogic.spring_boot_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * .
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    private final UserRepository repo;

    @Autowired
    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    @RequestMapping(value = "login", method = POST)
    @ResponseBody
    public String login(@RequestParam("username") String username,
                      @RequestParam("password") String password) {
        repo.lookupByName(username, password);
        return "";
    }

    @RequestMapping("register")
    public void register() {

        // throw new ForbiddenException();
    }

}
