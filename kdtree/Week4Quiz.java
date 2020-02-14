/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

public class Week4Quiz {
    public static void main(String[] args) {
        StdOut.println();
        StdOut.println("test1");
        StdOut.println();
        test1();
        // test2 BST.isBST
        StdOut.println();
        StdOut.println("test3");
        StdOut.println();
        test3();
    }

    private static void test3() {
        for (String[] sArray : new String[][] {
                { "D", "C", "B", "A", "G", "F", "E" },
                { "H", "C", "S", "A", "E", "R", "X" },
                }
        ) {
            BST<String, Integer> bst = new BST<>();
            BST3<String, Integer> bst3 = new BST3<>();
            BST31<String, Integer> bst31 = new BST31<>();

            for (String s : sArray) {
                bst.put(s, s.codePointAt(0));
                bst3.put(s, s.codePointAt(0));
                bst31.put(s, s.codePointAt(0));
            }

            String expect = String.join(" ", bst.keys());
            StdOut.println(expect);
            bst3.inorderTraversalWithConstantExtraSpace();
            StdOut.println();
            StdOut.println(String.join(" ", bst3.keysWithConstantExtraSpace()));
            StdOut.println(String.join(" ", bst31.keysWithConstantExtraSpace()));
            assert expect.equals(String.join(" ", bst3.keysWithConstantExtraSpace()));
            assert expect.equals(String.join(" ", bst31.keysWithConstantExtraSpace()));

            StdOut.println();
        }
    }

    private static void test1() {
        double a = +0.0;
        double b = -0.0;
        Double x = new Double(a);
        Double y = new Double(b);

        System.out.println(a == b);
        System.out.println(x.equals(y));

        a = Double.NaN;
        b = Double.NaN;
        x = Double.NaN;
        y = Double.NaN;

        System.out.println(a == b);
        System.out.println(x.equals(y));
    }
}
