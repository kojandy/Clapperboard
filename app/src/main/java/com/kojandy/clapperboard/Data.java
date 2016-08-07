package com.kojandy.clapperboard;

import io.realm.RealmObject;

public class Data extends RealmObject {
    public int scene;
    public String cut;
    public int take;
    public int camera;
    public boolean isOk;
    public String note;
}
