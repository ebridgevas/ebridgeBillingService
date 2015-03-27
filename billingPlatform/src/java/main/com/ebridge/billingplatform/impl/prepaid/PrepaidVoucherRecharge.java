package com.ebridge.billingplatform.impl.prepaid;

import com.comverse_in.prepaid.ccws.ServiceSoap;
import com.ebridge.billingplatform.AccountBalanceFactory;
import com.ebridge.billingplatform.VoucherRecharge;
import com.ebridge.commons.dto.BalanceDTO;
import com.ebridge.commons.util.TransactionException;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.log4j.Category;

import javax.inject.Named;
import java.math.BigDecimal;
import java.rmi.RemoteException;

import static com.ebridge.billingplatform.util.Util.balance;

/**
 * @author david@tekeshe.com
 */
public class PrepaidVoucherRecharge implements VoucherRecharge {

    @Inject
    @Named("prepaidServiceSoapProvider")
    private Provider<ServiceSoap> prepaidServiceSoapProvider;

    @Inject
    @Named("prepaidAccountBalanceFactory")
    private AccountBalanceFactory prepaidAccountBalanceFactory;

    private static final String CORE_BALANCE = "Core";

    private static Category CAT = Category.getInstance(PrepaidBalanceTransfer.class);

    @Override
    public BalanceDTO recharge( String uuid, String beneficiaryId, String rechargeVoucher )
            throws RemoteException, TransactionException {
        BalanceDTO balanceBeforeTxn = balance(
                CORE_BALANCE,
                prepaidAccountBalanceFactory.balances(beneficiaryId));
        prepaidServiceSoapProvider.get()
                .rechargeAccountBySubscriber(
                        beneficiaryId.substring(3),
                        null,
                        rechargeVoucher, uuid); //"Voucher recharge # " + uuid);

        BalanceDTO balanceAfterTxn = balance(
                CORE_BALANCE,
                prepaidAccountBalanceFactory.balances(beneficiaryId));
        balanceAfterTxn.setMobileNumber( beneficiaryId );
        balanceAfterTxn.setOtherAmount( balanceAfterTxn.getBalance().subtract( balanceBeforeTxn.getBalance()) );

        CAT.info( "{ result : " + balanceAfterTxn + "}" );

        return balanceAfterTxn;
    }
}
