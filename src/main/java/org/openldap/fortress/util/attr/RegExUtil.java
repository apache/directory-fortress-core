/*
 * This work is part of OpenLDAP Software <http://www.openldap.org/>.
 *
 * Copyright 1998-2014 The OpenLDAP Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted only as authorized by the OpenLDAP
 * Public License.
 *
 * A copy of this license is available in the file LICENSE in the
 * top-level directory of the distribution or, alternatively, at
 * <http://www.OpenLDAP.org/license.html>.
 */

package org.openldap.fortress.util.attr;

import org.openldap.fortress.GlobalErrIds;

import org.openldap.fortress.GlobalIds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Regular expression utilities to perform data validations on Fortress attributes.  These utils use the standard
 * java regular expression library.
 *
 * @author     Shawn McKinney
 */
class RegExUtil
{
    private static final String CLS_NM = RegExUtil.class.getName();
    private static final Logger LOG = LoggerFactory.getLogger( CLS_NM );
	private static final String safeTextPatternStr = org.openldap.fortress.cfg.Config.getProperty(GlobalIds.REG_EX_SAFE_TEXT);

	/**
	 *  Perform safe text validation on character string.
	 *
	 * @param  value Contains the string to check.
	 * @exception org.openldap.fortress.ValidationException  In the event the data validation fails.
	 */
	public static void safeText(String value)
		throws org.openldap.fortress.ValidationException
	{
		if (safeTextPatternStr == null || safeTextPatternStr.compareTo("") == 0)
		{
			LOG.debug("safeText can't find safeText regular expression pattern.  Check your Fortress cfg");
		}
		else
		{
			Pattern safeTextPattern = Pattern.compile(safeTextPatternStr);
			Matcher safeTextMatcher = safeTextPattern.matcher(value);
			if (!safeTextMatcher.find())
			{
				String error = "safeText has detected invalid value [" + value + "]";
				throw new org.openldap.fortress.ValidationException(GlobalErrIds.CONST_INVLD_TEXT, error);
			}
		}
	}
}