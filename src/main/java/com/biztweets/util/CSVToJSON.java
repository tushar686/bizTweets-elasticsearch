package com.biztweets.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexResponse;

import com.biztweets.model.Metadata;
import com.biztweets.model.Tweet;

public class CSVToJSON {

    private static final String DELIMITER = ",";
    Elasticsearch elasticsearch;
    ObjectMapper objMapper;

    public CSVToJSON() {
        elasticsearch = new Elasticsearch();
        elasticsearch.startElasticsearchConnectionManaully();
        objMapper = new ObjectMapper();
    }

    public static void main(final String[] args) throws UnknownHostException {
        final CSVToJSON convertor = new CSVToJSON();
        for (final File file : new File(args[0]).listFiles()) {
            if (file.getName().endsWith(".csv")) {
                convertor.readCsvToTweets(file);
            }
        }
        convertor.elasticsearch.closeElasticsearchConnection();
    }

    public void readCsvToTweets(final File csvFile) throws UnknownHostException {
        BufferedReader br = null;
        String line = "";
        try {
            br = new BufferedReader(new FileReader(csvFile));
            String[] header;
            header = br.readLine().split(DELIMITER);
            while ((line = br.readLine()) != null) {
                final Tweet tweets =
                    new Tweet("Quickbooks", csvFile.getName().substring(0, csvFile.getName().indexOf('.')), createMetadata(
                        csvFile, line, header), generateRandomTimeStamp());
                writeToElasticsearch(tweets);
            }
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void writeToElasticsearch(final Tweet tweets) throws ElasticsearchException, JsonGenerationException,
        JsonMappingException, IOException {
        final IndexResponse response =
            elasticsearch.client.prepareIndex("biztweets", "tweets").setSource(objMapper.writeValueAsString(tweets))
                .execute().actionGet();
    }

    private String generateFieldNameFromHeaderLine(final String[] header, final int i) {
        if (i < header.length) {
            return header[i];
        }
        return "field" + i;
    }

    private Timestamp generateRandomTimeStamp() {
        final long offset = Timestamp.valueOf("2010-01-01 00:00:00").getTime();
        final long end = Timestamp.valueOf("2014-03-01 00:00:00").getTime();
        final long diff = end - offset + 1;
        return new Timestamp(offset + (long)(Math.random() * diff));
    }

    private List<Metadata> createMetadata(final File csvFile, final String line, final String[] header) {
        final List<Metadata> metadata = new ArrayList<Metadata>();
        final String[] values = line.split(DELIMITER);
        for (int i = 0; i < values.length; i++) {
            metadata.add(new Metadata(generateFieldNameFromHeaderLine(header, i), values[i]));
        }
        return metadata;
    }
}
