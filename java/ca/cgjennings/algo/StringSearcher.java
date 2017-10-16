package ca.cgjennings.algo;

import java.util.stream.IntStream;

/**
 * A base class for implementing algorithms for searching for occurrences of
 * a pattern within a text string. Any {@link CharSequence} can be used for
 * the pattern and text, including {@link String}s.
 *
 * <p>Empty strings are treated as matching at every possible index,
 * including the index after the last character in the text.
 *
 * <p>This class has been set up so that implementations can easily be made
 * asynchronous, although that is not provided out of the box.
 *
 * @author Christopher G. Jennings
 */
public interface StringSearcher {
	/**
	 * Finds all matches of the pattern within the text and calls the specified
	 * listener to report their locations.
	 *
	 * @param pattern the pattern to search for
	 * @param text the text to search within
	 * @return a stream of the indices at which a match was found
	 */
	IntStream findAll( CharSequence pattern, CharSequence text );
}
