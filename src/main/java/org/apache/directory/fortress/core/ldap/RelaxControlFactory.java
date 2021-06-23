package org.apache.directory.fortress.core.ldap;


import org.apache.directory.api.asn1.DecoderException;
import org.apache.directory.api.asn1.util.Asn1Buffer;
import org.apache.directory.api.ldap.codec.api.CodecControl;
import org.apache.directory.api.ldap.codec.api.ControlContainer;
import org.apache.directory.api.ldap.codec.api.ControlFactory;
import org.apache.directory.api.ldap.codec.api.LdapApiService;
import org.apache.directory.api.ldap.model.message.Control;


/**
 * A codec {@link ControlFactory} implementation for {@link RelaxControl} controls.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class RelaxControlFactory implements ControlFactory<RelaxControl>
{
    /** The LDAP codec responsible for encoding and decoding Cascade Controls */
    private LdapApiService codec;


    /**
     * Creates a new instance of TransactionSpecificationFactory.
     *
     * @param codec The LDAP codec
     */
    public RelaxControlFactory( LdapApiService codec )
    {
        this.codec = codec;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getOid()
    {
        return RelaxControl.OID;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CodecControl<RelaxControl> newControl()
    {
        return new RelaxControlDecorator( codec, new RelaxControlImpl() );
    }


    public void encodeValue(Asn1Buffer var1, Control var2)
    {

    }

    public void decodeValue(ControlContainer var1, Control var2, byte[] var3) throws DecoderException
    {

    }

    public void decodeValue(Control var1, byte[] var2) throws DecoderException
    {

    }

}
