package org.overwired.jmpc.sal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.bff.javampd.Admin;
import org.bff.javampd.Database;
import org.bff.javampd.MPD;
import org.bff.javampd.Player;
import org.bff.javampd.Playlist;
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
        when(mockMPD.isConnected()).thenReturn(true).thenReturn(false).thenReturn(true);

        assertEquals("wrong admin object", mockAdmin, sal.getAdmin());
        sal.getAdmin();
        sal.getAdmin();
        sal.getAdmin();
        verify(mockBuilder, times(2)).build();
    }

    @Test(expected = MPDConnectionException.class)
    public void shouldPropagateMPDConnectionException() throws Exception {
        when(mockBuilder.build()).thenThrow(new MPDConnectionException("intentional test exception - ignore"));

        sal.getAdmin();
    }

    @Test
    public void testGetDatabase() throws MPDConnectionException {
        Database mockDatabase = mock(Database.class);
        when(mockMPD.getDatabase()).thenReturn(mockDatabase);

        assertEquals("wrong database object returned", mockDatabase, sal.getDatabase());
    }

    @Test
    public void testGetPlayer() throws Exception {
        Player mockPlayer = mock(Player.class);
        when(mockMPD.getPlayer()).thenReturn(mockPlayer);
        
        assertEquals(mockPlayer, sal.getPlayer());
    }
    
    
    @Test
    public void testGetPlaylist() throws Exception {
        Playlist mockPlaylist = mock(Playlist.class);
        when(mockMPD.getPlaylist()).thenReturn(mockPlaylist);

        assertEquals("wrong playlist", mockPlaylist, sal.getPlaylist());
    }
    

}
