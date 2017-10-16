package ca.cgjennings.algo;

import java.util.Arrays;
import java.util.Random;

/**
 * Some simple tests for this FJS algorithm implementation. I have not used
 * a testing framework (such as JUnit) to keep things simple: this is meant as
 * sample code rather than a library.
 *
 * @author Christopher G. Jennings
 */
public final class StringSearcherTests {
	public static void main(String[] args) {
		final String[] tests = new String[] {
			"aabxaabxaabxaabx",      "aaa", // best case
			"aaaaaaaaaaaaaaa",       "aba", // worst case
			"aabaabaabaxbabxaaabaa", "aabaa",
			"aaaaaaaaaaaaaaa",       "aaaa",
			"baabaabab",             "aabab",
			"bxaccxa",               "bbcc",
			"The cat sat on the mat", "at",
			"abacadabrabracabracadabrabrabracad", "abracadabra",
			"bbbbbbbbbbbbbbb", "a",
			"", "",
			"", "a",
			"a", "",
			fibostring(5), fibostring(3),
			fibostring(7), fibostring(4),
			fibostring(5), fibostring(1),
			fibostring(1), fibostring(5),
			fibostring(6), fibostring(6),
			fibostring( rand.nextInt(10) ), fibostring( rand.nextInt(5) ),
			fibostring( rand.nextInt(10) ), fibostring( rand.nextInt(5) ),
			fibostring( rand.nextInt(10) ), fibostring( rand.nextInt(5) ),
			fibostring( rand.nextInt(10) ), fibostring( rand.nextInt(5) ),
			fibostring( rand.nextInt(10) ), fibostring( rand.nextInt(5) ),
			fibostring( rand.nextInt(10) ), fibostring( rand.nextInt(5) ),
			fibostring( rand.nextInt(10) ), fibostring( rand.nextInt(5) ),
			fibostring( rand.nextInt(10) ), fibostring( rand.nextInt(5) ),
			fibostring( rand.nextInt(10) ), fibostring( rand.nextInt(5) ),
			fibostring( rand.nextInt(10) ), fibostring( rand.nextInt(5) ),
			fibostring( rand.nextInt(10) ), fibostring( rand.nextInt(5) ),
			fibostring( rand.nextInt(10) ), fibostring( rand.nextInt(5) ),
			randstring(12), randstring(14),
			randstring(12), randstring(13),
			randstring(12), randstring(12),
			randstring(12), randstring(11),
			randstring(12), randstring(10),
			randstring(12), randstring(9),
			randstring(12), randstring(8),
			randstring(12), randstring(7),
			randstring(12), randstring(6),
			randstring(12), randstring(5),
			randstring(12), randstring(4),
			randstring(12), randstring(3),
			randstring(12), randstring(2),
			randstring(12), randstring(1),
			randstring(12), "",
			randstring(12), randstring(3),
			randstring(12), randstring(2),
			randstring(12), randstring(1),
			randstring(12), randstring(3),
			randstring(12), randstring(2),
			randstring(12), randstring(1),
			randstring(12), randstring(3),
			randstring(12), randstring(2),
			randstring(12), randstring(1),
			randstring(12), randstring(3),
			randstring(12), randstring(2),
			randstring(12), randstring(1),
			randstring(12), randstring(3),
			randstring(12), randstring(2),
			randstring(12), randstring(1),
			randstring(12), randstring(3),
			randstring(12), randstring(2),
			randstring(12), randstring(1),
			randstring(12), randstring(3),
			randstring(12), randstring(2),
			randstring(12), randstring(1),
			randstring(2000), randstring(1),
		};

		runTests( tests );
	}

	private static void runTests( String[] tests ) {
		int passed = 0;
		StringSearcher fjs = new FJSStringSearcher();

		for( int t=0; t < tests.length; ) {
			final String text = tests[t++];
			final String pattern = tests[t++];

			passed += check( fjs, pattern, text );
		}

		System.out.printf( "Done, %d of %d tests passed\n", passed, tests.length/2 );
	}

	private static int check( StringSearcher toTest, String pattern, String text ) {
		final int[] a = toTest.findAll(pattern, text).toArray();
		final int[] b = new BruteForceStringSearcher().findAll(pattern, text).toArray();

		if( Arrays.equals( a, b ) ) {
			return 1;
		}

		System.out.println( "Failed:" );
		System.out.format( "  p=\"%s\", t=\"%s\"\n", pattern, text );
		System.out.format( "  got %s but expected %s\n", Arrays.toString(a), Arrays.toString(b) );

		return 0;
	}



	/**
	 * Returns the Fibonacci string of order {@code order}.
	 */
	private static String fibostring( int order ) {
		if( order == 0 ) {
			return "b";
		}

		String previous = fibostring( order-1 );
		StringBuilder current = new StringBuilder( previous.length() * 2 );

		for( int i=0; i < previous.length(); ++i ) {
			switch( previous.charAt(i) ) {
				case 'a':
					current.append( "ab" );
					break;
				case 'b':
					current.append( "a" );
					break;
			}
		}
		return current.toString();
	}

	/**
	 * Returns a pseudorandom string of length {@code len} consisting of only
	 * the letters 'a', 'b', and 'c'.
	 */
	private static String randstring( int len ) {
		final StringBuilder b = new StringBuilder( len );
		final char[] abc = new char[] {'a','b','c'};
		for( int i=0; i < len; ++i ) {
			b.append( abc[ rand.nextInt(3) ] );
		}
		return b.toString();
	}

    /** Used to generate deterministic "random" strings for testing. */
    private static final Random rand = new Random( 0xf );
}
