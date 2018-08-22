package com.devband.tronwalletforandroid;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.tron.common.utils.FileUtil;

import java.io.File;

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
    }

    @Test
    public void test() {
        Assert.assertEquals(1, 1);
    }
}