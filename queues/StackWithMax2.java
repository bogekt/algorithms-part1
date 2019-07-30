import java.util.Stack;

public class StackWithMax2 {

    public StackWithMax2() {
        this.stack = new Stack<>();
        this.max = new Stack<>();
    }

    private Stack<Double> stack;
    private Stack<Double> max;

    public static void main(String[] args) {
        // 1
        StackWithMax2 stack = new StackWithMax2();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assert stack.pop() == 3;
        assert stack.pop() == 2;

        stack.push(4);
        assert stack.pop() == 4;
        assert stack.pop() == 1;
        assert stack.isEmpty();

        // 2
        stack = new StackWithMax2();
        stack.push(4);
        stack.push(2);
        stack.push(3);
        stack.push(1);
        assert stack.max() == 4;
        stack.pop();
        assert stack.max() == 4;

        // 3
        stack = new StackWithMax2();
        stack.push(3);
        stack.push(2);
        stack.push(0);
        stack.push(4);
        assert stack.max() == 4;
        stack.pop();
        assert stack.max() == 3;
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public void push(double value) {
        stack.push(value);

        if (max.isEmpty()) {
            max.push(value);
        }
        else {
            double prevMax = max.peek();
            max.push(prevMax > value ? prevMax : value);
        }
    }

    public double pop() {
        max.pop();
        return stack.pop();
    }

    public double max() {
        return max.peek();
    }
}
