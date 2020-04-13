package com.example.bast.objects;

import java.net.InetAddress;
import java.util.ArrayList;

public class System {
    public final int id;
    public final String name;

    public System(final int id, final String name) {
        this.name = name;
        this.id = id;
    }

    public String getSystemName(){return name;}
}
