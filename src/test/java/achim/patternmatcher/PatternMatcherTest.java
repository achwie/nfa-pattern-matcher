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

	@Test(expected = IllegalArgumentException.class)
	public void match_shouldNotAcceptNullAsInput() {
		PatternMatcher m = createMatcher("ta*ste*");
		m.matches(null);
	}

	@Test
	public void match_shouldUseDotAsPlaceholderForOneChar_WithRandomChar() {
		PatternMatcher m = createMatcher("t.st");
		assertTrue(m.matches("tast"));
	}

	@Test
	public void match_shouldUseDotAsPlaceholderForOneChar_NoMatchMissingChar() {
		PatternMatcher m = createMatcher("t.st");
		assertFalse(m.matches("tst"));
	}

	@Test
	public void match_shouldUseDotAsPlaceholderForOneChar_NoMatchTooShort() {
		PatternMatcher m = createMatcher("t.st");
		assertFalse(m.matches("tas"));
	}

	@Test
	public void match_shouldUseDotAsPlaceholderForOneChar_NoMatchTooLong() {
		PatternMatcher m = createMatcher("t.st");
		assertFalse(m.matches("taste"));
	}

	@Test
	public void match_shouldUseStarAsPlaceholderForAnyChars_WithNoChars() {
		PatternMatcher m = createMatcher("t*st");
		assertTrue(m.matches("tst"));
	}

	@Test
	public void match_shouldUseStarAsPlaceholderForAnyChars_WithRandomChars() {
		PatternMatcher m = createMatcher("t*st");
		assertTrue(m.matches("tasrestfdst"));
	}

	@Test
	public void match_shouldUseStarAsPlaceholderForAnyChars_WithTwoStarsInPattern() {
		PatternMatcher m = createMatcher("t*st*");
		assertTrue(m.matches("tasrestfdstdsa"));
	}

	@Test
	public void match_shouldUseStarAsPlaceholderForAnyChars_NoMatch() {
		PatternMatcher m = createMatcher("ta*ste*");
		assertFalse(m.matches("takdfskteklsd"));
	}

	private PatternMatcher createMatcher(String pattern) {
		return PatternMatcher.create(pattern);
	}
}
