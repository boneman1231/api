package org.redcenter.api.osgi;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.redcenter.api.vo.ApiAttribute;
import org.redcenter.api.vo.ApiInvokeRequest;
import org.redcenter.api.vo.ApiMethod;

@Path("/")
public interface IRestService {
	
	@GET
	@Path("/getClasses")
	@Produces("application/json")
	public abstract ArrayList<ApiAttribute> getClasses();

	@GET
	@Path("/getMethods")
	@Produces("application/json")
	public abstract ArrayList<ApiMethod> getMethods(@QueryParam("className") String className);

	@POST
	@Path("/invoke")
	@Produces("application/json")
	public abstract ApiAttribute invoke(ApiInvokeRequest request);

}