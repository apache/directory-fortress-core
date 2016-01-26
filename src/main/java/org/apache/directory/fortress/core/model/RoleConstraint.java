package org.apache.directory.fortress.core.model;

import org.apache.directory.fortress.core.GlobalIds;

public class RoleConstraint {

	public static final String RC_TYPE_NAME = "type";
	
	private RoleConstraintType constraintType;
	private String value;
	
	public RoleConstraint(){
		
	}
	
	public RoleConstraint(String value, RoleConstraintType constraintType){
		this.constraintType = constraintType;
		this.value = value;
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
	
	//BANK_USER$seq$0type$filter$AccountId=12345&WithdrawLimit=500
	public String gerRawData(UserRole uRole){
        StringBuilder sb = new StringBuilder();
        
        sb.append(uRole.getName());        
        sb.append( GlobalIds.DELIMITER );
        sb.append(RC_TYPE_NAME);
        sb.append( GlobalIds.DELIMITER );
        sb.append(constraintType);
        sb.append( GlobalIds.DELIMITER );
        sb.append(value);
        
        return sb.toString();
	}
}