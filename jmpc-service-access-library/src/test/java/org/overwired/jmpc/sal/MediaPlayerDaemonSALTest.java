package org.overwired.jmpc.sal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.bff.javampd.Admin;
import org.bff.javampd.MPD;
import org.bff.javampd.exception.MPDConnectionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests the MediaPlayerDaemonSAL class.
 */
@RunWith(MockitoJUnitRunner.class)
public class MediaPlayerDaemonSALTest {

    @Mock
    private MPD.Builder mockBuilder;
    @Mock
    private MPD mockMPD;
    private MediaPlayerDaemonSAL sal;

    @Before
    public void setup() throws Exception {
        when(mockBuilder.build()).thenReturn(mockMPD);

        sal = new MediaPlayerDaemonSAL();
        sal.setBuilder(mockBuilder);
    }

    @Test
    public void shouldInvokeBuilderOnlyOnce() throws Exception {
        Admin mockAdmin = mock(Admin.class);
        when(mockMPD.getAdmin()).thenReturn(mockAdmin);
        when(mockMPD.isConnected()).thenReturn(true);

        assertEquals("wrong admin object", mockAdmin, sal.getAdmin());
        sal.getAdmin();
        verify(mockBuilder).build();
    }

    @Test
    public void shouldInvokeBuilderAgainWhenDisconnected() throws Exception {
        Admin mockAdmin = mock(Admin.class);
        when(mockMPD.getAdmin()).thenReturn(mockAdmin);
        when(mockMPD.isConnected()).thenReturn(false).thenReturn(true);

        assertEquals("wrong admin object", mockAdmin, sal.getAdmin());
        sal.getAdmin();
        verify(mockBuilder, times(2)).build();
    }

}
