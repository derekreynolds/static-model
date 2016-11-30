/**
 * 
 */
package com.evolvingreality.staticmodel.domain.util;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * This class represent the information needed to fill an <option></option> in
 * a html <select></select>.
 * @author Derek Reynolds
 *
 */
public class SelectOption {

	private final Long id;
	
	private final String groupName;
	
	private final String key;
	
	private final String value;

	
	public SelectOption(Long id, String groupName, String key, String value) {
		this.id = id;
		this.groupName = groupName;
		this.key = key;
		this.value = value;
	}
	
	public Long getId() {
		return id;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}


	@Override
	public int hashCode() {
		
		return new HashCodeBuilder(17, 37)
					.append(id)
					.append(groupName)
					.append(key)
					.append(value)
					.toHashCode();
		
	}

	@Override
	public boolean equals(Object obj) {
		
		SelectOption rhs = (SelectOption)obj;
		
		return new EqualsBuilder()
					.append(id, rhs.id)
					.append(groupName, rhs.groupName)
					.append(key, rhs.key)
					.append(value, rhs.value)
					.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
					.append("id", id)					
					.append("groupName", groupName)
					.append("key", key)
					.append("value", value)
					.toString();
	}
	
	
	
}
