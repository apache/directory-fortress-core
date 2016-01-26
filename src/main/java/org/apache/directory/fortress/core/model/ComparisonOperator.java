package org.apache.directory.fortress.core.model;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ComparisonOperator {

	EQUALS("eq"),
	NOT_EQUALS("neq"),
	GREATER_THAN("gt"),
	GREATER_THAN_OR_EQUAL_TO("gte"),
	LESS_THAN("lt"),
	LESS_THAN_OR_EQUAL_TO("lte");

	private final String name;       
	private static Map<String, ComparisonOperator> reverseLookup_ = new HashMap<String, ComparisonOperator>();

	static
	{
		EnumSet<ComparisonOperator> es = EnumSet.allOf(ComparisonOperator.class);

		for (ComparisonOperator co : es)
		{
			reverseLookup_.put(co.toString(), co);
		}
	}

	
	private ComparisonOperator(String s) {
		name = s;
	}

	public static ComparisonOperator fromName(String name)
	{
		if (name != null)
		{
			return reverseLookup_.get(name.trim());
		}

		return null;
	}
	
	public String toString() {
		return this.name;
	}
	
}