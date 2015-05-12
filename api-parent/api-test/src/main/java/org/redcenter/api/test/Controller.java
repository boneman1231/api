package org.redcenter.api.test;

import java.util.ArrayList;

import org.redcenter.api.ApiController;
import org.redcenter.api.test.TestApiClass;
import org.redcenter.api.vo.ApiAttribute;
import org.redcenter.api.vo.ApiInvokeRequest;
import org.redcenter.api.vo.ApiNode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Controller extends ApiController {

	public Controller() {
		addClass(TestApiClass.class);
	}

	@RequestMapping("/getClasses")
	public ArrayList<ApiAttribute> getClasses() {
		return super.getClasses();
	}

	@RequestMapping("/getMethods")
	public ArrayList<ApiNode> getMethods(
			@RequestParam("className") String className) {
		return super.getMethods(className);
	}

	@RequestMapping("/invoke")
	public ApiAttribute invoke(@RequestBody ApiInvokeRequest request) {
		return super.invoke(request);
	}
}
