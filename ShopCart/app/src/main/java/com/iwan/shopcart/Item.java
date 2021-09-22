package com.iwan.shopcart;

public class Item {
    public String name;
    public String item;
    public String numberOfItems;
    public String price;
    public String total;

    public Item(String name, String item, String numberOfItems, String price, String total) {
        this.name = name;
        this.item = item;
        this.numberOfItems = numberOfItems;
        this.price = price;
        this.total = total;
    }
}
