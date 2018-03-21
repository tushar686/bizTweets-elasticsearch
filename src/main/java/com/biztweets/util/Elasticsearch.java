package com.biztweets.util;

import static com.biztweets.util.Constants.CLUSTERNAME;
import static com.biztweets.util.Constants.MASTERSIP;
import static com.biztweets.util.Constants.PORT;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Elasticsearch {

    public Client client;

    @Autowired
    private PropertyReader propertyReader;

    @PostConstruct
    public void startElasticsearchConnection() {
        final Settings settings =
            ImmutableSettings.settingsBuilder().put("cluster.name", propertyReader.getProperty(CLUSTERNAME)).build();

        client =
            new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(propertyReader
                .getProperty(MASTERSIP), new Integer(propertyReader.getProperty(PORT)).intValue()));
    }

    public void startElasticsearchConnectionManaully() {
        final Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "tusharcluster").build();
        client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("192.168.1.6", 9300));
    }

    @PreDestroy
    public void closeElasticsearchConnection() {
        client.close();
    }

    public static void main(final String[] args) {
        final Elasticsearch elasticsearch = new Elasticsearch();
        elasticsearch.startElasticsearchConnection();

        final SearchResponse response =
            elasticsearch.client.prepareSearch("biztweets").setTypes("tweetsw")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(QueryBuilders.matchQuery("metadata.value", "F"))
                .setFrom(0).setSize(10).setExplain(true).addSort("insertionTime", SortOrder.DESC).execute().actionGet();

        System.out.println(response.getTookInMillis());
        System.out.println(response.getHits().hits().length);

        for (final SearchHit hit : response.getHits()) {
            System.out.println("Hit ID: " + hit.getSourceAsString());
        }

        elasticsearch.closeElasticsearchConnection();
    }

}
