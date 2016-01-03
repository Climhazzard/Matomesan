package com.jobs.matomesan;

import java.io.Serializable;

public class MyListInfo implements Serializable {
    private int id;
    private String name;
    private int flag;

    MyListInfo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    MyListInfo(int id, String name, int flag) {
        this.id = id;
        this.name = name;
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isChecked() {
        if (flag == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void setChecked(boolean flag) {
        if (flag) {
            this.flag = 1;
        } else {
            this.flag = 0;
        }
     }
}