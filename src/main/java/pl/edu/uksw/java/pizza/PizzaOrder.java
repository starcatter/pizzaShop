package pl.edu.uksw.java.pizza;

import java.util.ArrayList;
import java.util.List;

public class PizzaOrder {
    final PizzaSize size;
    final PizzaRecipe recipe;
    final List<Ingredient> extras = new ArrayList<>();
    private Pizza pizza = null;

    PizzaOrder(PizzaSize size, PizzaRecipe recipe, List<Ingredient> extras) {
        this.size = size;
        this.recipe = recipe;
        this.extras.addAll(extras);
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    public List<Ingredient> getIngredientList() {
        List<Ingredient> orderedIngredients = new ArrayList<>();
        orderedIngredients.addAll(recipe.ingredients());
        orderedIngredients.addAll(extras);

        return orderedIngredients;
    }

    @Override
    public String toString() {
        return "PizzaOrder{" +
                "size=" + size +
                ", recipe=" + recipe.name() +
                ", extras=" + extras +
                ", pizza=" + pizza +
                '}';
    }
}
