package com.minkostplan.eksamensprojekt.Model;

public class Ingredients {

    private int ingredientId;
    private String name;
    private double fat;
    private double proteins;
    private double carbohydrates;

    public Ingredients(String name, int ingredientId, double fat, double proteins, double carbohydrates) {
        this.name = name;
        this.ingredientId = ingredientId;
        this.fat = fat;
        this.proteins = proteins;
        this.carbohydrates = carbohydrates;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getProteins() {
        return proteins;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Ingredients{" +
                "ingredientId=" + ingredientId +
                ", name='" + name + '\'' +
                ", fat=" + fat +
                ", proteins=" + proteins +
                ", carbohydrates=" + carbohydrates +
                '}';
    }
}
