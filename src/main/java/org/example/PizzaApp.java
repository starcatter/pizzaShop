package org.example;

import java.util.ArrayList;
import java.util.List;

enum PizzaState {
    Ordered,
    Dough,
    Pie,
    Filled,
    Baking,
    Baked,
    Boxed
}

enum PizzaSize {
    S,
    M,
    L
}

enum Ingredient {
    Cheese,
    Peperoni,
    Ham,
    Mushrooms
}

record PizzaRecipe(String name, List<Ingredient> ingredients) {
}

class PizzaShopWorker {
    public PizzaShopWorker(PizzaShop shop) {
        this.shop = shop;
    }

    PizzaShop shop;
    Pizza workedPizza = null;

    public void update() {
        if (workedPizza != null) {
            preparePizza();
        } else if (!checkFurnace()) {
            pickUpOrder();
        }
    }

    private boolean checkFurnace() {
        for (var f : shop.furnaces) {
            if (f.contents != null && f.contents.state == PizzaState.Baked) {
                workedPizza = f.contents;
                f.contents = null;
                return true;
            }
        }
        return false;
    }

    private void pickUpOrder() {
        assert workedPizza == null;
        for (var pizza : shop.pizzas) {
            if (pizza.state == PizzaState.Ordered) {
                workedPizza = pizza;
            }
        }
        if (workedPizza != null) {
            shop.pizzas.remove(workedPizza);
        }
    }

    private void preparePizza() {
        assert workedPizza != null;
        switch (workedPizza.state) {
            case Ordered -> {
                workedPizza.state = PizzaState.Dough;
            }
            case Dough -> {
                workedPizza.state = PizzaState.Pie;
            }
            case Pie -> {
                if (workedPizza.ingredientsOnPizza.size() < workedPizza.recipe.ingredients().size()) {
                    workedPizza.ingredientsOnPizza.add(Ingredient.Cheese);
                } else {
                    workedPizza.state = PizzaState.Filled;
                }
            }
            case Filled -> {
                for (var f : shop.furnaces) {
                    if (f.contents == null) {
                        f.loadPizza(workedPizza);
                        workedPizza = null;
                    }
                }
            }
            case Baking -> {
                throw new RuntimeException("Hot pizza!");
            }
            case Baked -> {
                workedPizza.state = PizzaState.Boxed;
            }
            case Boxed -> {
                shop.pizzas.add(workedPizza);
                workedPizza = null;
            }
        }
    }
}

class Pizza {
    PizzaSize size;
    PizzaRecipe recipe;
    List<Ingredient> ingredientsOnPizza = new ArrayList<>();
    PizzaState state;

    public Pizza(PizzaSize size, PizzaRecipe recipe) {
        this.size = size;
        this.recipe = recipe;
        this.state = PizzaState.Ordered;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "size=" + size +
                ", recipe=" + recipe +
                ", ingredientsOnPizza=" + ingredientsOnPizza +
                ", state=" + state +
                '}';
    }
}

class PizzaFurnace {
    final int pizzaBakeTime;
    Pizza contents = null;
    int timeLeft = 0;

    PizzaFurnace(int pizzaBakeTime) {
        this.pizzaBakeTime = pizzaBakeTime;
    }

    void update() {
        if (contents != null && timeLeft > 0) {
            timeLeft--;
        } else if (contents != null) {
            contents.state = PizzaState.Baked;
        }
    }

    public void loadPizza(Pizza workedPizza) {
        contents = workedPizza;
        contents.state = PizzaState.Baking;
        timeLeft = pizzaBakeTime;
    }
}


class PizzaShop {
    List<Pizza> pizzas = new ArrayList<>();
    List<PizzaRecipe> menu;
    List<PizzaShopWorker> workers = new ArrayList<>();
    List<PizzaFurnace> furnaces = new ArrayList<>();

    public Pizza placeOrder(PizzaRecipe recipe, PizzaSize size) {
        Pizza p = new Pizza(size, recipe);
        pizzas.add(p);
        return p;
    }

    public PizzaShop(List<PizzaRecipe> menu, int worker_cnt, int furnace_cnt) {
        this.menu = menu;

        for (int i = 0; i < worker_cnt; i++) {
            workers.add(new PizzaShopWorker(this));
        }

        for (int i = 0; i < furnace_cnt; i++) {
            furnaces.add(new PizzaFurnace(5));
        }
    }

    public List<PizzaRecipe> getMenu() {
        return menu;
    }

    public void update() {
        for (PizzaShopWorker worker : workers) {
            worker.update();
        }

        for (PizzaFurnace furnace : furnaces) {
            furnace.update();
        }
    }
}

public class PizzaApp {

    public static void main(String[] args) {
        PizzaShop shop = new PizzaShop(List.of(
                new PizzaRecipe("Margherita", List.of(Ingredient.Cheese)),
                new PizzaRecipe("Peperoni", List.of(Ingredient.Cheese, Ingredient.Peperoni))),
                2, 1);

        List<Pizza> orderedPizzas = new ArrayList<>();
        for (var recipe : shop.getMenu()) {
            orderedPizzas.add(shop.placeOrder(recipe, PizzaSize.L));
        }

        do {
            shop.update();

            boolean allPizzasBoxed = true;
            for (var pizza : orderedPizzas) {
                if (pizza.state != PizzaState.Boxed) {
                    allPizzasBoxed = false;
                }
            }

            System.out.println(orderedPizzas);

            if (allPizzasBoxed) {
                break;
            }
        } while (true);


    }
}
