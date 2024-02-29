package pl.edu.uksw.java.pizza;

public class PizzaPie {
    PizzaPieState state = PizzaPieState.Dough;

    boolean isReady() {
        return state == PizzaPieState.Ready;
    }

    void workPie() {
        switch (state) {
            case Dough -> state = PizzaPieState.Flattened;
            case Flattened -> state = PizzaPieState.Stretched;
            case Stretched -> state = PizzaPieState.Ready;
            case Ready -> throw new RuntimeException("Overworked pizza pie!");
        }
    }

    @Override
    public String toString() {
        return "PizzaPie{" + "state=" + state + '}';
    }
}
