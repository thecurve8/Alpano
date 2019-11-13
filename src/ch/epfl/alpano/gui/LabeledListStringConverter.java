/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.gui;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;

import javafx.util.StringConverter;

/**
 * LabeledListStringConverter Manages the conversion between string representation and integer representation of a number
 */
public final class LabeledListStringConverter extends StringConverter<Integer> {
	private final List<String> strings;
	
	/**
	 * Constructor of the class that takes as input a variable number of strings representing numbers
	 * @param s
	 */
	public LabeledListStringConverter(String... s){
		requireNonNull(s);
		this.strings = Arrays.asList(s);
	}
	
	@Override
	public String toString(Integer integer) {
		return strings.get(integer);
	}

	@Override
	/**
	 * Converts the string provided into the corresponding integer in the list of Strings.
	 * @param string Provided string representation of a number
	 * @return Integer Corresponding Integer to the given string representation
	 * @throws IllegalArgumentException if the given string doesn't exist in the array and NullPointerException if string is null
	 */
	public Integer fromString(String string) {
		requireNonNull(string);
		Integer indexOfString = strings.indexOf(string);
		if(indexOfString == -1){
			throw new IllegalArgumentException("The given string doesn't have a correspondance as a number");
		}
		return indexOfString;
	}

}
