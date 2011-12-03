package com.jts.fortress.cli;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

import com.jts.fortress.*;
import com.jts.fortress.arbac.UserAdminRole;
import com.jts.fortress.rbac.*;
import com.jts.fortress.util.attr.VUtil;
import com.jts.fortress.util.time.Constraint;
import org.apache.log4j.Logger;


/**
 * Main program for Fortress Command Line Interpreter..
 *
 * @author smckinn
 * @created December 1, 2011
 */
public class CommandLineInterpreter
{
    private static final String CLS_NM = CommandLineInterpreter.class.getName();
    private static final Logger log = Logger.getLogger(CLS_NM);
    private AdminMgr adminMgr;
    private ReviewMgr reviewMgr;
    private AccessMgr accessMgr;

    /* THESE ARE THE HIGH LEVEL COMMANDS: */
    private static final String ADMIN = "admin";
    private static final String REVIEW = "review";
    private static final String SYSTEM = "system";

    /* THESE ARE THE 2ND LEVEL COMMANDS: */
    private static final String ADD_USER = "addUser";
    private static final String UPDATE_USER = "updateUser";
    private static final String DELETE_USER = "deleteUser";
    private static final String READ_USER = "readUser";
    private static final String FIND_USERS = "findUsers";
    private static final String ASSIGNED_USERS = "assignedUsers";
    private static final String CREATE_SESSION = "createSession";
    private static final String AUTHENTICATE = "authenticate";
    private static final String ASSIGNED_ROLES = "assignedRoles";

    private static final String ADD_ROLE = "addRole";
    private static final String UPDATE_ROLE = "updateRole";
    private static final String DELETE_ROLE = "deleteRole";
    private static final String READ_ROLE = "readRole";
    private static final String FIND_ROLES = "findRoles";
    private static final String ASSIGN_ROLE = "assignRole";

    private static final String ADD_POBJ = "addObject";
    private static final String UPDATE_POBJ = "updateObject";
    private static final String DELETE_POBJ = "deleteObject";
    private static final String READ_POBJ = "readObject";
    private static final String FIND_POBJS = "findObjects";

    private static final String ADD_PERM = "addPerm";
    private static final String UPDATE_PERM = "updatePerm";
    private static final String DELETE_PERM = "deletePerm";
    private static final String READ_PERM = "readPerm";
    private static final String FIND_PERMS = "findPerms";

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        CommandLineInterpreter cli = new CommandLineInterpreter();
        cli.runInteractiveMode();
    }

    /**
     *
     */
    private void runInteractiveMode()
    {
        if (!constructManagers())
        {
            String error = "Startup to interactive mode failed, goodbye.";
            log.error(error);
            return;
        }

        log.info("Startup to interactive mode success...");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean done = false;
        String input;
        try
        {
            while (!done)
            {
                log.info("CLI Options include " + ADMIN + ", " + REVIEW + ", or " + SYSTEM);
                log.info("Enter one from above or 'q' to quit");
                input = br.readLine();
                if (VUtil.isNotNullOrEmpty(input))
                {
                    if ("Q".equalsIgnoreCase(input))
                    {
                        log.info("Goodbye");
                        break;
                    }

                    String[] options = parseUserInput(input);
                    processUserInput(options);
                }
                input = "";
            }
        }
        catch (Exception e)
        {
            String error = CLS_NM + "runInteractiveMode caught Exception=" + e.toString();
            e.printStackTrace();
            log.error(error);
        }
    }

    /**
     *
     */
    private static void printUsage()
    {
        log.error(
            "Usage: userAdd  -u userId -p password -o orgUnit \n" +
                "                  [{-s,--size} a_number] [{-f,--fraction} a_float] [a_nother]");
    }

    /**
     * @param commands
     * @param options
     */
    private void processCommand(Set<String> commands, Options options)
    {
        if (commands.contains(ADMIN))
        {
            processAdminCommand(commands, options);
        }
        else if (commands.contains(REVIEW))
        {
            processReviewCommand(commands, options);
        }
        else if (commands.contains(SYSTEM))
        {
            processSystemCommand(commands, options);
        }
        else
        {
            log.warn("unknown admin operation detected");
        }
    }

    /**
     * @param commands
     * @param options
     */
    private void processAdminCommand(Set<String> commands, Options options)
    {
        String command;
        try
        {
            if (commands.contains(ADD_USER))
            {
                command = ADD_USER;
                log.info(command);
                User user = options.getUser();
                adminMgr.addUser(user);
            }
            else if (commands.contains(UPDATE_USER))
            {
                command = UPDATE_USER;
                log.info(command);
                User user = options.getUser();
                adminMgr.updateUser(user);
            }
            else if (commands.contains(DELETE_USER))
            {
                command = DELETE_USER;
                log.info(command);
                User user = options.getUser();
                adminMgr.deleteUser(user);
            }
            else if (commands.contains(ADD_ROLE))
            {
                command = ADD_ROLE;
                log.info(command);
                Role role = options.getRole();
                adminMgr.addRole(role);
            }
            else if (commands.contains(UPDATE_ROLE))
            {
                command = UPDATE_ROLE;
                log.info(command);
                Role role = options.getRole();
                adminMgr.updateRole(role);
            }
            else if (commands.contains(DELETE_ROLE))
            {
                command = DELETE_ROLE;
                log.info(command);
                Role role = options.getRole();
                adminMgr.deleteRole(role);
            }
            else if (commands.contains(ASSIGN_ROLE))
            {
                command = ASSIGN_ROLE;
                log.info(command);
                Role role = options.getRole();
                String userId = options.getUserId();
                adminMgr.assignUser(new UserRole(userId, role));
            }
            else if (commands.contains(ADD_POBJ))
            {
                command = ADD_POBJ;
                log.info(command);
                PermObj permObj = options.getPermObj();
                adminMgr.addPermObj(permObj);
            }
            else if (commands.contains(UPDATE_POBJ))
            {
                command = UPDATE_POBJ;
                log.info(command);
                PermObj permObj = options.getPermObj();
                adminMgr.updatePermObj(permObj);
            }
            else if (commands.contains(DELETE_POBJ))
            {
                command = DELETE_POBJ;
                log.info(command);
                PermObj permObj = options.getPermObj();
                adminMgr.deletePermObj(permObj);
            }
            else if (commands.contains(ADD_PERM))
            {
                command = ADD_PERM;
                log.info(command);
                Permission perm = options.getPermission();
                adminMgr.addPermission(perm);
            }
            else if (commands.contains(UPDATE_PERM))
            {
                command = UPDATE_PERM;
                log.info(command);
                Permission perm = options.getPermission();
                adminMgr.updatePermission(perm);
            }
            else if (commands.contains(DELETE_PERM))
            {
                command = DELETE_PERM;
                log.info(command);
                Permission permObj = options.getPermission();
                adminMgr.deletePermission(permObj);
            }
            else
            {
                log.warn("unknown admin operation detected");
                return;
            }
            log.info("command:" + command + " was successful");
        }
        catch (com.jts.fortress.SecurityException se)
        {
            String error = CLS_NM + ".processAdminCommand caught SecurityException=" + se + ", return code=" + se.getErrorId();
            log.error(error);
        }
    }

    /**
     * @param commands
     * @param options
     */
    private void processReviewCommand(Set<String> commands, Options options)
    {
        String command;
        try
        {
            if (commands.contains(READ_USER))
            {
                command = READ_USER;
                log.info(READ_USER);
                User inUser = options.getUser();
                User outUser = reviewMgr.readUser(inUser);
                printUser(outUser);
            }
            else if (commands.contains(FIND_USERS))
            {
                command = FIND_USERS;
                log.info(command);
                User user = options.getUser();
                List<User> outUsers = reviewMgr.findUsers(user);
                if (VUtil.isNotNullOrEmpty(outUsers))
                {
                    for (User outUser : outUsers)
                    {
                        printUser(outUser);
                    }
                }
            }
            else if (commands.contains(ASSIGNED_USERS))
            {
                command = ASSIGNED_USERS;
                log.info(command);
                Role inRole = options.getRole();
                List<User> outUsers = reviewMgr.assignedUsers(inRole);
                if (VUtil.isNotNullOrEmpty(outUsers))
                {
                    for (User outUser : outUsers)
                    {
                        printUser(outUser);
                    }
                }
            }
            else if (commands.contains(READ_ROLE))
            {
                command = READ_ROLE;
                log.info(command);
                Role inRole = options.getRole();
                Role outRole = reviewMgr.readRole(inRole);
                printRole(outRole);
            }
            else if (commands.contains(FIND_ROLES))
            {
                command = FIND_ROLES;
                log.info(command);
                String inRoleNm = options.getName();
                List<Role> outRoles = reviewMgr.findRoles(inRoleNm);
                if (VUtil.isNotNullOrEmpty(outRoles))
                {
                    int ctr = 0;
                    for (Role outRole : outRoles)
                    {
                        printSeparator();
                        printRow("R", "ROLE[" + ++ctr + "]", outRole.getName());
                        printRole(outRole);
                    }
                }
            }
            else if (commands.contains(ASSIGNED_ROLES))
            {
                command = ASSIGNED_ROLES;
                log.info(command);
                String userId = options.getUserId();
                List<UserRole> uRoles = reviewMgr.assignedRoles(new User(userId));
                if (uRoles != null)
                {
                    for (UserRole ur : uRoles)
                    {
                        printTemporal("R", ur, "RBACROLE");
                        printSeparator();
                    }
                }
            }
            else if (commands.contains(READ_POBJ))
            {
                command = READ_POBJ;
                log.info(command);
                PermObj inPermObj = options.getPermObj();
                PermObj outPermObj = reviewMgr.readPermObj(inPermObj);
                printPermObj(outPermObj);
            }
            else if (commands.contains(FIND_POBJS))
            {
                command = FIND_POBJS;
                log.info(command);
                PermObj inPermObj = options.getPermObj();
                List<PermObj> outPermObjs = reviewMgr.findPermObjs(inPermObj);
                if (VUtil.isNotNullOrEmpty(outPermObjs))
                {
                    int ctr = 0;
                    for (PermObj outPermObj : outPermObjs)
                    {
                        printSeparator();
                        printRow("PO", "POBJ[" + ++ctr + "]", outPermObj.getObjectName());
                        printPermObj(outPermObj);
                    }
                }
            }
            else if (commands.contains(READ_PERM))
            {
                command = READ_PERM;
                log.info(command);
                Permission inPerm = options.getPermission();
                Permission outPerm = reviewMgr.readPermission(inPerm);
                printPermission(outPerm);
            }
            else if (commands.contains(FIND_PERMS))
            {
                command = FIND_PERMS;
                log.info(command);
                Permission inPerm = options.getPermission();
                List<Permission> outPerms = reviewMgr.findPermissions(inPerm);
                if (VUtil.isNotNullOrEmpty(outPerms))
                {
                    int ctr = 0;
                    for (Permission outPerm : outPerms)
                    {
                        printSeparator();
                        printRow("P", "PERM[" + ++ctr + "]", outPerm.getAbstractName());
                        printPermission(outPerm);
                    }
                }
            }
            else
            {
                log.warn("unknown review operation detected");
                return;
            }
            log.info("command:" + command + " was successful");
        }
        catch (com.jts.fortress.SecurityException se)
        {
            String error = CLS_NM + ".processReviewCommand caught SecurityException=" + se + ", return code=" + se.getErrorId();
            log.error(error);
        }
    }

    /**
     * @param commands
     * @param options
     */
    private void processSystemCommand(Set<String> commands, Options options)
    {
        String command;
        try
        {
            if (commands.contains(CREATE_SESSION))
            {
                command = CREATE_SESSION;
                log.info(READ_USER);
                User inUser = options.getUser();
                Session session = accessMgr.createSession(inUser, false);
                printSession(session);
            }
            else if (commands.contains(AUTHENTICATE))
            {
                command = AUTHENTICATE;
                log.info(command);
                User inUser = options.getUser();
                Session session = accessMgr.authenticate(inUser.getUserId(), inUser.getPassword());
                printSession(session);
            }
            else if (commands.contains(ASSIGNED_ROLES))
            {
                command = ASSIGNED_ROLES;
                log.info(command);
                User inUser = options.getUser();
                Session session = accessMgr.createSession(inUser, true);
                List<UserRole> uRoles = accessMgr.sessionRoles(session);
                if (uRoles != null)
                {
                    for (UserRole ur : uRoles)
                    {
                        printTemporal("R", ur, "RBACROLE");
                        printSeparator();
                    }
                }
            }
            else
            {
                log.warn("unknown system operation detected");
                return;
            }
            log.info("command:" + command + " was successful");
        }
        catch (com.jts.fortress.SecurityException se)
        {
            String error = CLS_NM + ".processSystemCommand caught SecurityException=" + se + ", return code=" + se.getErrorId();
            log.error(error);
        }
    }

    /**
     * @param parser
     * @return
     */
    private Options loadOptions(CmdLineParser parser)
    {
        Options options = new Options(parser);
        return options;
    }

    /**
     * @param input
     * @return
     */
    private String[] parseUserInput(String input)
    {
        String[] options = null;
        StringTokenizer sToken = new StringTokenizer(input, " ");
        if (sToken.countTokens() > 0)
        {
            int indx = 0;
            options = new String[sToken.countTokens()];
            boolean isPassword = false;
            while (sToken.hasMoreTokens())
            {
                String arg = sToken.nextToken();
                options[indx++] = arg;
                if (!isPassword)
                {
                    log.info("arg:" + arg);
                }
                else
                {
                    isPassword = false;
                }
                if ("-p".equalsIgnoreCase(arg))
                {
                    isPassword = true;
                }
            }
        }
        return options;
    }

    /**
     * @param args
     */
    private void processUserInput(String[] args)
    {
        CmdLineParser parser = new CmdLineParser();
        Options options = loadOptions(parser);
        Set<String> commands = null;
        try
        {
            parser.parse(args);
            commands = loadCommandSet(parser.getRemainingArgs());
        }
        catch (CmdLineParser.OptionException e)
        {
            String error = CLS_NM + "processUserInput caught OptionException=" + e.toString();
            log.error(error);
            printUsage();
            System.exit(2);
        }
        if (commands != null && commands.size() > 0)
        {
            processCommand(commands, options);
        }
    }

    /**
     * @param otherArgs
     * @return
     */
    private Set<String> loadCommandSet(String[] otherArgs)
    {
        Set<String> commands = null;
        if (VUtil.isNotNullOrEmpty(otherArgs))
        {
            commands = new TreeSet<String>();
            for (String value : otherArgs)
            {
                commands.add(value);
            }
        }
        return commands;
    }

    /**
     * @param role
     */
    private void printRole(Role role)
    {
        String type = "R";
        if (role != null)
        {
            printRow(type, "NAME", role.getName());
            printRow(type, "IID ", role.getId());
            if (VUtil.isNotNullOrEmpty(role.getParents()))
            {
                for (String parentRole : role.getParents())
                {
                    printRow(type, "PRLE", parentRole);
                }
            }
            if (VUtil.isNotNullOrEmpty(role.getChildren()))
            {
                for (String childRole : role.getChildren())
                {
                    printRow(type, "CRLE", childRole);
                }
            }
            printTemporal(type, role, "ROLE");
        }
    }

    private void printPermission(Permission perm)
    {
        String type = "P";
        if (perm != null)
        {
            printRow(type, "POBJ", perm.getObjectName());
            printRow(type, "OPER", perm.getOpName());
            printRow(type, "IID", perm.getInternalId());
            printRow(type, "TYPE", perm.getType());
        }
        if (VUtil.isNotNullOrEmpty(perm.getRoles()))
        {
            for (String roleName : perm.getRoles())
            {
                printRow("R", "ROLE", roleName);
            }
        }
        if (VUtil.isNotNullOrEmpty(perm.getUsers()))
        {
            for (String userId : perm.getUsers())
            {
                printRow("U", "USER", userId);
            }
        }
        if (VUtil.isNotNullOrEmpty(perm.getProperties()))
        {
            printSeparator();
            int ctr = 0;
            for (Enumeration e = perm.getProperties().propertyNames(); e.hasMoreElements(); )
            {
                String key = (String) e.nextElement();
                String val = perm.getProperty(key);
                log.info(type + "   KEY" + ++ctr + " [" + key + "]");
                log.info(type + "   VAL" + ctr + " [" + val + "]");
            }
        }
    }

    private void printPermObj(PermObj permObj)
    {
        String type = "O";
        if (permObj != null)
        {
            printRow(type, "NAME", permObj.getObjectName());
            printRow(type, "IID ", permObj.getInternalId());
            printRow(type, "TYPE", permObj.getType());
            printRow(type, "OU  ", permObj.getOu());
        }
        if (VUtil.isNotNullOrEmpty(permObj.getProperties()))
        {
            printSeparator();
            int ctr = 0;
            for (Enumeration e = permObj.getProperties().propertyNames(); e.hasMoreElements(); )
            {
                String key = (String) e.nextElement();
                String val = permObj.getProperty(key);
                log.info(type + "   KEY" + ++ctr + " [" + key + "]");
                log.info(type + "   VAL" + ctr + " [" + val + "]");
            }
        }
    }

    private void printSession(Session session)
    {
        String type = "S";
        printRow(type, "UID ", session.getUserId());
        printRow(type, "IID ", session.getInternalUserId());
        printRow(type, "ERR ", "" + session.getErrorId());
        printRow(type, "WARN", "" + session.getWarningId());
        printRow(type, "MSG ", session.getMsg());
        printRow(type, "EXP ", "" + session.getExpirationSeconds());
        printRow(type, "GRAC", "" + session.getGraceLogins());
        printRow(type, "AUTH", "" + session.isAuthenticated());
        printRow(type, "LAST", "" + session.getLastAccess());
        printRow(type, "SID ", session.getSessionId());
        printSeparator();
        User user = session.getUser();
        if (user != null)
        {
            printUser(user);
        }
    }

    /**
     * @param user
     */
    private void printUser(User user)
    {
        String type = "U";
        if (user != null)
        {
            printRow(type, "UID ", user.getUserId());
            printRow(type, "IID ", user.getInternalId());
            log.info(type + "   IID  [" + user.getInternalId() + "]");
            printRow(type, "CN  ", user.getCn());
            printRow(type, "DESC", user.getDescription());
            printRow(type, "OU  ", user.getOu());
            printRow(type, "SN  ", user.getSn());
            printRow(type, "BDTE", user.getBeginDate());
            printRow(type, "EDTE", user.getEndDate());
            printRow(type, "BLDT", user.getBeginLockDate());
            printRow(type, "ELDT", user.getEndLockDate());
            printRow(type, "DMSK", user.getDayMask());
            printRow(type, "TO  ", "" + user.getTimeout());
            printRow(type, "REST", "" + user.isReset());

            printTemporal(type, user, "USER");
            if (VUtil.isNotNullOrEmpty(user.getRoles()))
            {
                for (UserRole ur : user.getRoles())
                {
                    printSeparator();
                    printTemporal("R", ur, "RBACROLE");
                    if (VUtil.isNotNullOrEmpty(ur.getParents()))
                    {
                        for (String parentRole : ur.getParents())
                        {
                            printRow("R", "PRLE", parentRole);
                        }
                    }
                }
            }
            if (VUtil.isNotNullOrEmpty(user.getAdminRoles()))
            {
                for (UserAdminRole ur : user.getAdminRoles())
                {
                    printSeparator();
                    printTemporal("A", ur, "ADMINROLE");
                    printAdminRole("A", ur);
                }
            }
            if (VUtil.isNotNullOrEmpty(user.getProperties()))
            {
                printSeparator();
                int ctr = 0;
                for (Enumeration e = user.getProperties().propertyNames(); e.hasMoreElements(); )
                {
                    String key = (String) e.nextElement();
                    String val = user.getProperty(key);
                    log.info(type + "   KEY" + ++ctr + " [" + key + "]");
                    log.info(type + "   VAL" + ctr + " [" + val + "]");
                }
            }
        }
    }

    /**
     * @param constraint
     * @param label
     */
    private void printTemporal(String type, Constraint constraint, String label)
    {
        if (constraint != null)
        {
            printRow(type, "TYPE", label);
            printRow(type, "NAME", constraint.getName());
            printRow(type, "BTME", constraint.getBeginTime());
            printRow(type, "ETME", constraint.getEndTime());
            printRow(type, "BDTE", constraint.getBeginDate());
            printRow(type, "EDTE", constraint.getEndDate());
            printRow(type, "BLDT", constraint.getBeginLockDate());
            printRow(type, "ELDT", constraint.getEndLockDate());
            printRow(type, "DMSK", constraint.getDayMask());
            printRow(type, "TO  ", "" + constraint.getTimeout());
        }
    }

    /**
     * @param ur
     */
    private void printAdminRole(String type, UserAdminRole ur)
    {
        if (ur != null)
        {
            printRow(type, "BEGR", ur.getBeginRange());
            printRow(type, "ENDR", ur.getEndRange());
            if (VUtil.isNotNullOrEmpty(ur.getOsP()))
            {
                printRow(type, "POUS", "" + ur.getOsP());
            }
            if (VUtil.isNotNullOrEmpty(ur.getOsU()))
            {
                printRow(type, "UOUS", "" + ur.getOsU());
            }
        }
    }

    /**
     * @param type
     * @param name
     * @param value
     */
    private void printRow(String type, String name, String value)
    {
        log.info(type + "   " + name + " [" + value + "]");
    }

    /**
     *
     */
    private void printSeparator()
    {
        log.info("------------------------------------------");
    }

    /**
     * @return
     */
    private boolean constructManagers()
    {
        boolean success = false;
        try
        {
            adminMgr = AdminMgrFactory.createInstance();
            reviewMgr = ReviewMgrFactory.createInstance();
            accessMgr = AccessMgrFactory.createInstance();
            success = true;
        }
        catch (com.jts.fortress.SecurityException se)
        {
            String error = CLS_NM + ".constructManagers caught SecurityException=" + se;
            log.error(error);
        }
        return success;
    }
}