package com.ebridge.messaging;

import com.ebridge.messaging.model.OutboundMsg;

import java.io.IOException;

/**
 * david@ebridgevas.com
 *
 */
public interface SecurityTokenSender {

    public void send(OutboundMsg outboundMsg) throws IOException;
}
