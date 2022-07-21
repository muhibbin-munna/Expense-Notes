package com.muhibbin.expensenote.Features.DetailsCRUD.CreateDetails;

public class DetailNote {
    private long id;
    private String name;
    private int activated;
    private double price;

    public DetailNote(long id, String name, double price, int activated) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.activated = activated;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getActivated() {
        return activated;
    }

    public void setActivated(int activated) {
        this.activated = activated;
    }
}