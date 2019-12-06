package com.wushaohong.zxing.result;

import com.google.zxing.client.result.TextParsedResult;

public class TextResult extends Result {
    private final String text;
    private final String language;

    public TextResult(TextParsedResult textParsedResult) {
        this.text = textParsedResult.getText();
        this.language = textParsedResult.getLanguage();
    }

    public String getText() {
        return text;
    }

    public String getLanguage() {
        return language;
    }
}
