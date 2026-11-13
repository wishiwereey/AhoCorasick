# Aho–Corasick
## 1. Project Description
This project implements Aho–Corasick algorithm for multiple pattern matching.

It includes:
- Trie construction
- Failure link construction using BFS
- Output links for reporting multiple matches
- Searching for all occurrences of patterns
- KMP algorithm for performance comparison
- Benchmark experiments using System.nanoTime()

## 2. How Aho–Corasick Works
The algorithm first constructs a trie containing all patterns. Patterns that share common prefixes also share trie nodes.
Next, failure links are constructed using breadth-first search. These links allow the algorithm to move to the longest suffix that is also a prefix of another pattern when a mismatch occurs.
Output links are used to report patterns ending at the current position, including patterns that are suffixes of other patterns.
During searching, the text is processed character by character, allowing multiple patterns to be found in a single scan.

## 3. Time and Space Complexity
- N = length of the text
- P = total length of all patterns
- K = number of patterns
- Z = total number of matches

### Aho–Corasick
- Trie construction: O(P) expected time
- Failure link construction: O(P) expected time with constant-time alphabet transitions, or potentially more with failure-chain traversal
- Searching: O(N + Z) expected time
- Overall: O(P + N + Z) under the standard bounded-alphabet assumption
- Space: O(P + Z)

### KMP
KMP searches for one pattern at a time.

- Single-pattern search: O(N + M)
- Multiple-pattern search: O(K × N + P + Z)
- Auxiliary space: O(L), where L is the maximum pattern length
- Total space including results: O(L + Z)

## 4. Experimental Setup
The benchmark uses:

Pattern counts: 10, 100, 1000. Text lengths: 1000, 10000, 100000
Each experiment is repeated 5 times. Execution time is measured using System.nanoTime() and converted to milliseconds.
Random texts and patterns use lowercase English letters. Pattern lengths range from 3 to 10 characters. A fixed random seed is used for reproducibility.
Both algorithms are tested on the same input data. 
The benchmark includes preprocessing time for both algorithms and performs a JVM warm-up before measurements.

## 5. Experimental Results
The CSV file contains:
- Number of patterns
- Text length
- Average Aho–Corasick execution time
- Average KMP execution time
- Number of matches

Results depend on the CPU, JVM, operating system, and runtime conditions.

## 6. Performance Discussion
Aho–Corasick is generally more efficient when searching for many patterns because the text is scanned only once.
Aho–Corasick requires additional memory to store the trie, failure links, and output links.

KMP is efficient for searching a single pattern but must scan the text separately for every pattern.
As the number of patterns increases, the execution time of repeated KMP searches tends to grow significantly.
For small numbers of patterns, KMP may be competitive because its preprocessing is relatively simple.

## 7. Advantages and Limitations

### Aho–Corasick
Advantages:
- Searches for multiple patterns simultaneously
- Processes the text in a single scan
- Efficient for large pattern sets

Limitations:
- More complex implementation
- Additional memory for trie nodes and links
- Preprocessing is required before searching

### KMP
Advantages:
- Simple and efficient for a single pattern
- Linear-time single-pattern matching
- Low auxiliary memory requirements

Limitations:
- Requires a separate scan for each pattern
- Less efficient for large numbers of patterns

## 8. Conclusion

Aho–Corasick is a suitable algorithm for searching multiple patterns in large texts. Its trie and failure links allow efficient matching without restarting the search for every pattern.

KMP remains a good choice for single-pattern searching, while Aho–Corasick is usually preferable when many patterns must be searched simultaneously.