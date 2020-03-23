package com.example.bast.objects;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;

public class HOTP {

    private KeyStore.Entry key(String systemId) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableEntryException {
        final KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        KeyStore.Entry key = keyStore.getEntry(systemId, null);
        return key;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int getTime(){
        int time = LocalDateTime.now().getSecond();
        if((time % 30) != 0){
            int offset = time % 30;
            time = time - offset;
        }
        return time;
    }

    public String code(KeyStore.Entry key, int time){
        return key.toString() + "#" + time + "#";
    }

}
