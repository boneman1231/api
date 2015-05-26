package org.redcenter.api.test;

import org.redcenter.api.annotation.Api;
import org.redcenter.api.annotation.Option;
import org.redcenter.api.spi.IApiClass;

@Api("測試功能2")
public class TestApiClass implements IApiClass{

	public String test() {
		return "test ";
	}
	
	@Api(value="Test Method", desc = "tooooooltip")
	public String test(@Api("the input") String input) {
		return "test " + input;
	}

	@Api("加法2")
	public String test(@Api(value="input x", desc="the x") int x, @Api("input y") int y) {
		return "x + y = " + (x + y);
	}
	
	@Api
	public String test2(
			@Api(desc="ssssssssssssss",
					options = { 
					@Option(key = "input1", value = "first input"),
					@Option("second input"),
					@Option("3input")}) String input) {
		return "test " + input;
	}
}
