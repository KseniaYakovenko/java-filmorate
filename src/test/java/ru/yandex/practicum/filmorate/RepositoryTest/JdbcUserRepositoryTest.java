package ru.yandex.practicum.filmorate.RepositoryTest;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.JdbcUserRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@JdbcTest
@Import(JdbcUserRepository.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JdbcUserRepositoryTest {

    private static final String TEST_USER_1_E_MAIL = "TEST_USER_1_E_MAIL";
    private static final String TEST_USER_1_LOGIN = "TEST_USER_1_LOGIN ";
    private static final String TEST_USER_1_NAME = "TEST_USER_1_NAME";
    private static final LocalDate TEST_USER_1_BIRTHDAY = LocalDate.of(2001, 1, 1);

    private static final String TEST_USER_2_E_MAIL = "TEST_USER_2_E_MAIL";
    private static final String TEST_USER_2_LOGIN = "TEST_USER_2_LOGIN ";
    private static final String TEST_USER_2_NAME = "TEST_USER_2_NAME";
    private static final LocalDate TEST_USER_2_BIRTHDAY = LocalDate.of(2002, 2, 2);

    private static final String TEST_USER_3_E_MAIL = "TEST_USER_3_E_MAIL";
    private static final String TEST_USER_3_LOGIN = "TEST_USER_3_LOGIN ";
    private static final String TEST_USER_3_NAME = "TEST_USER_3_NAME";
    private static final LocalDate TEST_USER_3_BIRTHDAY = LocalDate.of(2003, 3, 3);


    private final UserRepository userRepository;

    @Test
    public void testSaveAndGetAllUser() {
        User testUser = getTestUser1();
        User savedUser = userRepository.save(testUser);
        Long testId = savedUser.getId();
        List<User> usersFromBd = userRepository.getAll();
        User userFromBd = usersFromBd
                .stream()
                .filter(user -> Objects.equals(user.getId(), testId))
                .findFirst().orElse(null);
        Assertions.assertThat(userFromBd)
                .usingRecursiveComparison()
                .isEqualTo(testUser);
    }

    @Test
    public void testAddAndDeleteFriend() {
        User testUser1 = getTestUser1();
        User testUser2 = getTestUser2();
        User userFromDb1 = userRepository.save(testUser1);
        User userFromDb2 = userRepository.save(testUser2);

        Long user1Id = userFromDb1.getId();
        Long user2Id = userFromDb2.getId();

        userRepository.addFriend(user1Id, user2Id);

        Set<User> friendsForUser1 = userRepository.getFriends(user1Id);
        Set<User> friendsForUser2 = userRepository.getFriends(user2Id);

        Assertions.assertThat(friendsForUser1).contains(userFromDb2);
        Assertions.assertThat(friendsForUser2).doesNotContain(userFromDb1);

        User friendForUser1 = friendsForUser1.stream().findFirst().orElseThrow(AssertionError::new);
        userRepository.deleteFriend(user1Id, friendForUser1.getId());
        Set<User> friendsForUser1AfterDelete = userRepository.getFriends(user1Id);
        Assertions.assertThat(friendsForUser1AfterDelete).doesNotContain(userFromDb2);
    }

    @Test
    public void testCommonFriend() {
        User testUser1 = getTestUser1();
        User testUser2 = getTestUser2();
        User testUser3 = getTestUser3();
        User userFromDb1 = userRepository.save(testUser1);
        User userFromDb2 = userRepository.save(testUser2);
        User userFromDb3 = userRepository.save(testUser3);

        Long user1Id = userFromDb1.getId();
        Long user2Id = userFromDb2.getId();
        Long user3Id = userFromDb3.getId();

        userRepository.addFriend(user1Id, user3Id);
        userRepository.addFriend(user2Id, user3Id);

        Set<User> commonFriends = userRepository.getCommonFriends(user1Id, user2Id);

        Assertions.assertThat(commonFriends).containsOnly(userFromDb3);
    }

    static User getTestUser1() {
        User user = new User();
        user.setEmail(TEST_USER_1_E_MAIL);
        user.setLogin(TEST_USER_1_LOGIN);
        user.setName(TEST_USER_1_NAME);
        user.setBirthday(TEST_USER_1_BIRTHDAY);
        return user;
    }

    static User getTestUser2() {
        User user = new User();
        user.setEmail(TEST_USER_2_E_MAIL);
        user.setLogin(TEST_USER_2_LOGIN);
        user.setName(TEST_USER_2_NAME);
        user.setBirthday(TEST_USER_2_BIRTHDAY);
        return user;
    }

    static User getTestUser3() {
        User user = new User();
        user.setEmail(TEST_USER_3_E_MAIL);
        user.setLogin(TEST_USER_3_LOGIN);
        user.setName(TEST_USER_3_NAME);
        user.setBirthday(TEST_USER_3_BIRTHDAY);
        return user;
    }

}