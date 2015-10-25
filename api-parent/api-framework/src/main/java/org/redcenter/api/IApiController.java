package org.redcenter.api;

import java.util.ArrayList;
import java.util.Map;

import org.redcenter.api.vo.ApiAttribute;
import org.redcenter.api.vo.ApiInvokeRequest;
import org.redcenter.api.vo.ApiMethod;

public interface IApiController {

	public abstract Map<String, Class<?>> getMap();

	public abstract ArrayList<ApiAttribute> getClasses();

	public abstract ArrayList<ApiMethod> getMethods(String className);

	public abstract ApiAttribute invoke(ApiInvokeRequest request);

}