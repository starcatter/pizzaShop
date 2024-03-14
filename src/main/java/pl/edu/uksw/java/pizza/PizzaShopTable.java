package pl.edu.uksw.java.pizza;

class PizzaShopTable {
    PizzaShopTable() {
        id = id_cnt++;
    }

    public PizzaShopCustomer getCustomer() {
        return customer;
    }

    private PizzaShopCustomer customer;

    static int id_cnt = 0;
    final int id;

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
