package com.iwan.listviewandinputdialog;

public class Item {
    private String name;
    private String phone;
    private long id;

    public Item(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Item() { }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }
}
