package com.ebridge.billingplatform.inject;

import com.comverse_in.prepaid.ccws.ServiceSoap;
import com.ebridge.billingplatform.*;
import com.ebridge.billingplatform.impl.BillingPlatformInterfaceImpl;
import com.ebridge.billingplatform.impl.ClassOfServiceBasedSubscribedPackageIdentifier;
import com.ebridge.billingplatform.impl.postpaid.PostpaidAccountBalanceFactory;
import com.ebridge.billingplatform.impl.postpaid.PostpaidDataBundlePurchase;
import com.ebridge.billingplatform.impl.prepaid.PrepaidAccountBalanceFactory;
import com.ebridge.billingplatform.impl.prepaid.PrepaidBalanceTransfer;
import com.ebridge.billingplatform.impl.prepaid.PrepaidDataBundlePurchase;
import com.ebridge.billingplatform.impl.prepaid.PrepaidVoucherRecharge;
import com.ebridge.billingplatform.inject.providers.PostpaidPlatformServiceSoapProvider;
import com.ebridge.billingplatform.inject.providers.PrepaidPlatformServiceSoapProvider;
import com.ebridge.commons.util.ConfigurationService;
import com.ebridge.commons.util.JsonConfigurationService;
import com.ebridge.messaging.SecurityTokenSender;
import com.ebridge.messaging.impl.DatabaseBackedSecurityTokenSender;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.ztesoft.zsmart.bss.ws.customization.zimbabwe.WebServices;

/**
 * @author david@tekeshe.com
 */
public class BillingPlatformInterfaceModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(ConfigurationService.class).to(JsonConfigurationService.class);

        bind(SubscribedPackageIdentifier.class).to(ClassOfServiceBasedSubscribedPackageIdentifier.class);

        bind(AccountBalanceFactory.class).annotatedWith(Names.named("prepaidAccountBalanceFactory"))
                .to(PrepaidAccountBalanceFactory.class);

        bind(AccountBalanceFactory.class).annotatedWith(Names.named("postpaidAccountBalanceFactory"))
                .to(PostpaidAccountBalanceFactory.class);

        bind(DataBundlePurchase.class).annotatedWith(Names.named("prepaidDataBundlePurchase"))
                .to(PrepaidDataBundlePurchase.class);

        bind(DataBundlePurchase.class).annotatedWith(Names.named("postpaidDataBundlePurchase"))
                .to(PostpaidDataBundlePurchase.class);

        bind(BalanceTransfer.class).annotatedWith(Names.named("prepaidBalanceTransfer"))
                .to(PrepaidBalanceTransfer.class);

        bind(VoucherRecharge.class).annotatedWith(Names.named("prepaidVoucherRecharge"))
                .to(PrepaidVoucherRecharge.class);

        bind(ServiceSoap.class).annotatedWith(Names.named("prepaidServiceSoapProvider"))
                .toProvider(PrepaidPlatformServiceSoapProvider.class);

        bind(WebServices.class).annotatedWith(Names.named("postpaidServiceSoapProvider"))
                .toProvider(PostpaidPlatformServiceSoapProvider.class);

        bind(SecurityTokenSender.class).annotatedWith(Names.named("messageSender"))
                .to(DatabaseBackedSecurityTokenSender.class);

        bind(BillingPlatformInterface.class).to(BillingPlatformInterfaceImpl.class);
    }
}