package com.minkostplan.eksamensprojekt.Model;

/**
 * Represents an ingredient with its nutritional information.
 */
public class Ingredient {

    private int ingredientId;
    private String name;
    private double fat;
    private double protein;
    private double carbohydrate;
    private int calories;

    /**
     * Default constructor.
     */
    public Ingredient() {
    }

    /**
     * Parameterized constructor to initialize all fields.
     *
     * @param name the name of the ingredient
     * @param fat the amount of fat in grams
     * @param protein the amount of protein in grams
     * @param carbohydrate the amount of carbohydrate in grams
     * @param calories the amount of calories
     */
    public Ingredient(String name, double fat, double protein, double carbohydrate, int calories) {
        this.name = name;
        this.fat = fat;
        this.protein = protein;
        this.carbohydrate = carbohydrate;
        this.calories = calories;
    }

    /**
     * Gets the ingredient ID.
     *
     * @return the ingredient ID
     */
    public int getIngredientId() {
        return ingredientId;
    }

    /**
     * Sets the ingredient ID.
     *
     * @param ingredientId the ingredient ID to set
     */
    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    /**
     * Gets the name of the ingredient.
     *
     * @return the name of the ingredient
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the ingredient.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the amount of fat in grams.
     *
     * @return the amount of fat in grams
     */
    public double getFat() {
        return fat;
    }

    /**
     * Sets the amount of fat in grams.
     *
     * @param fat the amount of fat to set
     */
    public void setFat(double fat) {
        this.fat = fat;
    }

    /**
     * Gets the amount of protein in grams.
     *
     * @return the amount of protein in grams
     */
    public double getProtein() {
        return protein;
    }

    /**
     * Sets the amount of protein in grams.
     *
     * @param protein the amount of protein to set
     */
    public void setProtein(double protein) {
        this.protein = protein;
    }

    /**
     * Gets the amount of carbohydrate in grams.
     *
     * @return the amount of carbohydrate in grams
     */
    public double getCarbohydrate() {
        return carbohydrate;
    }

    /**
     * Sets the amount of carbohydrate in grams.
     *
     * @param carbohydrate the amount of carbohydrate to set
     */
    public void setCarbohydrate(double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    /**
     * Gets the amount of calories.
     *
     * @return the amount of calories
     */
    public int getCalories() {
        return calories;
    }

    /**
     * Sets the amount of calories.
     *
     * @param calories the amount of calories to set
     */
    public void setCalories(int calories) {
        this.calories = calories;
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
                '}';
    }
}
