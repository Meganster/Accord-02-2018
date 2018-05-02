package ru.mail.park.dao;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.mail.park.dao.UserDAO;
import ru.mail.park.models.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserDAOTest {
    private static final String LOGIN = "example_login";
    @SuppressWarnings("unused")
    private static final String LOGIN_FIRST = "example_login_1";
    @SuppressWarnings("unused")
    private static final String LOGIN_SECOND = "example_login_2";
    private static final String PASSWORD = "example_password";
    private static final String UPDATED_PASSWORD = "example_updated_password";
    private static final String UPDATED_AVATAR = "new_avatar.png";
    private static final int USER_PER_PAGE = 10;

    @Autowired
    private UserDAO userService;


    @Test
    public void testSimpleCreateUser() {
        final User userToRegister = new User("", LOGIN, PASSWORD, 0);

        if (userService.register(userToRegister)) {
            final User createdUser = userService.getUser(LOGIN);

            assertNotNull(createdUser);
            assertEquals(LOGIN, createdUser.getEmail());
            assertEquals(PASSWORD, createdUser.getPassword());
        } else {
            assert false;
        }
    }

    @Test
    public void testCreateDuplicatedUser() {
        final User user = new User("", LOGIN, PASSWORD, 0);
        if (userService.register(user)) {
            final User duplicatedUser = new User("", LOGIN, PASSWORD, 0);
            assert (!userService.register(duplicatedUser));
        } else {
            assert false;
        }
    }

    @Test
    public void testLoginUser() {
        final User userToLogin = new User("", LOGIN, PASSWORD, 0);
        if (userService.register(userToLogin) &&
                userService.login(userToLogin)) {
            final User createdUser = userService.getUser(LOGIN);
            assertNotNull(createdUser);
        } else {
            assert false;
        }
    }

    @Test
    public void testGetUser() {
        final User user = new User("", LOGIN, PASSWORD, 0);
        if (userService.register(user)) {
            final User existUser = userService.getUser(user.getEmail());
            assertNotNull(existUser);
        } else {
            assert false;
        }
    }

    @Test
    public void testUpdatetUser() {
        final User user = new User("", LOGIN, PASSWORD, 0);
        if (userService.register(user)) {
            user.setPassword(UPDATED_PASSWORD);
            final Boolean updatedUser = userService.updateUser(user);

            assertNotNull(updatedUser);
            assertEquals(true, updatedUser);
        } else {
            assert false;
        }
    }

    @Test
    public void testUpdateAvatar() {
        final User user = new User("", LOGIN, PASSWORD, 0);
        if (userService.register(user)) {
            user.setAvatar(UPDATED_AVATAR);
            final Boolean updatedUser = userService.updateAvatar(user);

            assertNotNull(updatedUser);
            assertEquals(true, updatedUser);
        } else {
            assert false;
        }
    }

    /*@Test
    public void testGetSortedUsersInfoByRating() {
        final User user0 = new User(LOGIN, LOGIN, PASSWORD, 9998);
        final User user1 = new User(LOGIN_FIRST, LOGIN_FIRST, PASSWORD, 9999);
        final User user2 = new User(LOGIN_SECOND, LOGIN_SECOND, PASSWORD, 10000);

        if (userService.register(user0) &&
                userService.register(user1) &&
                userService.register(user2)) {
            final ArrayList<User> listOfUsers = (ArrayList<User>) userService.getSortedUsersInfoByRating(USER_PER_PAGE, 1);

            assertNotNull(listOfUsers);
            assertEquals(listOfUsers.get(0).getNickname(), user2.getNickname());
            assertEquals(listOfUsers.get(1).getNickname(), user1.getNickname());
            assertEquals(listOfUsers.get(2).getNickname(), user0.getNickname());
        } else {
            assert false;
        }
    }*/

    @Test
    public void testGetLastPage() {
        final User user = new User("", LOGIN, PASSWORD, 0);
        if (userService.register(user)) {
            final Integer lastPage = userService.getLastPage(USER_PER_PAGE);

            assertNotNull(lastPage);
            assertEquals(lastPage.toString(), "1");
        } else {
            assert false;
        }
    }
}