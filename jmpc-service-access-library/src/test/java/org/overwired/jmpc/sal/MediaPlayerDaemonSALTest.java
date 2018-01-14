package org.overwired.jmpc.sal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.bff.javampd.admin.Admin;
import org.bff.javampd.database.MusicDatabase;
import org.bff.javampd.monitor.StandAloneMonitor;
import org.bff.javampd.server.MPD;
import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.server.MPDConnectionException;
import org.bff.javampd.song.SongSearcher;
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
    @Mock
    private StandAloneMonitor mockStandAloneMonitor;

    @Before
    public void setup() throws Exception {
        when(mockBuilder.build()).thenReturn(mockMPD);

        sal = new MediaPlayerDaemonSAL(mockBuilder);
    }

    @Test
    public void shouldInvokeBuilderOnlyOnce() throws Exception {
        when(mockMPD.getMonitor()).thenReturn(mockStandAloneMonitor);
        when(mockMPD.isConnected()).thenReturn(true);

        assertEquals("wrong monitor object", mockStandAloneMonitor, sal.getMonitor());
        sal.getMonitor();
        verify(mockBuilder).build();
    }

    @Test
    public void shouldInvokeBuilderAgainWhenDisconnected() throws Exception {
        when(mockMPD.getMonitor()).thenReturn(mockStandAloneMonitor);
        when(mockMPD.isConnected()).thenReturn(true).thenReturn(false).thenReturn(true);

        assertEquals("wrong monitor object", mockStandAloneMonitor, sal.getMonitor());
        sal.getMonitor();
        sal.getMonitor();
        sal.getMonitor();
        verify(mockBuilder, times(2)).build();
    }

    @Test(expected = MPDConnectionException.class)
    public void shouldPropagateMPDConnectionException() throws Exception {
        when(mockBuilder.build()).thenThrow(new MPDConnectionException("intentional test exception - ignore"));

        sal.getMonitor();
    }

    @Test
    public void shouldExposeTheSongSearcher() throws MPDConnectionException {
        SongSearcher mockSongSearcher = mock(SongSearcher.class);
        when(mockMPD.getSongSearcher()).thenReturn(mockSongSearcher);

        assertEquals("wrong song searcher object returned", mockSongSearcher, sal.getSongSearcher());
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
