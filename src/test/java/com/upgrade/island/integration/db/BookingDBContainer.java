package com.upgrade.island.integration.db;

import org.testcontainers.containers.PostgreSQLContainer;

public class BookingDBContainer extends PostgreSQLContainer<BookingDBContainer> {

    private static final String IMAGE_VERSION = "postgres:11.1";
    private static BookingDBContainer container;

    private BookingDBContainer() {
        super(IMAGE_VERSION);
    }

    public static BookingDBContainer getInstance() {
        if (container == null) {
            container = new BookingDBContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}

