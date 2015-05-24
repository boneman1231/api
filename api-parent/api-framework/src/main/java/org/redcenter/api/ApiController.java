package org.redcenter.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.redcenter.api.annotation.Api;
import org.redcenter.api.annotation.Option;
import org.redcenter.api.vo.ApiAttribute;
import org.redcenter.api.vo.ApiInvokeRequest;
import org.redcenter.api.vo.ApiNode;

public class ApiController {

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
			setAnnotatioAttr(clazz, attr);
			attr.setType(clazz);
			attrs.add(attr);
		}
		return attrs;
	}

	public ArrayList<ApiNode> getMethods(String className) {
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
			setAnnotatioAttr(method, apiMethod);
			apiMethod.setType(clazz);

			// set method input parameter
			setParamsAttributes(method, attrs);
			apiMethod.setInputs(attrs);

			apiMethods.add(apiMethod);
		}

		return apiMethods;
	}

	private void setParamsAttributes(Method method,
			ArrayList<ApiAttribute> attrs) {
		Annotation[][] paramAnnotations = method.getParameterAnnotations();
		Class<?>[] paramTypes = method.getParameterTypes();
		int i = 0;

		for (Class<?> paramType : paramTypes) {
			ApiAttribute attr = new ApiAttribute();
			attr.setKey("p" + i);
			attr.setType(paramType);
			attr.setName(paramType.getSimpleName()); // default

			// set api attribute for the parameter
			Annotation[] annotations = paramAnnotations[i++];
			for (Annotation annotation : annotations) {
				if (annotation instanceof Api) {
					Api apiAnnotation = (Api) annotation;
					setParamAttribute(apiAnnotation, attr);
					break;
				}
			}

			attrs.add(attr);
		}
	}

	private void setParamAttribute(Api apiAnnotation, ApiAttribute attr) {
		// set name by annotation
		String name = apiAnnotation.value();
		if (name != null && !name.isEmpty()) {
			attr.setName(name);
		}

		// set default value by annotation
		String value = apiAnnotation.value();
		if (value != null && !value.isEmpty()) {
			attr.setValue(value);
		}

		// set options by annotation
		Option[] options = apiAnnotation.options();
		if (options != null && options.length > 0) {
			setOptions(options, attr);
		}

		// set description
		String desc = apiAnnotation.desc();
		if (desc != null && !desc.isEmpty()) {
			attr.setDescription(desc);
		}
	}

	private void setOptions(Option[] options, ApiAttribute attr) {
		ArrayList<ApiAttribute> apiOptions = new ArrayList<ApiAttribute>();
		for (Option option : options) {
			String key = option.key();
			String value = option.value();
			if (key == null || key.isEmpty()) {
				key = value;
			}
			apiOptions.add(new ApiAttribute(key, value));
		}
		attr.setOptions(apiOptions);
		attr.setType(Option.class);
	}

	private void setAnnotatioAttr(Class<?> clazz, ApiAttribute attr) {
		boolean flag = clazz.isAnnotationPresent(Api.class);
		if (flag) {
			Api annotation = clazz.getAnnotation(Api.class);
			String name = annotation.value();
			if (name == null || name.isEmpty()) {
				attr.setName(clazz.getSimpleName());
			} else {
				attr.setName(name);
			}

			// set description
			String desc = annotation.desc();
			if (desc != null && !desc.isEmpty()) {
				attr.setDescription(desc);
			}
		} else {
			attr.setName(clazz.getSimpleName());
		}
	}

	private void setAnnotatioAttr(Method method, ApiAttribute attr) {
		boolean flag = method.isAnnotationPresent(Api.class);
		if (flag) {
			Api annotation = method.getAnnotation(Api.class);
			String name = annotation.value();
			if (name == null || name.isEmpty()) {
				attr.setName(method.getName());
			} else {
				attr.setName(name);
			}

			// set description
			String desc = annotation.desc();
			if (desc != null && !desc.isEmpty()) {
				attr.setDescription(desc);
			}
		} else {
			attr.setName(method.getName());
		}
	}

	public ApiAttribute invoke(ApiInvokeRequest request) {
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
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return new ApiAttribute("error", e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return new ApiAttribute("error", e.getMessage());
		} catch (InstantiationException e) {
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
			} else if (ReflectionUtils.isPrimitiveOrWrapper(paramType)) {
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
