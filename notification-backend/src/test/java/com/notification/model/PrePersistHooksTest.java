package com.notification.model;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class PrePersistHooksTest {

    private static Object readField(Object target, String fieldName) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        return f.get(target);
    }

    @Test
    void avalancheData_onCreate_shouldSetCreatedAt() throws Exception {
        AvalancheData data = new AvalancheData();
        assertNull(readField(data, "createdAt"));

        Method m = AvalancheData.class.getDeclaredMethod("onCreate");
        m.setAccessible(true);
        m.invoke(data);

        assertNotNull(readField(data, "createdAt"));
    }

    @Test
    void weatherData_onCreate_shouldSetCreatedAt() throws Exception {
        WeatherData data = new WeatherData();
        assertNull(readField(data, "createdAt"));

        Method m = WeatherData.class.getDeclaredMethod("onCreate");
        m.setAccessible(true);
        m.invoke(data);

        assertNotNull(readField(data, "createdAt"));
    }

    @Test
    void skiResort_onCreate_shouldSetCreatedAt() throws Exception {
        SkiResort resort = new SkiResort();
        assertNull(readField(resort, "createdAt"));

        Method m = SkiResort.class.getDeclaredMethod("onCreate");
        m.setAccessible(true);
        m.invoke(resort);

        assertNotNull(readField(resort, "createdAt"));
    }

    @Test
    void skiResortAvalancheRegion_onCreate_shouldDefaultPrimaryTrue() throws Exception {
        SkiResortAvalancheRegion mapping = new SkiResortAvalancheRegion();
        assertNull(readField(mapping, "createdAt"));
        assertNull(readField(mapping, "isPrimary"));

        Method m = SkiResortAvalancheRegion.class.getDeclaredMethod("onCreate");
        m.setAccessible(true);
        m.invoke(mapping);

        assertNotNull(readField(mapping, "createdAt"));
        assertEquals(true, readField(mapping, "isPrimary"));
    }

    @Test
    void skiResortLift_onCreate_shouldSetCreatedAtAndLastStatusChange() throws Exception {
        SkiResortLift lift = new SkiResortLift();
        assertNull(readField(lift, "createdAt"));
        assertNull(readField(lift, "lastStatusChange"));

        Method m = SkiResortLift.class.getDeclaredMethod("onCreate");
        m.setAccessible(true);
        m.invoke(lift);

        Object createdAt = readField(lift, "createdAt");
        Object lastStatusChange = readField(lift, "lastStatusChange");
        assertNotNull(createdAt);
        assertNotNull(lastStatusChange);
        assertEquals(createdAt, lastStatusChange);
    }

    @Test
    void skiResortSlope_onCreate_shouldSetCreatedAtAndLastStatusChange() throws Exception {
        SkiResortSlope slope = new SkiResortSlope();
        assertNull(readField(slope, "createdAt"));
        assertNull(readField(slope, "lastStatusChange"));

        Method m = SkiResortSlope.class.getDeclaredMethod("onCreate");
        m.setAccessible(true);
        m.invoke(slope);

        Object createdAt = readField(slope, "createdAt");
        Object lastStatusChange = readField(slope, "lastStatusChange");
        assertNotNull(createdAt);
        assertNotNull(lastStatusChange);
        assertEquals(createdAt, lastStatusChange);
    }
}
