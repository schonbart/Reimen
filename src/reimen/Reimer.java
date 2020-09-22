package reimen;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Diese Klasse beinhaltet Methoden, um anhand von selbstgewählten Regeln Reime zu finden.
 * This class contains methods for finding rhymes through self-defined rules.
 * @author tim
 *
 */
public abstract class Reimer {
	private Map<String, List<String>> rhymeRules;
	private Map<Integer, List<String>> rhymeLengths;
	private int longestRhymeLength = 0;

	/**
	 * Constructor.
	 */
	public Reimer() {
		rhymeRules = new HashMap<String, List<String>>();
		rhymeLengths = new HashMap<Integer, List<String>>();
	}

	/**
	 * Adds a rhyme. Rhymes are reflective automatically.
	 * @param rhyme The added rhyme.
	 */
	public void addRhyme(String rhyme) {
		if (!rhymeRules.containsKey(rhyme)) {
			List<String> list = new ArrayList<String>();
			list.add(rhyme);
			rhymeRules.put(rhyme, list);

			List<String> list2 = new ArrayList<String>();
			list2.add(rhyme);
			rhymeLengths.put(rhyme.length(), list2);
		}
	}

	/**
	 * Adds a rhyme-rule. Rhyme-rules are symmetric automatically.
	 * @param rhyme1 The first rhyme.
	 * @param rhyme2 The second rhyme.
	 */
	public void addRhymeRule(String rhyme1, String rhyme2) {
		if (rhymeRules.containsKey(rhyme1)) {
			if (!rhymeRules.get(rhyme1).contains(rhyme2)) {
				rhymeRules.get(rhyme1).add(rhyme2);
				rhymeRules.get(rhyme2).add(rhyme1);
			}
		}
	}

	/**
	 * Adds all rhymes.
	 * @param rhymes The rhymes.
	 */
	public void addAllRhymes(String... rhymes) {
		for (String rhyme : rhymes) {
			addRhyme(rhyme);
		}
	}

	/**
	 * Extracts the self-defined rhymes from a String.
	 * @param s The string.
	 * @return A list, containing the rhymes in order.
	 */
	public List<String> rhymesFromString(String s) {
		String sLowerCase = s.toLowerCase();
		List<Integer> rhymeLengths = getRhymeLengths();
		List<String> rhymes = new ArrayList<String>();
		for (int i = 0; i < s.length(); i++) {
			for (Integer j : rhymeLengths) {
				if (i + j < s.length()) {
					String comparedString = "";
					for (int h = 0; h < j; h++) {
						comparedString += sLowerCase.charAt(i + h);
					}
					if (rhymeRules.containsKey(comparedString)) {
						rhymes.add(comparedString);
						i += j;
					}
				}
			}
		}
		return rhymes;
	}

	/**
	 * Returns all rhyme-lengths, in descending order.
	 * @return All rhyme-lenghts.
	 */
	private List<Integer> getRhymeLengths() {
		Comparator<Integer> comparator = new Comparator<Integer>() {
			@Override
			public int compare(Integer arg0, Integer arg1) {
				if (arg0 == arg1)
					return 0;
				else if (arg0 > arg1)
					return -1;
				else
					return 1;
			}
		};
		return rhymeLengths.keySet().parallelStream().sorted(comparator).collect(Collectors.toList());
	}

	/**
	 * Determines if the two given strings rhyme.
	 * @param string1 The first string.
	 * @param string2 The second string.
	 * @param onlyLastSyllables Indicates if only the last syllables of string1 should be rhymed
	 * 							of if the strings should complete rhyme.
	 * @return Do they rhyme?
	 */
	public boolean doTheyRhyme(String string1, String string2, boolean onlyLastSyllables) {
		List<String> rhymes1 = rhymesFromString(string1);

		// List<String> rhymes2 = new ArrayList<String>();
		List<Integer> rhymeLengths = getRhymeLengths();
		String s2LowerCase = string2.toLowerCase();
		int count = rhymes1.size() - 1;

		for (int i = string2.length() - 1; i >= 0; i--) {
			for (Integer j : rhymeLengths) {
				if (i - j + 1>= 0) {
					String comparedString = "";
					for (int h = 0; h < j; h++) {
						comparedString = s2LowerCase.charAt(i - h) + comparedString;
					}
					if (rhymeRules.containsKey(comparedString)) {
						if (count < 0) {
							return true;
						}
						if (rhymeRules.get(comparedString).contains(rhymes1.get(count))) {
							// rhymes2.add(comparedString);
							i -= j;
							count--;
						} else {
							return false;
						}
					}
				}
			}
		}
		if (count < 0) {
			return true;
		} else {
			return false;
		}
	}
}
