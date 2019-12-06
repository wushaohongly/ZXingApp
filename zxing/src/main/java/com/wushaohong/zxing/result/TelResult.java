package com.wushaohong.zxing.result;

import com.google.zxing.client.result.TelParsedResult;

public class TelResult extends Result {
    private final String number;
    private final String telURI;
    private final String title;

    public TelResult(TelParsedResult telParsedResult) {
        this.number = telParsedResult.getNumber();
        this.telURI = telParsedResult.getTelURI();
        this.title = telParsedResult.getTitle();
    }

    public String getNumber() {
        return number;
    }

    public String getTelURI() {
        return telURI;
    }

    public String getTitle() {
        return title;
    }
}
