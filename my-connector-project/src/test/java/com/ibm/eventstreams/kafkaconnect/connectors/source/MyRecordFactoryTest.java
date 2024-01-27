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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collections;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.junit.Test;

import com.google.gson.Gson;
import com.ibm.eventstreams.kafkaconnect.connectors.source.data.weather.Response;

import static org.junit.Assert.assertEquals;


public class MyRecordFactoryTest {


    @Test
    public void verifyRecord() throws Exception {
        Response input = getTestData();
        SourceRecord output = getRecordFactory().createSourceRecord(input);

        Struct outputStruct = (Struct) output.value();
        assertEquals("Eastleigh",
            outputStruct.getStruct("location").getString("name"));
        assertEquals(Double.valueOf(51.03),
            outputStruct.getStruct("location").getStruct("coordinates").getFloat64("latitude"));
        assertEquals(Integer.valueOf(1706219100),
            outputStruct.getStruct("current").getInt32("last_updated"));
        assertEquals(Double.valueOf(9.5),
            outputStruct.getStruct("current").getStruct("temperature").getFloat64("feels_like"));
        assertEquals("Light rain",
            outputStruct.getStruct("current").getStruct("condition").getString("text"));
        assertEquals(Integer.valueOf(210),
            outputStruct.getStruct("current").getStruct("wind").getInt32("degree"));
    }



    private MyRecordFactory getRecordFactory() {
        AbstractConfig config = new AbstractConfig(MyConnectorConfig.CONFIG_DEF, Collections.EMPTY_MAP);
        return new MyRecordFactory(config);
    }

    private Response getTestData() throws FileNotFoundException {
        File testFile = new File("./src/test/resources/weather-api-response.json");
        Reader testFileReader = new FileReader(testFile);
        return new Gson().fromJson(testFileReader, Response.class);
    }
}