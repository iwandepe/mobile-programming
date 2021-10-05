package com.iwan.listviewandinputdialog;

public class Item {
    private String name;
    private String phone;

    public Item(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
}
