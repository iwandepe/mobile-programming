package com.iwan.shopcart;

public class Cart {
    public String name;
    public String item;
    public int numberOfItems;
    public int price;
    public long id;

    public Cart(String name, String item, int numberOfItems, int price) {
        this.name = name;
        this.item = item;
        this.numberOfItems = numberOfItems;
        this.price = price;
    }

    public Cart() {};
}
