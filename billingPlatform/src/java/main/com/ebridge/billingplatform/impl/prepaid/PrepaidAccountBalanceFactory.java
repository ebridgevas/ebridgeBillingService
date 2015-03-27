package com.ebridge.billingplatform.impl.prepaid;

import com.comverse_in.prepaid.ccws.BalanceEntity;
import com.comverse_in.prepaid.ccws.ServiceSoap;
import com.comverse_in.prepaid.ccws.SubscriberRetrieve;
import com.ebridge.billingplatform.AccountBalanceFactory;
import com.ebridge.commons.dto.BalanceDTO;
import com.ebridge.commons.dto.LanguageDTO;
import com.ebridge.commons.util.ConfigurationService;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.log4j.Category;

import javax.inject.Named;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.util.*;

/**
* @author david@tekeshe.com
*/
public class PrepaidAccountBalanceFactory implements AccountBalanceFactory {

    @Inject
    @Named("prepaidServiceSoapProvider")
    private Provider<ServiceSoap> prepaidServiceSoapProvider;

    @Inject
    private ConfigurationService configurationService;

    private static Category CAT = Category.getInstance(PrepaidAccountBalanceFactory.class);

    public Set<BalanceDTO> balances ( String mobileNumber ) throws RemoteException {

        CAT.debug("balances()");

        Set<BalanceDTO> balances = new TreeSet<>();

        System.out.println("Retrieving subscriber : " + mobileNumber );
        SubscriberRetrieve subscriberRetrieve
                = prepaidServiceSoapProvider.get().retrieveSubscriberWithIdentityNoHistory(
                mobileNumber.substring(3), null, 1);

        String cosName = subscriberRetrieve.getSubscriberData().getCOSName();

        List<String> cosBalances = configurationService.config().getCosBalances().get(cosName);
        if (cosBalances == null ) {
            cosBalances = new ArrayList<>();
            cosBalances.add("Core");
        }

        Map<String, LanguageDTO> balanceNames = configurationService.config().getBalanceNames();

        for (BalanceEntity balanceEntity : subscriberRetrieve.getSubscriberData().getBalances()) {

            System.out.println("Processing balanceName : " + balanceEntity.getBalanceName() +
                    " with an available balance of : " + balanceEntity.getAvailableBalance() +
                    " and a balance of : " + balanceEntity.getBalance() );

            if ( cosBalances.contains( balanceEntity.getBalanceName()) )  {

                double rate = 1.00;
                try {
                    if ("megabyte".equalsIgnoreCase(balanceNames.get(balanceEntity.getBalanceName()).getUnitOfMeasure())) {
                        rate = 0.12;
                    }
                } catch (Exception e ) {
                    rate = 1.00;
                }

                balances.add(
                        new BalanceDTO(
                                mobileNumber,
                                configurationService.config().getBalances().get(balanceEntity.getBalanceName()),
                                new BigDecimal(
                                        balanceEntity.getAvailableBalance() / rate
                                    ).setScale(2, BigDecimal.ROUND_HALF_UP),
                                balanceEntity.getAccountExpiration(), "PREPAID"));

            }

        }

        CAT.info( "INFO : {mobileNumber : " + mobileNumber + ", balances : " + balances + "}" );

        return balances;
    }
}
