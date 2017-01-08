# jmpc

JMPC is a Java-based client for [Music Player Daemon](https://www.musicpd.org/).

I am writing it because I built a Jukebox, originally based on a DOS PC running [DWJukebox](http://dwjukebox.com/), but a nearby lightning strike blew the PC, and I just don't have the energy to find the old drivers and old hardware I'd need to rebuild it using that software, so I've decided to attempt to replace the PC with a [Raspberry Pi](https://www.raspberrypi.org/) instead.

The idea is to run MPD on the Pi (because that's stupidly simple), and run a Java web-based front end to simulate the Jukebox interface.  I hope to be able to also run the web application and a browser all on the same Pi to drive the Jukebox.  We'll see how it goes, and change things up as necessary.

If this works, I get the bonus of being able to remotely control the Jukebox with a smartphone, tablet, or computer, which just adds interesting options.

