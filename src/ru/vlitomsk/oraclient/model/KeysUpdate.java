package ru.vlitomsk.oraclient.model;

import java.util.List;

/**
 * Created by vas on 08.06.2016.
 */
public class KeysUpdate {
    public List<PKey> pkeys;
    public List<FKey> fKeys;

    public KeysUpdate(List<PKey> pkeys, List<FKey> fKeys) {
        this.pkeys = pkeys;
        this.fKeys = fKeys;
    }
}
