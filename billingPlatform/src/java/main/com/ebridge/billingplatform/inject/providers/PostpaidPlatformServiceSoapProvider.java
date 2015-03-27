package com.ebridge.billingplatform.inject.providers;

import com.google.inject.Provider;
import com.ztesoft.zsmart.bss.ws.customization.zimbabwe.WebServices;
import com.ztesoft.zsmart.bss.ws.customization.zimbabwe.WebServicesService;
import com.ztesoft.zsmart.bss.ws.customization.zimbabwe.WebServicesServiceLocator;
import org.apache.log4j.Category;

import javax.xml.rpc.ServiceException;

/**
 * @author david@tekeshe.com
 */
public class PostpaidPlatformServiceSoapProvider implements Provider<WebServices> {

    private static Category CAT = Category.getInstance(PostpaidPlatformServiceSoapProvider.class);

    @Override
    public WebServices get() {

        WebServicesService locator = new WebServicesServiceLocator();
        WebServices postpaidService;
        try {
            CAT.debug("Postpaid web service init...");
            postpaidService = locator.getWebServices();
            CAT.debug("Postpaid web service initialized.");
        } catch (ServiceException e) {
            e.printStackTrace();
            postpaidService = null;
        }

        return postpaidService;
    }
}
