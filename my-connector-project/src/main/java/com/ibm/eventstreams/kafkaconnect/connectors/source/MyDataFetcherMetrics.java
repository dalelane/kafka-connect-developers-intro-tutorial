/**
 * Copyright 2026 IBM Corp. All Rights Reserved.
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

import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.common.metrics.MetricConfig;
import org.apache.kafka.common.metrics.PluginMetrics;
import org.apache.kafka.common.metrics.Sensor;
import org.apache.kafka.common.metrics.stats.Avg;
import org.apache.kafka.common.metrics.stats.CumulativeCount;
import org.apache.kafka.common.metrics.stats.Max;
import org.apache.kafka.common.metrics.stats.Min;
import org.apache.kafka.common.metrics.stats.Rate;
import org.apache.kafka.connect.source.SourceTaskContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Metrics recorder for MyDataFetcher. Records metrics about the use
 *  of the weather API in MySourceConnector
 */
public class MyDataFetcherMetrics {

    private static final Logger log = LoggerFactory.getLogger(MyDataFetcherMetrics.class);

    private Sensor apiCallSensor;
    private Sensor duplicateResponsesSensor;
    private Sensor apiFailureSensor;
    private Sensor apiResponseTimeSensor;

    private LinkedHashMap<String, String> tags = new LinkedHashMap<>();

    private final MetricConfig HOUR_WINDOW = new MetricConfig()
        .timeWindow(1, TimeUnit.HOURS)
        .samples(30);

    /**
     * Registers metrics that the connector will emit.
     *
     * @param context  used for access to PluginMetrics
     */
    public void initialize(SourceTaskContext context) {
        if (context == null) {
            log.info("metrics will not be recorded (no task context available)");
            return;
        }

        PluginMetrics pluginMetrics = context.pluginMetrics();

        // sensor for tracking all API call attempts
        apiCallSensor = pluginMetrics.addSensor("my-weather-connector-api-calls");
        apiCallSensor.add(
            pluginMetrics.metricName("num-calls", "Number of API calls made", tags),
            new CumulativeCount()
        );

        // Create sensor for tracking duplicate responses filtered
        duplicateResponsesSensor = pluginMetrics.addSensor("my-weather-connector-duplicate-responses");
        duplicateResponsesSensor.add(
            pluginMetrics.metricName("ignored-responses", "Number of duplicate API responses ignored", tags),
            new CumulativeCount()
        );

        // sensor for tracking failed API calls
        apiFailureSensor = pluginMetrics.addSensor("my-weather-connector-api-errors");
        apiFailureSensor.add(
            pluginMetrics.metricName("errors-rate", "Failed API calls per hour", tags),
            new Rate(TimeUnit.HOURS),
            HOUR_WINDOW
        );
        apiFailureSensor.add(
            pluginMetrics.metricName("errors-count", "Number of API calls that failed", tags),
            new CumulativeCount()
        );

        // sensor for tracking API response times
        apiResponseTimeSensor = pluginMetrics.addSensor("my-weather-connector-api-response-times");
        apiResponseTimeSensor.add(
            pluginMetrics.metricName("time-avg", "Average API response time in nanoseconds", tags),
            new Avg(),
            HOUR_WINDOW
        );
        apiResponseTimeSensor.add(
            pluginMetrics.metricName("time-min", "Minimum API response time in nanoseconds", tags),
            new Min(),
            HOUR_WINDOW
        );
        apiResponseTimeSensor.add(
            pluginMetrics.metricName("time-max", "Maximum API response time in nanoseconds", tags),
            new Max(),
            HOUR_WINDOW
        );

        log.info("Metrics initialized successfully");
    }

    /** Record that an API call attempt has been made. */
    public void recordApiCall() {
        if (apiCallSensor != null) {
            apiCallSensor.record();
        }
    }

    /** Records that an API call failed. */
    public void recordApiCallFailure() {
        if (apiFailureSensor != null) {
            apiFailureSensor.record();
        }
    }

    /**
     * Records that an API call returned a response that was ignored.
     *
     * This is called when an API response has the same/earlier timestamp
     * than a previously processed response, indicating it's a duplicate.
     */
    public void recordDuplicateResponseFiltered() {
        if (duplicateResponsesSensor != null) {
            duplicateResponsesSensor.record();
        }
    }

    /** Record the response time (in nanoseconds) for a successful API call. */
    public void recordApiCallSuccess(long responseTimeNs) {
        if (apiResponseTimeSensor != null) {
            apiResponseTimeSensor.record(responseTimeNs);
        }
    }
}
