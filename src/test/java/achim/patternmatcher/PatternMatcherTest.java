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

	@Test
	public void match_shouldUseDotAsPlaceholderForOneChar_WithRandomChar() {
		PatternMatcher m = createMatcher("t.st");
		assertTrue(m.matches("tast"));
	}

	@Test
	public void match_shouldUseDotAsPlaceholderForOneChar_WithNoCharGivenShouldNotMatch() {
		PatternMatcher m = createMatcher("t.st");
		assertFalse(m.matches("tst"));
	}

	@Test
	public void match_shouldUseDotAsPlaceholderForOneChar_WithRandomCharButTooShort() {
		PatternMatcher m = createMatcher("t.st");
		assertFalse(m.matches("tas"));
	}

	@Test
	public void match_shouldUseDotAsPlaceholderForOneChar_WithRandomCharButTooLong() {
		PatternMatcher m = createMatcher("t.st");
		assertFalse(m.matches("taste"));
	}

	private PatternMatcher createMatcher(String pattern) {
		return PatternMatcher.create(pattern);
	}
}
