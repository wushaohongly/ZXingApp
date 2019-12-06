package com.wushaohong.zxing.result;

import com.google.zxing.client.result.ISBNParsedResult;

public class ISBNResult extends Result {
    private final String isbn;

    public ISBNResult(ISBNParsedResult isbnParsedResult) {
        this.isbn = isbnParsedResult.getISBN();
    }

    public String getISBN() {
        return isbn;
    }
}
