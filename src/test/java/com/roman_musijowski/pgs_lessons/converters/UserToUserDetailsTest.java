package com.roman_musijowski.pgs_lessons.converters;

import com.roman_musijowski.pgs_lessons.models.User;
import com.roman_musijowski.pgs_lessons.models.security.Role;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserToUserDetailsTest {

    private Converter<User, UserDetails> converter;

    @Before
    public void setUp() throws Exception {
        converter = new UserToUserDetails();
    }

    @Test
    public void convert() {
        String userName = "admin@gmail.com";
        String password = "password";
        String roleName1 = "STUDENT";
        String roleName2 = "ADMIN";

        Role role1 = new Role();
        role1.setRoleId(1);
        role1.setRole(roleName1);

        Role role2 = new Role();
        role2.setRoleId(2);
        role2.setRole(roleName2);

        User user = new User();
        user.setUserName(userName);
        user.setEncryptedPassword(password);
        user.addRole(role1);
        user.addRole(role2);

        UserDetails userDetails = converter.convert(user);

        assertEquals(userDetails.getUsername(), userName);
        assertEquals(userDetails.getPassword(), password);
        for (Object o : userDetails.getAuthorities()) {
            System.out.println(o.toString());
        }

        assertEquals(2, userDetails.getAuthorities().size());
    }
}