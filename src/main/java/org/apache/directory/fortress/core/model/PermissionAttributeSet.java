package org.apache.directory.fortress.core.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ftPermissionAttributeSet")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "permission", propOrder =
    {
        "name",
        "attributes",
        "internalId",
        "description"
})
public class PermissionAttributeSet extends FortEntity {

	private String name;
	@XmlElement(nillable = true)
	private Set<PermissionAttribute> attributes;
    private String internalId;
    private String description;
    private String dn;
	
	public PermissionAttributeSet(){
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
    /**
     * Return the collection of optional Attributes that have been loaded into this entity.  This is stored as a multi-occurring
     * attribute of ftPA entries on the 'ftAttributeSet' object class.
     *
     * @return Set containing the roles which maps to 'ftRoles' attribute in 'ftOperation' object class.
     */
    public Set<PermissionAttribute> getAttributes()
    {
    	if(this.attributes == null){
    		attributes = new HashSet<PermissionAttribute>();
    	}
    	
        return this.attributes;
    }


    /**
     * Set the collection of optional Attributes that have been loaded into this entity.  This is stored as a multi-occurring
     * attribute of ftPAs on the 'ftOperation' object class.
     *
     * @param attributes maps to 'ftPA' attribute in 'ftOperation' object class.
     */
    public void setAttributes( Set<PermissionAttribute> attributes )
    {
        this.attributes = attributes;
    }

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId() {
        UUID uuid = UUID.randomUUID();
        this.internalId = uuid.toString();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDn() {
		return dn;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}
    
    /*
    private Set<PermissionAttribute> unloadPermissionAttributes( Entry entry )
    {
        Set<PermissionAttribute> permAttributes = null;
        List<String> ftPAs = getAttributes( entry, GlobalIds.FT_PERMISSION_ATTRIBUTE );

        if ( ftPAs != null )
        {
        	permAttributes = new HashSet<PermissionAttribute>();

            for ( String raw : ftPAs )
            {
                PermissionAttribute permAttribute = new ObjectFactory().createPermissionAttribute();
                permAttribute.load( raw );
                permAttributes.add( permAttribute );
            }
        }

        return permAttributes;
    }
    */

}
