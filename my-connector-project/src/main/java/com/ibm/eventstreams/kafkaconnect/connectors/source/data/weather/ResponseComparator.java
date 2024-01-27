package com.ibm.eventstreams.kafkaconnect.connectors.source.data.weather;

import java.util.Comparator;

public class ResponseComparator implements Comparator<Response> {

    @Override
    public int compare(Response o1, Response o2) {
        return o1.getEpochTimestamp().compareTo(o2.getEpochTimestamp());
    }
}
