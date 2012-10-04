package achim.patternmatcher;

/**
 * @author achim, Oct 3, 2012
 */
public class PatternMatcher {
	private final String pattern;

	private PatternMatcher(String pattern) {
		this.pattern = pattern;
	}

	public boolean matches(String str) {
		return pattern.equals(str);
	}

	public static PatternMatcher create(String pattern) {
		if (pattern == null)
			throw new IllegalArgumentException("Pattern must not be null!");

		return new PatternMatcher(pattern);
	}
}
