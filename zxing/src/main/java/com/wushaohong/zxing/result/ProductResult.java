package com.wushaohong.zxing.result;

import com.google.zxing.client.result.ProductParsedResult;

public class ProductResult extends Result {
    private final String productID;
    private final String normalizedProductID;

    public ProductResult(ProductParsedResult productParsedResult) {
        this.productID = productParsedResult.getProductID();
        this.normalizedProductID = productParsedResult.getNormalizedProductID();
    }

    public String getProductID() {
        return productID;
    }

    public String getNormalizedProductID() {
        return normalizedProductID;
    }
}
