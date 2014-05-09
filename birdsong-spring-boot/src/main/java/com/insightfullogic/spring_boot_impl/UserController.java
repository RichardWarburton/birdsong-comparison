package com.insightfullogic.spring_boot_impl;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        repo.checkUsersPassword(username, password);
        addCookie(USER, username, response);
        addCookie(PASS, password, response);
        return OK;
    }

    private void addCookie(String key, String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        response.addCookie(cookie);
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
    public String follow(@RequestParam("username") String toFollow,
                         HttpServletRequest request) {
        String user = (String) request.getAttribute("user");
        repo.follow(user, toFollow);
        return OK;
    }

    @RequestMapping(value = "unfollow", method = POST)
    @ResponseBody
    public String unfollow(@RequestParam("username") String toFollow,
                           HttpServletRequest request) {
        String user = (String) request.getAttribute("user");
        repo.unfollow(user, toFollow);
        return OK;
    }

    @RequestMapping(value = "information/{name}")
    @ResponseBody
    public Map<String, ?> information(@PathVariable("name") String name) {
        User user = repo.lookupByName(name);
        return ImmutableMap.of("following", toNames(user.getFollowing()),
                               "followers", toNames(user.getFollowers()));
    }

    private List<String> toNames(Set<User> users) {
        return users.stream()
                    .map(User::getName)
                    .collect(toList());
    }

}
