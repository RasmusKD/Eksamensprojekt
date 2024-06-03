package com.minkostplan.eksamensprojekt.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Repræsenterer en opskrift i systemet.
 */
public class Recipe {

    private int recipeId;
    private String title;
    private String description;
    private String method;
    private String cookingTime;
    private String imageUrl;
    private String mealTime;
    private int totalCalories;
    private int totalProtein;
    private int totalFat;
    private int totalCarbohydrates;
    private String week;
    private int adjustedCalories;
    private List<Ingredient> ingredients = new ArrayList<>();

    /**
     * Standardkonstruktør.
     */
    public Recipe() {
    }

    /**
     * Konstruktør med parametre til at initialisere alle felter.
     *
     * @param recipeId          opskriftens ID
     * @param title             opskriftens titel
     * @param description       beskrivelse af opskriften
     * @param ingredients       liste over ingredienser i opskriften
     * @param method            fremgangsmåden for opskriften
     * @param cookingTime       tilberedningstid for opskriften
     * @param imageUrl          URL til billede af opskriften
     * @param mealTime          måltidstid (f.eks. morgenmad, frokost, aftensmad)
     * @param totalCalories     totale kalorier i opskriften
     * @param totalProtein      totale proteiner i opskriften
     * @param totalFat          totale fedt i opskriften
     * @param totalCarbohydrates totale kulhydrater i opskriften
     * @param week               uge hvor opskriften anbefales
     */
    public Recipe(int recipeId, String title, String description, List<Ingredient> ingredients, String method, String cookingTime, String imageUrl, String mealTime, int totalCalories, int totalProtein, int totalFat, int totalCarbohydrates, String week) {
        this.recipeId = recipeId;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.method = method;
        this.cookingTime = cookingTime;
        this.imageUrl = imageUrl;
        this.mealTime = mealTime;
        this.totalCalories = totalCalories;
        this.totalProtein = totalProtein;
        this.totalFat = totalFat;
        this.totalCarbohydrates = totalCarbohydrates;
        this.week = week;
    }

    // Getters og setters for alle felter

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }

    public int getTotalProtein() {
        return totalProtein;
    }

    public void setTotalProtein(int totalProtein) {
        this.totalProtein = totalProtein;
    }

    public int getTotalFat() {
        return totalFat;
    }

    public void setTotalFat(int totalFat) {
        this.totalFat = totalFat;
    }

    public int getTotalCarbohydrates() {
        return totalCarbohydrates;
    }

    public void setTotalCarbohydrates(int totalCarbohydrates) {
        this.totalCarbohydrates = totalCarbohydrates;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
    public int getAdjustedCalories() {
        return adjustedCalories;
    }

    public void setAdjustedCalories(int adjustedCalories) {
        this.adjustedCalories = adjustedCalories;
    }

    public int getCalories() {
        return this.totalCalories;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeId=" + recipeId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", ingredients=" + ingredients +
                ", method='" + method + '\'' +
                ", cookingTime='" + cookingTime + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", mealTime='" + mealTime + '\'' +
                ", totalCalories=" + totalCalories +
                ", totalProtein=" + totalProtein +
                ", totalFat=" + totalFat +
                ", totalCarbohydrates=" + totalCarbohydrates +
                ", week='" + week + '\'' +
                '}';
    }
}
