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
package com.ibm.eventstreams.kafkaconnect.connectors.source.data.weather;

import java.util.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("region")
    @Expose
    private String region;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("lat")
    @Expose
    private Double lat;

    @SerializedName("lon")
    @Expose
    private Double lon;

    @SerializedName("tz_id")
    @Expose
    private String tz_id;

    @SerializedName("localtime_epoch")
    @Expose
    private Integer localtime_epoch;

    @SerializedName("localtime")
    @Expose
    private String localtime;


    public String getName() {
        return name;
    }
    public String getRegion() {
        return region;
    }
    public String getCountry() {
        return country;
    }
    public Double getLat() {
        return lat;
    }
    public Double getLon() {
        return lon;
    }
    public String getTzId() {
        return tz_id;
    }
    public Integer getLocalTimeEpoch() {
        return localtime_epoch;
    }
    public String getLocalTime() {
        return localtime;
    }



    @Override
    public int hashCode() {
        return Objects.hash(lat, localtime_epoch, lon);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Location other = (Location) obj;
        return Objects.equals(lat, other.lat) &&
               Objects.equals(localtime_epoch, other.localtime_epoch) &&
               Objects.equals(lon, other.lon);
    }
}
