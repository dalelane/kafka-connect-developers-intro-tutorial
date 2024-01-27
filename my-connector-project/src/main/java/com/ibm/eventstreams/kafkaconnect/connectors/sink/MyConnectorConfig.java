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

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;


/**
 * Defines the config that the sink connector expects
 *  Kafka Connect to provide.
 */
public class MyConnectorConfig {

    public static final String MY_SLACK_TOKEN = "my.slack.token";
    public static final String MY_SLACK_CHANNEL = "my.slack.channel";


    public static final ConfigDef CONFIG_DEF = new ConfigDef()
       .define(MY_SLACK_TOKEN,
               Type.STRING,
               "xoxp-000000000000-000000000000-0000000000000-00000000000000000000000000000000",
               new ConfigDef.NonEmptyString(),
               Importance.HIGH,
               "Slack token. A token usually begins with xoxb- (bot token) or xoxp- (user token). "
               + "You get them from each workspace that an app has been installed. "
               + "Visit https://api.slack.com/apps to create your token.")
       .define(MY_SLACK_CHANNEL,
               Type.STRING,
               "C000000000V",
               new ConfigDef.NonEmptyString(),
               Importance.HIGH,
               "ID for the channel to send messages to");
}