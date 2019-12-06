package com.wushaohong.zxing.result;

import com.google.zxing.client.result.URIParsedResult;

public class URIResult extends Result {
    private final String uri;
    private final String title;

    public URIResult(URIParsedResult uriParsedResult) {
        this.uri = uriParsedResult.getURI();
        this.title = uriParsedResult.getTitle();
    }

    public String getUri() {
        return uri;
    }

    public String getTitle() {
        return title;
    }
}
