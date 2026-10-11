# BST vs Hash Table
Daniyal Akylbek, SE-2530

## Project

This project implements a Binary Search Tree (BST) and a hash table in Java,
then compares insertion, searching, and deletion. Both store unique integer
keys. The BST is unbalanced; the hash table handles collisions with linked
lists (separate chaining).

## Time complexity

Here, *n* is the number of keys and *alpha* is the hash table's load factor.
Hash table average times assume a good distribution of keys and a bounded
load factor.

| Structure | Operation | Best | Average | Worst |
|---|---|---:|---:|---:|
| BST | Insert | O(1) | O(log n) | O(n) |
| BST | Search | O(1) | O(log n) | O(n) |
| BST | Delete | O(1) | O(log n) | O(n) |
| Hash table | Insert | O(1) | O(1 + alpha) | O(n) |
| Hash table | Search | O(1) | O(1 + alpha) | O(n) |
| Hash table | Delete | O(1) | O(1 + alpha) | O(n) |

BST operations depend on the tree height. Random insertion usually creates a
shorter tree, while sorted insertion can turn it into a chain. Hash table
operations are usually constant time when keys spread across buckets. If many
keys collide in one bucket, operations may take O(n). This implementation uses
a fixed bucket array and does not resize it.

## Experiment

The program tested 100, 1,000, and 10,000 unique keys. It measured random
insertion order for each size and also tested ascending sorted insertion as a
case that can make an unbalanced BST slow. The hash table used `2 * n + 1`
buckets. Each displayed time is the median of seven runs and represents a full
batch of *n* operations, in milliseconds. Searches were successful; deletion
removed all inserted keys. Full results, including the sorted-order runs, are
in [`results/results.csv`](results/results.csv).

| Elements | Structure | Insert (ms) | Search (ms) | Delete (ms) |
|---:|---|---:|---:|---:|
| 100 | BST | 0.009835 | 0.004597 | 0.005639 |
| 100 | Hash table | 0.003886 | 0.003576 | 0.003696 |
| 1,000 | BST | 0.066839 | 0.050705 | 0.040430 |
| 1,000 | Hash table | 0.052328 | 0.026540 | 0.008613 |
| 10,000 | BST | 0.980341 | 0.706425 | 0.770020 |
| 10,000 | Hash table | 0.277772 | 0.208739 | 0.109092 |

With random input, the hash table was faster in these measurements. With
10,000 sorted keys, the BST took 67.900 ms to insert, 94.505 ms to search, and
58.509 ms to delete; sorted insertion made it behave like a chain. The hash
table times for that same sorted-key test were 0.192, 0.057, and 0.069 ms.

Small timing differences can be affected by the computer, Java runtime, and
other running processes. The results illustrate this test workload; they do
not guarantee identical times on another computer.

## Conclusion

The hash table performed faster in this experiment because the keys were
distributed across its buckets. The ordinary BST was sensitive to insertion
order: sorted input produced a long chain and much slower operations. A BST
keeps keys ordered, while a hash table is useful for fast key lookup when order
is not needed. Both structures can take O(n) time in the worst case.

## Run

Requires JDK 11 or newer; no external libraries are needed. From the project
root:

    mkdir out
    javac -d out src/BST.java src/HashTable.java src/Main.java src/StructureTest.java
    java -cp out StructureTest
    java -cp out Main

`StructureTest` runs correctness checks. `Main` runs the benchmark and
overwrites `results/results.csv`.