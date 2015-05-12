package org.redcenter.api.target;

import org.redcenter.api.Api;

@Api("TestClass")
public class TestApiClass {

	@Api("Test Method")
	public String test(@Api("the input") String input) {
		return "test " + input;
	}

	@Api("The Method")
	public String test(@Api("input x") int x, @Api("input y") int y) {
		return "x + y = " + (x + y);
	}
}
