# FJS string searching algorithm sample code

String search algorithms find occurrences of a pattern string in a text, like the search feature of a text editor. The FJS (Franek-Jennings-Smyth) algorithm is the fastest known string search algorithm under a wide variety of conditions. It combines the linear-time worst case guarantee of the well-known KMP (Knuth-Morris-Pratt) algorithm with the fast average-case performance of the BMS (Boyer-Moore-Sunday) algorithm.

[More information, including an interactive demo of the algorithm in action.](https://cgjennings.ca/articles/fjs.html)

## The sample code

Sample implementations are currently provided in C and Java. Both implementations find *all* matches of the pattern string in the text, rather than simply finding the first or last.

### c/

The C implementation is meant as a starting point that you can customize to suit your specific needs. Note that it is also based on 8-bit characters. For wider characters you might want to adapt the simple hash strategy demonstrated by the Java code to improve performance on short texts. Another option is to process the string as 8-bit characters and ignore spurious matches. For example, cast pointers to 16-bit character strings to byte array pointers and then ignore "matches" that start at an odd offset.

### java/

The Java implementation is based on the `CharSequence` interface, making it usable with a number of common text-storing classes including `String`, `StringBuilder`, and `CharBuffer`. However, this also means it may be slower than, for example, `String.indexOf` for short texts, since that method has direct access to the underlying `char[]` while FJS must go through the `charAt` method. It is easy to adapt the code to work directly on raw arrays if desired.

The Java implementation returns matches as an `IntStream`, making it suitable for use with Java 8's functional programming model. Again, this is easy to change if desired.

## Comparing string search algorithms

When comparing string search algorithms, it is critical to distinguish between best-case performance and average-case performance. Virtually all searches you perform will be with "average-case" strings. This is because the worst-case strings involve searching for periodic patterns and texts, like searching for `aaab` in `aaaaaaaaaaaaaaaa`.

###Knuth-Morris-Pratt (KMP) is usually slow

If you have ever been disappointed by implementing the KMP algorithm you learned in school only to discover that it is *slower* than a simple, naïve implementation, you've been bitten by this difference. KMP performs well on worst-case strings, but on average-case strings it does pretty much the same thing the naïve algorithm does, just with more overhead. KMP is almost always the slowest algorithm you can choose for string search.

### Boyer-Moore (BM) and friends are usually fast

Boyer-Moore is another algorithm you may have heard of. On average-case strings it will beat the pants off of KMP and the naïve algorithm. It is able to skip past parts of the string that can't possibly match, so it can often find matches in sublinear time. That is, where the naïve algorithm always looks at every letter in the text at least once, BM usually examines only a fraction of the text. However, in order to do this it must set up a table whose size is that of the alphabet used by the strings. So, with 8-bit ASCII characters it must set up a table with 2^8^=256 entries for each search. In order to beat the naïve algorithm, the text you are searching needs to be significantly longer than the size of this table before the setup cost will be worthwhile. This can be a problem when dealing with Unicode, which is often stored in memory using 16-bit characters. (There are [other issues](http://unicode.org/reports/tr15/) to consider when searching Unicode as well.)

All of the above is only relevant when dealing with average-case strings, though. If you happen to feed it worst-case strings, you will get performance similar to the naïve algorithm.

One other thing to be careful of with BM is that some sources describe it as a linear-time or O(*n*) algorithm. This is only true when finding the *first* occurrence of a pattern in a text. KMP (and FJS) find *all* occurrences of the pattern in linear time.

### FJS is always fast

The FJS algorithm given here is always fast. It has worst-case performance about the same as KMP and average-case performance that is usually faster than any variant of BM (there are several). FJS is actually a hybrid algorithm that combines KMP with the fast BMS (Boyer-Moore-Sunday) algorithm in such a way that it ends up being significantly faster even than BMS on its own.

The caveat is that it requires *two* additional tables, one from each of its algorithmic parents. The first table is proportional to the size of the pattern. The cost of this table is generally insignificant. If the pattern is short, the cost is trivial. If the pattern is long, the cost is made up for by the fact that there are fewer possible match positions in the text, and the algorithm can make bigger jumps.

The second table, as with other algorithms related to BM, is proportional to the size of the alphabet. This can be a problem, as mentioned above. However, the Java implementation demonstrates a simple hashing strategy that greatly reduces the size of this table without significantly affecting performance except for certain perverse cases.

In extensive experimental testing, FJS was the fastest overall of a wide variety of string search algorithms. Even in the few tests where FJS was not fastest it was still near the top. This makes it an excellent choice as a general purpose string search algorithm.