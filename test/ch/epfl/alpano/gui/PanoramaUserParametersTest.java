package ch.epfl.alpano.gui;

import static org.junit.Assert.*;

import org.junit.Test;

public class PanoramaUserParametersTest {

	@Test (expected = NullPointerException.class)
	public void test() {
		new PanoramaUserParameters(null);		
	}
	
	
	PanoramaUserParameters p = new PanoramaUserParameters(50_000, 481_000, 121, 180, 100, 300, 110, 180, 2);
	
	@Test
	public void ParametersStockedCorrectlyTest(){
		assertEquals(60_000, p.observerLongitude(), 0);
		assertEquals(480_000, p.observerLatitude(), 0);
		assertEquals(300, p.observerElevation(), 0);
		assertEquals(180, p.centerAzimuth(), 0);
		assertEquals(100, p.horizontalFieldOfView(), 0);
		assertEquals(300, p.maxDistance(), 0);
		assertEquals(110, p.width(),0);
		assertEquals(180, p.height(), 0);
		assertEquals(2, p.superSamplingExponent(), 0);
	}

}
