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

import java.util.Collections;
import java.util.Map;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.storage.OffsetStorageReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.eventstreams.kafkaconnect.connectors.source.data.weather.Response;

public class MyRecordFactory {

    private static Logger log = LoggerFactory.getLogger(MyRecordFactory.class);

    private String location;

    public MyRecordFactory(AbstractConfig config) {
        location = config.getString(MyConnectorConfig.MY_LOCATION);
    }

    private static final Schema COORDS_SCHEMA = SchemaBuilder.struct()
        .name("coordinates")
            .field("latitude", Schema.FLOAT64_SCHEMA)
            .field("longitude", Schema.FLOAT64_SCHEMA)
        .build();

    private static final Schema LOCATION_SCHEMA = SchemaBuilder.struct()
        .name("location")
            .field("name", Schema.STRING_SCHEMA)
            .field("region", Schema.STRING_SCHEMA)
            .field("country", Schema.STRING_SCHEMA)
            .field("coordinates", COORDS_SCHEMA)
        .build();

    private static final Schema TEMPERATURE_SCHEMA = SchemaBuilder.struct()
        .name("temperature")
            .field("actual", Schema.FLOAT64_SCHEMA)
            .field("feels_like", Schema.FLOAT64_SCHEMA)
        .build();

    private static final Schema CONDITION_SCHEMA = SchemaBuilder.struct()
        .name("condition")
            .field("text", Schema.STRING_SCHEMA)
            .field("icon", Schema.STRING_SCHEMA)
            .field("code", Schema.INT32_SCHEMA)
        .build();

    private static final Schema WIND_SCHEMA = SchemaBuilder.struct()
        .name("wind")
            .field("speed", Schema.FLOAT64_SCHEMA)
            .field("degree", Schema.INT32_SCHEMA)
            .field("direction", Schema.STRING_SCHEMA)
            .field("gust", Schema.FLOAT64_SCHEMA)
        .build();

    private static final Schema CURRENT_SCHEMA = SchemaBuilder.struct()
        .name("report")
            .field("last_updated", Schema.INT32_SCHEMA)
            .field("temperature", TEMPERATURE_SCHEMA)
            .field("condition", CONDITION_SCHEMA)
            .field("wind", WIND_SCHEMA)
        .build();

    private static final Schema RESPONSE_SCHEMA = SchemaBuilder.struct()
        .name("weather")
            .field("location", LOCATION_SCHEMA)
            .field("current", CURRENT_SCHEMA)
        .build();


    public SourceRecord createSourceRecord(Response data) {
        return new SourceRecord(createSourcePartition(),
                                createSourceOffset(data),
                                getTopicName(data),
                                RESPONSE_SCHEMA,
                                createStruct(data));
    }

    private Map<String, Object> createSourcePartition() {
        return Collections.singletonMap("location", location);
    }

    private static final String SOURCE_OFFSET = "date";

    private Map<String, Object> createSourceOffset(Response data) {
        return Collections.singletonMap(SOURCE_OFFSET, data.getEpochTimestamp());
    }

    private String getTopicName(Response data) {
        return "WEATHER.DEMO";
    }

    private Struct createStruct(Response data) {
        Struct coordinates = new Struct(COORDS_SCHEMA);
        coordinates.put("latitude", data.getLocation().getLat());
        coordinates.put("longitude", data.getLocation().getLon());

        Struct location = new Struct(LOCATION_SCHEMA);
        location.put("name", data.getLocation().getName());
        location.put("region", data.getLocation().getRegion());
        location.put("country", data.getLocation().getCountry());
        location.put("coordinates", coordinates);

        Struct temperature = new Struct(TEMPERATURE_SCHEMA);
        temperature.put("actual", data.getCurrent().getTempC());
        temperature.put("feels_like", data.getCurrent().getFeelsLikeC());

        Struct condition = new Struct(CONDITION_SCHEMA);
        condition.put("text", data.getCurrent().getCondition().getText());
        condition.put("icon", data.getCurrent().getCondition().getIcon());
        condition.put("code", data.getCurrent().getCondition().getCode());

        Struct wind = new Struct(WIND_SCHEMA);
        wind.put("speed", data.getCurrent().getWindKph());
        wind.put("degree", data.getCurrent().getWindDegrees());
        wind.put("direction", data.getCurrent().getWindDir());
        wind.put("gust", data.getCurrent().getGustKph());

        Struct current = new Struct(CURRENT_SCHEMA);
        current.put("last_updated", data.getCurrent().getLastUpdatedEpoch());
        current.put("temperature", temperature);
        current.put("condition", condition);
        current.put("wind", wind);

        Struct response = new Struct(RESPONSE_SCHEMA);
        response.put("location", location);
        response.put("current", current);
        return response;
    }



    public Long getPersistedOffset(OffsetStorageReader offsetReader) {
        log.debug("retrieving persisted offset for previously produced events");

        if (offsetReader == null) {
            log.debug("no offset reader available");
            return 0L;
        }

        Map<String, Object> sourcePartition = createSourcePartition();
        Map<String, Object> persistedOffsetInfo = offsetReader.offset(sourcePartition);

        if (persistedOffsetInfo == null) {
            log.debug("no persisted offset for location " + location);
            return 0L;
        }

        Long offset = (Long) persistedOffsetInfo.getOrDefault(SOURCE_OFFSET, 0L);
        log.info("previous offset for " + location + " is " + offset);

        return offset;
    }
}
