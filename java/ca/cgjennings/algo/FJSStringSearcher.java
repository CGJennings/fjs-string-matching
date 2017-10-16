package ca.cgjennings.algo;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Finds all occurrences of a pattern string in a text string. It is guaranteed
 * to run in O(n) time, but usually runs in much less. This implementation uses
 * a simple hashing strategy to avoid paying a penalty for Unicode's large
 * alphabet size, with the consequence that this implementation will be slower
 * for certain large texts with many hash collisions than an unhashed FJS
 * implementation (very rare in practice).
 *
 * @author Christopher G. Jennings
 */
public final class FJSStringSearcher implements StringSearcher {

    /**
     * Creates a new {@code StringSearcher} that uses the FJS algorithm.
     */
    public FJSStringSearcher() {
        // reused since it does not depend on pattern size
        delta = new int[ ALPHABET_HASH_SIZE ];
    }

    // The hash size must be a power of 2; typical texts may not see a speedup
    // from using FJS if they are around this length or smaller.
    private static final int ALPHABET_HASH_SIZE = 128;
    private static final int HASH_MASK = ALPHABET_HASH_SIZE - 1;
    private final int[] delta;

    @Override
    public IntStream findAll( CharSequence p, CharSequence x ) {
        final int n = x.length();
        final int m = p.length();

        if( m == 0 ) {
            return IntStream.rangeClosed( 0, n );
        }
        if( m > n ) {
            return IntStream.empty();
        }

        final int beta[] = makeBeta( p );
        @SuppressWarnings( "LocalVariableHidesMemberVariable" )
        final int delta[] = makeDelta( p );
        final IntStream.Builder stream = IntStream.builder();

        int mp = m - 1, np = n - 1, i = 0, ip = i + mp, j = 0;

        outer:
        while( ip < np ) {
            if( j <= 0 ) {
                while( p.charAt( mp ) != x.charAt( ip ) ) {
                    ip += delta[x.charAt( ip + 1 ) & HASH_MASK];
                    if( ip >= np ) {
                        break outer;
                    }
                }
                j = 0;
                i = ip - mp;
                while( (j < mp) && (x.charAt(i) == p.charAt(j)) ) {
                    ++i;
                    ++j;
                }
                if( j == mp ) {
                    stream.accept( i-mp );
                    ++i;
                    ++j;
                }
                if( j <= 0 ) {
                    ++i;
                } else {
                    j = beta[j];
                }
            } else {
                while( (j < m) && (x.charAt(i) == p.charAt(j)) ) {
                    ++i;
                    ++j;
                }
                if( j == m ) {
                    stream.accept( i-m );
                }
                j = beta[j];
            }
            ip = i + mp - j;
        }

        // check final alignment p[0..m-1] == x[n-m..n-1]
        if( ip == np ) {
            if( j < 0 ) {
                j = 0;
            }
            i = n - m + j;
            while( j < m && x.charAt(i) == p.charAt(j) ) {
                ++i;
                ++j;
            }
            if( j == m ) {
                stream.accept( n-m );
            }
        }

        return stream.build();
    }

    /**
     * Construct the FJS Δ array for the pattern.
     *
     * @param pattern the search pattern
     */
    private int[] makeDelta( CharSequence pattern ) {
        final int m = pattern.length();
        @SuppressWarnings( "LocalVariableHidesMemberVariable" )
        final int[] delta = this.delta;

        Arrays.fill( delta, m + 1 );
        for( int i=0; i < m; ++i ) {
            final char ch = pattern.charAt(i);
            final int slot = ch & HASH_MASK;
            final int jump = m - i;
            if( jump < delta[slot] ) {
                delta[slot] = jump;
            }
        }
        return delta;
    }

    /**
     * Returns a new FJS β′ array for the pattern.
     *
     * @param pattern the search pattern
     * @return a new β′ array based on the borders of the pattern
     */
    private int[] makeBeta( CharSequence pattern ) {
        final int m = pattern.length();
        final int[] beta = new int[ m + 1 ];
        int i = 0, j = beta[0] = -1;

        while( i < m ) {
            while( (j > -1) && (pattern.charAt(i) != pattern.charAt(j)) ) {
                j = beta[j];
            }

            ++i;
            ++j;
            if( (i < m) && (pattern.charAt(i) == pattern.charAt(j)) ) {
                beta[i] = beta[j];
            } else {
                beta[i] = j;
            }
        }
        return beta;
    }
}