package org.redcenter.api.osgi;

import org.redcenter.api.ApiController;
import org.redcenter.api.IApiController;

/**
 * The Class RestService.
 */
public class RestService extends ApiController implements IRestService {
	
	public void bind(IApiController apiController) {
		map.putAll(apiController.getMap());
	}

	public void unbind(IApiController apiController) {
		for (String key:apiController.getMap().keySet()) {
			map.remove(key);
		}
	}
}