package com.example.finalproject;

public class Member {
    private int image;
    private String name;
    private String description;

    public Member() {
        super();
    }

    public Member(int image, String name, String description) {
        super();
        this.image = image;
        this.name = name;
        this.description = description;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
