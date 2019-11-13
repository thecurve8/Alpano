/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.gui;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ch.epfl.alpano.Preconditions;
import javafx.util.StringConverter;

/**
 * FixedPointStringConverter Manages the conversion between strings numbers with decimals
 */
public final class FixedPointStringConverter extends StringConverter<Integer> {
	private final int numberOfDecimals;
	
	/**
	 * Constructor of the class, defines the number of decimals to use
	 * @param numberOfDecimals
	 */
	public FixedPointStringConverter(int numberOfDecimals) {
		Preconditions.checkArgument(numberOfDecimals>0, "The number of Decimals has to be strictly positive");
		this.numberOfDecimals = numberOfDecimals;
	}

	@Override
	public String toString(Integer integer) {
		BigDecimal bigDecimal = new BigDecimal(integer).movePointLeft(numberOfDecimals).stripTrailingZeros();
		return bigDecimal.toPlainString();
	}

	@Override
	public Integer fromString(String string) {
		BigDecimal bigDecimal = new BigDecimal(string);
		BigDecimal finalBigDecimal = bigDecimal.movePointRight(numberOfDecimals).setScale(0, RoundingMode.HALF_UP).stripTrailingZeros();
		return finalBigDecimal.intValueExact();
	}

}
