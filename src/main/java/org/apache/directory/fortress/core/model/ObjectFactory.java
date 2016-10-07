/*
 *   Licensed to the Apache Software Foundation (ASF) under one
 *   or more contributor license agreements.  See the NOTICE file
 *   distributed with this work for additional information
 *   regarding copyright ownership.  The ASF licenses this file
 *   to you under the Apache License, Version 2.0 (the
 *   "License"); you may not use this file except in compliance
 *   with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.apache.directory.fortress.core.model;


import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This class contains factory methods for each Java content interface and Java element interface generated in the 
 * org.apache.directory.fortress packages.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation for XML content. 
 * The Java representation of XML content can consist of schema derived interfaces and classes representing the binding of 
 * schema type definitions, element declarations and model groups.  Factory methods for each of these are provided in this
 * class.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
@XmlRegistry
public class ObjectFactory
{
    private static final QName FORTUSER_QNAME = new QName( "", "fortUser" );
    private static final QName FORTSESSION_QNAME = new QName( "", "fortSession" );
    private static final QName FORTROLE_QNAME = new QName( "", "fortRole" );
    private static final QName FORTGRANT_QNAME = new QName( "", "fortGrant" );
    private static final QName FORTORGUNIT_QNAME = new QName( "", "fortOrgUnit" );
    private static final QName FORTENTITY_QNAME = new QName( "", "fortEntity" );
    private static final QName FORTADMINROLE_QNAME = new QName( "", "fortAdminRole" );
    private static final QName FORTUSERROLE_QNAME = new QName( "", "fortUserRole" );
    private static final QName FORTOBJECT_QNAME = new QName( "", "fortObject" );
    private static final QName FORTPERMISSION_QNAME = new QName( "", "fortPermission" );
    private static final QName FORTROLERELATIONSHIP_QNAME = new QName( "", "fortRoleRelationship" );
    private static final QName FORTSET_QNAME = new QName( "", "fortSet" );
    private static final QName FORTPOLICY_QNAME = new QName( "", "fortPolicy" );
    private static final QName FORTUSERADMINROLE_QNAME = new QName( "", "fortUserAdminRole" );
    private static final QName FORTADMINROLERELATIONSHIP_QNAME = new QName( "", "fortAdminRoleRelationship" );
    private static final QName FORTORGUNITRELATIONSHIP_QNAME = new QName( "", "fortOrgUnitRelationship" );
    private static final QName FORTBIND_QNAME = new QName( "", "fortBind" );
    private static final QName FORTUSERAUDIT_QNAME = new QName( "", "fortUserAudit" );
    private static final QName FORTAUTHZ_QNAME = new QName( "", "fortAuthZ" );
    private static final QName FORTMOD_QNAME = new QName( "", "fortMod" );
    private static final QName FORTROLEPERM_QNAME = new QName( "", "fortRolePerm" );
    private static final QName FORTRESPONSE_QNAME = new QName( "", "fortResponse" );
    private static final QName FORTREQUEST_QNAME = new QName( "", "fortRequest" );
    private static final QName FORTADDRESS_QNAME = new QName( "", "fortAddress" );
    private static final QName FORTPROPS_QNAME = new QName( "", "fortProps" );
    private static final QName FORTWARNING_QNAME = new QName( "", "fortWarning" );
    private static final QName FORTGROUP_QNAME = new QName( "", "fortGroup" );
    private static final QName FORTPERMATTR_QNAME = new QName( "", "fortPermissionAttribute" );
    private static final QName FORTPERMATTRSET_QNAME = new QName( "", "fortPermissionAttributeSet" );
    private static final QName FORTROLECONSTRAINT_QNAME = new QName( "", "fortRoleConstraint" );


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.FortEntity }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortEntity")
    public JAXBElement<FortEntity> createFortEntity( FortEntity value )
    {
        return new JAXBElement<>( FORTENTITY_QNAME, FortEntity.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FortResponse }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortResponse")
    public JAXBElement<FortResponse> createFortResponse( FortResponse value )
    {
        return new JAXBElement<>( FORTRESPONSE_QNAME, FortResponse.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FortRequest }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortRequest")
    public JAXBElement<FortRequest> createFortRequest( FortRequest value )
    {
        return new JAXBElement<>( FORTREQUEST_QNAME, FortRequest.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.SDSet }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortSet")
    public JAXBElement<SDSet> createFortSet( SDSet value )
    {
        return new JAXBElement<>( FORTSET_QNAME, SDSet.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.PwPolicy }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortPolicy")
    public JAXBElement<PwPolicy> createFortPolicy( PwPolicy value )
    {
        return new JAXBElement<>( FORTPOLICY_QNAME, PwPolicy.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.Session }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortSession")
    public JAXBElement<Session> createFortSession( Session value )
    {
        return new JAXBElement<>( FORTSESSION_QNAME, Session.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.User }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortUser")
    public JAXBElement<User> createFortUser( User value )
    {
        return new JAXBElement<>( FORTUSER_QNAME, User.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.UserRole }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortUserRole")
    public JAXBElement<UserRole> createFortUserRole( UserRole value )
    {
        return new JAXBElement<>( FORTUSERROLE_QNAME, UserRole.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.OrgUnit }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortOrgUnit")
    public JAXBElement<OrgUnit> createFortOrgUnit( OrgUnit value )
    {
        return new JAXBElement<>( FORTORGUNIT_QNAME, OrgUnit.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.Role }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortRole")
    public JAXBElement<Role> createFortRole( Role value )
    {
        return new JAXBElement<>( FORTROLE_QNAME, Role.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.PermGrant }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortGrant")
    public JAXBElement<PermGrant> createFortGrant( PermGrant value )
    {
        return new JAXBElement<>( FORTGRANT_QNAME, PermGrant.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.AdminRoleRelationship}{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortAdminRoleRelationship")
    public JAXBElement<AdminRoleRelationship> createFortAdminRoleRelationship( AdminRoleRelationship value )
    {
        return new JAXBElement<>( FORTADMINROLERELATIONSHIP_QNAME, AdminRoleRelationship.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.OrgUnitRelationship}{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortOrgUnitRelationship")
    public JAXBElement<OrgUnitRelationship> createFortOrgUnitRelationship( OrgUnitRelationship value )
    {
        return new JAXBElement<>( FORTORGUNITRELATIONSHIP_QNAME, OrgUnitRelationship.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.RoleRelationship}{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortRoleRelationship")
    public JAXBElement<RoleRelationship> createFortRoleRelationship( RoleRelationship value )
    {
        return new JAXBElement<>( FORTROLERELATIONSHIP_QNAME, RoleRelationship.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.Role }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortAdminRole")
    public JAXBElement<AdminRole> createFortAdminRole( AdminRole value )
    {
        return new JAXBElement<>( FORTADMINROLE_QNAME, AdminRole.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.UserAdminRole }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortUserAdminRole")
    public JAXBElement<UserAdminRole> createFortUserRole( UserAdminRole value )
    {
        return new JAXBElement<>( FORTUSERADMINROLE_QNAME, UserAdminRole.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.PermObj }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortObject")
    public JAXBElement<PermObj> createFortObject( PermObj value )
    {
        return new JAXBElement<>( FORTOBJECT_QNAME, PermObj.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.Permission }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortPermission")
    public JAXBElement<Permission> createFortPermission( Permission value )
    {
        return new JAXBElement<>( FORTPERMISSION_QNAME, Permission.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.Bind }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortBind")
    public JAXBElement<Bind> createFortEntity( Bind value )
    {
        return new JAXBElement<>( FORTBIND_QNAME, Bind.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.UserAudit }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortUserAudit")
    public JAXBElement<UserAudit> createFortUserAudit( UserAudit value )
    {
        return new JAXBElement<>( FORTUSERAUDIT_QNAME, UserAudit.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.AuthZ }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortAuthZ")
    public JAXBElement<AuthZ> createFortAuthZ( AuthZ value )
    {
        return new JAXBElement<>( FORTAUTHZ_QNAME, AuthZ.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.Mod }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortMod")
    public JAXBElement<Mod> createFortMod( Mod value )
    {
        return new JAXBElement<>( FORTMOD_QNAME, Mod.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.RolePerm }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortRolePerm")
    public JAXBElement<RolePerm> createFortRolePerm( RolePerm value )
    {
        return new JAXBElement<>( FORTROLEPERM_QNAME, RolePerm.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.Address }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortAddress")
    public JAXBElement<Address> createFortAddress( Address value )
    {
        return new JAXBElement<>( FORTADDRESS_QNAME, Address.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.Props }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortProps")
    public JAXBElement<Props> createFortProps( Props value )
    {
        return new JAXBElement<>( FORTPROPS_QNAME, Props.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.Warning }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortWarning")
    public JAXBElement<Warning> createFortWarning( Warning value )
    {
        return new JAXBElement<Warning>( FORTWARNING_QNAME, Warning.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Group }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortGroup")
    public JAXBElement<Group> createFortGroup( Group value )
    {
        return new JAXBElement<>( FORTGROUP_QNAME, Group.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.PermissionAttribute }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortPermissionAttribute")
    public JAXBElement<PermissionAttribute> createFortPermissionAttribute( PermissionAttribute value )
    {
        return new JAXBElement<>( FORTPERMATTR_QNAME, PermissionAttribute.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.PermissionAttributeSet }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortPermissionAttributeSet")
    public JAXBElement<PermissionAttributeSet> createFortPermissionAttributeSet( PermissionAttributeSet value )
    {
        return new JAXBElement<>( FORTPERMATTRSET_QNAME, PermissionAttributeSet.class, null, value );
    }


    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link org.apache.directory.fortress.core.model.PermissionAttributeSet }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "fortRoleConstraint")
    public JAXBElement<RoleConstraint> createFortRoleConstraint( RoleConstraint value )
    {
        return new JAXBElement<>( FORTROLECONSTRAINT_QNAME, RoleConstraint.class, null, value );
    }


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.apache.directory.fortress.model2
     */
    public ObjectFactory()
    {
    }


    /**
     * Create an instance of {@link User}
     */
    public User createUser()
    {
        return new User();
    }


    /**
     * Create an instance of {@link org.apache.directory.fortress.core.model.PwPolicy }
     */
    public PwPolicy createPswdPolicy()
    {
        return new PwPolicy();
    }


    /**
     * Create an instance of {@link Session}
     */
    public Session createSession()
    {
        return new Session();
    }


    /**
     * Create an instance of {@link SDSet}
     */
    public SDSet createSDset()
    {
        return new SDSet();
    }


    /**
     * Create an instance of {@link Role}
     */
    public Role createRole()
    {
        return new Role();
    }


    /**
     * Create an instance of {@link Group}
     */
    public Group createGroup()
    {
        return new Group();
    }


    /**
     * Create an instance of {@link PermGrant}
     */
    public PermGrant createPermGrant()
    {
        return new PermGrant();
    }


    /**
     * Create an instance of {@link RoleRelationship}
     */
    public RoleRelationship createRoleRelationship()
    {
        return new RoleRelationship();
    }


    /**
     * Create an instance of {@link AdminRoleRelationship}
     */
    public AdminRoleRelationship createAdminRoleRelationship()
    {
        return new AdminRoleRelationship();
    }


    /**
     * Create an instance of {@link OrgUnitRelationship}
     */
    public OrgUnitRelationship createOrgUnitRelationship()
    {
        return new OrgUnitRelationship();
    }


    /**
     * Create an instance of {@link PermObj}
     */
    public PermObj createPermObj()
    {
        return new PermObj();
    }


//    /**
//     * Create an instance of {@link PermissionAttributeSet}
//     */
//    public PermissionAttributeSet createPermAttributeSet()
//    {
//        return new PermissionAttributeSet();
//    }
//
//
    /**
     * Create an instance of {@link Permission}
     */
    public Permission createPermission()
    {
        return new Permission();
    }


    /**
     * Create an instance of {@link Role}
     */
    public AdminRole createAdminRole()
    {
        return new AdminRole();
    }


    /**
     * Create an instance of {@link org.apache.directory.fortress.core.model.UserRole}
     */
    public UserRole createUserRole()
    {
        return new UserRole();
    }


    /**
     * Create an instance of {@link OrgUnit}
     */
    public OrgUnit createOrgUnit()
    {
        return new OrgUnit();
    }


    /**
     * Create an instance of {@link UserAdminRole}
     */
    public UserAdminRole createUserAdminRole()
    {
        return new UserAdminRole();
    }


    /**
     * Create an instance of {@link UserAudit}
     */
    public UserAudit createUserAudit()
    {
        return new UserAudit();
    }


    /**
     * Create an instance of {@link Bind}
     */
    public Bind createBind()
    {
        return new Bind();
    }


    /**
     * Create an instance of {@link AuthZ}
     */
    public AuthZ createAuthZ()
    {
        return new AuthZ();
    }


    /**
     * Create an instance of {@link Mod}
     */
    public Mod createMod()
    {
        return new Mod();
    }


    /**
     * Create an instance of {@link RolePerm}
     */
    public RolePerm createRolePerm()
    {
        return new RolePerm();
    }


    /**
     * Create an instance of {@link FortResponse}
     */
    public FortResponse createFortResponse()
    {
        return new FortResponse();
    }


    /**
     * Create an instance of {@link FortRequest}
     */
    public FortRequest createFortRequest()
    {
        return new FortRequest();
    }


    /**
     * Create an instance of {@link Address}
     */
    public Address createAddress()
    {
        return new Address();
    }


    /**
     * Create an instance of {@link Props}
     */
    public Props createProps()
    {
        return new Props();
    }


    /**
     * Create an instance of {@link Warning}
     */
    public Warning createWarning( int id, String msg, Warning.Type type )
    {
        return new Warning( id, msg, type );
    }


    /**
     * Create an instance of {@link Warning}
     */
    public Warning createWarning( int id, String msg, Warning.Type type, String name )
    {
        return new Warning( id, msg, type, name );
    }
    
    /**
     * Create an instance of {@link org.apache.directory.fortress.core.model.PermissionAttribute}
     */
    public PermissionAttribute createPermissionAttribute()
    {
        return new PermissionAttribute();
    }


    /**
     * Create an instance of {@link PermissionAttributeSet}
     */
    public PermissionAttributeSet createPermissionAttributeSet()
    {
        return new PermissionAttributeSet();
    }


    /**
     * Create an instance of {@link RoleConstraint}
     */
    public RoleConstraint createRoleConstraint()
    {
        return new RoleConstraint();
    }
}
