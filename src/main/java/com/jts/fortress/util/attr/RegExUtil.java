/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */

package com.jts.fortress.util.attr;

import com.jts.fortress.constants.GlobalErrIds;

import org.apache.log4j.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Regular expression utilities to perform data validations on Fortress attributes.  These utils use the standard
 * java regular expression library.
 *
 * @author     Shawn McKinney
 * @created    August 30, 2009
 */
public class RegExUtil
{
    private static final String CLS_NM = RegExUtil.class.getName();
    private static final Logger log = Logger.getLogger(CLS_NM);
	private static final String safeTextPatternStr = com.jts.fortress.configuration.Config.getProperty(com.jts.fortress.constants.GlobalIds.REG_EX_SAFE_TEXT);

	/**
	 *  Perform safe text validation on character string.
	 *
	 * @param  value Contains the string to check.
	 * @exception com.jts.fortress.ValidationException  In the event the data validation fails.
	 */
	public static void safeText(String value)
		throws com.jts.fortress.ValidationException
	{
		if (safeTextPatternStr == null || safeTextPatternStr.compareTo("") == 0)
		{
			String warning = CLS_NM + ".safeText can't find safeText regular expression pattern.  Check your Fortress configuration";
			log.debug(warning);
		}
		else
		{
			Pattern safeTextPattern = Pattern.compile(safeTextPatternStr);
			Matcher safeTextMatcher = safeTextPattern.matcher(value);
			if (!safeTextMatcher.find())
			{
				String error = CLS_NM + ".safeText has detected invalid value [" + value + "]";
				throw new com.jts.fortress.ValidationException(GlobalErrIds.CONST_INVLD_TEXT, error);
			}
		}
	}
}