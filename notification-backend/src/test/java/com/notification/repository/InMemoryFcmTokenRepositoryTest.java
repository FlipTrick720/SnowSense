package com.notification.repository;

import com.notification.model.FcmToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryFcmTokenRepositoryTest {

    private InMemoryFcmTokenRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryFcmTokenRepository();
    }

    @Test
    void saveAndExistsAndFindAll_shouldWork() {
        assertFalse(repository.exists("t1"));

        repository.save(new FcmToken("t1"));

        assertTrue(repository.exists("t1"));
        List<FcmToken> all = repository.findAll();
        assertEquals(1, all.size());
        assertEquals("t1", all.get(0).getToken());
    }

    @Test
    void delete_shouldRemoveToken() {
        repository.save(new FcmToken("t1"));
        assertTrue(repository.exists("t1"));

        repository.delete("t1");

        assertFalse(repository.exists("t1"));
        assertTrue(repository.findAll().isEmpty());
    }
}
