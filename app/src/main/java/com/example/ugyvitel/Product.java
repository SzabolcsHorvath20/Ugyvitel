package com.example.ugyvitel;

public class Product {

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPlaintext() {
        return plaintext;
    }

    public int getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public int getPrice() {
        return price;
    }

    public boolean isPurchasable() {
        return purchasable;
    }

    public Product(String name, String description, String plaintext, int id, String icon, int price, boolean purchasable) {
        this.name = name;
        this.description = description;
        this.plaintext = plaintext;
        this.id = id;
        this.icon = icon;
        this.price = price;
        this.purchasable = purchasable;
    }

    private String name;
    private String description;
    private String plaintext;
    private int id;
    private String icon;
    private int price;
    private boolean purchasable;

    @Override
    public String toString() {
        return name;
    }
}
