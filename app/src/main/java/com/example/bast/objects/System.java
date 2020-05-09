package com.example.bast.objects;

public class System {
    public final int id;
    public final String name;
    public String TotpKey;

    public System(final int id, final String name) {
        this.name = name;
        this.id = id;
    }
    public System(final int id, final String name, String TotpKey) {
        this.name = name;
        this.id = id;
        this.TotpKey = TotpKey;
    }

    public int getSystemID(){return id;}
    public String getSystemName(){return name;}
    public String getTotpKey(){return TotpKey;}
    public void setTotpKey(String totpKey){TotpKey = totpKey;}
}
