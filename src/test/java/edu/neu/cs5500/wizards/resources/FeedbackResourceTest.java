package edu.neu.cs5500.wizards.resources;

import edu.neu.cs5500.wizards.core.Feedback;
import edu.neu.cs5500.wizards.core.User;
import edu.neu.cs5500.wizards.db.FeedbackDAO;
import edu.neu.cs5500.wizards.db.UserDAO;
import org.apache.commons.lang.RandomStringUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by amala on 18/07/16.
 */
public class FeedbackResourceTest {

    @Mock
    UserDAO userDAO;

    @Mock
    FeedbackDAO feedbackDAO;

    @Mock
    User user;

    @Mock
    Feedback feedback;

    Random rand = new Random();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(userDAO.retrieve(anyString())).thenReturn(user);
    }

    @Test
    public void testCreateFeedback() {
        when(feedbackDAO.create(anyInt(), anyInt(), anyString())).thenReturn(feedback);
        FeedbackResource feedbackResource = new FeedbackResource(feedbackDAO, userDAO);

        Response response = feedbackResource.create(RandomStringUtils.random(6), Mockito.mock(Feedback.class));
        assertEquals(HttpStatus.OK_200, response.getStatus());
        assertEquals(feedback, response.getEntity());

    }

    @Test
    public void testCreateFeedbackWithInvalidUser() {
        String randomUser = RandomStringUtils.random(10);
        when(userDAO.retrieve(anyString())).thenReturn(null);
        FeedbackResource feedbackResource = new FeedbackResource(feedbackDAO, userDAO);

        Response response = feedbackResource.create(randomUser, feedback);
        assertEquals(HttpStatus.NOT_FOUND_404, response.getStatus());
        assertEquals("Error: The user you are trying to create feedback for does not exist", response.getEntity());

    }

    @Test
    public void testGetAllFeedbackForUser() {
        List<Feedback> mockResult = new LinkedList<>();
        mockResult.add(feedback);
        when(feedbackDAO.retrieve(anyInt())).thenReturn(mockResult);
        FeedbackResource feedbackResource = new FeedbackResource(feedbackDAO, userDAO);

        Response response = feedbackResource.getAll(RandomStringUtils.random(5));
        assertEquals(mockResult, response.getEntity());

    }

    @Test
    public void testGetAllFeedbackForInvalidUser() {
        when(userDAO.retrieve(anyString())).thenReturn(null);
        FeedbackResource feedbackResource = new FeedbackResource(feedbackDAO, userDAO);

        Response response = feedbackResource.getAll(RandomStringUtils.random(10));
        assertEquals(HttpStatus.NOT_FOUND_404, response.getStatus());
        assertEquals("Error: User not found", response.getEntity());

    }


    @Test
    public void testGetFeedbackById() {
        when(feedbackDAO.retrieveOne(anyInt())).thenReturn(feedback);
        FeedbackResource feedbackResource = new FeedbackResource(feedbackDAO, userDAO);

        Response response = feedbackResource.getOne(RandomStringUtils.random(5), rand.nextInt());
        assertEquals(HttpStatus.OK_200, response.getStatus());
    }

    @Test
    public void testFetchingFeedbackForDifferentUser() {
        when(feedbackDAO.retrieveOne(anyInt())).thenReturn(feedback);
        when(feedback.getUserId()).thenReturn(rand.nextInt());
        when(user.getId()).thenReturn(rand.nextInt());
        FeedbackResource feedbackResource = new FeedbackResource(feedbackDAO, userDAO);

        Response response = feedbackResource.getOne(RandomStringUtils.random(5), rand.nextInt());
        assertEquals(HttpStatus.FORBIDDEN_403, response.getStatus());
        assertEquals("Forbidden: The feedback requested does not belong to this user", response.getEntity());
    }

    @Test
    public void testFetchingInvalidFeedback() {
        when(feedbackDAO.retrieveOne(anyInt())).thenReturn(null);

        FeedbackResource feedbackResource = new FeedbackResource(feedbackDAO, userDAO);

        Response response = feedbackResource.getOne(RandomStringUtils.random(5), rand.nextInt());
        assertEquals(HttpStatus.NOT_FOUND_404, response.getStatus());
        assertEquals("Error: No feedback matches your request", response.getEntity());
    }

    @Test
    public void testFetchingFeedbackForInvalidUser() {
        when(userDAO.retrieve(anyString())).thenReturn(null);
        FeedbackResource feedbackResource = new FeedbackResource(feedbackDAO, userDAO);

        Response response = feedbackResource.getOne(RandomStringUtils.random(5), rand.nextInt());
        assertEquals(HttpStatus.NOT_FOUND_404, response.getStatus());
        assertEquals("Error: User not found", response.getEntity());

    }

    @Test
    public void testDeletingFeedbackForInvalidUser() {
        when(userDAO.retrieve(anyString())).thenReturn(null);
        FeedbackResource feedbackResource = new FeedbackResource(feedbackDAO, userDAO);

        Response response = feedbackResource.delete(RandomStringUtils.random(5), rand.nextInt(), user);
        assertEquals(HttpStatus.NOT_FOUND_404, response.getStatus());
        assertEquals("Error: User does not exist", response.getEntity());
    }


    @Test
    public void testDeletingInvalidFeedback() {
        FeedbackResource feedbackResource = new FeedbackResource(feedbackDAO, userDAO);

        Response response = feedbackResource.delete(RandomStringUtils.random(5), rand.nextInt(), user);
        assertEquals(HttpStatus.NOT_FOUND_404, response.getStatus());
        assertEquals("Error: Feedback not found", response.getEntity());
    }

    @Test
    public void testDeletingFeedbackForDifferentUser() {
        when(feedback.getUserId()).thenReturn(rand.nextInt());
        when(feedbackDAO.retrieveOne(anyInt())).thenReturn(feedback);
        when(user.getId()).thenReturn(rand.nextInt());
        FeedbackResource feedbackResource = new FeedbackResource(feedbackDAO, userDAO);

        Response response = feedbackResource.delete(RandomStringUtils.random(5), rand.nextInt(), user);
        assertEquals(HttpStatus.FORBIDDEN_403, response.getStatus());
        assertEquals("Forbidden: The feedback requested does not belong to this user", response.getEntity());
    }

    @Test
    public void testDeletingFeedbackWithInvalidCredentials() {
        int random = rand.nextInt();
        when(feedback.getUserId()).thenReturn(random);
        when(feedbackDAO.retrieveOne(anyInt())).thenReturn(feedback);
        when(user.getId()).thenReturn(random);
        FeedbackResource feedbackResource = new FeedbackResource(feedbackDAO, userDAO);

        Response response = feedbackResource.delete(RandomStringUtils.random(5), rand.nextInt(), Mockito.mock(User.class));
        assertEquals(HttpStatus.UNAUTHORIZED_401, response.getStatus());
        assertEquals("Error: Invalid credentials", response.getEntity());
    }

    @Test
    public void testDeletingFeedbackById() {
        when(feedbackDAO.retrieveOne(anyInt())).thenReturn(feedback);
        FeedbackResource feedbackResource = new FeedbackResource(feedbackDAO, userDAO);

        Response response = feedbackResource.delete(RandomStringUtils.random(5), rand.nextInt(), user);
        assertEquals(HttpStatus.NO_CONTENT_204, response.getStatus());
    }
}
