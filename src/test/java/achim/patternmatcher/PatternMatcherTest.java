package achim.patternmatcher;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * @author achim, Oct 3, 2012
 */
public class PatternMatcherTest {

	@Test
	public void match_shouldMatchPlainString() {
		PatternMatcher m = createMatcher("test");
		assertTrue(m.matches("test"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void match_shouldNotAcceptNullAsPattern() {
		createMatcher(null);
	}

	private PatternMatcher createMatcher(String pattern) {
		return PatternMatcher.create(pattern);
	}
}
