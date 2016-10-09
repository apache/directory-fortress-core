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
package org.apache.directory.fortress.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.directory.fortress.core.model.Group;

import java.util.Enumeration;
import java.util.List;

import org.apache.directory.fortress.core.util.PropUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * * Test class for driving LDAP group APIs within a console.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
class GroupMgrConsole
{
    private GroupMgr groupMgr = null;
    private static final String CLS_NM = GroupMgrConsole.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    /**
     * put your documentation comment here
     */
    public GroupMgrConsole()
    {
        try
        {
            groupMgr = GroupMgrFactory.createInstance();
        }
        catch ( org.apache.directory.fortress.core.SecurityException e)
        {
            LOG.error(" constructor caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
    }

    /**
     * Description of the Method
     */
    void add()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter group name:");
            String name = ReaderUtil.readLn();
            System.out.println("Enter description:");
            String desc = ReaderUtil.readLn();
            Group group = new Group( name, desc );

            System.out.println("Is this a group of Users - Y or N");
            String choice = ReaderUtil.readLn();
            if (choice == null || choice.equalsIgnoreCase("Y"))
            {
                group.setType( Group.Type.USER );
            }
            else
            {
                group.setType( Group.Type.ROLE );
            }

            System.out.println("Enter protocol:");
            String protocol = ReaderUtil.readLn();
            group.setProtocol( protocol );
            System.out.println("Enter member name:");
            String mname = ReaderUtil.readLn();
            group.setMember( mname );
            groupMgr.add( group );
            System.out.println("Group successfully added");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("add caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    /**
     * Description of the Method
     */
    void update()
    {
        try
        {
            ReaderUtil.clearScreen();

            System.out.println("Enter group name:");
            String name = ReaderUtil.readLn();
            Group group = new Group( name );

            System.out.println("Enter description:");
            group.setDescription( ReaderUtil.readLn() );

            System.out.println("Enter protocol:");
            String protocol = ReaderUtil.readLn();
            group.setProtocol( protocol );

            System.out.println("Enter member userId or NULL to skip");
            String userId = ReaderUtil.readLn();
            if ( StringUtils.isNotEmpty( userId ))
                group.setMember( userId );

            System.out.println("Enter property key or NULL to skip");
            String key = ReaderUtil.readLn();
            if (StringUtils.isNotEmpty( key ))
            {
                System.out.println("Enter property value");
                String value = ReaderUtil.readLn();
                group.addProperty( key, value );
            }

            groupMgr.update( group );
            System.out.println("Group successfully updated");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("update caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    /**
     * Description of the Method
     */
    void delete()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter group name:");
            String name = ReaderUtil.readLn();
            Group group = new Group();
            group.setName( name );
            groupMgr.delete( group );
            System.out.println("Group successfully deleted");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("delete caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    void addProperty()
    {
        try
        {
            Group group = new Group();
            ReaderUtil.clearScreen();
            System.out.println("Enter group name:");
            group.setName( ReaderUtil.readLn() );
            System.out.println("Enter property key:");
            String key = ReaderUtil.readLn();
            System.out.println("Enter property value:");
            String value = ReaderUtil.readLn();
            groupMgr.add( group, key, value );
            System.out.println("Add property to Group successful");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("addProperty caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    void deleteProperty()
    {
        try
        {
            Group group = new Group();
            ReaderUtil.clearScreen();
            System.out.println("Enter group name:");
            group.setName( ReaderUtil.readLn() );
            System.out.println("Enter property key:");
            String key = ReaderUtil.readLn();
            System.out.println("Enter property value:");
            String value = ReaderUtil.readLn();
            groupMgr.delete( group, key, value );
            System.out.println("Delete property from Group successful");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("deleteProperty caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    void assign()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter userId");
            String userId = ReaderUtil.readLn();
            System.out.println("Enter group name");
            String name = ReaderUtil.readLn();
            groupMgr.assign( new Group( name ), userId );
            System.out.println("Group assigned user");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("assign caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    void deassign()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter userId");
            String userId = ReaderUtil.readLn();
            System.out.println("Enter group name");
            String name = ReaderUtil.readLn();
            groupMgr.assign( new Group( name ), userId );
            System.out.println("Group deassigned user");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("deassign caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    void readGroup()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter group name:");
            Group inGroup = new Group( ReaderUtil.readLn() );

            Group outGroup = groupMgr.read( inGroup );
            System.out.println("GROUP");
            System.out.println("    name        [" + outGroup.getName() + "]");
            System.out.println("    desc        [" + outGroup.getDescription() + "]");
            System.out.println("    protocol    [" + outGroup.getProtocol() + "]");
            if ( CollectionUtils.isNotEmpty( outGroup.getMembers() ) )
            {
                int ctr = 0;
                for (String member : outGroup.getMembers() )
                {
                    System.out.println("    member[" + ctr++ + "]=" + member);
                }
            }
            if ( PropUtil.isNotEmpty( outGroup.getProperties() ) )
            {
                int ctr = 0;
                for (Enumeration e = outGroup.getProperties().propertyNames(); e.hasMoreElements();)
                {
                    String key = (String) e.nextElement();
                    String val = outGroup.getProperty(key);
                    System.out.println("    prop key[" + ctr + "]=" + key);
                    System.out.println("    prop val[" + ctr++ + "]=" + val);
                }
            }
            System.out.println("Group node successfully read");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("readGroup caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    void findGroups()
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter group search name:");
            Group inGroup = new Group( ReaderUtil.readLn() );

            List<Group> outGroups = groupMgr.find( inGroup );
            if(CollectionUtils.isNotEmpty( outGroups ))
            {
                int grpctr = 0;
                for( Group outGroup : outGroups )
                {
                    System.out.println("GROUP[" + grpctr++ + "]");
                    System.out.println("    name        [" + outGroup.getName() + "]");
                    System.out.println("    desc        [" + outGroup.getDescription() + "]");
                    System.out.println("    protocol    [" + outGroup.getProtocol() + "]");
                    if ( CollectionUtils.isNotEmpty( outGroup.getMembers() ) )
                    {
                        int memberctr = 0;
                        for (String member : outGroup.getMembers() )
                        {
                            System.out.println("    member[" + memberctr++ + "]=" + member);
                        }
                    }
                    if ( PropUtil.isNotEmpty( outGroup.getProperties() ) )
                    {
                        int propctr = 0;
                        for (Enumeration e = outGroup.getProperties().propertyNames(); e.hasMoreElements();)
                        {
                            String key = (String) e.nextElement();
                            String val = outGroup.getProperty(key);
                            System.out.println("    prop key[" + propctr + "]=" + key);
                            System.out.println("    prop val[" + propctr++ + "]=" + val);
                        }
                    }
                }
                System.out.println("Group nodes successfully searched");
            }
            else
            {
                System.out.println("No Group nodes found");
            }
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("readGroup caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }
}