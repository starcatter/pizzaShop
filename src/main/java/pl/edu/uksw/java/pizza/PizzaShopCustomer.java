package pl.edu.uksw.java.pizza;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

class PizzaShopCustomer {
    private static final Logger log = LoggerFactory.getLogger(PizzaShopCustomer.class);
    Pizza pizza;
    PizzaOrder pizzaOrder;
    PizzaShopTable table;
    
    static int id_cnt = 0;
    final int id;

    private static final int MAX_EXTRAS = 5;

    PizzaShopCustomer() {
        id = id_cnt++;
    }

    public static List<Ingredient> pickExtras(int max) {
        List<Ingredient> extras = new ArrayList<>();

        int extrasCount = (int) (Math.random() * max);

        for (int i = 0; i < extrasCount; i++) {
            int ingredientIndex = (int) (Math.random() * (Ingredient.values().length - 1));
            extras.add(Ingredient.values()[ingredientIndex]);
        }

        return extras;
    }

    public PizzaOrder getNewOrder(List<PizzaRecipe> menu) {
        int recipeIndex = (int) (Math.random() * (menu.size()));
        int sizeIndex = (int) (Math.random() * (PizzaSize.values().length));

        var size = PizzaSize.values()[sizeIndex];
        var recipe = menu.get(recipeIndex);
        var extras = pickExtras(MAX_EXTRAS);

        pizzaOrder = new PizzaOrder(this, size, recipe, extras);
        return pizzaOrder;
    }

    public void update() {
        if (isSeated()) {
            if (isServed()) {
                if(pizza.hasSlicesLeft()) {
                    eatPizza();
                } else {
                    pay();
                    leave();
                }
            } else {
                log.warn("Customer waiting for pizza...");
                synchronized (this){
                    try {
                        wait();
                        log.info("Customer got pizza...");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private void pay() {
        // TODO: get a waiter and pay
    }

    private void leave() {
        assert table != null;
        table.vacate();
        table =  null;
    }

    private void eatPizza() {
        assert pizza != null;
        if (!pizza.isGoodToEat()) {
            leave();
            throw new RuntimeException("Can't eat this pizza! "+pizza);
        }
        pizza.eatSlice();
    }

    public boolean isSeated() {
        return table != null;
    }

    public boolean isServed() {
        return pizza != null;
    }

    public boolean shouldLeave() {
        return pizza != null && !pizza.hasSlicesLeft();
    }

    public Pizza getPizza() {
        return pizza;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    @Override
    public String toString() {
        return "PizzaShopCustomer{" +
                " id=" + id +
                ", pizza=" + pizza +
                ", pizzaOrder=" + (pizzaOrder != null ? pizzaOrder : "none yet") +
                ", seated=" + (table!=null) +
                '}';
    }

    public void sitHere(PizzaShopTable pizzaShopTable) {
        table=pizzaShopTable;
        var thread = new Thread(this::startTrhead);
        thread.setName("Customer #"+id);
        thread.start();
    }

    private void startTrhead() {
        while (this.isSeated()) {
            try {
                update();
            } catch (RuntimeException exception) {
                log.error(exception.getMessage());
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
