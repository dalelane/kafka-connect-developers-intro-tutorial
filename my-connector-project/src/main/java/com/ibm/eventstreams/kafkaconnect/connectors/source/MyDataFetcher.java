/**
 * Copyright 2024 IBM Corp. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.ibm.eventstreams.kafkaconnect.connectors.source;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TimerTask;
import java.util.TreeSet;

import org.apache.kafka.common.config.AbstractConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.ibm.eventstreams.kafkaconnect.connectors.source.data.weather.Response;
import com.ibm.eventstreams.kafkaconnect.connectors.source.data.weather.ResponseComparator;


/**
 * Invokes the weather REST API.
 *
 *  It stores the API responses in a local in-memory collection, and
 *  provides a getResponses() to retrieve the stored responses.
 */
public class MyDataFetcher extends TimerTask {

    private static Logger log = LoggerFactory.getLogger(MyDataFetcher.class);

    // Maintains a store of previous API responses.
    //  Implemented as a set, so any duplicate poll responses will be ignored.
    private final SortedSet<Response> fetchedRecords;

    // API that the fetcher class will make GET requests to
    private static final String API_URL = "http://api.weatherapi.com/v1/current.json";
    private URL urlObj;

    // JSON parser to convert the API responses into Java objects
    private final Gson apiResponseParser = new Gson();

    // epoch timestamp of the most recent API response
    private Long offset;



    public MyDataFetcher(AbstractConfig config, Long offset) {
        log.info("Creating a data fetcher starting from offset {}", offset);

        fetchedRecords = new TreeSet<>(new ResponseComparator());
        urlObj = getApiUrl(config);
        this.offset = offset;
    }



    @Override
    public void run() {
        try {
            // get the API response
            Reader reader = getApiReader();

            // parse the API response into a Java object
            Response apiResponse = apiResponseParser.fromJson(reader, Response.class);

            // only add the API response to the local cache if the
            //  the timestamp is later than the previous offset
            if (apiResponse.getEpochTimestamp() > offset) {
                synchronized (this) {
                    fetchedRecords.add(apiResponse);
                }
                offset = apiResponse.getEpochTimestamp();
            }
            else {
                log.debug("ignoring duplicate event {}", apiResponse.getCurrent().getLastUpdated());
            }
        }
        catch (IOException e) {
            log.error("Failed to fetch API data", e);
        }
    }


    /**
     * Retrieves stored API responses from the cache in the data fetcher.
     *  Retrieved API responses are removed from the cache.
     */
    public synchronized List<Response> getResponses() {
        List<Response> items = new ArrayList<>();

        while (fetchedRecords.isEmpty() == false) {
            Response nextItem = fetchedRecords.first();

            boolean removed = fetchedRecords.remove(nextItem);
            if (!removed) {
                log.error("failed to remove item from cache {}", nextItem);
            }
            else {
                items.add(nextItem);
            }
        }

        return items;
    }





    // helper function to build the URL including the query parameters
    //  from the connector config
    private URL getApiUrl(AbstractConfig config) {
        String key = config.getString(MyConnectorConfig.MY_API_KEY);
        String location = config.getString(MyConnectorConfig.MY_LOCATION);

        String apiQuery = "?" +
            "key=" + URLEncoder.encode(key, Charset.forName("UTF-8")) + "&" +
            "q=" + URLEncoder.encode(location, Charset.forName("UTF-8"));

        try {
            return URI.create(API_URL + apiQuery).toURL();
        }
        catch (MalformedURLException e) {
            log.error("failed to create URL", e);
            return null;
        }
    }

    // helper function for setting up a URL stream reader
    private Reader getApiReader() throws IOException {
        URLConnection conn = urlObj.openConnection();
        return new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
    }
}