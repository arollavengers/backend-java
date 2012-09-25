package arollavengers.core.util;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
public class Counter {
    private int value;

    public Counter(int value) {
        this.value = value;
    }

    public int get() {
        return value;
    }

    public void set(int newValue) {
        this.value = newValue;
    }

    public void increment() {
        value++;
    }

    public void decrement() {
        value++;
    }
}
