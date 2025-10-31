package com.notification.repository;

import com.notification.model.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryNotificationRepositoryTest {

    private InMemoryNotificationRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryNotificationRepository();
    }

    @Test
    void save_shouldStoreNotificationCorrectly() {
        // Arrange
        Notification notification = new Notification(
            "test-id-1",
            "Test Title",
            "Test Message",
            LocalDateTime.now()
        );

        // Act
        Notification result = repository.save(notification);

        // Assert
        assertNotNull(result);
        assertEquals("test-id-1", result.getId());
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Message", result.getMessage());
        assertNotNull(result.getTimestamp());
    }

    @Test
    void save_shouldUpdateExistingNotification() {
        // Arrange
        Notification notification1 = new Notification(
            "test-id-1",
            "Original Title",
            "Original Message",
            LocalDateTime.now()
        );
        repository.save(notification1);

        Notification notification2 = new Notification(
            "test-id-1",
            "Updated Title",
            "Updated Message",
            LocalDateTime.now()
        );

        // Act
        repository.save(notification2);
        Optional<Notification> result = repository.findById("test-id-1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Updated Title", result.get().getTitle());
        assertEquals("Updated Message", result.get().getMessage());
    }

    @Test
    void findAll_shouldReturnAllStoredNotifications() {
        // Arrange
        Notification notification1 = new Notification(
            "id-1",
            "Title 1",
            "Message 1",
            LocalDateTime.of(2024, 1, 1, 10, 0)
        );

        Notification notification2 = new Notification(
            "id-2",
            "Title 2",
            "Message 2",
            LocalDateTime.of(2024, 1, 2, 15, 30)
        );

        Notification notification3 = new Notification(
            "id-3",
            "Title 3",
            "Message 3",
            LocalDateTime.of(2024, 1, 3, 20, 45)
        );

        repository.save(notification1);
        repository.save(notification2);
        repository.save(notification3);

        // Act
        List<Notification> result = repository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(n -> n.getId().equals("id-1")));
        assertTrue(result.stream().anyMatch(n -> n.getId().equals("id-2")));
        assertTrue(result.stream().anyMatch(n -> n.getId().equals("id-3")));
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoNotifications() {
        // Act
        List<Notification> result = repository.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void threadSafety_shouldHandleConcurrentSaveOperations() throws InterruptedException {
        // Arrange
        int threadCount = 10;
        int notificationsPerThread = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        // Act
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < notificationsPerThread; j++) {
                        Notification notification = new Notification(
                            "thread-" + threadId + "-id-" + j,
                            "Title " + threadId + "-" + j,
                            "Message " + threadId + "-" + j,
                            LocalDateTime.now()
                        );
                        repository.save(notification);
                        successCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        // Assert
        List<Notification> allNotifications = repository.findAll();
        assertEquals(threadCount * notificationsPerThread, successCount.get());
        assertEquals(threadCount * notificationsPerThread, allNotifications.size());
    }

    @Test
    void threadSafety_shouldHandleConcurrentReadOperations() throws InterruptedException {
        // Arrange
        for (int i = 0; i < 5; i++) {
            repository.save(new Notification(
                "id-" + i,
                "Title " + i,
                "Message " + i,
                LocalDateTime.now()
            ));
        }

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);

        // Act
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    List<Notification> notifications = repository.findAll();
                    if (notifications.size() == 5) {
                        successCount.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        // Assert
        assertEquals(threadCount, successCount.get());
    }
}
