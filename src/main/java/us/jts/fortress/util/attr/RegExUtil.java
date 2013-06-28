/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.util.attr;

import us.jts.fortress.GlobalErrIds;

import us.jts.fortress.GlobalIds;
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
	private static final String safeTextPatternStr = us.jts.fortress.cfg.Config.getProperty(GlobalIds.REG_EX_SAFE_TEXT);

	/**
	 *  Perform safe text validation on character string.
	 *
	 * @param  value Contains the string to check.
	 * @exception us.jts.fortress.ValidationException  In the event the data validation fails.
	 */
	public static void safeText(String value)
		throws us.jts.fortress.ValidationException
	{
		if (safeTextPatternStr == null || safeTextPatternStr.compareTo("") == 0)
		{
			String warning = "safeText can't find safeText regular expression pattern.  Check your Fortress cfg";
			LOG.debug(warning);
		}
		else
		{
			Pattern safeTextPattern = Pattern.compile(safeTextPatternStr);
			Matcher safeTextMatcher = safeTextPattern.matcher(value);
			if (!safeTextMatcher.find())
			{
				String error = "safeText has detected invalid value [" + value + "]";
				throw new us.jts.fortress.ValidationException(GlobalErrIds.CONST_INVLD_TEXT, error);
			}
		}
	}
}