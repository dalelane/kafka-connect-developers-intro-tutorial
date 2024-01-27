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

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.ConfigDef.Type;


/**
 * Defines the config that the source connector expects 
 *  Kafka Connect to provide.
 */
public class MyConnectorConfig {

    public static final String MY_API_KEY = "my.weather.api.key";
    public static final String MY_LOCATION = "my.weather.location";
    public static final String MY_POLL_INTERVAL_MS = "my.weather.poll.interval.ms";


    public static final ConfigDef CONFIG_DEF = new ConfigDef()
       .define(MY_API_KEY,
               Type.STRING,
               "0000000000000000000000000000000",
               new ConfigDef.NonEmptyString(),
               Importance.HIGH,
               "API key for the weatherapi service. "
               + "Create a free key at https://www.weatherapi.com/signup.aspx")
       .define(MY_LOCATION,
               Type.STRING,
               "SO21 2JN",
               new ConfigDef.NonEmptyString(),
               Importance.HIGH,
               "Location to emit weather events for. "
               + "This can be a US Zipcode, UK Postcode, Canada Postalcode, "
               + "IP address, Latitude/Longitude (decimal degree) or city name.")
       .define(MY_POLL_INTERVAL_MS,
               Type.INT,
               60000, // by default, poll the API every minute
               ConfigDef.Range.atLeast(1000),  // poll interval should be at least 1 second
               Importance.HIGH,
               "How frequently the REST API should be polled (in milliseconds)");
}