package ch.epfl.alpano.gui;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

public class LabeledListConverterTest {

	LabeledListStringConverter c = new LabeledListStringConverter("zéro", "un", "deux", "trois");
	ArrayList<String> list = new ArrayList<>(Arrays.asList("zéro", "un", "deux", "trois"));
	
	@Test (expected = NullPointerException.class)
	public void NullStringThrowsNullPointerExceptionTest() {
		@SuppressWarnings("unused")
		LabeledListStringConverter a = new LabeledListStringConverter((String[])null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void InvalidInputFromStringTest(){
		c.fromString("aaa");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void EmptyStringForFromStringTest(){
		c.fromString("");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void NullStringForFromStringTest(){
		c.fromString(null);
	}
	
	@Test
	public void NormalValuesWorkTest(){
		for(int i=0; i<list.size(); ++i){
			assertEquals((int)i, (int)c.fromString(list.get(i)));
		}
	}
	
	@Test (expected = ArrayIndexOutOfBoundsException.class)
	public void ValueDoesntExistTest(){
		c.toString(-1);
	}
	
	@Test (expected = ArrayIndexOutOfBoundsException.class)
	public void ValueDoesntExistTest1(){
		c.toString(4);
	}
	
	@Test
	public void ValuesAreCorrectForToString(){
		for(int i=0; i<list.size(); ++i){
			assertEquals(list.get(i), c.toString(i));
		}
	}
	
	
	

}
