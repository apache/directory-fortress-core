/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

/*
 *  This module is used for testing purposes.
 */
package com.jts.fortress;

import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *  Description of the Class
 *
 * @author     Shawn McKinney
 */
public class ProcessMenuCommand
{
	final private Logger log = Logger.getLogger(ProcessMenuCommand.class.getName());
	final private AdminMgrConsole adminConsole = new AdminMgrConsole();
	final private ReviewMgrConsole reviewConsole = new ReviewMgrConsole();
	final private com.jts.fortress.AccessMgrConsole accessConsole = new com.jts.fortress.AccessMgrConsole();
	final private com.jts.fortress.PolicyMgrConsole policyConsole = new com.jts.fortress.PolicyMgrConsole();
	final private com.jts.fortress.AuditMgrConsole auditConsole = new com.jts.fortress.AuditMgrConsole();
	final private com.jts.fortress.DelegatedAdminMgrConsole delAdminConsole = new com.jts.fortress.DelegatedAdminMgrConsole();
	final private com.jts.fortress.DelegatedReviewMgrConsole delReviewConsole = new com.jts.fortress.DelegatedReviewMgrConsole();
	final private com.jts.fortress.DelegatedAccessMgrConsole delAccessConsole = new com.jts.fortress.DelegatedAccessMgrConsole();
	final private com.jts.fortress.ConfigMgrConsole cfgConsole = new com.jts.fortress.ConfigMgrConsole();
	final private EncryptMgrConsole encryptConsole = new EncryptMgrConsole();

    /**
     *
     */
	private void showMainMenu()
	{
		ReaderUtil.clearScreen();
		System.out.println("CHOOSE FUNCTION");
		System.out.println("1. ADMIN MANAGER FUNCTIONS");
		System.out.println("2. REVIEW MANAGER FUNCTIONS");
		System.out.println("3. ACCESS MANAGER FUNCTIONS");
		System.out.println("4. DELEGATED ADMIN MANAGER FUNCTIONS");
		System.out.println("5. DELEGATED REVIEW MANAGER FUNCTIONS");
		System.out.println("6. DELEGATED ACCESS MANAGER FUNCTIONS");
		System.out.println("7. PASSWORD POLICY MANAGER FUNCTIONS");
		System.out.println("8. AUDIT MANAGER FUNCTIONS");
		System.out.println("9. CONFIG MANAGER FUNCTIONS");
		System.out.println("A. ENCRYPTION MANAGER FUNCTIONS");
		System.out.println("Enter q or Q to quit");
	}



    /**
     *
     */
	public void processRbacControl()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean done = false;
		int input = 0;

		try
		{
			while (!done)
			{
				showMainMenu();
				input = br.read();
				switch (input)
				{
					case '1':
						processAdminFunction();
						break;
					case '2':
						processReviewFunction();
						break;
					case '3':
						processAccessFunction();
						break;
                    case '4':
                        processDelegatedAdminFunction();
                        break;
                    case '5':
                        processDelegatedReviewFunction();
                        break;
                    case '6':
                        processDelegatedAccessFunction();
                        break;
					case '7':
						processPasswordPolicyFunction();
						break;
					case '8':
						processAuditManagerFunction();
						break;
                    case '9':
                        processConfigManagerFunction();
                        break;
                    case 'a':
                    case 'A':
                        processEncryptManagerFunction();
						break;
					case 'q':
					case 'Q':
						done = true;
						break;
					default:
						break;
				}
				input = 0;
			}
		}
		catch (Exception e)
		{
			log.fatal("Exception caught in processRbacControl = " + e);
		}
	}


    public void processEncryptManagerFunction()
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean done = false;
        int input;

        try
        {
            while (!done)
            {
                showEncryptFunctionMenu();
                input = br.read();
                switch (input)
                {
                    case '1':
                        encryptConsole.encrypt();
                        break;
                    case '2':
                        encryptConsole.decrypt();
                        break;
                    case 'q':
                    case 'Q':
                        done = true;
                        break;
                    default:
                        break;
                }
                input = 0;
            }
        }
        catch (Exception e)
        {
            log.fatal("Exception caught in processEncryptManagerFunction = " + e);
        }
    }

    private void showEncryptFunctionMenu()
    {
        ReaderUtil.clearScreen();
        System.out.println("CHOOSE ENCRYPTION MANAGER FUNCTION");
        System.out.println("1.  Encrypt text value");
        System.out.println("2.  Decrypt text value");
    }

    public void processConfigManagerFunction()
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean done = false;
        int input;

        try
        {
            while (!done)
            {
                showConfigFunctionMenu();
                input = br.read();
                switch (input)
                {
                    case '1':
                        cfgConsole.addProp();
                        break;
                    case '2':
                        cfgConsole.updateProp();
                        break;
                    case '3':
                        cfgConsole.readProps();
                        break;
                    case '4':
                        cfgConsole.deleteProps();
                        break;
                    case 'q':
                    case 'Q':
                        done = true;
                        break;
                    default:
                        break;
                }
                input = 0;
            }
        }
        catch (Exception e)
        {
            log.fatal("Exception caught in processConfigManagerFunction = " + e);
        }
    }

    /**
     *
     */
    private void showConfigFunctionMenu()
    {
        ReaderUtil.clearScreen();
        System.out.println("CHOOSE CONFIG MANAGER FUNCTION");
        System.out.println("1.  Add Config Param");
        System.out.println("2.  Update Config Param");
        System.out.println("3.  Read Config Params");
        System.out.println("4.  Delete Config Params");
    }

    /**
     * 
     */
    public void processAuditManagerFunction()
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean done = false;
        int input;

        try
        {
            while (!done)
            {
                showAuditFunctionMenu();
                input = br.read();
                switch (input)
                {
                    case '1':
                        auditConsole.findBinds();
                        break;
                    case '2':
                        auditConsole.getBindReport();
                        break;
                    case '3':
                        auditConsole.findAuthZs();
                        break;
                    case '4':
                        auditConsole.getAuthZs();
                        break;
                    case '5':
                        auditConsole.getAuthReport();
                        break;
                    case '6':
                        auditConsole.getModReport();
                        break;                                        
                    case '7':
                        auditConsole.getAdminModReport();
                        break;
                    case '8':
                        auditConsole.getAuthNInvalidReport();
                        break;
                    case 'q':
                    case 'Q':
                        done = true;
                        break;
                    default:
                        break;
                }
                input = 0;
            }
        }
        catch (Exception e)
        {
            log.fatal("Exception caught in processAuditManagerFunction = " + e);
        }
    }

    /**
     *
     */
    private void showAuditFunctionMenu()
    {
        ReaderUtil.clearScreen();
        System.out.println("CHOOSE AUDIT MANAGER FUNCTION");
        System.out.println("1.  Find Audit Binds Raw Data");
        System.out.println("2.  Get Audit AuthN Formatted Data");
        System.out.println("3.  Find Audit AuthZs");
        System.out.println("4.  Get Audit AuthZs Raw Data");
        System.out.println("5.  Get Audit AuthZs Formatted Data");
        System.out.println("6.  Get Audit Mods Raw Data");
        System.out.println("7.  Get Admin Mods Raw Data");
        System.out.println("8.  Show AuthN Invalids");
        System.out.println("Enter q or Q to return to previous menu");
    }

    /**
     *
     */
    private void showAdminCommandMenu()
    {
        ReaderUtil.clearScreen();
        System.out.println("CHOOSE ADMIN MANAGER FUNCTION");
        System.out.println("1.  Add User");
        System.out.println("2.  Update User");
        System.out.println("3.  Delete User");
        System.out.println("4.  Unlock User");
        System.out.println("5.  Lock User Account");
        System.out.println("6.  Reset User Password");
        System.out.println("7.  Change User Password");
        System.out.println("8.  Add Perm Object");
        System.out.println("9.  Add Perm Operation");
        //System.out.println(".  Update  Perm");
        System.out.println("0.  Delete  Perm");
        System.out.println("A.  Add Role");
        System.out.println("B.  Update Role");
        System.out.println("C.  Delete Role");
        System.out.println("D.  Assign User to Role");
        System.out.println("E.  Deassign User from Role");
        System.out.println("F.  Grant Perm to Role");
        System.out.println("G.  Revoke Perm from Role");
        System.out.println("H.  Grant Perm to User");
        System.out.println("I.  Revoke Perm from User");
        System.out.println("J.  Add Role Inheritance");
        System.out.println("K.  Remove Role Inheritance");
        System.out.println("L.  Add Role Ascendant");
        System.out.println("M.  Add Role Descendant");
        System.out.println("N.  Add SSD Data Set");
        System.out.println("O.  Add DSD Data Set");
        System.out.println("X.  Test Annotation");
        System.out.println("Y.  Add Example");
        System.out.println("Z.  Test Config");
        System.out.println("Enter q or Q to return to previous menu");
    }


    /**
     *
     */
	public void processAdminFunction()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean done = false;
		int input;

		try
		{
			while (!done)
			{
				showAdminCommandMenu();
				input = br.read();
				switch (input)
				{
					case '1':
						adminConsole.addUser();
						break;
					case '2':
						adminConsole.updateUser();
						break;
					case '3':
						adminConsole.deleteUser();
						break;
                    case '4':
                        adminConsole.unlockUser();
                        break;
                    case '5':
                        adminConsole.lockUser();
                        break;
                    case '6':
                        adminConsole.resetPassword();
                        break;
                    case '7':
                        adminConsole.changePassword();
                        break;
                    case '8':
                        adminConsole.addPermObject();
                        break;
                    case '9':
                        adminConsole.addPermOperation();
                        break;
					//case '5':
						//adminConsole.updatePermObj();
					//	break;
					case '0':
						adminConsole.deletePermission();
						break;
                    case 'a':
                    case 'A':
						adminConsole.addRole();
						break;
                    case 'b':
                    case 'B':
                        adminConsole.updateRole();
                        break;
                    case 'c':
                    case 'C':
                        adminConsole.deleteRole();
                        break;
                    case 'd':
                    case 'D':
                        adminConsole.assignUser();
                        break;
                    case 'e':
                    case 'E':
                        adminConsole.deassignUser();
                        break;
                    case 'f':
                    case 'F':
                        adminConsole.grantPermission(true);
                        break;
                    case 'g':
                    case 'G':
                        adminConsole.revokePermission(true);
                        break;
                    case 'h':
                    case 'H':
                        adminConsole.grantPermission(false);
                        break;
                    case 'i':
                    case 'I':
                        adminConsole.revokePermission(false);
                        break;
                    case 'j':
                    case 'J':
                        adminConsole.addRoleInheritance();
                        break;
                    case 'k':
                    case 'K':
                        adminConsole.removeRoleInheritance();
                        break;
                    case 'l':
                    case 'L':
                        adminConsole.addRoleAscendant();
                        break;
                    case 'm':
                    case 'M':
                        adminConsole.addRoleDescendant();
                        break;
                    case 'n':
                    case 'N':
                        adminConsole.addSsd();
                        break;
                    case 'o':
                    case 'O':
                        adminConsole.addDsd();
                        break;
                    case 'x':
                    case 'X':
						adminConsole.addAnnotation();
						break;
                    case 'y':
                    case 'Y':
						adminConsole.addExample();
						break;
                    case 'z':
                    case 'Z':
                        adminConsole.testConfig();
						break;
					case 'q':
					case 'Q':
						done = true;
						break;
					default:
						break;
				}
				input = 0;
			}
		}
		catch (Exception e)
		{
			log.fatal("Exception caught in processAdminFunction = " + e);
		}
	}



    /**
     *
     */
    private void showReviewFunctionMenu()
    {
        ReaderUtil.clearScreen();
        System.out.println("CHOOSE REVIEW MANAGER FUNCTION");
        System.out.println("0. Search Users");
        System.out.println("1. Search Users by OU");
        System.out.println("2. Get User by IID");
        System.out.println("3. Read User");
        System.out.println("4. Search Permissions");
        System.out.println("5. Read Permissions");
        System.out.println("6. Read Role");
        System.out.println("7. Search Roles");
        System.out.println("8. Perm Roles");
        System.out.println("9. Perm Users");
        System.out.println("A. Authorized Users");
        System.out.println("B. Role Permissions");
        System.out.println("C. Get Assigned Roles");
        System.out.println("Enter q or Q to return to previous menu");
    }


    /**
     *
     */
	public void processReviewFunction()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean done = false;
		int input;

		try
		{
			while (!done)
			{
				showReviewFunctionMenu();
				input = br.read();
				switch (input)
				{
					case '0':
						reviewConsole.findUsers();
						break;
					case '1':
						reviewConsole.findUsersByOrg();
						break;
                    case '2':
                        reviewConsole.getUser();
                        break;
					case '3':
						reviewConsole.readUser();
						break;
					case '4':
						reviewConsole.searchPermissions();
						break;
					case '5':
						reviewConsole.readPermission();
						break;
                    case '6':
                        reviewConsole.readRole();
                        break;
                    case '7':
                        reviewConsole.findRoles();
                        break;
                    case '8':
                        reviewConsole.permissionRoles();
                        break;
                    case '9':
                        reviewConsole.permissionUsers();
                        break;
                    case 'a':
                    case 'A':
                        reviewConsole.authorizedUsers();
                        break;
                    case 'b':
                    case 'B':
                        reviewConsole.rolePermissions();
                        break;
                    case 'c':
                    case 'C':
                        reviewConsole.assignedRoles();
                        break;
					case 'q':
					case 'Q':
						done = true;
						break;
					default:
						break;
				}
				input = 0;
			}
		}
		catch (Exception e)
		{
			log.fatal("Exception caught in processReviewFunction = " + e);
		}
	}


    /**
     *
     */
    private void showAccessFunctionMenu()
    {
        ReaderUtil.clearScreen();
        System.out.println("CHOOSE ACCESS MANAGER FUNCTION");
        System.out.println("1. Authenticate");
        System.out.println("2. Create Session");
        System.out.println("3. Create Session Trusted");
        System.out.println("4. Create Session with Props");
        System.out.println("5. Check Access - RBAC");
        System.out.println("6. Session Roles");
        System.out.println("7. Add Active Role to Session");
        System.out.println("8. Drop Active Role from Session");
        System.out.println("9. Show User Data in Session");
        System.out.println("0. Show UserId in Session");
        System.out.println("A. Session Permissions");
        System.out.println("Enter q or Q to return to previous menu");
    }


    /**
     *
     */
	public void processAccessFunction()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean done = false;
		int input;

		try
		{
			while (!done)
			{
				showAccessFunctionMenu();
				input = br.read();
				switch (input)
				{
					case '1':
						accessConsole.authenticate();
						//accessConsole.createSession();
						break;
					case '2':
						accessConsole.createSession();
						break;
                    case '3':
                        accessConsole.createSessionTrusted();
                        break;
                    case '4':
                        accessConsole.createSessionProps();
                        break;
					case '5':
						accessConsole.checkAccess();
						break;
                    case '6':
                        accessConsole.sessionRoles();
                        break;
					case '7':
						accessConsole.addActiveRole();
						break;
					case '8':
						accessConsole.dropActiveRole();
						break;
					case '9':
						accessConsole.getUser();
						break;
					case '0':
						accessConsole.getUserId();
						break;
                    case 'a':
                    case 'A':                        
                        accessConsole.sessionPermissions();
                        break;
					case 'q':
					case 'Q':
						done = true;
						break;
					default:
						break;
				}
				input = 0;
			}
		}
		catch (Exception e)
		{
			log.fatal("Exception caught in processAccessFunction = " + e);
		}
	}


    /**
     *
     */
	public void processPasswordPolicyFunction()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean done = false;
		int input;

		try
		{
			while (!done)
			{
				showPasswordPolicyFunctionMenu();
				input = br.read();
				switch (input)
				{
					case '1':
						policyConsole.add();
						break;
					case '2':
						//policyConsole.updatePolicy();
						break;
					case '3':
						//policyConsole.deletePolicy();
						break;
					case '4':
						//policyConsole.readPolicy();
						break;
					case '5':
						//policyConsole.findPolicies();
						break;
					case '6':
						//policyConsole.updateUserPolicy();
						break;
					case '7':
						//policyConsole.deleteUserPasswordPolicy();
						break;
					case '8':
						//policyConsole.readOperationalAttributes();
						break;
					case '9':
						//policyConsole.resetUserPassword();
						break;
					case '0':
						//adminConsole.unlockAccount();
						break;
					case 'a':
					case 'A':
						//policyConsole.lockUserAccount();
						break;
					case 'b':
					case 'B':
						//policyConsole.clearResetFlag();
						break;
					case 'c':
					case 'C':
						//policyConsole.checkPasswordPolicy();
						break;
					case 'd':
					case 'D':
						//policyConsole.getLockedUsers();
						break;
					case 'e':
					case 'E':
						//policyConsole.isLockedUser();
						break;
					case 'q':
					case 'Q':
						done = true;
						break;
					default:
						break;
				}
				input = 0;
			}
		}
		catch (Exception e)
		{
			log.fatal("Exception caught in processPasswordPolicyFunction = " + e);
		}
	}


    /**
     *
     */
	private void showPasswordPolicyFunctionMenu()
	{
		ReaderUtil.clearScreen();
		System.out.println("CHOOSE PASSWORD POLICY FUNCTION");
		System.out.println("1. Create Password Policy");
		System.out.println("Enter q or Q to return to previous menu");
	}


    private void showDelegatedAdminCommandMenu()
    {
        ReaderUtil.clearScreen();
        System.out.println("CHOOSE DELEGATED ADMIN MANAGER FUNCTION");
        System.out.println("1.  Add ORG USO");
        System.out.println("2.  Add ORG PSO");
        System.out.println("3.  Update ORG USO");
        System.out.println("4.  Update ORG PSO");
        System.out.println("5.  Delete ORG USO");
        System.out.println("6.  Delete ORG PSO");
        System.out.println("7.  Add ORG Inheritance USO");
        System.out.println("8.  Add ORG Inheritance PSO");
        System.out.println("9.  Remove ORG Inheritance USO");
        System.out.println("0.  Remove ORG Inheritance PSO");
        System.out.println("A.  Add ORG Ascendant USO");
        System.out.println("B.  Add ORG Ascendant PSO");
        System.out.println("C.  Add ORG Descendant USO");
        System.out.println("D.  Add ORG Descendant PSO");        
        System.out.println("E.  Assign Admin Role");
        System.out.println("F.  Deassign Admin Role");
        System.out.println("G.  Add Admin Role");
        System.out.println("H.  Update Admin Role");
        System.out.println("I.  Grant Admin Perm to Admin Role");
        System.out.println("J.  Revoke Admin Perm from Admin Role");
        System.out.println("Enter q or Q to return to previous menu");
    }


    /**
     *
     */
	public void processDelegatedAdminFunction()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean done = false;
		int input;

		try
		{
			while (!done)
			{
				showDelegatedAdminCommandMenu();
				input = br.read();
				switch (input)
				{
                    case '1':
						delAdminConsole.addUSO();
						break;
                    case '2':
						delAdminConsole.addPSO();
						break;
                    case '3':
                        delAdminConsole.updateUSO();
                        break;
                    case '4':
                        delAdminConsole.updatePSO();
                        break;
                    case '5':
                        delAdminConsole.deleteUSO();
                        break;
                    case '6':
                        delAdminConsole.deletePSO();
                        break;
                    case '7':
                        delAdminConsole.addInheritanceUSO();
                        break;
                    case '8':
                        delAdminConsole.addInheritancePSO();
                        break;
                    case '9':
                        delAdminConsole.removeInheritanceUSO();
                        break;
                    case '0':
                        delAdminConsole.removeInheritancePSO();
                        break;
                    case 'a':
                    case 'A':
                        delAdminConsole.addAscendantUSO();
                        break;
                    case 'b':
                    case 'B':
                        delAdminConsole.addAscendantPSO();
                        break;
                    case 'c':
                    case 'C':
                        delAdminConsole.addDescendantUSO();
                        break;
                    case 'd':
                    case 'D':
                        delAdminConsole.addDescendantPSO();
                        break;
                    case 'e':
                    case 'E':
                        delAdminConsole.assignUser();
                        break;
                    case 'f':
                    case 'F':
                        delAdminConsole.deassignUser();
                        break;
                    case 'g':
                    case 'G':
                        delAdminConsole.addRole();
                        break;
                    case 'h':
                    case 'H':
                        delAdminConsole.updateRole();
                        break;
                    case 'i':
                    case 'I':
						delAdminConsole.grantPermission(true);
						break;
                    case 'j':
                    case 'J':
						delAdminConsole.revokePermission(true);
						break;
					case 'q':
					case 'Q':
						done = true;
						break;
					default:
						break;
				}
				input = 0;
			}
		}
		catch (Exception e)
		{
			log.fatal("Exception caught in processAdminFunction = " + e);
		}
	}


    /**
     *
     */
    private void showDelegatedReviewFunctionMenu()
    {
        ReaderUtil.clearScreen();
        System.out.println("CHOOSE DELEGATED REVIEW MANAGER FUNCTION");
        System.out.println("1. Read Role");
        System.out.println("2. Search Roles");
        System.out.println("3. Assigned Roles");
        System.out.println("4. Assigned Users");
        System.out.println("Enter q or Q to return to previous menu");
    }


    /**
     *
     */
	public void processDelegatedReviewFunction()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean done = false;
		int input;

		try
		{
			while (!done)
			{
				showDelegatedReviewFunctionMenu();
				input = br.read();
				switch (input)
				{
                    case '1':
                        delReviewConsole.readRole();
                        break;
                    case '2':
                        delReviewConsole.findRoles();
                        break;
                    case '3':
                        delReviewConsole.assignedRoles();
                        break;
                    case '4':
                        delReviewConsole.assignedUsers();
                        break;
                    case 'q':
                    case 'Q':
                        done = true;
                        break;
					default:
						break;
				}
				input = 0;
			}
		}
		catch (Exception e)
		{
			log.fatal("Exception caught in processReviewFunction = " + e);
		}
	}


    /**
     *
     */
    private void showDelegatedAccessFunctionMenu()
    {
        ReaderUtil.clearScreen();
        System.out.println("CHOOSE ACCESS MANAGER FUNCTION");
        System.out.println("1. Create Session");
        System.out.println("2. Create Session Trusted");
        System.out.println("3. Check Access");
        System.out.println("4. Can Assign");
        System.out.println("5. Can Deassign");
        System.out.println("6. Can Grant");
        System.out.println("7. Can Revoke");
        System.out.println("Enter q or Q to return to previous menu");
    }


    /**
     *
     */
	public void processDelegatedAccessFunction()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean done = false;
		int input;

		try
		{
			while (!done)
			{
				showDelegatedAccessFunctionMenu();
				input = br.read();
				switch (input)
				{
					case '1':
						delAccessConsole.createSession();
						break;
                    case '2':
                        delAccessConsole.createSessionTrusted();
                        break;
                    case '3':
                        delAccessConsole.checkAccess();
                        break;
                    case '4':
                        delAccessConsole.canAssign();
                        break;
                    case '5':
                        delAccessConsole.canDeassign();
                        break;
                    case '6':
                        delAccessConsole.canGrant();
                        break;
                    case '7':
                        delAccessConsole.canRevoke();
                        break;
					case 'q':
					case 'Q':
						done = true;
						break;
					default:
						break;
				}
				input = 0;
			}
		}
		catch (Exception e)
		{
			log.fatal("Exception caught in processDelegatedAccessFunction = " + e);
		}
	}
}


