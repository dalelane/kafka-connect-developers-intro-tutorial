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

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.connect.errors.DataException;
import org.apache.kafka.connect.errors.RetriableException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiErrorResponse;
import com.slack.api.methods.SlackApiException;


public class MySinkTask extends SinkTask {

    private static Logger log = LoggerFactory.getLogger(MySinkTask.class);

    private MethodsClient slackClient;
    private String channelId;


    @Override
    public void start(Map<String, String> properties) {
        log.info("Starting task {}", properties);

        // Create config options based on the provided properties map
        //  There may be more values in the config than are in the map
        //  if the config class specifies default values that weren't
        //  included in the map.
        AbstractConfig config = new AbstractConfig(MyConnectorConfig.CONFIG_DEF, properties);
        log.debug("Config {}", config);

        // prepare Slack client that the task will use
        channelId = config.getString(MyConnectorConfig.MY_SLACK_CHANNEL);
        String token = config.getString(MyConnectorConfig.MY_SLACK_TOKEN);

        slackClient = Slack.getInstance().methods(token);
    }



    @Override
    public void put(Collection<SinkRecord> records) {
        if (records.size() > 0) {
            log.debug("Received records from Connect");
        }

        try {
            for (SinkRecord record : records) {
                slackClient.chatPostMessage(req -> req
                    .text(record.value().toString())
                    .username("Kafka Connect")
                    .iconUrl("https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Apache_kafka-icon.svg/64px-Apache_kafka-icon.svg.png")
                    .channel(channelId));
            }
        }
        catch (IOException exc) {
            log.error("Failed to send message to Slack", exc);
            throw new DataException("Failed to deliver message to Slack", exc);
        }
        catch (SlackApiException slackExc) {
            final String failure = "Failed to send message to Slack";
            log.error(failure, slackExc);

            if (isSlackErrorRetriable(slackExc.getError())) {
                throw new RetriableException(failure, slackExc);
            }
            else {
                throw new DataException(failure, slackExc);
            }
        }
    }


    @Override
    public void stop() {
        log.info("Stopping task");
        slackClient = null;
    }


    @Override
    public String version() {
        return MySinkConnector.VERSION;
    }


    // Helper function used after we have failed to process a message
    //  given to us by Kafka Connect
    //
    // This is used to decide whether to ask Kafka Connect to give us
    //  the message again after a timeout interval to try and process
    //  it again, or if the error looks severe enough that the
    //  Sink Task should terminate and go into an error state.
    private boolean isSlackErrorRetriable(SlackApiErrorResponse error) {
        if (error != null) {
            switch (error.getError()) {
                case "internal_error":
                case "request_timeout":
                case "service_unavailable":
                    return true;
                default:
                // case "channel_not_found":
                // case "duplicate_channel_not_found":
                // case "is_archived":
                // case "message_limit_exceeded":
                // case "msg_too_long":
                // case "no_text":
                // case "not_in_channel":
                // case "rate_limited":
                // case "access_denied":
                // case "no_permission":
                // case "fatal_error":
                    return false;
            }
        }
        return false;
    }
}
