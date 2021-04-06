package org.apache.directory.fortress.core.ldap;


import org.apache.directory.api.asn1.Asn1Object;
import org.apache.directory.api.asn1.DecoderException;
import org.apache.directory.api.asn1.EncoderException;
import org.apache.directory.api.ldap.codec.api.ControlDecorator;
import org.apache.directory.api.ldap.codec.api.LdapApiService;

import java.nio.ByteBuffer;


public class RelaxControlDecorator extends ControlDecorator<RelaxControl> implements RelaxControl
{
    public RelaxControlDecorator(LdapApiService codec, RelaxControl control) {
        super(codec, control);
    }
    public int computeLength() {
        return 0;
    }

    public Asn1Object decode(byte[] controlBytes) throws DecoderException
    {
        return this;
    }

    public ByteBuffer encode(ByteBuffer buffer) throws EncoderException
    {
        return buffer;
    }
}
