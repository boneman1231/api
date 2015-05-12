package org.redcenter.api.test;

import org.redcenter.api.Api;

@Api("測試功能")
public class TestApiClass {

	public String test() {
		return "test ";
	}
	
	@Api("Test Method")
	public String test(@Api("the input") String input) {
		return "test " + input;
	}

	@Api("加法")
	public String test(@Api("input x") int x, @Api("input y") int y) {
		return "x + y = " + (x + y);
	}
}
