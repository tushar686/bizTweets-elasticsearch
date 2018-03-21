package com.biztweets.service;

import static com.biztweets.util.Constants.FOLLOW_TYPE;
import static com.biztweets.util.Constants.INDEX;
import static com.biztweets.util.Constants.INSERTIONTIME_FIELD;
import static com.biztweets.util.Constants.KEY_FIELD;
import static com.biztweets.util.Constants.TWEETS_PERPAGE;
import static com.biztweets.util.Constants.TYPE;
import static com.biztweets.util.Constants.USER_FIELD_IN_FOLLOW;
import static com.biztweets.util.Constants.VALUE_FIELD;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.OrFilterBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.biztweets.model.Follow;
import com.biztweets.model.Tweet;
import com.biztweets.model.TweetModel;
import com.biztweets.util.Elasticsearch;
import com.biztweets.util.PropertyReader;

@Repository
public class TweetsService {

    private static final String KEY_VALUE_DELIMITER = "---";
    @Autowired
    Elasticsearch elasticsearch;
    @Autowired
    private PropertyReader propertyReader;
    private int tweetsPerPage = 20;

    @PostConstruct
    public void init() {
        tweetsPerPage = Integer.parseInt(propertyReader.getProperty(TWEETS_PERPAGE));
    }

    public List<TweetModel> searchTweets(final String searchString, final int cursor) {
        final SearchResponse response =
            elasticsearch.client.prepareSearch(propertyReader.getProperty(INDEX)).setTypes(propertyReader.getProperty(TYPE))
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchQuery(VALUE_FIELD, searchString)).setFrom(tweetsPerPage * cursor)
                .setSize(tweetsPerPage).setExplain(true).addSort(INSERTIONTIME_FIELD, SortOrder.DESC).execute().actionGet();

        return convertSourceJSONToTweetObject(response);
    }

    public List<TweetModel> getFollowingTweets(final String user, final int cursor) {
        final List<Follow> followings = getFollowType(user);
        final OrFilterBuilder orFilter = FilterBuilders.orFilter();

        for (final Follow follow : followings) {
            final AndFilterBuilder andFilter = FilterBuilders.andFilter();
            final String key = getToken(follow.getFollowQuery(), 0);
            final String value = getToken(follow.getFollowQuery(), 1);
            andFilter.add(FilterBuilders.queryFilter(QueryBuilders.matchQuery(KEY_FIELD, key)));
            andFilter.add(FilterBuilders.queryFilter(QueryBuilders.matchQuery(VALUE_FIELD, value)));
            orFilter.add(FilterBuilders.orFilter(andFilter));
        }

        final SearchResponse response =
            elasticsearch.client.prepareSearch(propertyReader.getProperty(INDEX)).setTypes(propertyReader.getProperty(TYPE))
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.filteredQuery(QueryBuilders.matchAllQuery(), orFilter))
                .setFrom(tweetsPerPage * cursor).setSize(tweetsPerPage).setExplain(true)
                .addSort(INSERTIONTIME_FIELD, SortOrder.DESC).execute().actionGet();

        return convertSourceJSONToTweetObject(response);
    }

    private String getToken(final String followQuery, final int tokenNumber) {
        return StringUtils.split(followQuery, KEY_VALUE_DELIMITER)[tokenNumber];
    }

    public List<TweetModel> getTweets(final String user, final int cursor) {
        final SearchResponse response =
            elasticsearch.client.prepareSearch(propertyReader.getProperty(INDEX)).setTypes(propertyReader.getProperty(TYPE))
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setFrom(tweetsPerPage * cursor).setSize(tweetsPerPage)
                .setExplain(true).addSort(INSERTIONTIME_FIELD, SortOrder.DESC).execute().actionGet();

        return convertSourceJSONToTweetObject(response);
    }

    public TweetModel getTweet(final String id) {
        final GetResponse response =
            elasticsearch.client.prepareGet(propertyReader.getProperty(INDEX), propertyReader.getProperty(TYPE), id)
                .execute().actionGet();

        return ConvertSourceToTweetModel(new ObjectMapper(), response.getSourceAsString(), response.getId());
    }

    public boolean insertFollow(final Follow follow) {
        final ObjectMapper objMapper = new ObjectMapper();
        try {
            final IndexResponse response =
                elasticsearch.client
                    .prepareIndex(propertyReader.getProperty(INDEX), propertyReader.getProperty(FOLLOW_TYPE))
                    .setSource(objMapper.writeValueAsString(follow)).execute().actionGet();
            return response.isCreated();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Follow> getFollowType(final String user) {
        final SearchResponse response =
            elasticsearch.client.prepareSearch(propertyReader.getProperty(INDEX))
                .setTypes(propertyReader.getProperty(FOLLOW_TYPE)).setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchQuery(USER_FIELD_IN_FOLLOW, user)).execute().actionGet();

        return convertSourceJSONToFollowObject(response);
    }

    private List<TweetModel> convertSourceJSONToTweetObject(final SearchResponse response) {
        final ObjectMapper objMapper = new ObjectMapper();
        final List<TweetModel> tweetList = new ArrayList<TweetModel>();
        final Iterator<SearchHit> iterator = response.getHits().iterator();

        while (iterator.hasNext()) {
            final SearchHit searchHit = iterator.next();
            final TweetModel model = ConvertSourceToTweetModel(objMapper, searchHit.getSourceAsString(), searchHit.getId());
            tweetList.add(model);
        }

        return tweetList;
    }

    private TweetModel ConvertSourceToTweetModel(final ObjectMapper objMapper, final String sourceString, final String id) {
        final long currentTime = System.currentTimeMillis();
        Tweet tweet = null;
        try {
            tweet = objMapper.readValue(sourceString, Tweet.class);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        final Period period = getAgoPeriod(tweet.getInsertionTime(), currentTime);
        return new TweetModel(tweet, howMuchAgo(period), toolTipForAgo(period), id);
    }

    private List<Follow> convertSourceJSONToFollowObject(final SearchResponse response) {
        final ObjectMapper objMapper = new ObjectMapper();
        final List<Follow> followList = new ArrayList<Follow>();
        final Iterator<SearchHit> iterator = response.getHits().iterator();

        while (iterator.hasNext()) {
            try {
                final SearchHit searchHit = iterator.next();
                final Follow model = objMapper.readValue(searchHit.getSourceAsString(), Follow.class);
                followList.add(model);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        return followList;
    }

    private String howMuchAgo(final Period period) {
        if (period.getYears() != 0) {
            return period.getYears() + " Yrs ago";
        }
        if (period.getMonths() != 0) {
            return period.getMonths() + " month ago";
        }
        if (period.getDays() != 0) {
            return period.getDays() + " d ago";
        }
        if (period.getHours() != 0) {
            return period.getHours() + " hr ago";
        }
        if (period.getMinutes() != 0) {
            return period.getMinutes() + " min ago";
        }

        return period.getSeconds() + " sec ago";

    }

    private String toolTipForAgo(final Period period) {
        final StringBuilder toolTip = new StringBuilder();

        if (period.getYears() != 0) {
            toolTip.append(period.getYears() + " Yrs ");
        }
        if (period.getMonths() != 0) {
            toolTip.append(period.getMonths() + " month ");
        }
        if (period.getDays() != 0) {
            toolTip.append(period.getDays() + " d ");
        }
        if (period.getHours() != 0) {
            toolTip.append(period.getHours() + " hr ");
        }
        if (period.getMinutes() != 0) {
            toolTip.append(period.getMinutes() + " min ");
        }
        if (period.getSeconds() != 0) {
            toolTip.append(period.getSeconds() + " sec ");
        }

        return toolTip.append("ago").toString();

    }

    private Period getAgoPeriod(final Timestamp insertionTime, final long currentTime) {
        final Interval interval = new Interval(insertionTime.getTime(), currentTime);
        final Period period = interval.toPeriod();
        return period;
    }

}
