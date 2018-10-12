package com.devband.tronwalletforandroid.common.security;

import android.text.TextUtils;

import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.common.CustomPreference;
import com.devband.tronwalletforandroid.common.security.keystore.KeyStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
        TextUtils.class,
        BCrypt.class
})
public class PasswordEncoderImplTest {

    private static final String TESTABLE_PASSWORD = "test";
    private static final String ENCODED_PASSWORD = "EncodedPassword";
    private static final String SALT = "$2a$30$qu2FXZwTpBX926bpJl98Ce";

    private PasswordEncoderImpl passwordEncoder;

    @Mock
    private CustomPreference customPreference;

    @Mock
    private KeyStore keyStore;

    @Mock
    private UpdatableBCrypt updatableBCrypt;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockStatic(TextUtils.class);

        when(TextUtils.isEmpty(any(CharSequence.class))).thenAnswer(invocation -> {
            CharSequence a = (CharSequence) invocation.getArguments()[0];

            if (a == null || a.length() == 0) {
                return true;
            }

            return false;
        });

        doReturn(ENCODED_PASSWORD).when(updatableBCrypt).hash(TESTABLE_PASSWORD, SALT);
        doReturn(true).when(keyStore).createKeys(anyString());
        doReturn(SALT).when(customPreference).getSalt();
        doReturn(SALT).when(keyStore).decryptString(SALT, Constants.ALIAS_SALT);
        doReturn(ENCODED_PASSWORD).when(keyStore).encryptString(ENCODED_PASSWORD, Constants.ALIAS_PASSWORD_KEY);

        passwordEncoder = new PasswordEncoderImpl(customPreference, keyStore, updatableBCrypt);
    }

    @Test
    public void encodeTestSuccess() {
        String result = passwordEncoder.encode(TESTABLE_PASSWORD);

        assertNotNull(result);
        assertEquals(ENCODED_PASSWORD, result);
    }

    @Test
    public void matches() {
    }
}