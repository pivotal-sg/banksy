package org.banksy.eventlog;

import java.util.Vector;

public class EventLog {
    private Vector<Object> theStorageBin;

    public EventLog() {
        theStorageBin = new Vector<Object>();
    }

    public void save(Object event) {
        theStorageBin.add(event);
    }
}
