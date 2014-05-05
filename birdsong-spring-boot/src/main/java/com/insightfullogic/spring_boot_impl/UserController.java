package com.insightfullogic.spring_boot_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * .
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    public static final String USER = "user";
    public static final String PASS = "pass";

    private static final String OK = "ok";

    private final UserRepository repo;

    @Autowired
    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    @RequestMapping(value = "login", method = POST)
    @ResponseBody
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpServletResponse response) {
        repo.lookupByNameAndPassword(username, password);
        response.addCookie(new Cookie(USER, username));
        response.addCookie(new Cookie(PASS, password));
        return OK;
    }

    @RequestMapping(value = "register", method = POST)
    @ResponseBody
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password) {
        repo.register(username, password);
        return OK;
    }

    @RequestMapping(value = "follow", method = POST)
    @ResponseBody
    public String follow(@RequestParam("username") String toFollow, HttpServletRequest request) {
        String user = (String) request.getAttribute("user");
        repo.follow(user, toFollow);
        return OK;
    }

    @RequestMapping(value = "information/{name}")
    @ResponseBody
    public Map<String, ?> information(@PathVariable("name") String name) {
        User user = repo.lookupByName(name);
        List<String> following = toNames(user.getFollowing());
        List<String> followers = toNames(user.getFollowers());

        Map<String, Object> response = new HashMap<>();
        response.put("following", following);
        response.put("followers", followers);
        return response;
    }

    private List<String> toNames(Set<User> users) {
        return users.stream()
                    .map(User::getName)
                    .collect(toList());
    }

}
