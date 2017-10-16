package ca.cgjennings.algo;

import java.util.stream.IntStream;

/**
 * A brute force or naïve implementation of {@link StringSearcher}. Brute force
 * string search finds all occurrences of a pattern in a text in O(m*n) time.
 *
 * @author Christopher G. Jennings
 */
public final class BruteForceStringSearcher implements StringSearcher {

    /**
     * Creates a new {@code StringSearcher} that uses brute force to find
     * matches.
     */
    public BruteForceStringSearcher() {
    }

    @Override
    @SuppressWarnings( "empty-statement" )
    public IntStream findAll( CharSequence p, CharSequence x ) {
        final int m = p.length(), n = x.length();

        final IntStream.Builder stream = IntStream.builder();
        int i, j;

        for( j=0; j <= n-m; ++j ) {
            for( i=0; i < m && p.charAt(i) == x.charAt(i+j); ++i );
            if( i >= p.length() ) {
                stream.accept(j);
            }
        }

        return stream.build();
    }
}