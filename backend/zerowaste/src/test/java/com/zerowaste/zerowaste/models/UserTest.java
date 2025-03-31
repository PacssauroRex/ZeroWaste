package com.zerowaste.zerowaste.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.zerowaste.models.user.User;
import com.zerowaste.models.user.UserRole;

class UserTest {
    
    @Test
    void createUserFullConstructorTest() {
        //Criação dos dados
        Long userId = 1L;
        String nome = "arthur";
        String email = "arthur@gmail.com";
        String senha = "11223344";
        UserRole role = UserRole.ADMIN;

        //Uso do construtor
        User user = new User(
            userId,
            nome,
            email,
            senha, 
            role,
            LocalDate.now(),
            null,
            null
        );

        //Validação
        assertEquals(userId, user.getId());
        assertEquals(nome, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(senha, user.getPassword());
        assertEquals(role, user.getRole());
        assertEquals(LocalDate.now(), user.getCreatedAt());
        assertNull(user.getUpdatedAt());
        assertNull(user.getDeletedAt());
    }

    @Test
    void createUserEmptyConstructorTest() {
        //Criação dos dados
        Long userId = 1L;
        String nome = "arthur";
        String email = "arthur@gmail.com";
        String senha = "11223344";
        UserRole role = UserRole.ADMIN;

        //Uso do construtor
        User user = new User();
        user.setId(userId);
        user.setName(nome);
        user.setEmail(email);
        user.setPassword(senha);
        user.setRole(role);
        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(null);
        user.setDeletedAt(null);

        //Validação
        assertEquals(userId, user.getId());
        assertEquals(nome, user.getName());
        assertEquals(email, user.getUsername());
        assertEquals(senha, user.getPassword());
        assertEquals(role, user.getRole());
        assertEquals(LocalDate.now(), user.getCreatedAt());
        assertNull(user.getUpdatedAt());
        assertNull(user.getDeletedAt());
    }
    
    @Test
    void isAccountNonExpiredTest() {
        User user = new User();

        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void isAccountNonLockedTest() {
        User user = new User();

        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpiredTest() {
        User user = new User();

        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void isEnabledTest() {
        User user = new User();

        assertTrue(user.isEnabled());
    }

    @Test
    void getUserAuthority() {
        User user = new User();
        user.setRole(UserRole.USER);

        assertEquals(List.of(new SimpleGrantedAuthority("ROLE_USER")), user.getAuthorities());
    }

    @Test
    void getAdminAuthority() {
        User user = new User();
        user.setRole(UserRole.ADMIN);

        assertEquals(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")), user.getAuthorities());
    }

    @Test
    void hashCodeEqualityTest() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test@example.com");
        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("test@example.com");

        assertTrue(user1.equals(user2));
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void hashCodeDifferentObjectsTest() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("teste@example.com");
        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("outro@example.com");

        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void hashCodeWithNullValuesTest() {
        User user1 = new User();
        User user2 = new User();

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void hashCodeWithDifferentIdOrEmailTest() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test@example.com");
        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("test@example.com");

        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void equalsReflexiveTest() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        assertTrue(user.equals(user));
    }

    @Test
    void equalsSymmetricTest() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test@example.com");
        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("test@example.com");

        assertTrue(user1.equals(user2));
        assertTrue(user2.equals(user1));
    }

    @Test
    void equalsTransitiveTest() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test@example.com");
        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("test@example.com");
        User user3 = new User();
        user3.setId(1L);
        user3.setEmail("test@example.com");
       
        assertTrue(user1.equals(user2));
        assertTrue(user2.equals(user3));
        assertTrue(user1.equals(user3));
    }

    @Test
    void equalsConsistentTest() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test@example.com");
        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("test@example.com");

        assertTrue(user1.equals(user2));
        assertTrue(user1.equals(user2)); //Chamada sequencial deve retornar o mesmo valor
    }

    @Test
    void equalsWithNullTest() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        
        assertFalse(user.equals(null));
    }

    @Test
    void equalsDifferentClassTest() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        String otherObject = "Some other object";

        assertFalse(user.equals(otherObject));
    }

    @Test
    void equalsWithDifferentIdTest() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test@example.com");
        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("test@example.com");

        assertFalse(user1.equals(user2));
    }

    @Test
    void equalsWithDifferentEmailTest() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("teste@example.com");
        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("outro@example.com");

        assertFalse(user1.equals(user2));
    }

    @Test
    void equalsWithNullIdTest() {
        User user1 = new User();
        user1.setId(null);
        user1.setEmail("test@example.com");
        User user2 = new User();
        user2.setId(null);
        user2.setEmail("test@example.com");

        assertTrue(user1.equals(user2));
    }

    @Test
    void equalsWithNullEmailTest() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail(null);
        User user2 = new User();
        user2.setId(1L);
        user2.setEmail(null);

        assertTrue(user1.equals(user2));
    }
}
