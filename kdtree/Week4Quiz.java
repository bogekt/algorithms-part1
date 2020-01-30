/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class Week4Quiz {
    public static void main(String[] args) {
        test1();
        // test2 BST.isBST
        test3();
    }

    private static void test3() {
        BST<String, Integer> bst = new BST<>();
        bst.put("D", 4);
        bst.put("C", 3);
        bst.put("B", 2);
        bst.put("A", 1);
        bst.put("G", 7);
        bst.put("F", 6);
        bst.put("E", 5);
        BST3<String, Integer> bst3 = new BST3<>();
        bst3.put("D", 4);
        bst3.put("C", 3);
        bst3.put("B", 2);
        bst3.put("A", 1);
        bst3.put("G", 7);
        bst3.put("F", 6);
        bst3.put("E", 5);
        StdOut.println(String.join(" ", bst.keys()));
        bst3.inorderTraversalWithConstantExtraSpace();

        StdOut.println();

        bst = new BST<>();
        bst.put("H", 0);
        bst.put("C", 0);
        bst.put("S", 0);
        bst.put("A", 0);
        bst.put("E", 0);
        bst.put("R", 0);
        bst.put("X", 0);

        bst3 = new BST3<>();
        bst3.put("H", 0);
        bst3.put("C", 0);
        bst3.put("S", 0);
        bst3.put("A", 0);
        bst3.put("E", 0);
        bst3.put("R", 0);
        bst3.put("X", 0);
        StdOut.println(String.join(" ", bst.keys()));
        bst3.inorderTraversalWithConstantExtraSpace();
        // assert String.join(" ", bst.keys()).equals(
        //         String.join(" ", bst3.keys()));
    }

    private static void test1() {
        double a = +0.0;
        double b = -0.0;
        Double x = new Double(a);
        Double y = new Double(b);

        System.out.println(a == b);
        System.out.println(x.equals(y));

        System.out.println();

        a = Double.NaN;
        b = Double.NaN;
        x = Double.NaN;
        y = Double.NaN;

        System.out.println(a == b);
        System.out.println(x.equals(y));
    }
}
