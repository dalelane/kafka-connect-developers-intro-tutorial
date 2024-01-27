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

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.stream.Collectors;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.apache.kafka.connect.storage.OffsetStorageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of a source connector task.
 *
 *  It gets weather data from https://www.weatherapi.com
 */
public class MySourceTask extends SourceTask {

    private static Logger log = LoggerFactory.getLogger(MySourceTask.class);

    // fires at regular/fixed poll intervals
    private Timer fetchTimer;
    // invoked by the timer to call the weather API
    private MyDataFetcher dataFetcher;

    // factory class for creating Connect records from the API responses
    private MyRecordFactory recordFactory;


    @Override
    public void start(Map<String, String> properties) {
        log.info("Starting task {}", properties);

        // Create config options based on the provided properties map
        //  There may be more values in the config than are in the map
        //  if the config class specifies default values that weren't
        //  included in the map.
        AbstractConfig config = new AbstractConfig(MyConnectorConfig.CONFIG_DEF, properties);
        log.debug("Config {}", config);

        // Helper class that will create Connect SourceRecord objects
        recordFactory = new MyRecordFactory(config);

        // read the offset for the last event emitted by a previous
        //  running instance of this connector
        Long offset = recordFactory.getPersistedOffset(getOffsetStorageReader());

        // Helper class for calling the weather API
        dataFetcher = new MyDataFetcher(config, offset);

        // Set up timer to call the weather API at each poll interval
        fetchTimer = new Timer();
        int pollInterval = config.getInt(MyConnectorConfig.MY_POLL_INTERVAL_MS);
        fetchTimer.scheduleAtFixedRate(dataFetcher, 0, pollInterval);
    }


    @Override
    public void stop() {
        log.info("Stopping task");

        if (fetchTimer != null) {
            fetchTimer.cancel();
        }
        fetchTimer = null;
        dataFetcher = null;
        recordFactory = null;
    }


    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        return dataFetcher.getResponses()
            .stream()
            .map(r -> recordFactory.createSourceRecord(r))
            .collect(Collectors.toList());
    }


    @Override
    public String version() {
        return MySourceConnector.VERSION;
    }


    // returns the reader class that provides access to offsets previously stored
    //  by the connector
    //
    // if the connector hasn't run before, this could return null
    private OffsetStorageReader getOffsetStorageReader () {
        if (context == null) {
            log.debug("No context - assuming that this is the first time the Connector has run");
            return null;
        }
        else if (context.offsetStorageReader() == null) {
            log.debug("No offset reader - assuming that this is the first time the Connector has run");
            return null;
        }
        else {
            return context.offsetStorageReader();
        }
    }
}