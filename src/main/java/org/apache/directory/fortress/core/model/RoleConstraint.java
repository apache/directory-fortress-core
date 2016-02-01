package org.apache.directory.fortress.core.model;

import org.apache.directory.fortress.core.GlobalIds;

public class RoleConstraint {

	public static final String RC_TYPE_NAME = "type";
	
	private RoleConstraintType constraintType;
	private String value;
	private String paSetName;
	
	public RoleConstraint(){
		
	}
	
	public RoleConstraint(String value, RoleConstraintType constraintType, String paSetName){
		this.constraintType = constraintType;
		this.value = value;
		this.paSetName = paSetName;
	}	
	
	public RoleConstraintType getConstraintType() {
		return constraintType;
	}
	public void setConstraintType(RoleConstraintType constraintType) {
		this.constraintType = constraintType;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getPaSetName() {
		return paSetName;
	}

	public void setPaSetName(String paSetName) {
		this.paSetName = paSetName;
	}	
	
	//BANK_USER$seq$0type$filter$ecomm.merchant.id$AccountId=12345&WithdrawLimit=500
	public String getRawData(UserRole uRole){
        StringBuilder sb = new StringBuilder();
        
        sb.append(uRole.getName());        
        sb.append( GlobalIds.DELIMITER );
        sb.append(RC_TYPE_NAME);
        sb.append( GlobalIds.DELIMITER );
        sb.append(constraintType);
        sb.append( GlobalIds.DELIMITER );
        sb.append(paSetName);
        sb.append( GlobalIds.DELIMITER );
        sb.append(value);
        
        return sb.toString();
	}

}