package com.wushaohong.zxing.result;

import com.google.zxing.client.result.GeoParsedResult;

public class GeoResult extends Result {
    private final double latitude;
    private final double longitude;
    private final double altitude;
    private final String query;

    public GeoResult(GeoParsedResult geoParsedResult) {
        this.latitude = geoParsedResult.getLatitude();
        this.longitude = geoParsedResult.getLongitude();
        this.altitude = geoParsedResult.getAltitude();
        this.query = geoParsedResult.getQuery();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public String getQuery() {
        return query;
    }
}
