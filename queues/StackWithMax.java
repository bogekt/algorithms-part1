public class StackWithMax {
    private Node current;

    private class Node {
        Node next;
        Node max;
        double value;
    }

    public static void main(String[] args) {
        StackWithMax stack = new StackWithMax();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assert stack.pop() == 3;
        assert stack.pop() == 2;

        stack.push(4);
        assert stack.pop() == 4;
        assert stack.pop() == 1;
        assert stack.isEmpty();

        stack.push(3);
        stack.push(2);
        stack.push(0);
        stack.push(4);
        assert stack.max() == 4;
        stack.pop();
        assert stack.max() == 3;
    }

    public boolean isEmpty() {
        return current == null;
    }

    public void push(double value) {
        Node node = new Node();
        node.next = current;
        node.value = value;

        current = node;

        setCurrentMax();
    }

    public double pop() {
        double value = current.value;
        current = current.next;

        return value;
    }

    public double max() {
        return current.max.value;
    }

    private void setCurrentMax() {
        if (
                current.next == null ||
                        current.value >= current.next.max.value
        ) {
            current.max = current;
        }
        else {
            current.max = current.next.max;
        }
    }
}
