package pl.edu.uksw.java.pizza;

public class PizzaOven {
    final int pizzaBakeTime;
    Pizza contents = null;
    int timeLeft = 0;

    PizzaOven(int pizzaBakeTime) {
        this.pizzaBakeTime = pizzaBakeTime;
    }

    void update() {
        if (contents != null) {
            timeLeft--;

            if (timeLeft > 0) {
                contents.setBakeState(PizzaBakeState.Baking);
            } else if (timeLeft == 0) {
                contents.setBakeState(PizzaBakeState.Baked);
            } else {
                contents.setBakeState(PizzaBakeState.Burned);
            }
        }
    }

    public void loadPizza(Pizza workedPizza) {
        contents = workedPizza;
        timeLeft = pizzaBakeTime;
    }

    public Pizza removePizza() {
        var pizza = contents;
        contents = null;
        timeLeft = 0;

        return pizza;
    }

    public boolean canAcceptPizza() {
        return contents == null;
    }

    public boolean pizzaReady() {
        return contents != null && contents.isBaked();
    }
}
