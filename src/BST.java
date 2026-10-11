public class BST {
    private static class Node {
        int key;
        Node left;
        Node right;

        Node(int key) {
            this.key = key;
        }
    }

    private Node root;

    public boolean insert(int key) {
        if (root == null) {
            root = new Node(key);
            return true;
        }
        Node current = root;
        while (true) {
            if (key == current.key) {
                return false;
            }
            if (key < current.key) {
                if (current.left == null) {
                    current.left = new Node(key);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node(key);
                    return true;
                }
                current = current.right;
            }
        }
    }

    public boolean search(int key) {
        Node current = root;
        while (current != null) {
            if (key == current.key) {
                return true;
            }
            current = key < current.key ? current.left : current.right;
        }
        return false;
    }

    public boolean delete(int key) {
        Node parent = null;
        Node current = root;
        while (current != null && current.key != key) {
            parent = current;
            current = key < current.key ? current.left : current.right;
        }
        if (current == null) {
            return false;
        }

        if (current.left != null && current.right != null) {
            Node successorParent = current;
            Node successor = current.right;
            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }
            current.key = successor.key;
            parent = successorParent;
            current = successor;
        }

        Node child = current.left != null ? current.left : current.right;
        if (parent == null) {
            root = child;
        } else if (parent.left == current) {
            parent.left = child;
        } else {
            parent.right = child;
        }
        return true;
    }
}
