package com.ebridge.messaging.dao;

import com.ebridge.commons.dao.DataBaseConnectionPool;
import com.ebridge.messaging.model.OutboundMsg;
import com.ebridge.messaging.util.DatabaseException;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * david@ebridgevas.com
 *
 * TODO - Redesign persistence layer. Consider Java Persistence Architecture (JPA)
 *
 */
public class OutboundMsgDao {

    private static Connection connection;

    static {
        try {
            connection = DataBaseConnectionPool.getConnection();
        } catch (Exception e) {
            // TODO handle exception
            e.printStackTrace();
        }
    }

    public static Boolean persist( OutboundMsg outboundMsg ) throws DatabaseException {

        if ( connection == null) {
            try {
                connection = DataBaseConnectionPool.getConnection();
            } catch (Exception e) {
                // TODO handle exception
                e.printStackTrace();
            }
        }

        String sql = "" +
            "INSERT INTO outbound_msg (" +
                     "message_id, message_status, channel_type, destination_id, source_id, payload ) " +
            " VALUES ("  + outboundMsg.getMessageId() + "," +
                     "'" + outboundMsg.getMessageStatus() + "'," +
                     "'" + outboundMsg.getChannelType() + "'," +
                     "'" + outboundMsg.getDestinationId() + "'," +
                     "'" + outboundMsg.getSourceId() + "'," +
                     "'" + outboundMsg.getPayload() + "' )";

        System.out.println(sql);

        Statement stmt = null;
        ResultSet rs = null;
        try {

            stmt = connection.createStatement();
            return stmt.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            throw new DatabaseException( e.getMessage() );
        } finally {
            try {rs.close();} catch (Exception e){}
            try {stmt.close();} catch (Exception e){}
        }
    }

    public static Boolean updateStatus( BigInteger messageId, String newMessageStatus ) throws DatabaseException {

        if ( connection == null) {
            try {
                connection = DataBaseConnectionPool.getConnection();
            } catch (Exception e) {
                // TODO handle exception
                e.printStackTrace();
            }
        }

        String sql = "" +
                " UPDATE outbound_msg SET message_status = '" + newMessageStatus + "'" +
                " WHERE message_id = " + messageId;

        System.out.println(sql);

        Statement stmt = null;
        ResultSet rs = null;
        try {

            stmt = connection.createStatement();
            return stmt.executeUpdate(sql) > 0;
        } catch (SQLException e) {
            throw new DatabaseException( e.getMessage() );
        } finally {
            try {rs.close();} catch (Exception e){}
            try {stmt.close();} catch (Exception e){}
        }
    }

    public static List<OutboundMsg> getPendingMessages(String channelType) throws DatabaseException {


        List<OutboundMsg> msgs = new ArrayList<>();

        if ( connection == null) {
            try {
                connection = DataBaseConnectionPool.getConnection();
            } catch (Exception e) {
                // TODO handle exception
                e.printStackTrace();
            }
        }

        String sql = "" +
                "SELECT message_id, message_status, channel_type, destination_id, source_id, payload " +
                " FROM outbound_msg " +
                "WHERE message_status = 'QUEUED'" +
                "  AND channel_type = '" + channelType + "'";

        System.out.println(sql);

        Statement stmt = null;
        ResultSet rs = null;
        try {

            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            while ( rs.next() ) {
                msgs.add(new OutboundMsg(
                                new BigInteger(rs.getString("message_id")),
                                rs.getString("message_status"),
                                rs.getString("channel_type"),
                                rs.getString("destination_id"),
                                rs.getString("source_id"),
                                rs.getString("payload")));
            }
        } catch (SQLException e) {
            throw new DatabaseException( e.getMessage() );
        } finally {
            try {rs.close();} catch (Exception e){}
            try {stmt.close();} catch (Exception e){}
        }
        return msgs;
    }
}
