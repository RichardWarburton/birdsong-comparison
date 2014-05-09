package com.insightfullogic.spring_boot_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * .
 */
@Controller
@RequestMapping("/")
public class SongController {

    private static final String OK = "ok";

    private final SongRepository repo;

    @Autowired
    public SongController(SongRepository repo) {
        this.repo = repo;
    }

    @RequestMapping(value = "sing", method = POST)
    @ResponseBody
    public String sing(@RequestBody String lyrics,
                       HttpServletRequest request) {
        repo.sing(lyrics, getUser(request), null);
        return OK;
    }

    @RequestMapping(value = "cover/{originalId}", method = POST)
    @ResponseBody
    public String cover(@PathVariable("originalId") String originalId,
                        @RequestBody String lyrics,
                        HttpServletRequest request) {
        repo.sing(lyrics, getUser(request), originalId);
        return OK;
    }

    @RequestMapping(value = "listen/{since}")
    @ResponseBody
    public SongBook listen(@PathVariable("since") String since,
                           HttpServletRequest request) {
        SongBook songBook = repo.listen(getUser(request), since);
        return songBook;
    }

    private String getUser(HttpServletRequest request) {
        return (String) request.getAttribute("user");
    }

}
