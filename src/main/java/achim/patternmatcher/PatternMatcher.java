package achim.patternmatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * A very simple pattern matcher to match patterns against strings. Allows the
 * following metacharacters in patterns:
 * </p>
 * <ul>
 * <li><code>.</code> (dot) - Any char with exactly one occurence.
 * <li><code>*</code> (star) - Any char-sequence of length zero or more
 * </ul>
 * <p>
 * So the pattern <code>H.m*er</code> would match <code>Hammer</code>,
 * <code>Homer</code>, or <code>Hamburger</code> but not <code>Hmier</code>.
 * </p>
 * <p>
 * Internally, the matcher uses a NFA (nondeterministic finite automaton) to
 * check whether the input string matches the pattern or not. Inspiration for
 * the implementation came from Russ Cox' excellent article <a
 * href="http://swtch.com/~rsc/regexp/regexp1.html">Regular Expression Matching
 * Can Be Simple And Fast (but is slow in Java, Perl, PHP, Python, Ruby,
 * ...)</a>. I also loved the explanation with the "penny machine" from
 * Mark-Jason Dominus' article <a
 * href="http://perl.plover.com/Regex/article.html">How Regexes Work</a>.
 * </p>
 * 
 * @author Achim Wiedemann, Oct 3, 2012
 */
public class PatternMatcher {
	private final State startState;
	private List<State> currentStates = new ArrayList<State>();

	private PatternMatcher(String pattern) {
		startState = buildNfa(pattern);
	}

	/**
	 * Runs a match against a given input string.
	 * 
	 * @param str The input string
	 * @return
	 */
	public boolean matches(String str) {
		if (str == null)
			throw new IllegalArgumentException("Input string must not be null!");

		resetAutomaton();

		for (char ch : str.toCharArray())
			moveStates(ch);

		return isInMatchState();
	}

	/**
	 * Creates a new matcher for the given pattern.
	 * 
	 * @param pattern The pattern to match against input strings.
	 * @return The matcher for the given pattern.
	 */
	// Use factory method because we don't wanna throw exceptions from the c'tor
	public static PatternMatcher create(String pattern) {
		if (pattern == null)
			throw new IllegalArgumentException("Pattern must not be null!");

		return new PatternMatcher(pattern);
	}

	private void resetAutomaton() {
		currentStates.clear();
		currentStates.add(startState);
	}

	private void moveStates(char ch) {
		final List<State> next = new ArrayList<State>();

		// Since an NFA can be in multiple states at the same time, we need to
		// check which states are "reachable" from the current states
		for (State s : currentStates)
			next.addAll(s.transistableStates(ch));

		currentStates.clear();
		currentStates.addAll(next);
	}

	private boolean isInMatchState() {
		for (State s : currentStates)
			if (s.match)
				return true;

		return false;
	}

	private State buildNfa(String pattern) {
		State s = new State();
		final State startState = s;

		for (char ch : pattern.toCharArray()) {
			final Transition t = createTransition(ch, s);

			s.addTransition(t);
			s = t.outcome; // Move on to the next state
		}

		// The end of the pattern is the matching state
		s.match = true;

		return startState;
	}

	private Transition createTransition(char ch, State prevState) {
		switch (ch) {
		case '.':
			return new MatchAnySingleCharTransition(new State());
		case '*':
			// Create a loop in the state machine because with a star-metachar,
			// every input char is valid and leads to the previous state again
			return new MatchAnySingleCharTransition(prevState);
		default:
			return new MatchSingleCharTransition(new State(), ch);
		}
	}

	/**
	 * Represents a state in the state machine. Has a <code>match</code>-flag to
	 * determine whether the state machine is in a match-state (i.e. the input
	 * matches the pattern). A state can transist into other states, depending
	 * on the input.
	 */
	private static class State {
		private boolean match;
		private List<Transition> transitions = new ArrayList<Transition>();

		public void addTransition(Transition t) {
			transitions.add(t);
		}

		public List<State> transistableStates(char ch) {
			List<State> reachable = new ArrayList<State>(transitions.size());

			for (Transition t : transitions)
				if (t.mayTransist(ch))
					reachable.add(t.outcome);

			return reachable;
		}
	}

	/**
	 * Transitions control the flow of the state-machine. Based on conditions
	 * the automaton can transist from one state to another.
	 */
	private static abstract class Transition {
		private final State outcome;

		public Transition(State outcome) {
			this.outcome = outcome;
		}

		public abstract boolean mayTransist(char ch);
	}

	private static class MatchAnySingleCharTransition extends Transition {
		public MatchAnySingleCharTransition(State outcome) {
			super(outcome);
		}

		@Override
		public boolean mayTransist(char ch) {
			return true;
		}
	}

	private static class MatchSingleCharTransition extends Transition {
		private final char matchChar;

		public MatchSingleCharTransition(State outcome, char matchChar) {
			super(outcome);
			this.matchChar = matchChar;
		}

		@Override
		public boolean mayTransist(char ch) {
			return matchChar == ch;
		}
	}
}
