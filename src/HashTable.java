public class HashTable {
    private static class Node {
        int key;
        Node next;

        Node(int key, Node next) {
            this.key = key;
            this.next = next;
        }
    }

    private final Node[] buckets;

    public HashTable(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        buckets = new Node[capacity];
    }

    private int hash(int key) {
        return Math.floorMod(key, buckets.length);
    }

    public boolean insert(int key) {
        int index = hash(key);
        Node current = buckets[index];
        while (current != null) {
            if (current.key == key) {
                return false;
            }
            current = current.next;
        }
        buckets[index] = new Node(key, buckets[index]);
        return true;
    }

    public boolean search(int key) {
        Node current = buckets[hash(key)];
        while (current != null) {
            if (current.key == key) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public boolean delete(int key) {
        int index = hash(key);
        Node previous = null;
        Node current = buckets[index];
        while (current != null) {
            if (current.key == key) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }
}
