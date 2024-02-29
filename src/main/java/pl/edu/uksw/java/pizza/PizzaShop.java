package pl.edu.uksw.java.pizza;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class PizzaShop {
    List<PizzaOrder> orders = new ArrayList<>();
    List<Pizza> pizzas = new ArrayList<>();
    List<PizzaRecipe> menu;
    List<PizzaShopWorker> workers = new ArrayList<>();
    List<PizzaOven> pizzaOvens = new ArrayList<>();

    public PizzaShop(List<PizzaRecipe> menu, int worker_cnt, int oven_cnt) {
        this.menu = menu;

        for (int i = 0; i < worker_cnt; i++) {
            workers.add(new PizzaShopWorker(this));
        }

        for (int i = 0; i < oven_cnt; i++) {
            pizzaOvens.add(new PizzaOven(5));
        }
    }

    public void placeOrder(PizzaOrder order) {
        orders.add(order);
    }

    public List<PizzaRecipe> getMenu() {
        return menu;
    }

    Optional<PizzaOven> getFreeOven() {
        return pizzaOvens.stream().filter(PizzaOven::canAcceptPizza).findFirst();
    }

    Optional<PizzaOven> getReadyOven() {
        return pizzaOvens.stream().filter(PizzaOven::pizzaReady).findFirst();
    }

    Optional<Pizza> getReadyPizza() {
        return pizzas.stream().filter(Pizza::isReadyToBake).findFirst();
    }

    Optional<PizzaOrder> getNewOrder() {
        return orders.stream().filter(order -> order.getPizza() == null).findFirst();
    }

    public void update() {
        for (PizzaShopWorker worker : workers) {
            boolean didSomeWork = worker.update();
            if(!didSomeWork){
                System.out.println("Idle worker");
            }
        }

        for (PizzaOven pizzaOven : pizzaOvens) {
            pizzaOven.update();
        }
    }
}
