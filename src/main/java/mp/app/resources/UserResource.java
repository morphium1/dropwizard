package mp.app.resources;

import mp.app.core.User;
import mp.app.service.user.UserService;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Path("/{id}")
    public User getUserById(@PathParam("id") String userId,  @Context ContainerRequestContext request, @HeaderParam("Request-ID") String requestID) {
        return userService.getUserById(userId);
    }

    @PUT
    public Response createUser(@Valid User user) {
        userService.addUser(user);
        return Response.created(UriBuilder.fromPath("/api/user/{id}").build(user.getId())).build();
    }

}
