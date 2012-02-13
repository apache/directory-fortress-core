/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */
package com.jts.fortress.rest;

/**
 * Contains global Ids used for calling En Masse REST server.
 *
 * @author smckinn
 * @created February 10, 2012
 */
public class Ids
{
    static enum Services
    {
        rbacAuthN,
        rbacCreate,
        rbacAuthZ,
        rbacPerms,
        rbacRoles,
        rbacAuthzRoles,
        rbacAdd,
        rbacDrop,
        rbacUserId,
        rbacUser,

        userRead,
        userSearch,
        userAdd,
        userUpdate,
        userDelete,
        userDisable,
        userPerms,
        userChange,
        userLock,
        userUnlock,
        userReset,
        userGrant,
        userRevoke,
        userAuthzed,
        userAsigned,
        userAsignedAdmin,

        roleRead,
        roleSearch,
        roleAdd,
        roleUpdate,
        roleDelete,
        roleAsgn,
        roleDeasgn,
        roleDescendant,
        roleAscendent,
        roleAddinherit,
        roleDelinherit,
        roleGrant,
        roleRevoke,
        rolePerms,
        roleAuthzed,
        roleAsigned,
        roleAsigned2,

        objRead,
        objSearch,
        objAdd,
        objUpdate,
        objDelete,

        permRead,
        permSearch,
        permAdd,
        permUpdate,
        permDelete,
        permUsers,
        permRoles,
        permRolesAuthzed,
        permUsersAuthzed,

        ssdSets,
        ssdRead,
        ssdRoles,
        ssdCard,
        ssdSearch,
        ssdAdd,
        ssdAddMember,
        ssdDelMember,
        ssdDelete,
        ssdCardUpdate,

        dsdSets,
        dsdRead,
        dsdRoles,
        dsdCard,
        dsdSearch,
        dsdAdd,
        dsdAddMember,
        dsdDelMember,
        dsdDelete,
        dsdCardUpdate,

        adminAuthZ,
        adminRoles,
        adminAdd,
        adminDrop,
        adminAssign,
        adminDeassign,
        adminGrant,
        adminRevoke,

        // DELEGATED ADMIN:
        arleRead,
        arleUpdate,
        arleAdd,
        arleDelete,
        arleSearch,
        arleDescendant,
        arleAscendent,
        arleAddinherit,
        arleDelinherit,
        adminSearch,
        arleAsgn,
        arleDeasgn,
        arleAsigned,

        orgRead,
        orgSearch,
        orgAdd,
        orgUpdate,
        orgDelete,
        orgDescendant,
        orgAscendent,
        orgAddinherit,
        orgDelinherit,

        // PW Policy:
        pswdRead,
        pswdUpdate,
        pswdAdd,
        pswdDelete,
        pswdSearch,
        pswdUserAdd,
        pswdUserDelete,

        auditBinds,
        auditAuthzs,
        auditUserAuthzs,
        auditSessions,
        auditMods,
        auditInvld,
    }
}