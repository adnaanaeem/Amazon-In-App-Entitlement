package com.adnan.jbsia.dani;


/**
 * This is a simple example used in AmazonInApp InAppPurchase Sample App, to show how
 * developer's application holding the customer's InAppPurchase data.
 * 
 * 
 */
public class UserIapData {
    private final String amazonUserId;
    private final String amazonMarketplace;


    public String getAmazonUserId() {
        return amazonUserId;
    }

    public String getAmazonMarketplace() {
        return amazonMarketplace;
    }

    public UserIapData(final String amazonUserId, final String amazonMarketplace) {
        this.amazonUserId = amazonUserId;
        this.amazonMarketplace = amazonMarketplace;
    }
}
