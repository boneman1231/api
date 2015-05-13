package org.redcenter.api;

import org.redcenter.api.Api;

@Api("TestClass")
public class TestApiClass {

	@Api(value = "Test Method", desc = "tooltip")
	public String test(@Api("the input") String input) {
		return "test " + input;
	}

	@Api("The Method")
	public String test(@Api("input x") int x, @Api("input y") int y) {
		return "x + y = " + (x + y);
	}

	@Api
	public String test2(
			@Api(options = { 
					@Option(key = "input1", value = "first input"),
					@Option("second input") }) String input) {
		return "test " + input;
	}
}
