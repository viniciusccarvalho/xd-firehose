package org.springframework.xd.firehose;

import events.EnvelopeOuterClass;
import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;

/**
 * Created by vcarvalho on 3/17/15.
 */
public class EnvelopeTransformer extends AbstractTransformer {

    @Override
    protected Object doTransform(Message<?> message) throws Exception {
        byte[] bytes = (byte[]) message.getPayload();

        return EnvelopeOuterClass.Envelope.parseFrom(bytes);
    }
}
