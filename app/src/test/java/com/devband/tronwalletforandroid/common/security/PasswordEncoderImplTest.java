package com.devband.tronwalletforandroid.common.security;

import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordEncoderImplTest {

    PasswordEncoderImpl passwordEncoder = new PasswordEncoderImpl();

    @Test
    public void encode() {
        String encodedPassword = passwordEncoder.encode("12345678", "sfsa89fysa98yfsydf8");

        assertNotNull(encodedPassword);
    }

    @Test
    public void matches() {
    }
}