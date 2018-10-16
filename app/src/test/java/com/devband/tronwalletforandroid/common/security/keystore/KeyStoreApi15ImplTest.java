package com.devband.tronwalletforandroid.common.security.keystore;

import com.devband.tronwalletforandroid.common.CustomPreference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class KeyStoreApi15ImplTest {

    private static final byte[] KEY = {
            0, -120, 3, 77, 73, 94, 41, 9, 3, 20, 52, 7, 102, 109, -116, 8
    };

    private static final String TEST_PLAIN_TEXT = "sdfkljasldfjdsafsaduf90sdaf790sd7f90saduf90sdayf90sadyf9ysdafy0as";
    private static final String TEST_KEY_ALIAS = "test";

    KeyStoreApi15Impl keyStoreApi15;

    @Mock
    CustomPreference customPreference;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        keyStoreApi15 = new KeyStoreApi15Impl(customPreference);
        keyStoreApi15.init();

        doReturn(KEY).when(customPreference).retrieveEncryptedIv(TEST_KEY_ALIAS);
    }

    @Test
    public void encryptText() {
        String encryptText = keyStoreApi15.encryptString(TEST_PLAIN_TEXT, TEST_KEY_ALIAS);
        String decryptText = keyStoreApi15.decryptString(encryptText, TEST_KEY_ALIAS);

        assertNotNull(encryptText);
        assertNotNull(decryptText);
        assertEquals("sdfkljasldfjdsafsaduf90sdaf790sd7f90saduf90sdayf90sadyf9ysdafy0as", decryptText);

        verify(customPreference, times(2)).retrieveEncryptedIv(TEST_KEY_ALIAS);
    }

}