package mp.app.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import mp.app.core.Email;
import mp.app.core.User;
import mp.app.service.user.UserService;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserResourceTest {

    private static UserService userServiceMock = mock(UserService.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new UserResource(userServiceMock))
            .build();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetUserById() throws Exception {
        when(userServiceMock.getUserById("123")).thenReturn(createTestUser());

        Invocation invocation = resources.client().target("/user/123").request().buildGet();

        User user = invocation.invoke(User.class);

        assertThat(user.getLastname(),is("Morales"));
    }

    @Test
    public void testCreateUser() throws Exception {

        Response response = resources.client().target("/user").request().put(Entity.entity(createTestUser(), MediaType.APPLICATION_JSON));

        assertThat(response.getStatus(), is(201));


        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userServiceMock).addUser(userCaptor.capture());

        assertThat(userCaptor.getValue().getLastname(), is("Morales"));

    }

    private User createTestUser() {
        User user = new User("123","Morales");
        user.setFirstname("Pedro");
        user.setEmail(new Email("pedro.morales@dirtybastards.com"));
        return user;
    }
}