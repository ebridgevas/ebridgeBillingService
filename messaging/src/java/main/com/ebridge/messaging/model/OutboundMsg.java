package com.ebridge.messaging.model;

import java.math.BigInteger;

/**
 * david@ebridgevas.com
 *
 */
public class OutboundMsg {

    private final BigInteger messageId;
    private final String messageStatus;
    private final String channelType;
    private final String destinationId;
    private final String sourceId;
    private final String payload;

    public OutboundMsg( BigInteger messageId,
                        String messageStatus,
                        String channelType,
                        String destinationId,
                        String sourceId,
                        String payload) {
        this.messageId = messageId;
        this.messageStatus = messageStatus;
        this.channelType = channelType;
        this.destinationId = destinationId;
        this.sourceId = sourceId;
        this.payload = payload;
    }

    public BigInteger getMessageId() {
        return messageId;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public String getChannelType() {
        return channelType;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getPayload() {
        return payload;
    }
}
