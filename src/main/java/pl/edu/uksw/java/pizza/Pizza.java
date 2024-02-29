package pl.edu.uksw.java.pizza;

import java.util.ArrayList;
import java.util.List;

class Pizza {
    private final PizzaOrder order;
    private final List<Ingredient> ingredientsOnPizza = new ArrayList<>();
    private final PizzaPie pie = new PizzaPie();
    private PizzaBakeState bakeState = PizzaBakeState.Raw;

    public Pizza(PizzaOrder order) {
        this.order = order;
    }

    public Ingredient getNextIngredient() {
        List<Ingredient> orderedIngredients = order.getIngredientList();

        for (int i = 0; i < orderedIngredients.size(); i++) {
            if (ingredientsOnPizza.size() > i) {
                if (ingredientsOnPizza.get(i) != orderedIngredients.get(i)) {
                    throw new RuntimeException("Invalid ingredients!");
                }
            } else {
                return orderedIngredients.get(i);
            }
        }

        return null;
    }

    public boolean hasAllIngredients() {
        return ingredientsOnPizza.equals(order.getIngredientList());
    }

    public void addIngredient(Ingredient ingredient) {
        ingredientsOnPizza.add(ingredient);
    }

    boolean isPieReady() {
        return pie.isReady();
    }

    public void workPie() {
        pie.workPie();
    }

    public PizzaBakeState getBakeState() {
        return bakeState;
    }

    public void setBakeState(PizzaBakeState bakeState) {
        this.bakeState = bakeState;
    }

    public boolean isBaked() {
        return bakeState == PizzaBakeState.Baked || bakeState == PizzaBakeState.Burned;
    }

    public boolean isReadyToServe() {
        return pie.isReady() && hasAllIngredients() && isBaked();
    }

    public boolean isReadyToBake() {
        return hasAllIngredients() && bakeState == PizzaBakeState.Raw;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "order=" + order.getIngredientList() +
                ", ingredientsOnPizza=" + ingredientsOnPizza +
                ", bakeState=" + bakeState +
                ", pie=" + pie +
                '}';
    }
}
