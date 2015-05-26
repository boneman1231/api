package org.redcenter.api.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ServiceLoader;

import org.redcenter.api.ApiController;
import org.redcenter.api.spi.IApiClass;
import org.redcenter.api.vo.ApiAttribute;
import org.redcenter.api.vo.ApiInvokeRequest;
import org.redcenter.api.vo.ApiNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringController extends ApiController {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SpringController.class,
				args);
		System.out.println("Let's inspect the beans provided by Spring Boot:");

		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}
	}

	public SpringController() {
//		try {
//			// initialize class loader by path
//			URL url = new URL("/lib");
//			URLClassLoader ucl = new URLClassLoader(new URL[] { url },
//					getClass().getClassLoader());
//			
//			ServiceLoader<IApiClass> serviceLoader = ServiceLoader.load(
//					IApiClass.class, ucl);

			ServiceLoader<IApiClass> serviceLoader = ServiceLoader.load(
					IApiClass.class);
			for (IApiClass service : serviceLoader) {
				addClass(service.getClass());
				System.out.println("===============================Add " + service.getClass() + " api class.");
			}
		
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
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
