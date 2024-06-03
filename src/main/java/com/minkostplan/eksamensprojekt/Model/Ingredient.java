package com.minkostplan.eksamensprojekt.Model;

/**
 * Repræsenterer en ingrediens med dens ernæringsinformation.
 */
public class Ingredient {

    private int ingredientId;
    private String name;
    private double fat;
    private double protein;
    private double carbohydrate;
    private int calories;
    private double quantity;
    private boolean bought;



    /**
     * Standardkonstruktør.
     */
    public Ingredient() {
    }

    /**
     * Konstruktør med parametre til at initialisere alle felter.
     *
     * @param name          ingrediensens navn
     * @param fat           mængden af fedt i gram
     * @param protein       mængden af protein i gram
     * @param carbohydrate  mængden af kulhydrater i gram
     * @param calories      mængden af kalorier
     */
    public Ingredient(String name, double fat, double protein, double carbohydrate, int calories) {
        this.name = name;
        this.fat = fat;
        this.protein = protein;
        this.carbohydrate = carbohydrate;
        this.calories = calories;
    }


    // Getters og setters for alle felter

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }
    @Override
    public String toString() {
        return "Ingredient{" +
                "ingredientId=" + ingredientId +
                ", name='" + name + '\'' +
                ", fat=" + fat +
                ", protein=" + protein +
                ", carbohydrate=" + carbohydrate +
                ", calories=" + calories +
                ", quantity=" + quantity +
                '}';
    }
}
