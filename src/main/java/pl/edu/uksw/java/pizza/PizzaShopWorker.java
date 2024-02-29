package pl.edu.uksw.java.pizza;

class PizzaShopWorker {
    PizzaShop shop;
    Pizza workedPizza = null;
    public PizzaShopWorker(PizzaShop shop) {
        this.shop = shop;
    }

    public boolean update() {
        if (workedPizza != null) {
            preparePizza();
            return true;
        }

        if (checkOvens()) {
            return true;
        }

        return pickUpOrder();
    }

    private boolean checkOvens() {
        assert workedPizza == null;

        var readyOven = shop.getReadyOven();
        if (readyOven.isPresent()) {
            workedPizza = readyOven.get().removePizza();
            return true;
        }

        return false;
    }

    private boolean pickUpOrder() {
        assert workedPizza == null;

        var readyPizza = shop.getReadyPizza();
        if (readyPizza.isPresent()) {
            workedPizza = readyPizza.get();
            return true;
        }

        var newOrder = shop.getNewOrder();
        if (newOrder.isPresent()) {
            PizzaOrder order = newOrder.get();

            workedPizza = new Pizza(order);
            order.setPizza(workedPizza);

            return true;
        }

        return false;
    }

    private void preparePizza() {
        assert workedPizza != null;

        if (!workedPizza.isPieReady()) {
            workedPizza.workPie();
            return;
        }

        if (!workedPizza.hasAllIngredients()) {
            var ingredient = workedPizza.getNextIngredient();
            workedPizza.addIngredient(ingredient);
            return;
        }

        if (workedPizza.isReadyToBake()) {
            var freeOven = shop.getFreeOven();
            if (freeOven.isPresent()) {
                freeOven.get().loadPizza(workedPizza);
                workedPizza = null;
                return;
            }
        }

        putAwayPizza();
    }

    private void putAwayPizza() {
        shop.pizzas.add(workedPizza);
        workedPizza = null;
    }
}