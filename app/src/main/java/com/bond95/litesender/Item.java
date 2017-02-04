package com.bond95.litesender;

public class Item {
    private String name;
    private String image;

    public Item(String n, String img)
    {
        name = n;
        image = img;
    }
    public String getName()
    {
        return name;
    }

    public String getImage() {
        return image;
    }
    public int compareTo(Item o) {
        if(this.name != null)
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        else
            throw new IllegalArgumentException();
    }
}