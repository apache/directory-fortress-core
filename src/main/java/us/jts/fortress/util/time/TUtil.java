/*
 * Copyright (c) 2009-2013, JoshuaTree. All Rights Reserved.
 */

package us.jts.fortress.util.time;

import java.util.GregorianCalendar;

/**
 * Utility class to convert current time/date into internal format, {@link Time}, used for {@link Constraint} checks {@link CUtil#validateConstraints(us.jts.fortress.rbac.Session, us.jts.fortress.util.time.CUtil.ConstraintType, boolean)}.
 * This utility processes custom date formats and should not be used by external programs.
 *
 * @author Shawn McKinney
 */
public class TUtil
{

    /**
     * Get the curent timestamp from Java and convert to {@link Time} format.
     *
     * @return Time
     */
    public static Time getCurrentTime()
    {
        Time time = new Time();
        GregorianCalendar gc = new GregorianCalendar();
        String szMinute = "" + gc.get(GregorianCalendar.MINUTE);
        String szHour = "" + gc.get(GregorianCalendar.HOUR_OF_DAY);

        time.day = "" + gc.get(GregorianCalendar.DAY_OF_WEEK);
        String szDay = "" + gc.get(GregorianCalendar.DAY_OF_MONTH);
        int month = gc.get(GregorianCalendar.MONTH);
        String szMonth = "" + (month + 1);
        String szYear = "" + gc.get(GregorianCalendar.YEAR);

        if (szMinute.length() == 1)
        {
            szMinute = "0" + szMinute;
        }
        if (szHour.length() == 1)
        {
            szHour = "0" + szHour;
        }
        if (szDay.length() == 1)
        {
            szDay = "0" + szDay;
        }
        if (szMonth.length() == 1)
        {
            szMonth = "0" + szMonth;
        }
        String szCurrentTime = szHour + szMinute;

        time.currentTime = new Integer(szCurrentTime);
        time.date = szYear + szMonth + szDay;
        return time;
    }
}

