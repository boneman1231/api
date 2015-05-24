package org.redcenter.api;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.redcenter.api.vo.ApiAttribute;
import org.redcenter.api.vo.ApiInvokeRequest;
import org.redcenter.api.vo.ApiNode;

@Path("/")
public class RestService extends ApiController {

	@GET
	@Path("/getClasses")
	@Produces("application/json")
	public ArrayList<ApiAttribute> getClasses() {
		return super.getClasses();
	}

	@GET
	@Path("/getMethods")
	@Produces("application/json")
	public ArrayList<ApiNode> getMethods(String className) {
		return super.getMethods(className);
	}

	@POST
	@Path("/invoke")
	@Produces("application/json")
	public ApiAttribute invoke(ApiInvokeRequest request) {
		return super.invoke(request);
	}

}