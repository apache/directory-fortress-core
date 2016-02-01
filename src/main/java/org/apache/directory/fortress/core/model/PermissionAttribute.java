package org.apache.directory.fortress.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;

//AccountId?dataType=int&required=true&validValues=SELF,ANY,int&comparator=equals&default=SELF

@XmlRootElement(name = "ftPA")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "permission", propOrder =
    {
        "opName",
        "objName",
        "required",
        "operator",
        "dataType",
        "validValues",
        "defaultValue"
})
public class PermissionAttribute extends FortEntity implements Serializable {

    /** Default serialVersionUID */
    private static final long serialVersionUID = 1L;
    
    private static final String REQUIRED_PARAM_NAME = "required";
    private static final String OPERATOR_PARAM_NAME = "operator";
    private static final String DATA_TYPE_PARAM_NAME = "dataType";
    private static final String VALID_VALUES_PARAM_NAME = "validValues";
    private static final String DEFAULT_VALUE_PARAM_NAME = "defaultValue";
    
    private String attributeName;
    private Boolean required = false;
    private ComparisonOperator operator;
    //TODO: make this an enum?
    private String dataType;
    //TODO: should this be an enum?
    private List<String> validValues;
    private String defaultValue;
    
    public PermissionAttribute()
    {
    	
    }
    
    public PermissionAttribute(String attributeName)
    {
    	this.attributeName = attributeName;
    }
    
    public String toFtPAString(){
    	String ftPA = attributeName + "?";
    	
    	List<String> qualifiers = new ArrayList<String>();    	
    	
    	if(required != null){
    		qualifiers.add(REQUIRED_PARAM_NAME + "=" + String.valueOf(required));
    	}
    	if(operator != null){
    		qualifiers.add(OPERATOR_PARAM_NAME + "=" + operator);
    	}
    	if(dataType != null){
    		qualifiers.add(DATA_TYPE_PARAM_NAME + "=" + dataType);
    	}
    	if(validValues != null && validValues.size() > 0){    		    		
    		qualifiers.add(VALID_VALUES_PARAM_NAME + "=" + StringUtils.join(validValues, ","));
    	}
    	if(defaultValue != null){
    		qualifiers.add(DEFAULT_VALUE_PARAM_NAME + "=" + defaultValue);
    	}
    		
    	ftPA += StringUtils.join(qualifiers, "&");
    	
    	return ftPA;
    }
    
	public Boolean getRequired() {
		return required;
	}
	public void setRequired(Boolean required) {
		this.required = required;
	}
	public ComparisonOperator getOperator() {
		return operator;
	}
	public void setOperator(ComparisonOperator operator) {
		this.operator = operator;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public List<String> getValidValues() {
		return validValues;
	}
	public void setValidValues(List<String> validValues) {
		this.validValues = validValues;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	
	/**
     * This method loads PermissionAttribute entity instance variables with data that was retrieved from the
     * 'ftPA' attribute on the 'ftOperation' object class.
     *
     * @param szRawData contains a raw formatted String that maps to 'ftPA' attribute on 'ftOperation' object class
     */
    public void load( String szRawData )
    {    	
        if ( ( szRawData != null ) && ( szRawData.length() > 0 ) )
        {
            String[] data = StringUtils.split(szRawData, "?"); 
            attributeName = data[0];
            		
            String[] parameters = data[1].split("&");
            for (String param : parameters){
            	String[] paramSplit = param.split("=");
            	String name = paramSplit[0];
            	String val = paramSplit[1];
            	
            	if(name.equals(REQUIRED_PARAM_NAME)){
            		required = Boolean.parseBoolean(val);
            	}
            	if(name.equals(OPERATOR_PARAM_NAME)){
            		operator = ComparisonOperator.fromName(val);
            	}
            	if(name.equals(DATA_TYPE_PARAM_NAME)){
            		dataType = val;
            	}
            	if(name.equals(VALID_VALUES_PARAM_NAME)){
            		validValues = Arrays.asList(val.split(","));
            	}
            	if(name.equals(DEFAULT_VALUE_PARAM_NAME)){
            		defaultValue = val;
            	}
            	
            }
        }
    }
    
    /**
     * Checks that attribute names are equal
     */
    @Override
    public boolean equals( Object thatObj )
    {
        if ( this == thatObj )
        {
            return true;
        }

        if ( this.getAttributeName() == null )
        {
            return false;
        }

        if ( !( thatObj instanceof PermissionAttribute ) )
        {
            return false;
        }

        PermissionAttribute thatPermObj = ( PermissionAttribute ) thatObj;

        if ( thatPermObj.getAttributeName() == null )
        {
            return false;
        }

        return thatPermObj.getAttributeName().equalsIgnoreCase( this.getAttributeName() );
    }
    
    @Override
    public int hashCode()
    {
        int result = 31 * ( attributeName != null ? attributeName.hashCode() : 0 );
        return result;
    }

    
}
