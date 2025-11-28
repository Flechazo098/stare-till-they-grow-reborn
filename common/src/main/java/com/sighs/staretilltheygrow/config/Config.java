package com.sighs.staretilltheygrow.config;


public class Config {
    public static ConfigService INSTANCE;

    public static void init(ConfigService service) {
        INSTANCE = service;
    }

    public static ConfigService get() {
        return INSTANCE;
    }
}