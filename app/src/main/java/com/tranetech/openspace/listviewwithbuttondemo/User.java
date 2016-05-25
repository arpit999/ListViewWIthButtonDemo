package com.tranetech.openspace.listviewwithbuttondemo;

/**
 * Created by Arpit Patel on 08-Apr-16.
 */
public class User {
    String name;
    String address;
    String location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User(String name, String address, String location) {
        super();
        this.name = name;
        this.address = address;
        this.location = location;
    }

}
