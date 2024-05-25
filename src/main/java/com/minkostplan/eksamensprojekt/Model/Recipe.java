package com.minkostplan.eksamensprojekt.Model;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private int recipeId;
    private String title;
    private String description;
    private String method;
    private String cookingTime;
    private String imageUrl;
    private String mealTime; // Added mealTime
    private int totalCalories; // Added totalCalories
    private int totalProtein; // Added totalProtein
    private int totalFat; // Added totalFat
    private int totalCarbohydrates; // Added totalCarbohydrates
    private String day; // Added day
    private int adjustedCalories;
    private List<Ingredient> ingredients = new ArrayList<>();

    public Recipe() {
    }

    public Recipe(int recipeId, String title, String description, List<Ingredient> ingredients, String method, String cookingTime, String imageUrl, String mealTime, int totalCalories, int totalProtein, int totalFat, int totalCarbohydrates, String day) {
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
        this.day = day;
    }
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    public int getAdjustedCalories() {
        return adjustedCalories;
    }
    public int getCalories() {
        return this.totalCalories; // Assuming totalCalories represents the calories of the recipe
    }

    public void setAdjustedCalories(int adjustedCalories) {
        this.adjustedCalories = adjustedCalories;
    }
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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
                ", day='" + day + '\'' +
                ", adjustedCalories=" + adjustedCalories +
                '}';
    }
}
