package org.redcenter.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.redcenter.api.target.TestApiClass;
import org.redcenter.api.vo.ApiAttribute;
import org.redcenter.api.vo.ApiInvokeRequest;
import org.redcenter.api.vo.ApiNode;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class ApiController {

	public static void main(String[] args) {
		ApiController controller = new ApiController();

		System.out.println("getClasses:");
		ArrayList<ApiAttribute> list = controller.getClasses();
		for (ApiAttribute apiAttribute : list) {
			System.out.println(apiAttribute.getName() + "="
					+ apiAttribute.getKey());
		}
		System.out.println();

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

//		System.out.println("invokde:");
//		ApiInvokeRequest request = new ApiInvokeRequest();
//		request.setClassName(TestApiClass.class.getName());
//		request.setMethodName("test");
//		request.setParams(Arrays.asList(new String[] { "input" }));
//		String value = controller.invoke(request);
//		System.out.println(value);
//
//		request.setParams(Arrays.asList(new String[] { "1", "3" }));
//		value = controller.invoke(request);
//		System.out.println(value);
	}

	@SuppressWarnings("rawtypes")
	protected Map<String, Class> map = new HashMap<String, Class>();

	public ApiController() {
//		map.put(TestApiClass.class.getName(), TestApiClass.class);
	}

	@RequestMapping("/getClasses")
	public ArrayList<ApiAttribute> getClasses() {
		ArrayList<ApiAttribute> attrs = new ArrayList<ApiAttribute>();
		for (Class<?> clazz : map.values()) {
			ApiAttribute attr = new ApiAttribute();
			attr.setKey(clazz.getName());
			setAnnotatioName(clazz, attr);
			attr.setType(clazz.getName());
			attrs.add(attr);
		}
		return attrs;
	}

	@RequestMapping("/getMethods")
	public ArrayList<ApiNode> getMethods(
			@RequestParam("className") String className) {
		ArrayList<ApiNode> apiMethods = new ArrayList<ApiNode>();

		Class<?> clazz = map.get(className);
		if (clazz == null) {
			return apiMethods;
		}

		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			ApiNode apiMethod = new ApiNode();
			ArrayList<ApiAttribute> attrs = new ArrayList<ApiAttribute>();

			apiMethod.setKey(method.getName());
			setAnnotatioName(method, apiMethod);
			apiMethod.setType(clazz.getName());

			// set method input parameter
			setApiAttribute(method, attrs);
			apiMethod.setInputs(attrs);

			apiMethods.add(apiMethod);
		}

		return apiMethods;
	}

	private void setApiAttribute(Method method, ArrayList<ApiAttribute> attrs) {
		Annotation[][] paramAnnotations = method.getParameterAnnotations();
		Class<?>[] paramTypes = method.getParameterTypes();
		int i = 0;

		for (Class<?> paramType : paramTypes) {
			ApiAttribute attr = new ApiAttribute();
			attr.setKey("p" + i);
			attr.setType(paramType.getName());

			Annotation[] annotations = paramAnnotations[i++];
			for (Annotation annotation : annotations) {
				if (annotation instanceof Api) {
					Api apiAnnotation = (Api) annotation;

					String name = apiAnnotation.value();
					if (name == null || name.isEmpty()) {
						attr.setName(paramType.getSimpleName());
					} else {
						attr.setName(name);
					}

					break;
				}
			}

			attrs.add(attr);
		}
	}

	private void setAnnotatioName(Class<?> clazz, ApiAttribute attr) {
		boolean flag = clazz.isAnnotationPresent(Api.class);
		if (flag) {
			Api annotation = (Api) clazz.getAnnotation(Api.class);
			String name = annotation.value();
			if (name == null || name.isEmpty()) {
				attr.setName(clazz.getSimpleName());
			} else {
				attr.setName(name);
			}
		} else {
			attr.setName(clazz.getSimpleName());
		}
	}

	private void setAnnotatioName(Method method, ApiAttribute attr) {
		boolean flag = method.isAnnotationPresent(Api.class);
		if (flag) {
			Api annotation = (Api) method.getAnnotation(Api.class);
			String name = annotation.value();
			if (name == null || name.isEmpty()) {
				attr.setName(method.getName());
			} else {
				attr.setName(name);
			}
		} else {
			attr.setName(method.getName());
		}
	}

	@RequestMapping("/invoke")
	public ApiAttribute invoke(@RequestBody ApiInvokeRequest request) {
		String className = request.getClassName();
		String methodName = request.getMethodName();
		List<String> params = request.getParams();
		int paramSize = 0;
		if (params != null) {
			paramSize = params.size();
		}

		// get method object
		Class<?> clazz = map.get(className);
		try {
			Method method = null;
			Method[] methods = clazz.getDeclaredMethods();
			for (Method m : methods) {
				if (m.getName().equals(methodName)
						&& m.getParameterTypes().length == paramSize) {
					method = m;
					break;
				}
			}

			if (method == null) {
				String msg = "Error! Can't find specified function.";
				System.err.println(msg);
//				return msg;
				return null;
			}

			// TODO check no-argument constructor
			Object instance = clazz.newInstance();

			// get argument objects
			Object[] arguments = getParametrs(method, params);

			// invoke
			String result = (String) method.invoke(instance, arguments);
//			System.out.println(result);
//			return result;
			ApiAttribute apiResult = new ApiAttribute();
			apiResult.setName(result);
			return apiResult;

		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
//			return e.getMessage();
			return null;
		}
	}

	private Object[] getParametrs(Method method, List<String> params) {
		List<Object> parameters = new ArrayList<Object>();
		Class<?>[] paramTypes = method.getParameterTypes();
		for (int i = 0; i < paramTypes.length; i++) {
			Class<?> paramType = paramTypes[i];
			String value = params.get(i);

			if (paramType == String.class) {
				parameters.add(value);
			} else if (ClassUtils.isPrimitiveOrWrapper(paramType)) {
				// only wrapper type has valueOf method
				paramType = ReflectionUtils.getWrapperClass(paramType);
				Object obj = ReflectionUtils.valueOf(paramType, value);
				parameters.add(obj);
			} else {
				// TODO throw exception?
				System.err.println(method.getName() + " parameter type "
						+ paramType.getName() + " is not primitive ");
			}
		}
		return parameters.toArray();
	}
}
