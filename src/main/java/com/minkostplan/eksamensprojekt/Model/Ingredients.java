package com.minkostplan.eksamensprojekt.Model;

public class Ingredients {

    private int ingredientsId;
    private String name;
    private double fat;
    private double protein;
    private double carbohydrate;
    private int calories;
    public Ingredients() {
    }

    public Ingredients(String name, double fat, double protein, double carbohydrate, int calories) {
        this.name = name;
        this.fat = fat;
        this.protein = protein;
        this.carbohydrate = carbohydrate;
        this.calories = calories;
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

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    @Override
    public String toString() {
        return "Ingredients{" +
                "ingredientsId=" + ingredientsId +
                ", name='" + name + '\'' +
                ", fat=" + fat +
                ", protein=" + protein +
                ", carbohydrate=" + carbohydrate +
                ", calories=" + calories +
                '}';
    }
}
