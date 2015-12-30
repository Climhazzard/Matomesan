package com.jobs.matomesan;

import java.io.Serializable;

public class MyListInfo implements Serializable {
    private final int id;
    private final String name;

    MyListInfo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}