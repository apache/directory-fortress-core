
package org.apache.directory.fortress.core.impl;

import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.model.FortEntity;
import org.apache.directory.fortress.core.model.PermObj;

public class PermObjDAO extends PermDAO implements PropertyProvider<PermObj>
{

    @Override
    public String getDn( PermObj entity )
    {
        return this.getDn( entity, entity.getContextId() );
    }

    @Override
    public FortEntity getEntity( PermObj entity ) throws FinderException
    {
        return this.getPerm( entity );
    }

}
