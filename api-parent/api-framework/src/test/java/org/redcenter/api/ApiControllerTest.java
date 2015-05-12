package org.redcenter.api;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.redcenter.api.vo.ApiAttribute;
import org.redcenter.api.vo.ApiNode;

public class ApiControllerTest {
	private ApiController controller = null;

	@Before
	public void setUp() throws Exception {
		controller = new ApiController();
	}

	@After
	public void tearDown() throws Exception {
		controller = null;
	}

	@Test
	public void testGetClasses() {
		controller.addClass(TestApiClass.class);

		System.out.println("getClasses:");
		ArrayList<ApiAttribute> list = controller.getClasses();
		for (ApiAttribute apiAttribute : list) {
			System.out.println(apiAttribute.getName() + "="
					+ apiAttribute.getKey());
		}

		assertEquals("TestClass", list.get(0).getName());
		assertEquals("org.redcenter.api.TestApiClass", list.get(0).getKey());
		System.out.println();
	}

	@Test
	public void testGetMethods() {
		controller.addClass(TestApiClass.class);

		System.out.println("getMethods:");
		ArrayList<ApiNode> methods = controller.getMethods(TestApiClass.class
				.getName());
		for (ApiNode method : methods) {
			StringBuilder sb = new StringBuilder();
			sb.append(method.getName() + "=" + method.getKey() + "(");
			String prefix = "";
			for (ApiAttribute apiAttribute : method.getInputs()) {
				sb.append(prefix);
				prefix = ",";
				sb.append(apiAttribute.getName() + "=" + apiAttribute.getKey());
			}
			System.out.println(sb.append(")"));
		}
		System.out.println();
	}

	@Test
	public void testInvoke() {
		// System.out.println("invokde:");
		// ApiInvokeRequest request = new ApiInvokeRequest();
		// request.setClassName(TestApiClass.class.getName());
		// request.setMethodName("test");
		// request.setParams(Arrays.asList(new String[] { "input" }));
		// String value = controller.invoke(request);
		// System.out.println(value);
		//
		// request.setParams(Arrays.asList(new String[] { "1", "3" }));
		// value = controller.invoke(request);
		// System.out.println(value);
	}
}
