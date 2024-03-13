package pl.edu.uksw.java.pizza;

import java.util.List;

public record PizzaRecipe(String name, List<Ingredient> ingredients) {
}
