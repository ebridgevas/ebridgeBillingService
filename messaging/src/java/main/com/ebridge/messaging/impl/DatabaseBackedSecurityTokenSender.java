package com.ebridge.messaging.impl;

import com.ebridge.messaging.SecurityTokenSender;
import com.ebridge.messaging.dao.OutboundMsgDao;
import com.ebridge.messaging.model.OutboundMsg;
import com.ebridge.messaging.util.DatabaseException;

import java.io.IOException;

/**
 * @author david@tekeshe.com
 *
 */
public class DatabaseBackedSecurityTokenSender implements SecurityTokenSender {

    @Override
    public void send ( OutboundMsg outboundMsg) throws IOException {

        try {
            OutboundMsgDao.persist(outboundMsg);
        } catch (DatabaseException e) {
            throw new IOException( e );
        }
    }
}
