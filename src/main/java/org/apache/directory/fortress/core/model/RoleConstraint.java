/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.directory.fortress.core.model;

import java.io.Serializable;

import org.apache.directory.fortress.core.GlobalIds;

public class RoleConstraint implements Serializable {

    private static final long serialVersionUID = 1L;
	
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