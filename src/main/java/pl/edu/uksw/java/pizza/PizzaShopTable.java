package pl.edu.uksw.java.pizza;

class PizzaShopTable {
    public PizzaShopCustomer getCustomer() {
        return customer;
    }

    private PizzaShopCustomer customer;

    public void seatCustomer(PizzaShopCustomer customer) {
        this.customer = customer;
        customer.table = this;
    }

    public void vacate() {
        customer = null;
    }
    public boolean isFree() {
        return customer == null;
    }

    @Override
    public String toString() {
        return "PizzaShopTable{" +
                "customer=" + customer +
                '}';
    }
}
