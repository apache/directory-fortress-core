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
package org.apache.directory.fortress.core.cli;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.*;
import org.apache.directory.fortress.core.SecurityException;
import org.apache.directory.fortress.core.model.Group;
import org.apache.directory.fortress.core.GroupMgr;
import org.apache.directory.fortress.core.GroupMgrFactory;
import org.apache.directory.fortress.core.model.Address;
import org.apache.directory.fortress.core.model.PermObj;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.util.PropUtil;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.SDSet;
import org.apache.directory.fortress.core.model.Session;
import org.apache.directory.fortress.core.model.User;
import org.apache.directory.fortress.core.model.UserRole;
import org.slf4j.LoggerFactory;
import org.apache.directory.fortress.core.model.AdminRole;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.model.UserAdminRole;
import org.apache.directory.fortress.core.model.Relationship;
import org.apache.directory.fortress.core.model.Constraint;


/**
 * Main program for Fortress Command Line Interpreter..
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class CommandLineInterpreter
{
    private static final String CLS_NM = CommandLineInterpreter.class.getName();
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger( CLS_NM );
    private AdminMgr adminMgr;
    private ReviewMgr reviewMgr;
    private AccessMgr accessMgr;
    private DelAdminMgr delAdminMgr;
    private GroupMgr groupMgr;
    //private DelReviewMgr delReviewMgr;
    //private DelAccessMgr delAccessMgr;
    //private PwPolicyMgr pwPolicyMgr;

    /* THESE ARE THE HIGH LEVEL COMMANDS: */
    private static final String ADMIN = "admin";
    private static final String REVIEW = "review";
    private static final String SYSTEM = "system";
    private static final String DELEGATED_ADMIN = "dadmin";
    private static final String DELEGATED_REVIEW = "dreview";
    private static final String DELEGATED_SYSTEM = "dsystem";
    private static final String GROUP = "group";

    /* THESE ARE THE 2ND LEVEL COMMANDS: */
    private static final String ADD_USER = "auser";
    private static final String UPDATE_USER = "uuser";
    private static final String DELETE_USER = "duser";
    private static final String READ_USER = "ruser";
    private static final String FIND_USERS = "fuser";
    private static final String ASSIGNED_USERS = "asgnuser";

    private static final String CHANGE_PASSWORD = "change";
    private static final String LOCK_USER_ACCOUNT = "lock";
    private static final String UNLOCK_USER_ACCOUNT = "unlock";
    private static final String RESET_PASSWORD = "reset";

    private static final String ADD_ROLE = "arole";
    private static final String UPDATE_ROLE = "urole";
    private static final String DELETE_ROLE = "drole";
    private static final String READ_ROLE = "rrole";
    private static final String FIND_ROLES = "frole";
    private static final String ASSIGN_ROLE = "asgnrole";
    private static final String DEASSIGN_ROLE = "dsgnrole";
    private static final String ADD_ROLE_INHERITANCE = "arel";
    private static final String DELETE_ROLE_INHERITANCE = "drel";
    private static final String CREATE_SSD_SET = "asset";
    private static final String DELETE_SSD_SET = "dsset";
    private static final String CREATE_DSD_SET = "adset";
    private static final String DELETE_DSD_SET = "ddset";

    private static final String ADD_POBJ = "aobj";
    private static final String UPDATE_POBJ = "uobj";
    private static final String DELETE_POBJ = "dobj";
    private static final String READ_POBJ = "robj";
    private static final String FIND_POBJS = "fobj";

    private static final String ADD_PERM = "aperm";
    private static final String UPDATE_PERM = "uperm";
    private static final String DELETE_PERM = "dperm";
    private static final String READ_PERM = "rperm";
    private static final String FIND_PERMS = "fperm";
    private static final String GRANT = "grant";
    private static final String REVOKE = "revoke";

    private static final String ADD_GROUP = "agroup";
    private static final String UPDATE_GROUP = "ugroup";
    private static final String DELETE_GROUP = "dgroup";
    private static final String READ_GROUP = "rgroup";
    private static final String FIND_GROUP = "fgroup";
    private static final String ASSIGN_GROUP = "asgngroup";
    private static final String DEASSIGN_GROUP = "dsgngroup";
    private static final String ADD_GROUP_PROP = "agprop";
    private static final String DELETE_GROUP_PROP = "dgprop";

    private static final String ADD_USERORG = "auou";
    private static final String UPDATE_USERORG = "uuou";
    private static final String DELETE_USERORG = "duou";
    private static final String ADD_USERORG_INHERITANCE = "aurel";
    private static final String DELETE_USERORG_INHERITANCE = "durel";
    private static final String ADD_PERMORG = "apou";
    private static final String UPDATE_PERMORG = "upou";
    private static final String DELETE_PERMORG = "dpou";
    private static final String ADD_PERMORG_INHERITANCE = "aprel";
    private static final String DELETE_PERMORG_INHERITANCE = "dprel";

    private static final String CREATE_SESSION = "createsession";
    private static final String AUTHENTICATE = "authenticate";
    private static final String ASSIGNED_ROLES = "assignedroles";
    private static final String CHECK_ACCESS = "checkaccess";


    /**
     * @param args
     */
    public static void main( String[] args )
    {
        CommandLineInterpreter cli = new CommandLineInterpreter();
        cli.runInteractiveMode();
        System.exit( 0 );
    }


    /**
     *
     */
    private void runInteractiveMode()
    {
        if ( !constructManagers() )
        {
            String error = "Startup to interactive mode failed, goodbye.";
            LOG.error( error );
            return;
        }

        LOG.info( "Startup to interactive mode success..." );
        BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
        String input;
        while ( true )
        {
            try
            {
                LOG.info( "CLI function groups include " + ADMIN + ", " + REVIEW + ", " + SYSTEM + ", "
                    + DELEGATED_ADMIN + ", " + GROUP );
                LOG.info( "Enter one from above or 'q' to quit" );
                input = br.readLine();
                if ( StringUtils.isNotEmpty( input ) )
                {
                    if ( "Q".equalsIgnoreCase( input ) )
                    {
                        LOG.info( "Goodbye" );
                        break;
                    }

                    String[] options = parseUserInput( input );
                    processUserInput( options );
                }
            }
            catch ( Exception e )
            {
                String error = "runInteractiveMode caught Exception=" + e.toString();
                LOG.error( error );
                e.printStackTrace();
            }
        }
    }


    private static void printUsage()
    {
        LOG.error( "Usage: group function options" );
        LOG.error( "where group is: admin, review, system, dadmin or group" );
        LOG.error( "Check out the Command Line Reference manual for what the valid function and option combinations are." );
    }


    /**
     * @param commands
     * @param options
     */
    private void processCommand( Set<String> commands, Options options )
    {
        if ( commands.contains( ADMIN ) )
        {
            processAdminCommand( commands, options );
        }
        else if ( commands.contains( REVIEW ) )
        {
            processReviewCommand( commands, options );
        }
        else if ( commands.contains( SYSTEM ) )
        {
            processSystemCommand( commands, options );
        }
        else if ( commands.contains( DELEGATED_ADMIN ) )
        {
            processDelegatedAdminCommand( commands, options );
        }
        else if ( commands.contains( GROUP ) )
        {
            processGroupCommand( commands, options );
        }
        /*
        TODO: add these for delegated admin and review support:
        else if ( commands.contains( DELEGATED_REVIEW ) )
        {
            processDelegatedReviewCommand(commands, options);
        }
        else if ( commands.contains( DELEGATED_SYSTEM ) )
        {
            processDelegatedSystemCommand(commands, options);
        }
        */
        else
        {
            LOG.warn( "unknown admin operation detected" );
        }
    }


    private void processDelegatedAdminCommand( Set<String> commands, Options options )
    {
        String command;
        try
        {
            if ( commands.contains( ADD_ROLE ) )
            {
                command = ADD_ROLE;
                LOG.info( command );
                AdminRole role = options.getAdminRole();
                delAdminMgr.addRole( role );
            }
            else if ( commands.contains( UPDATE_ROLE ) )
            {
                command = UPDATE_ROLE;
                LOG.info( command );
                AdminRole role = options.getAdminRole();
                delAdminMgr.updateRole( role );
            }
            else if ( commands.contains( DELETE_ROLE ) )
            {
                command = DELETE_ROLE;
                LOG.info( command );
                AdminRole role = options.getAdminRole();
                delAdminMgr.deleteRole( role );
            }
            else if ( commands.contains( ASSIGN_ROLE ) )
            {
                command = ASSIGN_ROLE;
                LOG.info( command );
                Role role = options.getRole();
                String userId = options.getUserId();
                delAdminMgr.assignUser( new UserAdminRole( userId, role ) );
            }
            else if ( commands.contains( DEASSIGN_ROLE ) )
            {
                command = DEASSIGN_ROLE;
                LOG.info( command );
                Role role = options.getRole();
                String userId = options.getUserId();
                delAdminMgr.deassignUser( new UserAdminRole( userId, role ) );
            }
            else if ( commands.contains( ADD_ROLE_INHERITANCE ) )
            {
                command = ADD_ROLE_INHERITANCE;
                LOG.info( command );
                Relationship relationship = options.getRelationship();
                delAdminMgr.addInheritance( new AdminRole( relationship.getParent() ),
                    new AdminRole( relationship.getChild() ) );
            }
            else if ( commands.contains( DELETE_ROLE_INHERITANCE ) )
            {
                command = DELETE_ROLE_INHERITANCE;
                LOG.info( command );
                Relationship relationship = options.getRelationship();
                delAdminMgr.deleteInheritance( new AdminRole( relationship.getParent() ),
                    new AdminRole( relationship.getChild() ) );
            }
            else if ( commands.contains( ADD_POBJ ) )
            {
                command = ADD_POBJ;
                LOG.info( command );
                PermObj permObj = options.getPermObj();
                delAdminMgr.addPermObj( permObj );
            }
            else if ( commands.contains( UPDATE_POBJ ) )
            {
                command = UPDATE_POBJ;
                LOG.info( command );
                PermObj permObj = options.getPermObj();
                delAdminMgr.updatePermObj( permObj );
            }
            else if ( commands.contains( DELETE_POBJ ) )
            {
                command = DELETE_POBJ;
                LOG.info( command );
                PermObj permObj = options.getPermObj();
                delAdminMgr.deletePermObj( permObj );
            }
            else if ( commands.contains( ADD_PERM ) )
            {
                command = ADD_PERM;
                LOG.info( command );
                Permission perm = options.getPermission();
                delAdminMgr.addPermission( perm );
            }
            else if ( commands.contains( UPDATE_PERM ) )
            {
                command = UPDATE_PERM;
                LOG.info( command );
                Permission perm = options.getPermission();
                delAdminMgr.updatePermission( perm );
            }
            else if ( commands.contains( DELETE_PERM ) )
            {
                command = DELETE_PERM;
                LOG.info( command );
                Permission permObj = options.getPermission();
                delAdminMgr.deletePermission( permObj );
            }
            else if ( commands.contains( GRANT ) )
            {
                command = GRANT;
                LOG.info( command );
                Permission perm = options.getPermission();
                AdminRole role = options.getAdminRole();
                role.setName( options.getRoleNm() );
                delAdminMgr.grantPermission( perm, role );
            }
            else if ( commands.contains( REVOKE ) )
            {
                command = REVOKE;
                LOG.info( command );
                Permission perm = options.getPermission();
                AdminRole role = options.getAdminRole();
                role.setName( options.getRoleNm() );
                delAdminMgr.revokePermission( perm, role );
            }
            else if ( commands.contains( ADD_USERORG ) )
            {
                command = ADD_USERORG;
                LOG.info( command );
                OrgUnit orgUnit = options.getOrgUnit();
                orgUnit.setType( OrgUnit.Type.USER );
                delAdminMgr.add( orgUnit );
            }
            else if ( commands.contains( UPDATE_USERORG ) )
            {
                command = UPDATE_USERORG;
                LOG.info( command );
                OrgUnit orgUnit = options.getOrgUnit();
                orgUnit.setType( OrgUnit.Type.USER );
                delAdminMgr.update( orgUnit );
            }
            else if ( commands.contains( DELETE_USERORG ) )
            {
                command = DELETE_USERORG;
                LOG.info( command );
                OrgUnit orgUnit = options.getOrgUnit();
                orgUnit.setType( OrgUnit.Type.USER );
                delAdminMgr.delete( orgUnit );
            }
            else if ( commands.contains( ADD_USERORG_INHERITANCE ) )
            {
                command = ADD_USERORG_INHERITANCE;
                LOG.info( command );
                Relationship relationship = options.getRelationship();
                delAdminMgr.addInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.USER ), new OrgUnit(
                    relationship.getChild(), OrgUnit.Type.USER ) );
            }
            else if ( commands.contains( DELETE_USERORG_INHERITANCE ) )
            {
                command = DELETE_USERORG_INHERITANCE;
                LOG.info( command );
                Relationship relationship = options.getRelationship();
                delAdminMgr.deleteInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.USER ), new OrgUnit(
                    relationship.getChild(), OrgUnit.Type.USER ) );
            }
            else if ( commands.contains( ADD_PERMORG ) )
            {
                command = ADD_PERMORG;
                LOG.info( command );
                OrgUnit orgUnit = options.getOrgUnit();
                orgUnit.setType( OrgUnit.Type.PERM );
                delAdminMgr.add( orgUnit );
            }
            else if ( commands.contains( UPDATE_PERMORG ) )
            {
                command = UPDATE_PERMORG;
                LOG.info( command );
                OrgUnit orgUnit = options.getOrgUnit();
                orgUnit.setType( OrgUnit.Type.PERM );
                delAdminMgr.update( orgUnit );
            }
            else if ( commands.contains( DELETE_PERMORG ) )
            {
                command = DELETE_PERMORG;
                LOG.info( command );
                OrgUnit orgUnit = options.getOrgUnit();
                orgUnit.setType( OrgUnit.Type.PERM );
                delAdminMgr.delete( orgUnit );
            }
            else if ( commands.contains( ADD_PERMORG_INHERITANCE ) )
            {
                command = ADD_PERMORG_INHERITANCE;
                LOG.info( command );
                Relationship relationship = options.getRelationship();
                delAdminMgr.addInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.PERM ), new OrgUnit(
                    relationship.getChild(), OrgUnit.Type.PERM ) );
            }
            else if ( commands.contains( DELETE_PERMORG_INHERITANCE ) )
            {
                command = DELETE_PERMORG_INHERITANCE;
                LOG.info( command );
                Relationship relationship = options.getRelationship();
                delAdminMgr.deleteInheritance( new OrgUnit( relationship.getParent(), OrgUnit.Type.PERM ), new OrgUnit(
                    relationship.getChild(), OrgUnit.Type.PERM ) );
            }
            else
            {
                LOG.warn( "unknown delegated admin operation detected" );
                return;
            }
            LOG.info( "command:{} was successful", command );
        }
        catch ( org.apache.directory.fortress.core.SecurityException se )
        {
            String error = "processDelegatedAdminCommand caught SecurityException=" + se + ", return code="
                + se.getErrorId();
            LOG.error( error );
        }
    }


    /**
     * @param commands
     * @param options
     */
    private void processAdminCommand( Set<String> commands, Options options )
    {
        String command;
        try
        {
            if ( commands.contains( ADD_USER ) )
            {
                command = ADD_USER;
                LOG.info( command );
                User user = options.getUser();
                adminMgr.addUser( user );
            }
            else if ( commands.contains( UPDATE_USER ) )
            {
                command = UPDATE_USER;
                LOG.info( command );
                User user = options.getUser();
                adminMgr.updateUser( user );
            }
            else if ( commands.contains( DELETE_USER ) )
            {
                command = DELETE_USER;
                LOG.info( command );
                User user = options.getUser();
                adminMgr.deleteUser( user );
            }
            else if ( commands.contains( ADD_ROLE ) )
            {
                command = ADD_ROLE;
                LOG.info( command );
                Role role = options.getRole();
                adminMgr.addRole( role );
            }
            else if ( commands.contains( UPDATE_ROLE ) )
            {
                command = UPDATE_ROLE;
                LOG.info( command );
                Role role = options.getRole();
                adminMgr.updateRole( role );
            }
            else if ( commands.contains( DELETE_ROLE ) )
            {
                command = DELETE_ROLE;
                LOG.info( command );
                Role role = options.getRole();
                adminMgr.deleteRole( role );
            }
            else if ( commands.contains( ASSIGN_ROLE ) )
            {
                command = ASSIGN_ROLE;
                LOG.info( command );
                Role role = options.getRole();
                String userId = options.getUserId();
                adminMgr.assignUser( new UserRole( userId, role ) );
            }
            else if ( commands.contains( DEASSIGN_ROLE ) )
            {
                command = DEASSIGN_ROLE;
                LOG.info( command );
                Role role = options.getRole();
                String userId = options.getUserId();
                adminMgr.deassignUser( new UserRole( userId, role ) );
            }
            else if ( commands.contains( ADD_ROLE_INHERITANCE ) )
            {
                command = ADD_ROLE_INHERITANCE;
                LOG.info( command );
                Relationship relationship = options.getRelationship();
                adminMgr.addInheritance( new Role( relationship.getParent() ), new Role( relationship.getChild() ) );
            }
            else if ( commands.contains( DELETE_ROLE_INHERITANCE ) )
            {
                command = DELETE_ROLE_INHERITANCE;
                LOG.info( command );
                Relationship relationship = options.getRelationship();
                adminMgr.deleteInheritance( new Role( relationship.getParent() ), new Role( relationship.getChild() ) );
            }
            else if ( commands.contains( ADD_POBJ ) )
            {
                command = ADD_POBJ;
                LOG.info( command );
                PermObj permObj = options.getPermObj();
                adminMgr.addPermObj( permObj );
            }
            else if ( commands.contains( UPDATE_POBJ ) )
            {
                command = UPDATE_POBJ;
                LOG.info( command );
                PermObj permObj = options.getPermObj();
                adminMgr.updatePermObj( permObj );
            }
            else if ( commands.contains( DELETE_POBJ ) )
            {
                command = DELETE_POBJ;
                LOG.info( command );
                PermObj permObj = options.getPermObj();
                adminMgr.deletePermObj( permObj );
            }
            else if ( commands.contains( ADD_PERM ) )
            {
                command = ADD_PERM;
                LOG.info( command );
                Permission perm = options.getPermission();
                adminMgr.addPermission( perm );
            }
            else if ( commands.contains( UPDATE_PERM ) )
            {
                command = UPDATE_PERM;
                LOG.info( command );
                Permission perm = options.getPermission();
                adminMgr.updatePermission( perm );
            }
            else if ( commands.contains( DELETE_PERM ) )
            {
                command = DELETE_PERM;
                LOG.info( command );
                Permission permObj = options.getPermission();
                adminMgr.deletePermission( permObj );
            }
            else if ( commands.contains( GRANT ) )
            {
                command = GRANT;
                LOG.info( command );
                Permission perm = options.getPermission();
                Role role = options.getRole();
                role.setName( options.getRoleNm() );
                adminMgr.grantPermission( perm, role );
            }
            else if ( commands.contains( REVOKE ) )
            {
                command = REVOKE;
                LOG.info( command );
                Permission perm = options.getPermission();
                Role role = options.getRole();
                role.setName( options.getRoleNm() );
                adminMgr.revokePermission( perm, role );
            }
            else if ( commands.contains( CREATE_SSD_SET ) )
            {
                command = CREATE_SSD_SET;
                LOG.info( command );
                SDSet ssd = options.getSdSet();
                ssd.setType( SDSet.SDType.STATIC );
                adminMgr.createSsdSet( ssd );
            }
            else if ( commands.contains( DELETE_SSD_SET ) )
            {
                command = DELETE_SSD_SET;
                LOG.info( command );
                SDSet ssd = options.getSdSet();
                ssd.setType( SDSet.SDType.STATIC );
                adminMgr.deleteSsdSet( ssd );
            }
            else if ( commands.contains( CREATE_DSD_SET ) )
            {
                command = CREATE_DSD_SET;
                LOG.info( command );
                SDSet ssd = options.getSdSet();
                ssd.setType( SDSet.SDType.DYNAMIC );
                adminMgr.createDsdSet( ssd );
            }
            else if ( commands.contains( DELETE_DSD_SET ) )
            {
                command = DELETE_DSD_SET;
                LOG.info( command );
                SDSet ssd = options.getSdSet();
                ssd.setType( SDSet.SDType.DYNAMIC );
                adminMgr.deleteDsdSet( ssd );
            }
            else if ( commands.contains( CHANGE_PASSWORD ) )
            {
                command = CHANGE_PASSWORD;
                LOG.info( command );
                User user = options.getUser();
                String newPassword = options.getNewPassword();
                adminMgr.changePassword( user, newPassword );
            }
            else if ( commands.contains( RESET_PASSWORD ) )
            {
                command = RESET_PASSWORD;
                LOG.info( command );
                User user = options.getUser();
                String newPassword = options.getNewPassword();
                adminMgr.resetPassword( user, newPassword );
            }
            else if ( commands.contains( LOCK_USER_ACCOUNT ) )
            {
                command = LOCK_USER_ACCOUNT;
                LOG.info( command );
                User user = options.getUser();
                adminMgr.lockUserAccount( user );
            }
            else if ( commands.contains( UNLOCK_USER_ACCOUNT ) )
            {
                command = UNLOCK_USER_ACCOUNT;
                LOG.info( command );
                User user = options.getUser();
                adminMgr.unlockUserAccount( user );
            }
            else
            {
                LOG.warn( "unknown admin operation detected" );
                return;
            }
            LOG.info( "command:{} was successful", command );
        }
        catch ( SecurityException se )
        {
            String error = "processAdminCommand caught SecurityException=" + se + ", return code=" + se.getErrorId();
            LOG.error( error );
        }
    }


    /**
     * @param commands
     * @param options
     */
    private void processReviewCommand( Set<String> commands, Options options )
    {
        String command;
        try
        {
            if ( commands.contains( READ_USER ) )
            {
                command = READ_USER;
                LOG.info( READ_USER );
                User inUser = options.getUser();
                User outUser = reviewMgr.readUser( inUser );
                printUser( outUser );
            }
            else if ( commands.contains( FIND_USERS ) )
            {
                command = FIND_USERS;
                LOG.info( command );
                User user = options.getUser();
                List<User> outUsers = reviewMgr.findUsers( user );
                if ( CollectionUtils.isNotEmpty( outUsers ) )
                {
                    int ctr = 0;
                    for ( User outUser : outUsers )
                    {
                        printRow( "U", "CTR ", "" + ctr++ );
                        printUser( outUser );
                    }
                }
            }
            else if ( commands.contains( ASSIGNED_USERS ) )
            {
                command = ASSIGNED_USERS;
                LOG.info( command );
                Role inRole = options.getRole();
                List<User> outUsers = reviewMgr.assignedUsers( inRole );
                if ( CollectionUtils.isNotEmpty( outUsers ) )
                {
                    for ( User outUser : outUsers )
                    {
                        printUser( outUser );
                    }
                }
            }
            else if ( commands.contains( READ_ROLE ) )
            {
                command = READ_ROLE;
                LOG.info( command );
                Role inRole = options.getRole();
                Role outRole = reviewMgr.readRole( inRole );
                printRole( outRole );
            }
            else if ( commands.contains( FIND_ROLES ) )
            {
                command = FIND_ROLES;
                LOG.info( command );
                String inRoleNm = options.getName();
                List<Role> outRoles = reviewMgr.findRoles( inRoleNm );
                if ( CollectionUtils.isNotEmpty( outRoles ) )
                {
                    int ctr = 0;
                    for ( Role outRole : outRoles )
                    {
                        printSeparator();
                        printRow( "R", "ROLE[" + ++ctr + "]", outRole.getName() );
                        printRole( outRole );
                    }
                }
            }
            else if ( commands.contains( ASSIGNED_ROLES ) )
            {
                command = ASSIGNED_ROLES;
                LOG.info( command );
                String userId = options.getUserId();
                List<UserRole> uRoles = reviewMgr.assignedRoles( new User( userId ) );
                if ( uRoles != null )
                {
                    for ( UserRole ur : uRoles )
                    {
                        printTemporal( "R", ur, "RBACROLE" );
                        printSeparator();
                    }
                }
            }
            else if ( commands.contains( READ_POBJ ) )
            {
                command = READ_POBJ;
                LOG.info( command );
                PermObj inPermObj = options.getPermObj();
                PermObj outPermObj = reviewMgr.readPermObj( inPermObj );
                printPermObj( outPermObj );
            }
            else if ( commands.contains( FIND_POBJS ) )
            {
                command = FIND_POBJS;
                LOG.info( command );
                PermObj inPermObj = options.getPermObj();
                List<PermObj> outPermObjs = reviewMgr.findPermObjs( inPermObj );
                if ( CollectionUtils.isNotEmpty( outPermObjs ) )
                {
                    int ctr = 0;
                    for ( PermObj outPermObj : outPermObjs )
                    {
                        printSeparator();
                        printRow( "PO", "POBJ[" + ++ctr + "]", outPermObj.getObjName() );
                        printPermObj( outPermObj );
                    }
                }
            }
            else if ( commands.contains( READ_PERM ) )
            {
                command = READ_PERM;
                LOG.info( command );
                Permission inPerm = options.getPermission();
                Permission outPerm = reviewMgr.readPermission( inPerm );
                printPermission( outPerm );
            }
            else if ( commands.contains( FIND_PERMS ) )
            {
                command = FIND_PERMS;
                LOG.info( command );
                Permission inPerm = options.getPermission();
                List<Permission> outPerms = reviewMgr.findPermissions( inPerm );
                if ( CollectionUtils.isNotEmpty( outPerms ) )
                {
                    int ctr = 0;
                    for ( Permission outPerm : outPerms )
                    {
                        printSeparator();
                        printRow( "P", "PERM[" + ++ctr + "]", outPerm.getAbstractName() );
                        printPermission( outPerm );
                    }
                }
            }
            else
            {
                LOG.warn( "unknown review operation detected" );
                return;
            }
            LOG.info( "command:{} was successful", command );
        }
        catch ( SecurityException se )
        {
            String error = "processReviewCommand caught SecurityException=" + se + ", return code=" + se.getErrorId();
            LOG.error( error );
        }
    }


    /**
     * @param commands
     * @param options
     */
    private void processSystemCommand( Set<String> commands, Options options )
    {
        String command;
        try
        {
            if ( commands.contains( CREATE_SESSION ) )
            {
                command = CREATE_SESSION;
                LOG.info( READ_USER );
                User inUser = options.getUser();
                Session session = accessMgr.createSession( inUser, false );
                printSession( session );
            }
            else if ( commands.contains( AUTHENTICATE ) )
            {
                command = AUTHENTICATE;
                LOG.info( command );
                User inUser = options.getUser();
                Session session = accessMgr.authenticate( inUser.getUserId(), inUser.getPassword() );
                printSession( session );
            }
            else if ( commands.contains( ASSIGNED_ROLES ) )
            {
                command = ASSIGNED_ROLES;
                LOG.info( command );
                User inUser = options.getUser();
                Session session = accessMgr.createSession( inUser, true );
                List<UserRole> uRoles = accessMgr.sessionRoles( session );
                if ( uRoles != null )
                {
                    for ( UserRole ur : uRoles )
                    {
                        printTemporal( "R", ur, "RBACROLE" );
                        printSeparator();
                    }
                }
            }
            else if ( commands.contains( CHECK_ACCESS ) )
            {
                command = CHECK_ACCESS;
                LOG.info( command );
                Permission inPerm = options.getPermission();
                User inUser = options.getUser();
                Session session = accessMgr.createSession( inUser, true );
                boolean result = accessMgr.checkAccess( session, inPerm );
                printRow( "CA", "PERM", "" + result );
            }
            else
            {
                LOG.warn( "unknown system operation detected" );
                return;
            }
            LOG.info( "command:{} was successful", command );
        }
        catch ( SecurityException se )
        {
            String error = "processSystemCommand caught SecurityException=" + se + ", return code=" + se.getErrorId();
            LOG.error( error );
        }
    }


    /**
     * @param commands
     * @param options
     */
    private void processGroupCommand( Set<String> commands, Options options )
    {
        String command;
        try
        {
            if ( commands.contains( ADD_GROUP ) )
            {
                command = ADD_GROUP;
                LOG.info( command );
                Group group = options.getGroup();
                groupMgr.add( group );
            }
            else if ( commands.contains( UPDATE_GROUP ) )
            {
                command = UPDATE_GROUP;
                LOG.info( command );
                Group group = options.getGroup();
                groupMgr.update( group );
            }
            else if ( commands.contains( DELETE_GROUP ) )
            {
                command = DELETE_GROUP;
                LOG.info( command );
                Group group = options.getGroup();
                groupMgr.delete( group );
            }
            else if ( commands.contains( READ_GROUP ) )
            {
                command = READ_GROUP;
                LOG.info( command );
                Group group = options.getGroup();
                Group outGroup = groupMgr.read( group );
                printGroup( outGroup );
            }
            else if ( commands.contains( FIND_GROUP ) )
            {
                command = FIND_GROUP;
                LOG.info( command );
                Group inGroup = options.getGroup();
                List<Group> groups = groupMgr.find( inGroup );
                if ( CollectionUtils.isNotEmpty( groups ) )
                {
                    for ( Group outGroup : groups )
                    {
                        printGroup( outGroup );
                    }
                }
            }
            else if ( commands.contains( ASSIGN_GROUP ) )
            {
                command = ASSIGN_GROUP;
                LOG.info( command );
                Group group = options.getGroup();
                if ( CollectionUtils.isNotEmpty( group.getMembers() ) )
                {
                    for ( String member : group.getMembers() )
                    {
                        groupMgr.assign( group, member );
                    }
                }
            }
            else if ( commands.contains( DEASSIGN_GROUP ) )
            {
                command = DEASSIGN_GROUP;
                LOG.info( command );
                Group group = options.getGroup();
                if ( CollectionUtils.isNotEmpty( group.getMembers() ) )
                {
                    for ( String member : group.getMembers() )
                    {
                        groupMgr.deassign( group, member );
                    }
                }
            }
            else if ( commands.contains( ADD_GROUP_PROP ) )
            {
                command = ADD_GROUP_PROP;
                LOG.info( command );
                Group group = options.getGroup();
                if ( PropUtil.isNotEmpty( group.getProperties() ) )
                {
                    for ( Enumeration<?> e = group.getProperties().propertyNames(); e.hasMoreElements(); )
                    {
                        String key = ( String ) e.nextElement();
                        String val = group.getProperty( key );
                        groupMgr.add( group, key, val );
                    }
                }
            }
            else if ( commands.contains( DELETE_GROUP_PROP ) )
            {
                command = DELETE_GROUP_PROP;
                LOG.info( command );
                Group group = options.getGroup();
                if ( PropUtil.isNotEmpty( group.getProperties() ) )
                {
                    for ( Enumeration<?> e = group.getProperties().propertyNames(); e.hasMoreElements(); )
                    {
                        String key = ( String ) e.nextElement();
                        String val = group.getProperty( key );
                        groupMgr.delete( group, key, val );
                    }
                }
            }
            else
            {
                LOG.warn( "unknown group operation detected" );
                return;
            }
            LOG.info( "command:{} was successful", command );
        }
        catch ( SecurityException se )
        {
            String error = "processGroupCommand caught SecurityException=" + se + ", return code=" + se.getErrorId();
            LOG.error( error );
        }
    }


    /**
     * @param parser
     * @return entity containing user options
     */
    private Options loadOptions( CmdLineParser parser )
    {
        return new Options( parser );
    }


    /**
     *
     * @param input
     * @return array of strings
     */
    private String[] parseUserInput( String input )
    {
        List<String> options = new ArrayList<>();
        // Break into separate tokens Strings that are delimited by "", '', or white space:
        Pattern regex = Pattern.compile( "[^\\s\"']+|\"([^\"]*)\"|'([^']*)'" );
        Matcher regexMatcher = regex.matcher( input );

        boolean isPassword = false;
        while ( regexMatcher.find() )
        {
            String arg;
            if ( regexMatcher.group( 1 ) != null )
            {
                // Add double-quoted string without the quotes
                arg = regexMatcher.group( 1 );
            }
            else if ( regexMatcher.group( 2 ) != null )
            {
                // Add single-quoted string without the quotes
                arg = regexMatcher.group( 2 );
            }
            else
            {
                // Add unquoted word
                arg = regexMatcher.group();
            }
            options.add( arg );
            if ( !isPassword )
            {
                LOG.info( "arg:{}", arg );
            }
            else
            {
                isPassword = false;
            }
            if ( "-p".equalsIgnoreCase( arg ) )
            {
                isPassword = true;
            }
        }
        return options.toArray( new String[options.size()] );
    }


    /**
     * @param args
     */
    private void processUserInput( String[] args )
    {
        CmdLineParser parser = new CmdLineParser();
        Options options = loadOptions( parser );
        Set<String> commands = null;
        try
        {
            parser.parse( args );
            commands = loadCommandSet( parser.getRemainingArgs() );
        }
        catch ( CmdLineParser.OptionException e )
        {
            String error = "processUserInput caught OptionException=" + e.toString();
            LOG.error( error );
            printUsage();
            //System.exit(2);
        }
        if ( commands != null && commands.size() > 0 )
        {
            processCommand( commands, options );
        }
    }


    /**
     * @param otherArgs
     */
    private Set<String> loadCommandSet( String[] otherArgs )
    {
        Set<String> commands = null;
        if ( ArrayUtils.isNotEmpty( otherArgs ) )
        {
            commands = new HashSet<>();
            Collections.addAll( commands, otherArgs );
        }
        return commands;
    }


    /**
     * @param role
     */
    private void printRole( Role role )
    {
        String type = "R";
        if ( role != null )
        {
            printRow( type, "NAME", role.getName() );
            printRow( type, "IID ", role.getId() );
            if ( CollectionUtils.isNotEmpty( role.getParents() ) )
            {
                for ( String parentRole : role.getParents() )
                {
                    printRow( type, "PRLE", parentRole );
                }
            }
            if ( CollectionUtils.isNotEmpty( role.getChildren() ) )
            {
                for ( String childRole : role.getChildren() )
                {
                    printRow( type, "CRLE", childRole );
                }
            }
            printTemporal( type, role, "ROLE" );
        }
    }


    private void printPermission( Permission perm )
    {
        String type = "P";

        if ( perm != null )
        {
            printRow( type, "POBJ", perm.getObjName() );
            printRow( type, "OPER", perm.getOpName() );
            printRow( type, "IID", perm.getInternalId() );
            printRow( type, "TYPE", perm.getType() );
        }

        if ( CollectionUtils.isNotEmpty( perm != null ? perm.getRoles() : null ) )
        {
            for ( String roleName : perm.getRoles() )
            {
                printRow( "R", "ROLE", roleName );
            }
        }

        if ( perm != null )
        {
            if ( CollectionUtils.isNotEmpty( perm.getUsers() ) )
            {
                for ( String userId : perm.getUsers() )
                {
                    printRow( "U", "USER", userId );
                }
            }

            if ( PropUtil.isNotEmpty( perm.getProperties() ) )
            {
                printSeparator();
                int ctr = 0;

                for ( Enumeration<?> e = perm.getProperties().propertyNames(); e.hasMoreElements(); )
                {
                    String key = ( String ) e.nextElement();
                    String val = perm.getProperty( key );
                    ++ctr;
                    LOG.info( "{}   KEY{} [{}]", type, ctr, key );
                    LOG.info( "{}   VAL{} [{}]", type, ctr, val );
                }
            }
        }
    }


    private void printPermObj( PermObj permObj )
    {
        String type = "O";
        if ( permObj != null )
        {
            printRow( type, "NAME", permObj.getObjName() );
            printRow( type, "IID ", permObj.getInternalId() );
            printRow( type, "TYPE", permObj.getType() );
            printRow( type, "OU  ", permObj.getOu() );
        }
        if ( PropUtil.isNotEmpty( permObj != null ? permObj.getProperties() : null ) )
        {
            printSeparator();
            int ctr = 0;
            for ( Enumeration<?> e = permObj.getProperties().propertyNames(); e.hasMoreElements(); )
            {
                String key = ( String ) e.nextElement();
                String val = permObj.getProperty( key );
                ++ctr;
                LOG.info( "{}   KEY{} [{}]", type, ctr, key );
                LOG.info( "{}   VAL{} [{}]", type, ctr, val );
            }
        }
    }


    private void printSession( Session session )
    {
        String type = "S";
        printRow( type, "UID ", session.getUserId() );
        printRow( type, "IID ", session.getInternalUserId() );
        printRow( type, "ERR ", "" + session.getErrorId() );
        printRow( type, "WARN", "" + session.getWarnings() );
        printRow( type, "MSG ", session.getMsg() );
        printRow( type, "EXP ", "" + session.getExpirationSeconds() );
        printRow( type, "GRAC", "" + session.getGraceLogins() );
        printRow( type, "AUTH", "" + session.isAuthenticated() );
        printRow( type, "LAST", "" + session.getLastAccess() );
        printRow( type, "SID ", session.getSessionId() );
        printSeparator();
        User user = session.getUser();
        if ( user != null )
        {
            printUser( user );
        }
    }


    /**
     * @param user
     */
    private void printUser( User user )
    {
        String type = "U";
        if ( user != null )
        {
            printRow( type, "UID ", user.getUserId() );
            printRow( type, "IID ", user.getInternalId() );
            printRow( type, "CN  ", user.getCn() );
            printRow( type, "DESC", user.getDescription() );
            printRow( type, "OU  ", user.getOu() );
            printRow( type, "SN  ", user.getSn() );
            printRow( type, "BDTE", user.getBeginDate() );
            printRow( type, "EDTE", user.getEndDate() );
            printRow( type, "BLDT", user.getBeginLockDate() );
            printRow( type, "ELDT", user.getEndLockDate() );
            printRow( type, "DMSK", user.getDayMask() );
            printRow( type, "TO  ", "" + user.getTimeout() );
            printRow( type, "REST", "" + user.isReset() );
            printTemporal( type, user, "USER" );
            printAddress( type, user.getAddress(), "ADDR" );
            printPhone( type, user.getPhones(), "PHNE" );
            printPhone( type, user.getPhones(), "MOBL" );

            if ( CollectionUtils.isNotEmpty( user.getRoles() ) )
            {
                for ( UserRole ur : user.getRoles() )
                {
                    printSeparator();
                    printTemporal( "R", ur, "RBACROLE" );
                    if ( CollectionUtils.isNotEmpty( ur.getParents() ) )
                    {
                        for ( String parentRole : ur.getParents() )
                        {
                            printRow( "R", "PRLE", parentRole );
                        }
                    }
                }
            }
            if ( CollectionUtils.isNotEmpty( user.getAdminRoles() ) )
            {
                for ( UserAdminRole ur : user.getAdminRoles() )
                {
                    printSeparator();
                    printTemporal( "A", ur, "ADMINROLE" );
                    printAdminRole( "A", ur );
                }
            }
            if ( PropUtil.isNotEmpty( user.getProperties() ) )
            {
                printSeparator();
                int ctr = 0;
                for ( Enumeration<?> e = user.getProperties().propertyNames(); e.hasMoreElements(); )
                {
                    String key = ( String ) e.nextElement();
                    String val = user.getProperty( key );
                    ++ctr;
                    LOG.info( "{}   KEY{} [{}]", type, ctr, key );
                    LOG.info( "{}   VAL{} [{}]", type, ctr, val );
                }
            }
        }
    }


    private void printGroup( Group group )
    {
        String type = "G";
        if ( group != null )
        {
            printSeparator();
            printRow( type, "GROUP DATA", group.getName() );
            printRow( type, "NAME ", group.getName() );
            printRow( type, "DESC", group.getDescription() );
            printRow( type, "PROT", group.getProtocol() );
            if ( CollectionUtils.isNotEmpty( group.getMembers() ) )
            {
                int memctr = 0;
                for ( String member : group.getMembers() )
                {
                    printRow( type, "MEMBER[" + ++memctr + "]", member );
                }
            }
            if ( PropUtil.isNotEmpty( group.getProperties() ) )
            {
                int propctr = 0;
                for ( Enumeration<?> e = group.getProperties().propertyNames(); e.hasMoreElements(); )
                {
                    String key = ( String ) e.nextElement();
                    String val = group.getProperty( key );
                    printRow( type, "PROP[" + ++propctr + "]", key + "=" + val );
                }
            }
        }
    }


    /**
     * @param constraint
     * @param label
     */
    private void printTemporal( String type, Constraint constraint, String label )
    {
        if ( constraint != null )
        {
            printRow( type, "TYPE", label );
            printRow( type, "NAME", constraint.getName() );
            printRow( type, "BTME", constraint.getBeginTime() );
            printRow( type, "ETME", constraint.getEndTime() );
            printRow( type, "BDTE", constraint.getBeginDate() );
            printRow( type, "EDTE", constraint.getEndDate() );
            printRow( type, "BLDT", constraint.getBeginLockDate() );
            printRow( type, "ELDT", constraint.getEndLockDate() );
            printRow( type, "DMSK", constraint.getDayMask() );
            printRow( type, "TO  ", "" + constraint.getTimeout() );
        }
    }


    private void printAddress( String type, Address address, String label )
    {
        if ( address != null )
        {
            printRow( type, "TYPE", label );
            System.out.println( label );
            if ( CollectionUtils.isNotEmpty( address.getAddresses() ) )
            {
                for ( String addr : address.getAddresses() )
                {
                    printRow( type, "LINE", addr );
                }
            }
            printRow( type, "CITY", address.getCity() );
            printRow( type, "PROV", address.getState() );
            printRow( type, "ZIPC", address.getPostalCode() );
            printRow( type, "PBOX", address.getPostOfficeBox() );
        }
    }


    private void printPhone( String type, List<String> phones, String label )
    {
        if ( phones != null )
        {
            printRow( type, "TYPE", label );
            for ( String phone : phones )
            {
                printRow( type, "TELE", phone );
            }
        }
    }


    /**
     * @param ur
     */
    private void printAdminRole( String type, UserAdminRole ur )
    {
        if ( ur != null )
        {
            printRow( type, "BEGR", ur.getBeginRange() );
            printRow( type, "ENDR", ur.getEndRange() );
            if ( CollectionUtils.isNotEmpty( ur.getOsPSet() ) )
            {
                printRow( type, "POUS", "" + ur.getOsPSet() );
            }
            if ( CollectionUtils.isNotEmpty( ur.getOsUSet() ) )
            {
                printRow( type, "UOUS", "" + ur.getOsUSet() );
            }
        }
    }


    /**
     * @param type
     * @param name
     * @param value
     */
    private void printRow( String type, String name, String value )
    {
        LOG.info( "{}   {} [{}]", type, name, value );
    }


    /**
     *
     */
    private void printSeparator()
    {
        LOG.info( "------------------------------------------" );
    }


    /**
     */
    private boolean constructManagers()
    {
        String contextId = GlobalIds.HOME;
        boolean success = false;
        // This property can be overriden with system property:
        String tenant = System.getProperty( GlobalIds.TENANT );
        if ( StringUtils.isNotEmpty( tenant ) && !tenant.equals( "${tenant}" ) )
        {
            contextId = tenant;
        }
        try
        {
            adminMgr = AdminMgrFactory.createInstance( contextId );
            reviewMgr = ReviewMgrFactory.createInstance( contextId );
            accessMgr = AccessMgrFactory.createInstance( contextId );
            accessMgr = AccessMgrFactory.createInstance( contextId );
            groupMgr = GroupMgrFactory.createInstance( contextId );
            delAdminMgr = DelAdminMgrFactory.createInstance(contextId);
            //delReviewMgr = DelReviewMgrFactory.createInstance(contextId);
            //delAccessMgr = DelAccessMgrFactory.createInstance(contextId);
            //pwPolicyMgr = PwPolicyMgrFactory.createInstance(contextId);
            success = true;
        }
        catch ( SecurityException se )
        {
            String error = "constructManagers caught SecurityException=" + se;
            LOG.error( error );
        }
        return success;
    }
}