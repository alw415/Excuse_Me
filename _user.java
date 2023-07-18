package com.ab1.excuseme;

import java.io.Serializable;

/**
 * Created by wdona on 4/15/2017.
 */

public class _user implements Serializable {

    private String name;
    private String role;
    private String storage_name;

    public _user(String name, String role, String storage_name){
        setName(name);
        setRole(role);
        setStorage_name(storage_name);
    }
    public _user(){
        setName("Anonymous");
        setRole("Student");
        setStorage_name("general");
    }

    public String getStorage_name() {
        return storage_name;
    }

    public void setStorage_name(String storage_name) {
        this.storage_name = storage_name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName() + " " + getRole() + " " + getStorage_name();
    }
}
