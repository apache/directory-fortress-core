package org.apache.directory.fortress.core.ldap;



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
