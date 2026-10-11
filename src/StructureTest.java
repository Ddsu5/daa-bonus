import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class StructureTest {
    public static void main(String[] args) {
        testBSTDeletion();
        testCollisions();
        testAgainstReference();
        testSortedTree();
        System.out.println("All correctness tests passed.");
    }

    private static void check(boolean condition) {
        if (!condition) {
            throw new AssertionError("Test failed");
        }
    }

    private static void testBSTDeletion() {
        BST tree = new BST();
        check(!tree.delete(5));
        for (int key : new int[]{50, 30, 70, 20, 40, 60, 80, 65}) {
            check(tree.insert(key));
        }
        check(!tree.insert(50));
        check(tree.delete(20));
        check(tree.delete(60));
        check(tree.search(65));
        check(tree.delete(50));
        for (int key : new int[]{30, 40, 65, 70, 80}) {
            check(tree.search(key));
            check(tree.delete(key));
        }
        check(!tree.search(50));
        check(tree.insert(9));
        check(tree.delete(9));
        tree.insert(9);
        tree.insert(4);
        check(tree.delete(9));
        check(tree.search(4));
    }

    private static void testCollisions() {
        HashTable table = new HashTable(5);
        for (int key : new int[]{-5, 0, 5, 10}) {
            check(table.insert(key));
        }
        check(!table.insert(5));
        check(!table.search(15));
        check(table.delete(5));
        check(table.delete(10));
        check(table.delete(-5));
        check(table.search(0));
        check(table.delete(0));
        check(!table.delete(0));
        check(table.insert(Integer.MIN_VALUE));
        check(table.search(Integer.MIN_VALUE));
        check(table.delete(Integer.MIN_VALUE));
    }

    private static void testAgainstReference() {
        BST tree = new BST();
        HashTable table = new HashTable(17);
        Set<Integer> reference = new HashSet<>();
        Random random = new Random(123);
        for (int i = 0; i < 20000; i++) {
            int key = random.nextInt(1000) - 500;
            int operation = random.nextInt(3);
            boolean expected;
            if (operation == 0) {
                expected = reference.add(key);
                check(tree.insert(key) == expected);
                check(table.insert(key) == expected);
            } else if (operation == 1) {
                expected = reference.contains(key);
                check(tree.search(key) == expected);
                check(table.search(key) == expected);
            } else {
                expected = reference.remove(key);
                check(tree.delete(key) == expected);
                check(table.delete(key) == expected);
            }
        }
        for (int key = -500; key < 500; key++) {
            check(tree.search(key) == reference.contains(key));
            check(table.search(key) == reference.contains(key));
        }
    }

    private static void testSortedTree() {
        BST tree = new BST();
        for (int i = 0; i < 10000; i++) check(tree.insert(i));
        check(tree.search(9999));
        check(!tree.search(10000));
        for (int i = 9999; i >= 0; i--) check(tree.delete(i));
        check(!tree.search(0));
    }
}
