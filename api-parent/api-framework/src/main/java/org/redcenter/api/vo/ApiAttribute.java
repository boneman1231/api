package org.redcenter.api.vo;

import java.util.ArrayList;

import org.redcenter.api.HtmlUtils;

public class ApiAttribute {

	/**
	 * to match class or method name
	 */
	protected String key;

	/**
	 * to display in UI component
	 */
	protected String name;

	protected String type;

	protected String value;
	
	protected ArrayList<ApiAttribute> options;

	protected String description;

	public ApiAttribute() {
	}

	public ApiAttribute(String key, String value) {
		this.key = key;
		this.name = key;
		this.type = String.class.getName();
		this.value = value;
		this.description = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(Class<?> typeClass) {
		// get input type for Angular JS
		this.type = HtmlUtils.getHtmlType(typeClass);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ArrayList<ApiAttribute> getOptions() {
		return options;
	}

	public void setOptions(ArrayList<ApiAttribute> options) {
		this.options = options;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
