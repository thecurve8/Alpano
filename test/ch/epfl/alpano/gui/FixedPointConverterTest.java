package ch.epfl.alpano.gui;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class FixedPointConverterTest {

	FixedPointStringConverter c = new FixedPointStringConverter(4);

	@Test (expected = IllegalArgumentException.class)
	public void NumberOfDecimalsIsStrictlyPositiveTest() {
		new FixedPointStringConverter(-1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void NumberOfDecimalsIsStrictlyPositiveTest1() {
		new FixedPointStringConverter(0);
	}
	
	@Test (expected = NumberFormatException.class)
	public void InvalidInputForFromString(){
		c.fromString("un");
	}
	
	@Test (expected = NumberFormatException.class)
	public void EmptyStringForFromString(){
		c.fromString("");
	}
	
	@Test (expected = NullPointerException.class)
	public void NullForFromString(){
		c.fromString(null);
	}
	
	@Test
	public void FromStringWorksForGivenValues(){
		ArrayList<String> list = new ArrayList<>(Arrays.asList("00.00000", "12.3456789", "555.55555", "9", "5.54321", "444.4444" ));
		ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(0, 123457, 5555556, 90000, 55432, 4444444));
		for(int i=0; i<list.size(); ++i){
			assertEquals(expected.get(i), c.fromString(list.get(i)));
		}
	}
	
	@Test
	public void ToStringWorksWithGivenValuesTest(){
		ArrayList<String> expected = new ArrayList<>(Arrays.asList("0", "12.3457", "555.5556", "9", "5.5432", "444.4444", "0.0001", "3.2" ));
		ArrayList<Integer> list = new ArrayList<>(Arrays.asList(0, 123457, 5555556, 90000, 55432, 4444444, 1, 32000));
		for(int i=0; i<list.size(); ++i){
			assertEquals(expected.get(i), c.toString(list.get(i)));
		}
		
	}
	
	

}
