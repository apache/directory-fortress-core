
/*
 * Copyright (c) 2009-2012. Joshua Tree Software, LLC.  All Rights Reserved.
 */
/*
 *  This class is used for testing purposes.
 */
package com.jts.fortress.misc;

import java.util.HashMap;

/**
 *  Description of the Class
 *
 *@author     Shawn McKinney
 *@created    August 14, 2010
 */
public class SSDTest
{

	/**
	 *  put your documentation comment here
	 *
	 *@param  args
	 */
	public static void main(String[] args)
	{
		System.out.println("SSDTest...");
		
		HashMap<SSDKey, String> h1 = new HashMap<SSDKey, String>();
		SSDKey key1 = new SSDKey("r1", "r2");
        h1.put(key1, "test1");

        SSDKey key2 = new SSDKey("r2", "r3");
        h1.put(key2, "test2");

        SSDKey key3 = new SSDKey("r3", "r4");
        h1.put(key3, "test3");

        String keyVal = h1.get(key1);
        if(keyVal != null)
        {
            System.out.println("test 1 keyVal=" + keyVal);
        }
        else
        {
            System.out.println("test 1 keyVal is null!");
        }

        keyVal = h1.get(key2);
        if(keyVal != null)
        {
            System.out.println("test 2 keyVal=" + keyVal);
        }
        else
        {
            System.out.println("test 2 keyVal is null!");
        }

        keyVal = h1.get(key3);
        if(keyVal != null)
        {
            System.out.println("test 3 keyVal=" + keyVal);
        }
        else
        {
            System.out.println("test 3 keyVal is null!");
        }

        SSDKey key4 = new SSDKey("r2", "r1");
        keyVal = h1.get(key4);
        if(keyVal != null)
        {
            System.out.println("test 4 keyVal=" + keyVal);
        }
        else
        {
            System.out.println("test 4 keyVal is null!");
        }


	}
}

