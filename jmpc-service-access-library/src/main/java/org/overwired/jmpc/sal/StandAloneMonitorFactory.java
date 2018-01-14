package org.overwired.jmpc.sal;

import org.bff.javampd.monitor.StandAloneMonitor;

/**
 * Get a Monitor.
 */
public interface StandAloneMonitorFactory {
    /**
     * Get a monitor to use for listener (de)registration.
     * @return the monitor.
     */
    StandAloneMonitor getMonitor();
}
