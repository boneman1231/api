package org.redcenter.api.vo;

import java.util.ArrayList;

public class ApiMethod extends ApiAttribute {

	/**
	 * input parameters
	 */
	private ArrayList<ApiAttribute> inputs;

	public ArrayList<ApiAttribute> getInputs() {
		return inputs;
	}

	public void setInputs(ArrayList<ApiAttribute> inputs) {
		this.inputs = inputs;
	}
}
