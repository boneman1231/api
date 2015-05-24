package org.redcenter.api.test;

import java.util.ArrayList;
import java.util.Arrays;

import org.redcenter.api.ApiController;
import org.redcenter.api.test.TestApiClass;
import org.redcenter.api.vo.ApiAttribute;
import org.redcenter.api.vo.ApiInvokeRequest;
import org.redcenter.api.vo.ApiNode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Controller extends ApiController {

	public static void main(String[] args) {
		SpringApplication.run(Controller.class, args);
		
//		ApplicationContext ctx = SpringApplication.run(ApiController.class,
//				args);
//		System.out.println("Let's inspect the beans provided by Spring Boot:");
//
//		String[] beanNames = ctx.getBeanDefinitionNames();
//		Arrays.sort(beanNames);
//		for (String beanName : beanNames) {
//			System.out.println(beanName);
//		}
	}
	
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
