package com.devband.tronwalletforandroid;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;

import com.devband.tronwalletforandroid.tron.Tron;
import com.devband.tronwalletforandroid.tron.grpc.GrpcClient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.tron.common.utils.ByteArray;
import org.tron.common.utils.FileUtil;
import org.tron.protos.Protocol;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Environment.class, File.class, FileUtil.class})
public class ExampleUnitTest {

    @Mock
    private File directory;

    @Mock
    private Context context;

    @Mock
    private Resources resources;

    @Before
    public void setUp() {
        mockStatic(Environment.class, File.class, FileUtil.class);

        when(Environment.getExternalStorageDirectory())
                .thenReturn(directory);

        when(directory.mkdirs())
                .thenReturn(true);

        when(context.getResources())
                .thenReturn(resources);

        when(context.getResources().getStringArray(R.array.fullnode_ip_list))
                .thenReturn(new String[] {"47.91.216.69:50051"});

        PowerMockito.when(FileUtil.saveData(directory, "3b3ac61c48c8dbc7f37df7fb23f47473", false))
                .thenAnswer(invocation -> {
                    System.out.println(invocation.getArgumentAt(1, String.class));
                    return true;
                });
    }

    @Test
    public void registerWalletTest() {
        Tron tron = Tron.getInstance(context);

        assertEquals(tron.registerWaller(null), Tron.ERROR_INVALID_PASSWORD);
        assertEquals(tron.registerWaller(""), Tron.ERROR_INVALID_PASSWORD);
        assertEquals(tron.registerWaller("aaaa"), Tron.ERROR_INVALID_PASSWORD);
        assertEquals(tron.registerWaller("aaaaaaaaa"), Tron.SUCCESS);
    }

    @Test
    public void tronAccountTest() {
        GrpcClient grpcClient = new GrpcClient("47.91.216.69:50051");

        byte[] addressBytes = ByteArray.fromHexString("A0B4750E2CD76E19DCA331BF5D089B71C3C2798548");
        Protocol.Account account = grpcClient.queryAccount(addressBytes);

        assertNotNull(account);

        assertEquals(new String(account.getAccountName().toByteArray()), "Zion");
    }
}