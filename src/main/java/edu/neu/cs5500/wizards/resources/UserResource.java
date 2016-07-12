package edu.neu.cs5500.wizards.resources;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import edu.neu.cs5500.wizards.core.User;
import edu.neu.cs5500.wizards.db.UserDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserDAO userDao;


    public UserResource(UserDAO userDao) {
        this.userDao = userDao;
    }

    //Create user
    @POST
    @Timed
    @UnitOfWork
    @ExceptionMetered
    public Response post(User user) {
        if (user == null) {
            return Response
                    .status(HttpStatus.BAD_REQUEST_400)
                    .entity("{\"Error\": \"User is empty\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        else if (userDao.retrieve(user.getUsername()) != null) {
            return Response
                    .status(HttpStatus.BAD_REQUEST_400)
                    .entity("{\"Error\": \"User already exists!\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        User createdUser = userDao.create(user);
        return Response.ok(createdUser).build();
    }


    //Update an existing user
    @PUT
    @Timed
    @UnitOfWork
    @Path("/{username}")
    @ExceptionMetered
    public Response put(@PathParam("username") String username, User user) {
        User existingUser = userDao.retrieve(username);
        if(existingUser == null){
            return Response
                    .status(HttpStatus.BAD_REQUEST_400)
                    .entity("{\"Error\": \"Invalid username, update failed\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        if (user.getPassword() != null) {
            String password = user.getPassword();
            existingUser.setPassword(password);
        }
        if (user.getFirstname() != null) {
            existingUser.setFirstname(user.getFirstname());
        }
        if (user.getLastname() != null) {
            existingUser.setLastname(user.getLastname());
        }
        if (user.getAddress() != null) {
            existingUser.setAddress(user.getAddress());
        }
        if (user.getUsername() != null) {
            return Response
                    .status(HttpStatus.BAD_REQUEST_400)
                    .entity("{\"Error\": \"Username cannot be changed\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        userDao.update(existingUser.getUsername(), existingUser.getPassword(), existingUser.getFirstname(), existingUser.getLastname(), existingUser.getAddress());

        return Response.ok(existingUser).build();
    }
	
    @GET
    @Path("/{username}")
    @Timed
    @UnitOfWork
    @ExceptionMetered
    //Get user by username
    public Response get(@PathParam("username") String username, @Auth User auth_user) {
        System.out.println(auth_user);
        User user = userDao.retrieve(username);
        if (user == null) {
            return Response
                    .status(HttpStatus.BAD_REQUEST_400)
                    .entity("{\"Error\": \"User not found\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        return Response.ok(user).build();
    }

    @DELETE
    @Timed
    @UnitOfWork
    @ExceptionMetered
    /* delete user by username*/
    public Response delete(User existingUser) {
        if(existingUser == null || userDao.retrieve(existingUser.getUsername()) == null){
            return Response
                    .status(HttpStatus.BAD_REQUEST_400)
                    .entity("{\"Error\": \"User not found\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        if (existingUser.getUsername().equals("admin")) {
            return Response
                    .status(HttpStatus.BAD_REQUEST_400)
                    .entity("{\"Error\": \"Admin cannot be deleted\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        userDao.delete(existingUser);

        return Response.status(204).build();
    }

    @OPTIONS
    @Timed
    @UnitOfWork
    @ExceptionMetered
    public void options() { }
}
