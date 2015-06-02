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

import org.apache.directory.fortress.core.model.AdminRole;
import org.apache.directory.fortress.core.model.OrgUnit;
import org.apache.directory.fortress.core.impl.TestUtils;
import org.apache.directory.fortress.core.model.UserAdminRole;
import org.apache.directory.fortress.core.model.Permission;
import org.apache.directory.fortress.core.model.Role;
import org.apache.directory.fortress.core.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description of the Class
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 */
public class DelegatedAdminMgrConsole
{
    private DelAdminMgr dAmgr = null;
    private AdminMgr aMgr = null;
    private static final String CLS_NM = DelegatedAdminMgrConsole.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );

    /**
     * put your documentation comment here
     */
    public DelegatedAdminMgrConsole()
    {
        try
        {
            dAmgr = DelAdminMgrFactory.createInstance(TestUtils.getContext());
            aMgr = AdminMgrFactory.createInstance(TestUtils.getContext());
        }
        catch ( org.apache.directory.fortress.core.SecurityException e)
        {
            LOG.error(" constructor caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
    }


    protected void addRole()
    {
        AdminRole re = new AdminRole();
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter role name:");
            re.setName(ReaderUtil.readLn());
            System.out.println("Enter Role's description field");
            re.setDescription(ReaderUtil.readLn());

            System.out.println("Enter OSP name (or NULL to skip):");
            String val = ReaderUtil.readLn();
            for (int i = 0; val != null && val.length() > 0; i++)
            {
                re.setOsP(val);
                System.out.println("Enter next name (or NULL if done entering OSPs):");
                val = ReaderUtil.readLn();
            }

            System.out.println("Enter OSU name (or NULL to skip):");
            val = ReaderUtil.readLn();
            for (int i = 0; val != null && val.length() > 0; i++)
            {
                re.setOsU(val);
                System.out.println("Enter next name (or NULL if done entering OSUs):");
                val = ReaderUtil.readLn();
            }

            AdminRole re2 = dAmgr.addRole(re);
            System.out.println("name [" + re2.getName() + "]");
            System.out.println("internalId [" + re2.getId() + "]");
            System.out.println("name description [" + re2.getDescription() + "]");
            System.out.println("has been updated");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("updateRole caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }



    /**
     * 
     */
    protected void updateRole()
    {
        AdminRole re = new AdminRole();
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter role name:");
            re.setName(ReaderUtil.readLn());
            System.out.println("Enter Role's description field");
            re.setDescription(ReaderUtil.readLn());

            System.out.println("Enter OSP name (or NULL to skip):");
            String val = ReaderUtil.readLn();
            for (int i = 0; val != null && val.length() > 0; i++)
            {
                re.setOsP(val);
                System.out.println("Enter next name (or NULL if done entering OSPs):");
                val = ReaderUtil.readLn();
            }

            System.out.println("Enter OSU name (or NULL to skip):");
            val = ReaderUtil.readLn();
            for (int i = 0; val != null && val.length() > 0; i++)
            {
                re.setOsU(val);
                System.out.println("Enter next name (or NULL if done entering OSUs):");
                val = ReaderUtil.readLn();
            }

            AdminRole re2 = dAmgr.updateRole(re);
            System.out.println("name [" + re2.getName() + "]");
            System.out.println("internalId [" + re2.getId() + "]");
            System.out.println("name description [" + re2.getDescription() + "]");
            System.out.println("has been updated");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("updateRole caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }



    protected void addUSO()
    {
        add(OrgUnit.Type.USER);
    }


    protected void addPSO()
    {
        add(OrgUnit.Type.PERM);
    }


    protected void assignUser()
    {
        try
        {
            ReaderUtil.clearScreen();
            UserAdminRole uAdminRole  = new UserAdminRole();
            System.out.println("Enter userId");
            uAdminRole.setUserId(ReaderUtil.readLn());
            System.out.println("Enter role name");
            uAdminRole.setName(ReaderUtil.readLn());
            dAmgr.assignUser(uAdminRole);
            System.out.println("userId [" + uAdminRole.getUserId() + "] name [" + uAdminRole.getName() + "]");
            System.out.println("has been assigned");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("assignUser caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    protected void deassignUser()
    {
        try
        {
            ReaderUtil.clearScreen();
            UserAdminRole uAdminRole = new UserAdminRole();
            System.out.println("Enter userId");
            String userId = ReaderUtil.readLn();
            uAdminRole.setUserId(userId);
            System.out.println("Enter role name");
            String roleNm = ReaderUtil.readLn();
            uAdminRole.setName(roleNm);
            dAmgr.deassignUser(uAdminRole);
            System.out.println("userId [" + userId + "] name [" + roleNm + "]");
            System.out.println("has been deassigned");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("deassignUser caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    private void add(OrgUnit.Type type)
    {
        OrgUnit ou = new OrgUnit();

        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter orgunit name:");
            ou.setName(ReaderUtil.readLn());
            System.out.println("Enter description field");
            ou.setDescription(ReaderUtil.readLn());
            ou.setType(type);
            OrgUnit ou2 = dAmgr.add(ou);
            System.out.println("name [" + ou2.getName() + "]");
            System.out.println("internalId [" + ou2.getId() + "]");
            System.out.println("name description [" + ou2.getDescription() + "]");
            System.out.println("has been added");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("add caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    protected void updateUSO()
    {
        update(OrgUnit.Type.USER);
    }


    protected void updatePSO()
    {
        update(OrgUnit.Type.PERM);
    }


    private void update(OrgUnit.Type type)
    {
        OrgUnit ou = new OrgUnit();

        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter orgunit name:");
            ou.setName(ReaderUtil.readLn());
            System.out.println("Enter description field");
            ou.setDescription(ReaderUtil.readLn());
            ou.setType(type);
            OrgUnit ou2 = dAmgr.update(ou);
            System.out.println("name [" + ou2.getName() + "]");
            System.out.println("internalId [" + ou2.getId() + "]");
            System.out.println("name description [" + ou2.getDescription() + "]");
            System.out.println("has been updated");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("update caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    protected void deleteUSO()
    {
        delete(OrgUnit.Type.USER);
    }


    protected void deletePSO()
    {
        delete(OrgUnit.Type.PERM);
    }


    private void delete(OrgUnit.Type type)
    {
        OrgUnit ou = new OrgUnit();

        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter orgunit name:");
            ou.setName(ReaderUtil.readLn());
            ou.setType(type);
            dAmgr.delete(ou);
            System.out.println("name [" + ou.getName() + "]");
            System.out.println("has been deleted");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("delete caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    protected void addInheritanceUSO()
    {
        addInheritance(OrgUnit.Type.USER);
    }


    protected void addInheritancePSO()
    {
        addInheritance(OrgUnit.Type.PERM);
    }

    /**
     * 
     */
    private void addInheritance(OrgUnit.Type type)
    {
        try
        {
            OrgUnit cou = new OrgUnit();
            OrgUnit pou = new OrgUnit();
            cou.setType(type);
            pou.setType(type);
            ReaderUtil.clearScreen();
            System.out.println("Enter child orgunit name:");
            cou.setName(ReaderUtil.readLn());
            System.out.println("Enter parent orgunit name:");
            pou.setName(ReaderUtil.readLn());
            dAmgr.addInheritance(pou, cou);
            System.out.println("child orgunit [" + cou.getName() + "]");
            System.out.println("parent orgunit [" + pou.getName() + "]");
            System.out.println("inheritance relationship has been added");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("addInheritance caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    protected void removeInheritanceUSO()
    {
        removeInheritance(OrgUnit.Type.USER);
    }


    protected void removeInheritancePSO()
    {
        removeInheritance(OrgUnit.Type.PERM);
    }

    /**
     *
     */
    private void removeInheritance(OrgUnit.Type type)
    {
        try
        {
            OrgUnit cou = new OrgUnit();
            OrgUnit pou = new OrgUnit();
            cou.setType(type);
            pou.setType(type);
            ReaderUtil.clearScreen();
            System.out.println("Enter child orgunit name:");
            cou.setName(ReaderUtil.readLn());
            System.out.println("Enter parent orgunit name:");
            pou.setName(ReaderUtil.readLn());
            dAmgr.deleteInheritance(pou, cou);
            System.out.println("child orgunit [" + cou.getName() + "]");
            System.out.println("parent orgunit [" + pou.getName() + "]");
            System.out.println("inheritance relationship has been removed");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("removeInheritance caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    protected void addAscendantUSO()
    {
        addAscendant(OrgUnit.Type.USER);
    }


    protected void addAscendantPSO()
    {
        addAscendant(OrgUnit.Type.PERM);
    }

    /**
     * 
     */
    private void addAscendant(OrgUnit.Type type)
    {
        try
        {
            OrgUnit cou = new OrgUnit();
            OrgUnit pou = new OrgUnit();
            cou.setType(type);
            pou.setType(type);
            ReaderUtil.clearScreen();
            System.out.println("Enter child orgunit name:");
            cou.setName(ReaderUtil.readLn());
            System.out.println("Enter parent orgunit name to add to repo:");
            pou.setName(ReaderUtil.readLn());
            System.out.println("Enter parent orgunit description:");
            pou.setDescription(ReaderUtil.readLn());

            dAmgr.addAscendant(cou, pou);
            System.out.println("child orgunit [" + cou.getName() + "]");
            System.out.println("parent orgunit [" + pou.getName() + "]");
            System.out.println("parent orgunit and inheritance relationship has been added");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("addAscendant caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

    protected void addDescendantUSO()
    {
        addDescendant(OrgUnit.Type.USER);
    }


    protected void addDescendantPSO()
    {
        addDescendant(OrgUnit.Type.PERM);
    }

    /**
     *
     */
    private void addDescendant(OrgUnit.Type type)
    {
        try
        {
            OrgUnit cou = new OrgUnit();
            OrgUnit pou = new OrgUnit();
            cou.setType(type);
            pou.setType(type);
            ReaderUtil.clearScreen();
            System.out.println("Enter child orgunit name to add to repo:");
            cou.setName(ReaderUtil.readLn());
            System.out.println("Enter child orgunit description:");
            cou.setDescription(ReaderUtil.readLn());
            System.out.println("Enter parent orgunit name:");
            pou.setName(ReaderUtil.readLn());
            dAmgr.addDescendant(pou, cou);
            System.out.println("child orgunit [" + cou.getName() + "]");
            System.out.println("parent orgunit [" + pou.getName() + "]");
            System.out.println("child orgunit and inheritance relationship has been added");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("addDescendant caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }




    protected void grantPermission(boolean isRole)
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter perm object");
            String object = ReaderUtil.readLn();
            System.out.println("Enter perm operation");
            String operation = ReaderUtil.readLn();
            Permission pOp = new Permission(object, operation);
            pOp.setAdmin(true);

            String name;
            if (isRole)
            {
                System.out.println("Enter role name");
                name = ReaderUtil.readLn();
                aMgr.grantPermission(pOp, new Role(name));

            }
            else
            {
                System.out.println("Enter userId");
                name = ReaderUtil.readLn();
                aMgr.grantPermission(pOp, new User(name));
            }

            System.out.println("perm object [" + object + "] operation [" + operation + "] has been granted to [" + name + "]");
            System.out.println("has been granted");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("grantPermission caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    protected void revokePermission(boolean isRole)
    {
        try
        {
            ReaderUtil.clearScreen();
            System.out.println("Enter perm object");
            String object = ReaderUtil.readLn();
            System.out.println("Enter perm operation");
            String operation = ReaderUtil.readLn();
            Permission pOp = new Permission(object, operation);
            pOp.setAdmin(true);

            String name;
            if (isRole)
            {
                System.out.println("Enter role name");
                name = ReaderUtil.readLn();
                aMgr.revokePermission(pOp, new Role(name));

            }
            else
            {
                System.out.println("Enter userId");
                name = ReaderUtil.readLn();
                aMgr.revokePermission(pOp, new User(name));
            }

            System.out.println("perm object [" + object + "] operation [" + operation + "] has been granted to [" + name + "]");
            System.out.println("has been revoked.");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("revokePermission caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }




    /**
     *
     */
    void addRoleInheritance()
    {
        try
        {
            AdminRole cre = new AdminRole();
            AdminRole pre = new AdminRole();
            ReaderUtil.clearScreen();
            System.out.println("Enter child adminRole name:");
            cre.setName(ReaderUtil.readLn());
            System.out.println("Enter parent adminRole name:");
            pre.setName(ReaderUtil.readLn());

            dAmgr.addInheritance(pre, cre);
            System.out.println("child adminRole [" + cre.getName() + "]");
            System.out.println("parent adminRole [" + pre.getName() + "]");
            System.out.println("inheritance relationship has been added");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("addRoleInheritance caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     *
     */
    void removeRoleInheritance()
    {
        try
        {
            AdminRole cre = new AdminRole();
            AdminRole pre = new AdminRole();
            ReaderUtil.clearScreen();
            System.out.println("Enter child adminRole name:");
            cre.setName(ReaderUtil.readLn());
            System.out.println("Enter parent adminRole name:");
            pre.setName(ReaderUtil.readLn());
            dAmgr.deleteInheritance(pre, cre);
            System.out.println("child adminRole [" + cre.getName() + "]");
            System.out.println("parent adminRole [" + pre.getName() + "]");
            System.out.println("inheritance relationship has been removed");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("removeRoleInheritance caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     *
     */
    void addRoleAscendant()
    {
        try
        {
            AdminRole cre = new AdminRole();
            AdminRole pre = new AdminRole();
            ReaderUtil.clearScreen();
            System.out.println("Enter child adminRole name:");
            cre.setName(ReaderUtil.readLn());
            System.out.println("Enter parent adminRole name to add to repo:");
            pre.setName(ReaderUtil.readLn());
            System.out.println("Enter parent adminRole description:");
            pre.setDescription(ReaderUtil.readLn());

            dAmgr.addAscendant(cre, pre);
            System.out.println("child adminRole [" + cre.getName() + "]");
            System.out.println("parent adminRole [" + pre.getName() + "]");
            System.out.println("parent adminRole and inheritance relationship has been added");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("addRoleAscendant caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }


    /**
     *
     */
    void addRoleDescendant()
    {
        try
        {
            AdminRole cre = new AdminRole();
            AdminRole pre = new AdminRole();
            ReaderUtil.clearScreen();
            System.out.println("Enter child adminRole name to add to repo:");
            cre.setName(ReaderUtil.readLn());
            System.out.println("Enter child adminRole description:");
            cre.setDescription(ReaderUtil.readLn());
            System.out.println("Enter parent adminRole name:");
            pre.setName(ReaderUtil.readLn());
            dAmgr.addDescendant(pre, cre);
            System.out.println("child adminRole [" + cre.getName() + "]");
            System.out.println("parent adminRole [" + pre.getName() + "]");
            System.out.println("child adminRole and inheritance relationship has been added");
            System.out.println("ENTER to continue");
        }
        catch (SecurityException e)
        {
            LOG.error("addRoleDescendant caught SecurityException rc=" + e.getErrorId() + ", msg=" + e.getMessage(), e);
        }
        ReaderUtil.readChar();
    }

}


