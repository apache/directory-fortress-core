
package org.apache.directory.fortress.core.impl;

import org.apache.directory.fortress.core.FinderException;
import org.apache.directory.fortress.core.model.FortEntity;
import org.apache.directory.fortress.core.model.Permission;

public class PermOpDAO extends PermDAO implements PropertyProvider<Permission>
{

    @Override
    public String getDn( Permission entity )
    {
        return this.getDn( entity, entity.getContextId() );
    }

    @Override
    public FortEntity getEntity( Permission entity ) throws FinderException
    {
        return this.getPerm( entity );
    }

}
