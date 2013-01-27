/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.util.attr;

import us.jts.fortress.GlobalErrIds;

import us.jts.fortress.GlobalIds;
import org.apache.log4j.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Regular expression utilities to perform data validations on Fortress attributes.  These utils use the standard
 * java regular expression library.
 *
 * @author     Shawn McKinney
 */
public class RegExUtil
{
    private static final String CLS_NM = RegExUtil.class.getName();
    private static final Logger log = Logger.getLogger(CLS_NM);
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
			String warning = CLS_NM + ".safeText can't find safeText regular expression pattern.  Check your Fortress cfg";
			log.debug(warning);
		}
		else
		{
			Pattern safeTextPattern = Pattern.compile(safeTextPatternStr);
			Matcher safeTextMatcher = safeTextPattern.matcher(value);
			if (!safeTextMatcher.find())
			{
				String error = CLS_NM + ".safeText has detected invalid value [" + value + "]";
				throw new us.jts.fortress.ValidationException(GlobalErrIds.CONST_INVLD_TEXT, error);
			}
		}
	}
}