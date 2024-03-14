package pl.edu.uksw.java.pizza;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

class PizzaShop {
    private static final Logger log = LoggerFactory.getLogger(PizzaShop.class);
    List<PizzaOrder> orders = new ArrayList<>();
    List<Pizza> pizzas = new ArrayList<>();
    List<PizzaRecipe> menu;
    List<PizzaShopWorker> workers = new ArrayList<>();
    List<PizzaOven> pizzaOvens = new ArrayList<>();
    List<PizzaShopCustomer> customers = new ArrayList<>();
    List<PizzaShopTable> tables = new ArrayList<>();

    public PizzaShop(List<PizzaRecipe> menu, int cook_cnt, int helper_cnt, int oven_cnt, int table_cnt) {
        this.menu = menu;

        for (int i = 0; i < cook_cnt; i++) {
            workers.add(new PizzaShopCook(this));
        }

        for (int i = 0; i < helper_cnt; i++) {
            workers.add(new PizzaShopHelper(this));
        }

        for (int i = 0; i < oven_cnt; i++) {
            pizzaOvens.add(new PizzaOven(5));
        }

        for (int i = 0; i < table_cnt; i++) {
            tables.add(new PizzaShopTable());
        }
    }

    public void placeOrder(PizzaOrder order) {
        orders.add(order);
    }

    Optional<PizzaShopTable> getFreeTable() {
        return tables.stream().filter(PizzaShopTable::isFree).findFirst();
    }

    public boolean seatCustomer(PizzaShopCustomer customer) {
        var freeTable = getFreeTable();
        if (freeTable.isPresent()) {
            PizzaShopTable pizzaShopTable = freeTable.get();
            pizzaShopTable.seatCustomer(customer);

            log.info("seated customer: " + pizzaShopTable) ;

            orders.add(customer.getNewOrder(getMenu()));
            return true;
        }
        return false;
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
        // pizzas bake in the ovens
        for (PizzaOven pizzaOven : pizzaOvens) {
            pizzaOven.update();
        }

        // workers do their thing
        for (PizzaShopWorker worker : workers) {
            boolean didSomeWork = worker.update();
            if (!didSomeWork) {
                log.warn("Idle worker");
            }
        }

        // pizzas serve themselves (???)
        for (Iterator<Pizza> pizzaIterator = pizzas.iterator(); pizzaIterator.hasNext(); ) {
            Pizza pizza = pizzaIterator.next();
            if (pizza.isReadyToServe()) {
                var order = pizza.getOrder();
                var customer = order.customer;

                log.info("Pizza ready for " + customer);
                synchronized (customer) {
                    customer.setPizza(pizza);
                    customer.notify();
                }

                pizzaIterator.remove();
            }
        }

        // customers get seated
        for (Iterator<PizzaShopCustomer> iterator = customers.iterator(); iterator.hasNext(); ) {
            var customer = iterator.next();
            assert !customer.isSeated();

            if (seatCustomer(customer)) {
                iterator.remove();
            } else {
                log.warn("Customer waiting for a free table...");
            }
        }

        // customers eat and go
//        try {
//            for (var table : tables) {
//                if (table.isFree()) continue;
//
//                table.getCustomer().update();
//            }
//        } catch (RuntimeException exception) {
//            log.error(exception.getMessage());
//        }
    }

    public void addCustomer(PizzaShopCustomer customer) {
        customers.add(customer);
    }

    public boolean isIdle(){
        return customers.isEmpty() && tables.stream().filter(PizzaShopTable::isFree).count() == tables.size();
    }

    @Override
    public String toString() {
        return "PizzaShop{" +
                "\n\torders=" + orders.size() +
                "\n\t raw pizzas=" + pizzas.stream().filter(Pizza::isReadyToBake).count() +
                "\n\t ready pizzas=" + pizzas.stream().filter(Pizza::isReadyToServe).count() +
                "\n\t workers=" + workers.size() +
                "\n\t idle pizzaOvens=" + pizzaOvens.stream().filter(PizzaOven::canAcceptPizza).count() +
                "\n\t customers in line=" + customers.size() +
                "\n\t tables=" + tables +
                '}';
    }
}
