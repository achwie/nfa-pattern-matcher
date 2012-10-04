package achim.patternmatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Matches patterns against strings. Allows for the following metacharacters in
 * patterns:
 * <ul>
 * <li><code>.</code> - Any char with exactly one occurence.
 * </ul>
 * 
 * @author achim, Oct 3, 2012
 */
public class PatternMatcher {
	private final State automaton;
	private List<State> current = new ArrayList<State>();

	private PatternMatcher(String pattern) {
		automaton = analyze(pattern);
	}

	public boolean matches(String str) {
		current.clear();
		current.add(automaton);

		final List<State> next = new ArrayList<State>();
		for (char ch : str.toCharArray()) {
			next.clear();

			for (State s : current)
				next.addAll(s.nextStatesFor(ch));

			current.clear();
			current.addAll(next);
		}

		for (State s : current)
			if (s.match)
				return true;

		return false;
	}

	// Use factory method because we don't wanna throw exceptions from the c'tor
	public static PatternMatcher create(String pattern) {
		if (pattern == null)
			throw new IllegalArgumentException("Pattern must not be null!");

		return new PatternMatcher(pattern);
	}

	/**
	 * Builds an automaton from a given matcher-pattern.
	 * 
	 * @param pattern The pattern to analyze
	 * @return The start-state of the automaton
	 */
	private State analyze(String pattern) {
		State s = new State();
		final State startState = s;

		for (char ch : pattern.toCharArray()) {
			final Transition t;

			switch (ch) {
			case '.':
				t = new MatchAnyCharTransition();
				break;
			default:
				t = new MatchSingleCharTransition(ch);
				break;
			}

			s.addTransition(t);
			t.outcome = new State();

			s = t.outcome;
		}

		s.match = true;

		return startState;
	}

	private static class State {
		private boolean match;
		private List<Transition> transitions = new ArrayList<Transition>();

		public void addTransition(Transition t) {
			transitions.add(t);
		}

		public List<State> nextStatesFor(char ch) {
			List<State> reachable = new ArrayList<State>(transitions.size());

			for (Transition t : transitions)
				if (t.mayTransist(ch))
					reachable.add(t.outcome);

			return reachable;
		}
	}

	private static abstract class Transition {
		private State outcome;

		public abstract boolean mayTransist(char ch);
	}

	private static class MatchAnyCharTransition extends Transition {
		@Override
		public boolean mayTransist(char ch) {
			return true;
		}
	}

	private static class MatchSingleCharTransition extends Transition {
		private final char matchChar;

		public MatchSingleCharTransition(char matchChar) {
			this.matchChar = matchChar;
		}

		@Override
		public boolean mayTransist(char ch) {
			return matchChar == ch;
		}
	}
}
