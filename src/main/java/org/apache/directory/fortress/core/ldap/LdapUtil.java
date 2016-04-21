package org.apache.directory.fortress.core.ldap;

import org.apache.directory.fortress.core.GlobalIds;
import org.apache.directory.fortress.core.util.Config;


public class LdapUtil {

	private boolean ldapfilterSizeFound = false;
	private int ldapFilterSize = 25;	
    private char[] ldapMetaChars;
    private String[] ldapReplVals;
	
    private static volatile LdapUtil INSTANCE = null; 

    public static LdapUtil getInstance() {
        if(INSTANCE == null) {
            synchronized (LdapUtil.class) {
                if(INSTANCE == null){
        	        INSTANCE = new LdapUtil();
                }
            }
        }
        return INSTANCE;
    }        

    /**
    *
    */
   private static char[] loadLdapEscapeChars()
   {
       if ( !LdapUtil.getInstance().isLdapfilterSizeFound() )
       {
           return null;
       }

       char[] ldapMetaChars = new char[LdapUtil.getInstance().getLdapFilterSize()];

       for ( int i = 1;; i++ )
       {
           String prop = GlobalIds.LDAP_FILTER + i;
           String value = Config.getInstance().getProperty( prop );

           if ( value == null )
           {
               break;
           }

           ldapMetaChars[i - 1] = value.charAt( 0 );
       }

       return ldapMetaChars;
   }


   /**
    *
    */
   private static String[] loadValidLdapVals()
   {
       if ( !LdapUtil.getInstance().isLdapfilterSizeFound() )
       {
           return null;
       }

       String[] ldapReplacements = new String[LdapUtil.getInstance().getLdapFilterSize()];

       for ( int i = 1;; i++ )
       {
           String prop = GlobalIds.LDAP_SUB + i;
           String value = Config.getInstance().getProperty( prop );

           if ( value == null )
           {
               break;
           }

           ldapReplacements[i - 1] = value;
       }

       return ldapReplacements;
   }

    
	public boolean isLdapfilterSizeFound() {
		return ldapfilterSizeFound;
	}

	public void setLdapfilterSizeFound(boolean ldapfilterSizeFound) {
		this.ldapfilterSizeFound = ldapfilterSizeFound;
	}

	public int getLdapFilterSize() {
		return ldapFilterSize;
	}

	public void setLdapFilterSize(int ldapFilterSize) {
		this.ldapFilterSize = ldapFilterSize;
	}

	public char[] getLdapMetaChars() {
		return ldapMetaChars;
	}

	public void setLdapMetaChars(char[] ldapMetaChars) {
		this.ldapMetaChars = ldapMetaChars;
	}

	public String[] getLdapReplVals() {
		return ldapReplVals;
	}

	public void setLdapReplVals(String[] ldapReplVals) {
		this.ldapReplVals = ldapReplVals;
	}
	
}
