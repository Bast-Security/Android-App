package com.example.bast.objects;

import java.net.InetAddress;
import java.util.ArrayList;

public class System {
    private InetAddress address;
    private int port;
    private String systemName;
    private String serviceName;
    private boolean isOrphan;

    //constructor for known system
    public System(String systemName, InetAddress addr, int port, boolean isOrphan) {
        this.systemName = systemName;
        this.address = addr;
        this.port = port;
        this.isOrphan = isOrphan;
    }

    public int getPort() {
        return port;
    }

    public boolean isOrphan() {
        return isOrphan;
    }

    public void adopt() {
        this.isOrphan = false;
    }

    public InetAddress getIp() {
        return this.address;
    }

    // getters and setters
    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public boolean isConnected() {
        return getIp() != null;
    }
}
