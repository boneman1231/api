package org.redcenter.api.vo;

public class ApiAttribute {

	protected String key;

	protected String name;

	protected String type;

	protected String value;

	public ApiAttribute() {
	}

	public ApiAttribute(String key, String value) {
		this.key = key;
		this.name = key;
		this.type = String.class.getName();
		this.value = value;
	}

	public ApiAttribute(String key, String name, String type, String value) {
		this.key = key;
		this.name = name;
		this.type = type;
		this.value = value;
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

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
