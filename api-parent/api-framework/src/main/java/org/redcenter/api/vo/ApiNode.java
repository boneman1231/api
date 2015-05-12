package org.redcenter.api.vo;

import java.util.ArrayList;

public class ApiNode extends ApiAttribute {

	private ArrayList<ApiAttribute> inputs;

	public ArrayList<ApiAttribute> getInputs() {
		return inputs;
	}

	public void setInputs(ArrayList<ApiAttribute> inputs) {
		this.inputs = inputs;
	}
}
