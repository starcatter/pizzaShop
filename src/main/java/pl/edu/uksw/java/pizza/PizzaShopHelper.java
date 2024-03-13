package pl.edu.uksw.java.pizza;

class PizzaShopHelper implements PizzaShopWorker {
    PizzaShop shop;
    Pizza workedPizza = null;
    public PizzaShopHelper(PizzaShop shop) {
        this.shop = shop;
    }

    @Override
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

        return false;
    }

    private void preparePizza() {
        assert workedPizza != null;

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