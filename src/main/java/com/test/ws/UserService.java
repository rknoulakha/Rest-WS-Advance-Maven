package com.test.ws;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.test.beans.User;
import com.test.business.UserBusiness;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/user")
public class UserService implements ContainerResponseFilter {

	UserBusiness userservice = new UserBusiness();
	
	// Return all the users
	@GET
	public Response getUser(@QueryParam("year") int year, @QueryParam("start") int start, @QueryParam("end") int end) {

		List<User> userOutput;
		if (year > 0) {
			userOutput = userservice.getAllUserForYear(year);
		} else {
			userOutput = userservice.getAllUser();
		}
		if (start >= 0 && end > 0) {
			userOutput = userservice.getAllUsersPagination(start, end);
		}
		return Response.status(Status.OK).entity(userOutput).build();	
		}

	// Return particular user
	@GET
	@Path("/{userId}")
	public Response getUser(@PathParam("userId") long userId) {
		return Response.status(Status.OK).entity(userservice.getUser(userId)).build();	
		}

	// Insert new user and return it
	@Path("/insert")
	@POST
	public Response addUser(User user) {
		userservice.addUser(user);
		return Response.status(Status.CREATED).entity(userservice.getUser(user.getId())).build();
		}

	// Update existing user and return updated user
	@Path("/update/{userId}")
	@PUT
	public Response updateUser(@PathParam("userId") long userId, User user) {
		user.setId(userId);
		userservice.updateUser(user);
		return Response.status(Status.OK).entity(userservice.getUser(user.getId())).build();
		}

	// Delete existing user
	@Path("/delete/{userId}")
	@DELETE
	public Response deleteUser(@PathParam("userId") long userId) {
		userservice.removeUser(userId);
		return Response.status(Status.OK).entity(userId).build();
		}

	// Get address of user
	@Path("/{userId}/address")
	public UserAddressService getUserAddressService() {
		return new UserAddressService();
	}

	
	/*
	    Allow Cors Platform : Otherwise you will not be able to run/test your WS on other port.
	    for eg. : Your service is host on 8080 port and if you don't override the method the it
	    will run on the same port so you can test it via Postman or via creating front-end on 
	    same port but if you are using front end on other port like node then it will give you 
	    cors platform error.
	*/
	 
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
		responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
		responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
		responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
	}
}
