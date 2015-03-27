package com.ebridge.services.payment.telecash.dao;

import com.ebridge.commons.dao.DataBaseConnectionPool;
import com.ebridge.commons.dto.PayDto;
import data.ws.obopay.com.ObopayWebServiceData;

import java.sql.*;
import java.util.Date;

/**
 * @author david@tekeshe.com
 */
public class PayDAO {

    private static Connection connection;

    static {
        try {
            connection = DataBaseConnectionPool.getConnection();
        } catch (Exception e) {
            // TODO handle exception
            e.printStackTrace();
        }
    }

    public static void persist(PayDto pay) {

        if ( connection == null) {
            try {
                connection = DataBaseConnectionPool.getConnection();
            } catch (Exception e) {
                // TODO handle exception
                e.printStackTrace();
            }
        }

        String sql = " INSERT INTO payments (requestId, localApiIdType, localTier1AgentId, localTier1AgentName," +
                " localTier2AgentId, localTier3AgentId, localTier1AgentPassword, localCustomerPhoneNumber," +
                " localNationalID, localSenderFirstName, localSenderLastName, localTransactionType," +
                " localInstrumentType, localProcessorCode, localCashInAmount, localPaymentDetails1," +
                " localPaymentDetails2, localPaymentDetails3, localPaymentDetails4, localTxnAmount," +
                " localCurrencyType, localNetTxnAmount, localFeeAmount, localTaxAmount," +
                " localCountry, localTerminalID, localTxnStatus, localErrorCode,localOpsTransactionId, " +
                " localTransactionDate, localRemark, localReference1, localReference2,localReference3, " +
                " localReference4, localReference5 ) " +
                " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                " ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, pay.getLocalRequestId());
            stmt.setString(2, pay.getLocalApiIdType());
            stmt.setString(3, pay.getLocalTier1AgentId());
            stmt.setString(4, pay.getLocalTier1AgentName());
            stmt.setString(5, pay.getLocalTier2AgentId());
            stmt.setString(6, pay.getLocalTier3AgentId());
            stmt.setString(7, pay.getLocalTier1AgentPassword());
            stmt.setString(8, pay.getLocalCustomerPhoneNumber());
            stmt.setString(9, pay.getLocalNationalID());
            stmt.setString(10, pay.getLocalSenderFirstName());
            stmt.setString(11, pay.getLocalSenderLastName());
            stmt.setString(12, pay.getLocalTransactionType());
            stmt.setString(13, pay.getLocalInstrumentType());
            stmt.setString(14, pay.getLocalProcessorCode());
            stmt.setBigDecimal(15, pay.getLocalCashInAmount());
            stmt.setString(16, pay.getLocalPaymentDetails1());
            stmt.setString(17, pay.getLocalPaymentDetails2());
            stmt.setString(18, pay.getLocalPaymentDetails3());
            stmt.setString(19, pay.getLocalPaymentDetails4());
            stmt.setBigDecimal(20, pay.getLocalTxnAmount());
            stmt.setString(21, pay.getLocalCurrencyType());
            stmt.setBigDecimal(22, pay.getLocalNetTxnAmount());
            stmt.setBigDecimal(23, pay.getLocalFeeAmount());
            stmt.setBigDecimal(24, pay.getLocalTaxAmount());
            stmt.setString(25, pay.getLocalCountry());
            stmt.setString(26, pay.getLocalTerminalID());
            stmt.setString(27, pay.getLocalTxnStatus());
            stmt.setString(28, pay.getLocalErrorCode());
            stmt.setString(29, pay.getLocalOpsTransactionId());
            stmt.setTimestamp(30, new Timestamp(pay.getLocalTransactionDate().getTime()));
            stmt.setString(31, pay.getLocalRemark());
            stmt.setString(32, pay.getLocalReference1());
            stmt.setString(33, pay.getLocalReference2());
            stmt.setString(34, pay.getLocalReference3());
            stmt.setString(35, pay.getLocalReference4());
            stmt.setString(36, pay.getLocalReference5());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {stmt.close();} catch (Exception e){}
        }

    }

    public static void update( ObopayWebServiceData response ) {

        response.getRequestId();
        response.getOpsTransactionId();
        response.getErrorCode();
        response.getRemark();

        String sql = "UPDATE payments " +
                     "SET localErrorCode = ? , localRemark = ?, localTxnStatus = ?, " + "localOpsTransactionId = ? " +
                     " WHERE requestId = ? ";

        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, response.getErrorCode());
            stmt.setString(2, response.getRemark());
            stmt.setString(3, response.getTxnStatus());
            stmt.setString(4, response.getOpsTransactionId());
            stmt.setString(5, response.getRequestId() );

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {stmt.close();} catch (Exception e){}
        }
    }

    public static PayDto from(ObopayWebServiceData input) {

        PayDto pay = new PayDto( input.getRequestId() );

        pay.setLocalApiIdType(input.getApiIdType());
        pay.setLocalTier1AgentId(input.getTier1AgentId());
        pay.setLocalTier1AgentName(input.getTier1AgentName());
        pay.setLocalTier2AgentId(input.getTier2AgentId());
        pay.setLocalTier3AgentId(input.getTier3AgentId());
        pay.setLocalTier1AgentPassword(input.getTier1AgentPassword());
        pay.setLocalCustomerPhoneNumber(input.getCustomerPhoneNumber());
        pay.setLocalNationalID(input.getNationalID());
        pay.setLocalSenderFirstName(input.getSenderFirstName());
        pay.setLocalSenderLastName(input.getSenderLastName());
        pay.setLocalTransactionType(input.getTransactionType());
        pay.setLocalInstrumentType(input.getInstrumentType());
        pay.setLocalProcessorCode(input.getProcessorCode());
        pay.setLocalCashInAmount(input.getCashInAmount());
        pay.setLocalPaymentDetails1(input.getPaymentDetails1());
        pay.setLocalPaymentDetails2(input.getPaymentDetails2());
        pay.setLocalPaymentDetails3(input.getPaymentDetails3());
        pay.setLocalPaymentDetails4(input.getPaymentDetails4());
        pay.setLocalTxnAmount(input.getTxnAmount());
        pay.setLocalCurrencyType(input.getCurrencyType());
        pay.setLocalNetTxnAmount(input.getNetTxnAmount());
        pay.setLocalFeeAmount(input.getFeeAmount());
        pay.setLocalTaxAmount(input.getTaxAmount());
        pay.setLocalCountry(input.getCountry());
        pay.setLocalTerminalID(input.getTerminalID());
        pay.setLocalTxnStatus(input.getTxnStatus());
        pay.setLocalErrorCode(input.getErrorCode());
        pay.setLocalOpsTransactionId(input.getOpsTransactionId());
        Date transactionDate = (input.getTransactionDate() != null ? input.getTransactionDate().getTime() : new Date());
        pay.setLocalTransactionDate( transactionDate );
        pay.setLocalRemark(input.getRemark());
        pay.setLocalReference1(input.getReference1());
        pay.setLocalReference2(input.getReference2());
        pay.setLocalReference3(input.getReference3());
        pay.setLocalReference4(input.getReference4());
        pay.setLocalReference5(input.getReference5());

        return pay;
    }

    public static PayDto payDto(String requestId) {

        String sql =
                " SELECT requestId, localApiIdType, localTier1AgentId, localTier1AgentName," +
                "        localTier2AgentId, localTier3AgentId, localTier1AgentPassword, localCustomerPhoneNumber," +
                "        localNationalID, localSenderFirstName, localSenderLastName, localTransactionType," +
                "        localInstrumentType, localProcessorCode, localCashInAmount, localPaymentDetails1," +
                "        localPaymentDetails2, localPaymentDetails3, localPaymentDetails4, localTxnAmount," +
                "        localCurrencyType, localNetTxnAmount, localFeeAmount, localTaxAmount," +
                "        localCountry, localTerminalID, localTxnStatus, localErrorCode,localOpsTransactionId, " +
                "        localTransactionDate, localRemark, localReference1, localReference2,localReference3, " +
                "        localReference4, localReference5 " +
                "  FROM payments " +
                " WHERE requestId = ?";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, requestId);

            rs = stmt.executeQuery();

            if ( rs.next() ) {
                return from(rs);
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {stmt.close();} catch (Exception e){}
            try {rs.close();} catch (Exception e){}
        }
        return null;
    }

    public static PayDto from(ResultSet rs) throws SQLException {

        PayDto pay = new PayDto(rs.getString("requestId"));

        pay.setLocalApiIdType(rs.getString("localApiIdType"));
        pay.setLocalTier1AgentId(rs.getString("localTier1AgentId"));
        pay.setLocalTier1AgentName(rs.getString("localTier1AgentName"));
        pay.setLocalTier2AgentId(rs.getString("localTier2AgentId"));
        pay.setLocalTier3AgentId(rs.getString("localTier3AgentId"));
        pay.setLocalTier1AgentPassword(rs.getString("localTier1AgentPassword"));
        pay.setLocalCustomerPhoneNumber(rs.getString("localCustomerPhoneNumber"));
        pay.setLocalNationalID(rs.getString("localNationalID"));
        pay.setLocalSenderFirstName(rs.getString("localSenderFirstName"));
        pay.setLocalSenderLastName(rs.getString("localSenderLastName"));
        pay.setLocalTransactionType(rs.getString("localTransactionType"));
        pay.setLocalInstrumentType(rs.getString("localInstrumentType"));
        pay.setLocalProcessorCode(rs.getString("localProcessorCode"));
        pay.setLocalCashInAmount(rs.getBigDecimal("localCashInAmount"));
        pay.setLocalPaymentDetails1(rs.getString("localPaymentDetails1"));
        pay.setLocalPaymentDetails2(rs.getString("localPaymentDetails2"));
        pay.setLocalPaymentDetails3(rs.getString("localPaymentDetails3"));
        pay.setLocalPaymentDetails4(rs.getString("localPaymentDetails4"));
        pay.setLocalTxnAmount(rs.getBigDecimal("localTxnAmount"));
        pay.setLocalCurrencyType(rs.getString("localCurrencyType"));
        pay.setLocalNetTxnAmount(rs.getBigDecimal("localNetTxnAmount"));
        pay.setLocalFeeAmount(rs.getBigDecimal("localFeeAmount"));
        pay.setLocalTaxAmount(rs.getBigDecimal("localTaxAmount"));
        pay.setLocalCountry(rs.getString("localCountry"));
        pay.setLocalTerminalID(rs.getString("localTerminalID"));
        pay.setLocalTxnStatus(rs.getString("localTxnStatus"));
        pay.setLocalErrorCode(rs.getString("localErrorCode"));
        pay.setLocalOpsTransactionId(rs.getString("localOpsTransactionId"));
        pay.setLocalTransactionDate(rs.getTimestamp("localTransactionDate"));
        pay.setLocalRemark(rs.getString("localRemark"));
        pay.setLocalReference1(rs.getString("localReference1"));
        pay.setLocalReference2(rs.getString("localReference2"));
        pay.setLocalReference3(rs.getString("localReference3"));
        pay.setLocalReference4(rs.getString("localReference4"));
        pay.setLocalReference5(rs.getString("localReference5"));

        return pay;
    }
    /*

    create table payments (
        requestId varchar(60),
        localApiIdType varchar(5),
        localTier1AgentId varchar(20),
        localTier1AgentName varchar(60),
        localTier2AgentId varchar(20),
        localTier3AgentId varchar(20),
        localTier1AgentPassword varchar(20),
        localCustomerPhoneNumber varchar(20),
        localNationalID varchar(60),
        localSenderFirstName varchar(60),
        localSenderLastName varchar(60),
        localTransactionType varchar(20),
        localInstrumentType varchar(20),
        localProcessorCode varchar(20),
        localCashInAmount decimal(18,2),
        localPaymentDetails1 varchar(60),
        localPaymentDetails2 varchar(60),
        localPaymentDetails3 varchar(60),
        localPaymentDetails4 varchar(60),
        localTxnAmount decimal(18,2),
        localCurrencyType varchar(10),
        localNetTxnAmount decimal(18,2),
        localFeeAmount decimal(18,2),
        localTaxAmount decimal(18,2),
        localCountry varchar(20),
        localTerminalID varchar(20),
        localTxnStatus varchar(20),
        localErrorCode varchar(20),
        localOpsTransactionId varchar(20),
        localTransactionDate timestamp,
        localRemark varchar(128),
        localReference1 varchar(60),
        localReference2 varchar(60),
        localReference3 varchar(60),
        localReference4 varchar(60),
        localReference5 varchar(60),
        primary key (requestId) );

        insert into payments (
        requestId varchar(60),
        localApiIdType varchar(5),
        localTier1AgentId varchar(20),
        localTier1AgentName varchar(60),
        localTier2AgentId varchar(20),
        localTier3AgentId varchar(20),
        localTier1AgentPassword varchar(20),
        localCustomerPhoneNumber varchar(20),
        localNationalID varchar(60),
        localSenderFirstName varchar(60),
        localSenderLastName varchar(60),
        localTransactionType varchar(20),
        localInstrumentType varchar(20),
        localProcessorCode varchar(20),
        localCashInAmount decimal(18,2),
        localPaymentDetails1 varchar(60),
        localPaymentDetails2 varchar(60),
        localPaymentDetails3 varchar(60),
        localPaymentDetails4 varchar(60),
        localTxnAmount decimal(18,2),
        localCurrencyType varchar(10),
        localNetTxnAmount decimal(18,2),
        localFeeAmount decimal(18,2),
        localTaxAmount decimal(18,2),
        localCountry varchar(20),
        localTerminalID varchar(20),
        localTxnStatus varchar(20),
        localErrorCode varchar(20),
        localOpsTransactionId varchar(20),
        localTransactionDate timestamp,
        localRemark varchar(128),
        localReference1 varchar(60),
        localReference2 varchar(60),
        localReference3 varchar(60),
        localReference4 varchar(60),
        localReference5 varchar(60),
        primary key (requestId) );
     */
}
