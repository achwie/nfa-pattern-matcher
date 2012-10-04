A very simple pattern matcher to match patterns against strings. Allows the following metacharacters in patterns:

 * `.` (dot) - Any char with exactly one occurence.
 * `*` (star) - Any char-sequence of length zero or more

So the pattern `H.m*er` would match `Hammer`, `Homer`, or `Hamburger` but not `Hmier`.

Internally, the matcher uses a NFA (nondeterministic finite automaton) to check whether the input string matches the pattern or not. Inspiration for the implementation came from Russ Cox' excellent article [Regular Expression Matching Can Be Simple And Fast (but is slow in Java, Perl, PHP, Python, Ruby, ...)](http://swtch.com/~rsc/regexp/regexp1.html). I also loved the explanation with the "penny machine" in Mark-Jason Dominus' article [How Regexes Work](http://perl.plover.com/Regex/article.html).
