package org.banksy.eventlog;

import com.google.common.eventbus.EventBus;

import java.util.Vector;

public class EventLog {
    private Vector<Object> theStorageBin;
    private EventBus bus;

    public EventLog(EventBus bus) {
        this.bus = bus;
        theStorageBin = new Vector();
    }

    public EventLog() {
        this.bus = new EventBus();
        theStorageBin = new Vector();
    }

    public void save(Object event) {
        theStorageBin.add(event);
        bus.post(event);
    }

    public Object latest() {
        return theStorageBin.lastElement();
    }

    public int size() {
        return theStorageBin.size();
    }
}
