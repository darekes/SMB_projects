package pl.dsamsel.mp1.Models;

public class Product {

    private int id;
    private String name;
    private int price;
    private int quantity;
    private boolean isBought;

    public Product(int id, String name, int price, int quantity, boolean isBought) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.isBought = isBought;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setIsBought(boolean isBought) {
        this.isBought = isBought;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    public boolean isBought() {
        return this.isBought;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return this.quantity;
    }
}
