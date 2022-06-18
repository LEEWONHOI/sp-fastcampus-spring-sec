package com.sp.fc.user.service;

import com.sp.fc.user.domain.Authority;
import com.sp.fc.user.domain.User;
import com.sp.fc.user.service.helper.UserTestHelper;
import com.sp.fc.user.service.helper.WithUserTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *
 * 사용자 생성
 * 이름 수정
 * 권한 부여
 * 권한 취소
 * email 검색
 * role 중복해서 추가되지 않는다.
 * email 이 중복해서 들어가는가?
 *
 */

@DataJpaTest
public class UserTest extends WithUserTest {

    @BeforeEach
    protected void before() {
        prepareUserServices();
    }

    @Test
    @DisplayName("1. 사용자 생성")
    public void test_1() throws Exception {
        userTestHelper.createUser(school, "user1");
        List<User> list = StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
        assertEquals(1, list.size());
        UserTestHelper.assertUser(school, list.get(0), "user1");
    }
    
    @Test
    @DisplayName("2. 이름 수정")        
    public void test_2() throws Exception {
        User user = userTestHelper.createUser(school, "user1");
        userService.updateUsername(user.getUserId(), "user2");
        List<User> list = StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
        assertEquals("user2", list.get(0).getName());
    }
    
    @Test
    @DisplayName("3. 권한 부여")        
    public void test_3() throws Exception {
        User user = userTestHelper.createUser(school, "user1", Authority.ROLE_STUDENT);
        userService.addAuthority(user.getUserId(), Authority.ROLE_TEACHER);
        User savedUser = userService.findUser(user.getUserId()).get();
        userTestHelper.assertUser(school, savedUser, "user1", Authority.ROLE_STUDENT, Authority.ROLE_TEACHER);
    }
    
    @Test
    @DisplayName("4. 권한 취소")        
    public void test_4() throws Exception {
        User user = userTestHelper.createUser(school, "admin", Authority.ROLE_STUDENT, Authority.ROLE_TEACHER);
        userService.removeAuthority(user.getUserId(), Authority.ROLE_STUDENT);
        User savedUser = userService.findUser(user.getUserId()).get();
        userTestHelper.assertUser(school, savedUser, "admin", Authority.ROLE_TEACHER);
    }

    @Test
    @DisplayName("5. email 검색")
    public void test_5() {
        User user1 = userTestHelper.createUser(school, "user1");
        User foundUser = (User) userSecurityService.loadUserByUsername("user1@test.com");
        userTestHelper.assertUser(school, foundUser, "user1");
    }
   
    @Test
    @DisplayName("6. role 중복해서 추가되지 않는다")
    public void test_6() throws Exception {
        User user1 = userTestHelper.createUser(school, "user1", Authority.ROLE_STUDENT);
        userService.addAuthority(user1.getUserId(), Authority.ROLE_STUDENT);
        userService.addAuthority(user1.getUserId(), Authority.ROLE_STUDENT);
        User savedUser = userService.findUser(user1.getUserId()).get();
        assertEquals(1, savedUser.getAuthorities().size());
        userTestHelper.assertUser(school, savedUser, "user1", Authority.ROLE_STUDENT);
    }

    @Test
    @DisplayName("7. email이 중복되어서 들어가는가?")
    public void test_7() throws Exception {
        userTestHelper.createUser(school, "user1");
        assertThrows(DataIntegrityViolationException.class, () -> {
            userTestHelper.createUser(school, "user1");
        });
    }

}








