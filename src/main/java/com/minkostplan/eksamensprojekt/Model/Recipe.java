package com.minkostplan.eksamensprojekt.Model;

public class Recipe {

    private int recipeId;
    private String title;
    private String description;
    private String ingredients;
    private String method;
    private String cookingTime;
    private String imageUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeId=" + recipeId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", method='" + method + '\'' +
                ", cookingTime='" + cookingTime + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public Recipe(int recipeId, String title, String description, String ingredients, String method, String cookingTime, String imageUrl) {
        this.recipeId = recipeId;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.method = method;
        this.cookingTime = cookingTime;
        this.imageUrl = imageUrl;


    }
}
