package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.alpano.summit.Summit;

public class SumitTestAlex {

	@Test (expected =  NullPointerException.class)
	public void testNull() {
		new Summit(null, new GeoPoint(0, 0), 0);
	}
	
	@Test
	public void normalSummit(){
		new Summit("Test", new GeoPoint(1, 1), 12);
	}
	
	Summit a = new Summit("Alex", new GeoPoint(0.5, -0.6), 123);
	
	@Test 
	public void testName(){
		assertTrue(a.name().equals("Alex"));
	}
	
	@Test
	public void testPosition(){
		assertTrue(a.position().longitude() == 0.5);
		assertTrue(a.position().latitude() == -0.6);
	}
	
	@Test
	public void testElevation(){
		assertTrue(a.elevation() == 123);
	}
	
	@Test
	public void StringTest(){
		assertEquals("Alex (28.6479,-34.3775) 123", a.toString());
	}
}
