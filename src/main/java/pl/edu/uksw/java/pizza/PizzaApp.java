package pl.edu.uksw.java.pizza;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class PizzaApp {
    private static final Logger log = LoggerFactory.getLogger(PizzaApp.class);
    public static void main(String[] args) throws InterruptedException {
        PizzaShop shop = new PizzaShop(List.of(
                new PizzaRecipe("Margherita", List.of(Ingredient.Cheese)),
                new PizzaRecipe("Peperoni", List.of(Ingredient.Cheese, Ingredient.Peperoni))),
                4, 1, 2, 2);


        List<PizzaShopCustomer> customers = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            PizzaShopCustomer customer = new PizzaShopCustomer();
            customers.add(customer);

            shop.addCustomer(customer);
        }

        do {
            shop.update();
            log.info(shop.toString());

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (!shop.isIdle());
    }
}
