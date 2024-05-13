package com.minkostplan.eksamensprojekt.Model;

public class Ingredients {

    private int ingredientsId;
    private String name;
    private double fat;
    private double protein;
    private double carbohydrate;

    public Ingredients() {
    }

    public Ingredients(int ingredientsId, String name, double fat, double protein, double carbohydrate) {
        this.ingredientsId = ingredientsId;
        this.name = name;
        this.fat = fat;
        this.protein = protein;
        this.carbohydrate = carbohydrate;
    }

    public int getIngredientsId() {
        return ingredientsId;
    }

    public void setIngredientsId(int ingredientsId) {
        this.ingredientsId = ingredientsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    @Override
    public String toString() {
        return "Ingredients{" +
                "ingredientsId=" + ingredientsId +
                ", name='" + name + '\'' +
                ", fat=" + fat +
                ", protein=" + protein +
                ", carbohydrate=" + carbohydrate +
                '}';
    }
}
