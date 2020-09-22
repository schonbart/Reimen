package reimen;

import java.util.List;
import java.util.Map;

/**
 * This class is derived from Reimer to be instantiated and tested.
 * @author tim
 *
 */
public class DeutschRapper extends Reimer {
	
	/**
	 * Constructor.
	 */
	public DeutschRapper() {
		super();
		this.addAllRhymes("a", "e", "i", "o", "u", "au", "eu", "ei", "y");
		this.addRhymeRule("a", "ei");
		
	}
	
	/** 
	 * A main-method for testing.
	 * @param args Console parameters (not used).
	 */
	public static void main(String[] args) {
		Reimer a = new DeutschRapper();
		String string1 = "Eiersalat";
		String string2 = "Ei er eilat";
		System.out.println(a.rhymesFromString(string1).toString());
		System.out.println(a.rhymesFromString(string2).toString());
		System.out.println(a.doTheyRhyme("Eiersalat", "a er ei ei", true));
	}

}
