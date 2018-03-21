package com.biztweets.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.biztweets.model.Follow;
import com.biztweets.model.TweetModel;
import com.biztweets.service.TweetsService;

@Controller
public class BizTweetsController {

    @Autowired
    private TweetsService tweetsService;

    @RequestMapping(value = "/searchTweets", method = RequestMethod.GET)
    @ResponseBody
    public List<TweetModel> searchTweets(@RequestParam final String searchString, @RequestParam final int cursor) {
        return tweetsService.searchTweets(searchString, cursor);
    }

    @RequestMapping(value = "/getTweets", method = RequestMethod.GET)
    @ResponseBody
    public List<TweetModel> getTweets(@RequestParam("user") final String user, @RequestParam("cursor") final int cursor) {
        return tweetsService.getTweets(user, cursor);
    }

    @RequestMapping(value = "/getFollowingTweets", method = RequestMethod.GET)
    @ResponseBody
    public List<TweetModel> getFollowingTweets(@RequestParam("user") final String user,
        @RequestParam("cursor") final int cursor) {
        return tweetsService.getFollowingTweets(user, cursor);
    }

    @RequestMapping(value = "/getTweet/{id}", method = RequestMethod.GET)
    @ResponseBody
    public TweetModel getTweet(@PathVariable("id") final String id) {
        return tweetsService.getTweet(id);
    }

    @RequestMapping(value = "/follow", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    public void follow(@RequestBody final Follow follow) {
        tweetsService.insertFollow(follow);
    }

    @RequestMapping(value = "/getFollowType/{user}", method = RequestMethod.GET)
    @ResponseBody
    public List<Follow> getFollowType(@PathVariable("user") final String user) {
        return tweetsService.getFollowType(user);
    }

}
