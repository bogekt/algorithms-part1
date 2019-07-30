import java.util.Stack;

public class QueueWithTwoStacks<Item> {
    public QueueWithTwoStacks() {
        this.stack1 = new Stack<>();
        this.stack2 = new Stack<>();
    }

    private Stack<Item> stack1;
    private Stack<Item> stack2;

    public static void main(String[] args) {
        // 1
        QueueWithTwoStacks<Integer> queue = new QueueWithTwoStacks<>();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assert queue.dequeue() == 1;
        assert queue.dequeue() == 2;

        queue.enqueue(4);
        assert queue.dequeue() == 3;
        assert queue.dequeue() == 4;
        assert queue.isEmpty();
    }

    public boolean isEmpty() {
        return stack1.isEmpty();
    }

    public void enqueue(Item value) {
        reverseStack(stack2, stack1);

        stack1.push(value);
    }

    public Item dequeue() {
        reverseStack(stack1, stack2);

        return stack2.pop();
    }

    public void reverseStack(Stack<Item> origin, Stack<Item> reverse) {
        while (!origin.isEmpty()) {
            reverse.push(origin.pop());
        }
    }
}
