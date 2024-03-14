package pl.edu.uksw.java.pizza;

import java.util.ArrayList;
import java.util.List;

public class PizzaOrder {

    final PizzaShopCustomer customer;
    final PizzaSize size;
    final PizzaRecipe recipe;
    final List<Ingredient> extras = new ArrayList<>();
    private Pizza pizza = null;

    static int id_cnt = 0;
    final int id;

    PizzaOrder(PizzaShopCustomer customer, PizzaSize size, PizzaRecipe recipe, List<Ingredient> extras) {
        this.customer = customer;
        this.size = size;
        this.recipe = recipe;
        this.extras.addAll(extras);
        id = id_cnt++;
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
                "id=" + id +
                ", customer=" + customer.id +
                ", size=" + size +
                ", recipe=" + recipe.ingredients() +
                ", extras=" + extras +
                ", pizza=" + pizza +
                '}';
    }
}
