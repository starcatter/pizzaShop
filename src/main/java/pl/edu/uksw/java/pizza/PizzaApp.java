package pl.edu.uksw.java.pizza;

import java.util.ArrayList;
import java.util.List;


public class PizzaApp {

    private static final int MAX_EXTRAS = 5;

    public static List<Ingredient> pickExtras(int max) {
        List<Ingredient> extras = new ArrayList<>();

        int extrasCount = (int) (Math.random() * max);

        for (int i = 0; i < extrasCount; i++) {
            int ingredientIndex = (int) (Math.random() * (Ingredient.values().length - 1));
            extras.add(Ingredient.values()[ingredientIndex]);
        }

        return extras;
    }

    public static PizzaOrder getNewOrder(PizzaShop shop, int maxExtras) {
        int recipeIndex = (int) (Math.random() * (shop.getMenu().size()));
        int sizeIndex = (int) (Math.random() * (PizzaSize.values().length));

        var size = PizzaSize.values()[sizeIndex];
        var recipe = shop.getMenu().get(recipeIndex);
        var extras = pickExtras(maxExtras);

        return new PizzaOrder(size, recipe, extras);
    }

    public static void main(String[] args) {
        PizzaShop shop = new PizzaShop(List.of(
                new PizzaRecipe("Margherita", List.of(Ingredient.Cheese)),
                new PizzaRecipe("Peperoni", List.of(Ingredient.Cheese, Ingredient.Peperoni))),
                4, 1, 2);


        List<PizzaOrder> orders = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            PizzaOrder order = getNewOrder(shop, MAX_EXTRAS);
            orders.add(order);

            shop.placeOrder(order);
        }

        do {
            shop.update();
            orders.forEach(System.out::println);
        } while (orders.stream().anyMatch(order -> !order.getPizza().isReadyToServe()));
    }
}
