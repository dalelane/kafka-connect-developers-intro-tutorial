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

public class Current {

    @SerializedName("last_updated_epoch")
    @Expose
    private Integer last_updated_epoch;

    @SerializedName("last_updated")
    @Expose
    private String last_updated;

    @SerializedName("temp_c")
    @Expose
    private Double temp_c;

    @SerializedName("temp_f")
    @Expose
    private Double temp_f;

    @SerializedName("is_day")
    @Expose
    private Integer is_day;

    @SerializedName("condition")
    @Expose
    private Condition condition;

    @SerializedName("wind_mph")
    @Expose
    private Double wind_mph;

    @SerializedName("wind_kph")
    @Expose
    private Double wind_kph;

    @SerializedName("wind_degree")
    @Expose
    private Integer wind_degree;

    @SerializedName("wind_dir")
    @Expose
    private String wind_dir;

    @SerializedName("pressure_mb")
    @Expose
    private Double pressure_mb;

    @SerializedName("pressure_in")
    @Expose
    private Double pressure_in;

    @SerializedName("precip_mm")
    @Expose
    private Double precip_mm;

    @SerializedName("precip_in")
    @Expose
    private Double precip_in;

    @SerializedName("humidity")
    @Expose
    private Integer humidity;

    @SerializedName("cloud")
    @Expose
    private Integer cloud;

    @SerializedName("feelslike_c")
    @Expose
    private Double feelslike_c;

    @SerializedName("feelslike_f")
    @Expose
    private Double feelslike_f;

    @SerializedName("vis_km")
    @Expose
    private Double vis_km;

    @SerializedName("vis_miles")
    @Expose
    private Double vis_miles;

    @SerializedName("uv")
    @Expose
    private Double uv;

    @SerializedName("gust_mph")
    @Expose
    private Double gust_mph;

    @SerializedName("gust_kph")
    @Expose
    private Double gust_kph;




    public Integer getLastUpdatedEpoch() {
        return last_updated_epoch;
    }
    public String getLastUpdated() {
        return last_updated;
    }
    public Double getTempC() {
        return temp_c;
    }
    public Double getTempF() {
        return temp_f;
    }
    public Integer getIsDay() {
        return is_day;
    }
    public Condition getCondition() {
        return condition;
    }
    public Double getWindMph() {
        return wind_mph;
    }
    public Double getWindKph() {
        return wind_kph;
    }
    public Integer getWindDegrees() {
        return wind_degree;
    }
    public String getWindDir() {
        return wind_dir;
    }
    public Double getPressureMb() {
        return pressure_mb;
    }
    public Double getPressureIn() {
        return pressure_in;
    }
    public Double getPrecipMm() {
        return precip_mm;
    }
    public Double getPrecipIn() {
        return precip_in;
    }
    public Integer getHumidity() {
        return humidity;
    }
    public Integer getCloud() {
        return cloud;
    }
    public Double getFeelsLikeC() {
        return feelslike_c;
    }
    public Double getFeelsLikeF() {
        return feelslike_f;
    }
    public Double getVisibilityKm() {
        return vis_km;
    }
    public Double getVisibilityMiles() {
        return vis_miles;
    }
    public Double getUV() {
        return uv;
    }
    public Double getGustMph() {
        return gust_mph;
    }
    public Double getGustKph() {
        return gust_kph;
    }



    @Override
    public int hashCode() {
        return Objects.hash(last_updated, last_updated_epoch);
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

        Current other = (Current) obj;
        return Objects.equals(last_updated, other.last_updated) &&
               Objects.equals(last_updated_epoch, other.last_updated_epoch);
    }
}
