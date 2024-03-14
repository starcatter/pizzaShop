package pl.edu.uksw.java.pizza;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for pizza App.
 */
public class AppTest {
    PizzaShop shop = null;

    @BeforeEach
    public void resetShop() {
        shop = new PizzaShop(List.of(
                new PizzaRecipe("Margherita", List.of(Ingredient.Cheese)),
                new PizzaRecipe("Peperoni", List.of(Ingredient.Cheese, Ingredient.Peperoni))),
                1, 1, 1,1);
    }

    @Test
    public void testMargherita() {
        var m = shop.getMenu();

        assertFalse(m.isEmpty());

        PizzaRecipe pizzaRecipe = m.get(0);

        assertEquals(pizzaRecipe.name(), "Margherita");

        PizzaOrder order = new PizzaOrder(null, PizzaSize.L, pizzaRecipe, new ArrayList<>());

        shop.placeOrder(order);

        int maxWaitTime = 10;
        while (order.getPizza() == null || !order.getPizza().isReadyToServe()) {
            assertTrue(maxWaitTime > 0, "Took to long to make pizza");

            shop.update();
            maxWaitTime--;
        }

        assertTrue(order.getPizza().hasAllIngredients());
    }

    @Test
    public void testExtras() {
        var m = shop.getMenu();

        assertFalse(m.isEmpty());

        PizzaRecipe pizzaRecipe = m.get(1);

        assertEquals(pizzaRecipe.name(), "Peperoni");

        List<Ingredient> extras = List.of(Ingredient.Ham, Ingredient.Mushrooms);
        PizzaOrder order = new PizzaOrder(null, PizzaSize.L, pizzaRecipe, extras);

        shop.placeOrder(order);

        int maxWaitTime = 15;
        while (order.getPizza() == null || !order.getPizza().isReadyToServe()) {
            assertTrue(maxWaitTime > 0, "Took to long to make pizza");

            shop.update();
            maxWaitTime--;
        }

        assertTrue(order.getPizza().hasAllIngredients());
    }


    @Test
    public void testWrongPizza() {
        var m = shop.getMenu();

        assertFalse(m.isEmpty());

        PizzaRecipe pizzaRecipe = m.get(1);

        assertEquals(pizzaRecipe.name(), "Peperoni");

        PizzaOrder order = new PizzaOrder(null, PizzaSize.L, pizzaRecipe, new ArrayList<>());

        Pizza pizza = new Pizza(order);

        pizza.addIngredient(Ingredient.Ham);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                pizza::getNextIngredient,
                "Expected to throw, but didn't"
        );

        assertTrue(thrown.getMessage().contains("Invalid ingredients!"));
    }

}
