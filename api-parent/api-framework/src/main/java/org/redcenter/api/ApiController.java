package org.redcenter.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.redcenter.api.vo.ApiAttribute;
import org.redcenter.api.vo.ApiInvokeRequest;
import org.redcenter.api.vo.ApiNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@SpringBootApplication
public class ApiController {

	public static void main(String[] args) {
		// SpringApplication.run(Application.class, args);
		ApplicationContext ctx = SpringApplication.run(ApiController.class,
				args);
		System.out.println("Let's inspect the beans provided by Spring Boot:");

		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}
	}

	@SuppressWarnings("rawtypes")
	protected Map<String, Class> map = new HashMap<String, Class>();

	public ApiController() {
	}

	protected void addClass(Class<?> clazz) {
		map.put(clazz.getName(), clazz);
	}

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

	public ArrayList<ApiNode> getMethods(
			@RequestParam("className") String className) {
		ArrayList<ApiNode> apiMethods = new ArrayList<ApiNode>();

		Class<?> clazz = map.get(className);
		if (clazz == null) {
			return apiMethods;
		}

		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			// only show methods that have annotation
			Api annotation = method.getAnnotation(Api.class);
			if (annotation == null) {
				continue;
			}

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
			attr.setName(paramType.getSimpleName()); // default

			Annotation[] annotations = paramAnnotations[i++];
			for (Annotation annotation : annotations) {
				if (annotation instanceof Api) {
					Api apiAnnotation = (Api) annotation;
					String name = apiAnnotation.value();
					if (name != null && name.isEmpty()) {
						// set name by annotation
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
			Api annotation = clazz.getAnnotation(Api.class);
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
			Api annotation = method.getAnnotation(Api.class);
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
				return new ApiAttribute("error", msg);
			}

			// TODO check no-argument constructor
			Object instance = clazz.newInstance();

			// get argument objects
			Object[] arguments = getParametrs(method, params);

			// invoke
			String result = (String) method.invoke(instance, arguments);
			ApiAttribute apiResult = new ApiAttribute("result", result);
			return apiResult;

		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
			return new ApiAttribute("error", e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return new ApiAttribute("error", e.getMessage());
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
