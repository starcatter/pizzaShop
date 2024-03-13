package pl.edu.uksw.java.pizza;

class PizzaShopCook implements PizzaShopWorker {
    PizzaShop shop;
    Pizza workedPizza = null;
    public PizzaShopCook(PizzaShop shop) {
        this.shop = shop;
    }

    @Override
    public boolean update() {
        if (workedPizza != null) {
            preparePizza();
            return true;
        }

        return pickUpOrder();
    }

    private boolean pickUpOrder() {
        assert workedPizza == null;

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