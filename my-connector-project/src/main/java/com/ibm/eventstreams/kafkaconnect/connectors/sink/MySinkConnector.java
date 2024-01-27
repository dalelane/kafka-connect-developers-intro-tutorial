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
package com.ibm.eventstreams.kafkaconnect.connectors.sink;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Example of a Kafka Connect sink connector.
 *
 *  It sends messages to Slack.
 */
public class MySinkConnector extends SinkConnector {

    // Version of the connector that will be returned by Connect
    //  in Connect's REST API.
    //
    // It helps to keep this consistent with the version in pom.xml
    protected static final String VERSION = "0.0.1";


    private final Logger log = LoggerFactory.getLogger(MySinkConnector.class);

    private Map<String, String> configProps = null;


    @Override
    public ConfigDef config() {
        return MyConnectorConfig.CONFIG_DEF;
    }


    @Override
    public Class<? extends Task> taskClass() {
        return MySinkTask.class;
    }


    @Override
    public void start(Map<String, String> props) {
        log.info("Starting connector {}", props);
        configProps = props;
    }


    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        // For many connectors, individual tasks will all be
        //  given the same config.
        // If you need individual tasks to have custom config
        //  then this can be applied here.
        List<Map<String, String>> taskConfigs = new ArrayList<>();
        for (int task = 0; task < maxTasks; task++) {
            taskConfigs.add(configProps);
        }
        return taskConfigs;
    }


    @Override
    public void stop() {
        log.info("Stopping connector");
    }


    @Override
    public String version() {
        return VERSION;
    }
}
