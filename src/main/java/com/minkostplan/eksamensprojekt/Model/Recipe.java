package com.minkostplan.eksamensprojekt.Model;

import java.util.List;

public class Recipe {

    private String title;
    private String description;
    private List<String> ingredients;
    private String method;
    private String cookingTime;
    private String imageUrl;

    public Recipe() {
    }

    public Recipe(String title, String description, List<String> ingredients, String method, String cookingTime, String imageUrl) {
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.method = method;
        this.cookingTime = cookingTime;
        this.imageUrl = imageUrl;
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

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
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


    @Override
    public String toString() {
        return "Recipe{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", ingredients=" + ingredients +
                ", method='" + method + '\'' +
                ", cookingTime='" + cookingTime + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}