package ch.epfl.alpano;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PanoramaParametersTestAlex {

	@Test
	public void testNormal() {
		new PanoramaParameters(new GeoPoint(0, 0), 0, 0, 1, 100, 10, 10);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void failarguments1(){
		new PanoramaParameters(new GeoPoint(0, 0), 0, 6.30, 1, 100, 10, 10);

	}
	@Test (expected = IllegalArgumentException.class)
	public void failarguments2(){
		new PanoramaParameters(new GeoPoint(0, 0), 0, -6.30, 1, 100, 10, 10);
	}
	@Test (expected = IllegalArgumentException.class)
	public void failarguments3(){
		new PanoramaParameters(new GeoPoint(0, 0), 0, 0, 0, 100, 10, 10);
	}
	@Test (expected = IllegalArgumentException.class)
	public void failarguments4(){
		new PanoramaParameters(new GeoPoint(0, 0), 0, 0, 3.15*2, 100, 10, 10);

	}
	@Test (expected = IllegalArgumentException.class)
	public void failarguments5(){
		new PanoramaParameters(new GeoPoint(0, 0), 0, 0, 1, 100, 0, 10);

	}
	@Test (expected = IllegalArgumentException.class)
	public void failarguments6(){
		new PanoramaParameters(new GeoPoint(0, 0), 0, 0, 1, 100, 10, 0);

	}
	@Test (expected = IllegalArgumentException.class)
	public void failarguments7(){
		new PanoramaParameters(new GeoPoint(0, 0), 0, 0, 1, 0, 10, 10);

	}

	@Test
	public void verticalFieldOfView(){
		PanoramaParameters a = new PanoramaParameters(new GeoPoint(0, 0), 0, 0, 1.5, 100, 20, 10);
		assertEquals(0.7105263, a.verticalFieldOfView(), 0.0001);
	}
	
	//delta = 2/500 
	PanoramaParameters odd = new PanoramaParameters(new GeoPoint(0, 0), 0, 1, 2, 200, 501, 11);
	
	@Test
	public void testAzimuthForX1(){
		assertEquals(1, odd.azimuthForX(250), 0);
	}
	
	@Test
	public void testAzimuthForX2(){
		assertEquals(1-250*2.0/500, odd.azimuthForX(0), 0);
	}
	
	@Test
	public void testAzimuthForX3(){
		assertEquals(1-25*2.0/50, odd.azimuthForX(0), 0);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void IllegalxForAzimuthFromX(){
		odd.azimuthForX(-1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void IllegalxForAzimuthFromX1(){
		odd.azimuthForX(501);
	}
	
	@Test 
	public void AzimuthForX4(){
		assertEquals(1 + 250*2.0/500, odd.azimuthForX(500), 0);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void xForAzimuthTest1(){
		odd.xForAzimuth(2.001);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void xForAzimuthTest2(){
		odd.xForAzimuth(-0.0001);
	}
	
	@Test 
	public void xForAzimuthTest3(){
		assertEquals(250, odd.xForAzimuth(1), 0);
	}
	
	@Test 
	public void xForAzimuthTest4(){
		assertEquals(0, odd.xForAzimuth(0), 0);
	}
	
	@Test 
	public void xForAzimuthTest5(){
		assertEquals(499, odd.xForAzimuth(2-(2.0/500)), 0.0000001);
	}
	
	@Test
	public void testAltitudeForY1(){
		assertEquals(0, odd.altitudeForY(5), 0);
	}
	
	@Test
	public void testAltitudeForY2(){
		assertEquals(5*2.0/500, odd.altitudeForY(0), 0);
	}
	
	@Test
	public void testAltitudeForY3(){
		assertEquals(-5*2.0/500, odd.altitudeForY(10), 0);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void IllegalxForAltituderomY(){
		odd.altitudeForY(-1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void IllegalxForAltitudeFromY1(){
		odd.altitudeForY(11);
	}
	
	@Test 
	public void TestAltitudeForY4(){
		assertEquals(-4*2.0/500, odd.altitudeForY(9), 0);
	}
	
	//vh = 2/500 * 10 = 2/50 -> from -2/100 to +2/100
	
	@Test (expected = IllegalArgumentException.class)
	public void yForAltitudeTest1(){
		odd.yForAltitude(2.0/100 +0.0001);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void yForAltitudeTest2(){
		odd.yForAltitude(-2.0/100-0.0001);
	}
	
	@Test 
	public void yForAltitudeTest3(){
		assertEquals(5, odd.yForAltitude(0), 0);
	}
	
	@Test 
	public void yForAltitudeTest4(){
		assertEquals(0, odd.yForAltitude(2.0/100), 1E-8);
	}
	
	@Test 
	public void yForAltitudeTest5(){
		assertEquals(9, odd.yForAltitude(-2.0/100 + (2.0/500)), 1E-6);
	}
	
	@Test
	public void TestIsValidIndex(){
		assertTrue(odd.isValidSampleIndex(0, 0));
	}
	
	@Test
	public void TestIsValidIndex0(){
		assertTrue(odd.isValidSampleIndex(500, 0));
	}
	
	@Test
	public void TestIsValidIndex1(){
		assertTrue(odd.isValidSampleIndex(0, 10));
	}
	
	@Test
	public void TestIsValidIndex2(){
		assertTrue(odd.isValidSampleIndex(500, 10));
	}
	
	@Test
	public void TestIsValidIndex3(){
		assertTrue(odd.isValidSampleIndex(50, 1));
	}
	
	@Test
	public void TestIsValidIndex4(){
		assertFalse(odd.isValidSampleIndex(501, 10));
	}
	
	@Test
	public void TestIsValidIndex5(){
		assertFalse(odd.isValidSampleIndex(501, 11));
	}
	
	@Test
	public void TestIsValidIndex6(){
		assertFalse(odd.isValidSampleIndex(50, 11));
	}
	
	@Test
	public void TestIsValidIndex7(){
		assertFalse(odd.isValidSampleIndex(50, -1));
	}
	
	@Test
	public void TestIsValidIndex8(){
		assertFalse(odd.isValidSampleIndex(-1, -1));
	}
	
	@Test
	public void TestIsValidIndex9(){
		assertFalse(odd.isValidSampleIndex(-1, 1));
	}
	
	@Test
	public void TestIsValidIndex10(){
		assertFalse(odd.isValidSampleIndex(-123, 123));
	}
	
	@Test
	public void TestLinearSample(){
		assertEquals(0, odd.linearSampleIndex(0, 0));
	}
	
	@Test
	public void TestLinearSample1(){
		assertEquals(1, odd.linearSampleIndex(1, 0));
	}
	
	@Test
	public void TestLinearSample2(){
		assertEquals(501*5 +4, odd.linearSampleIndex(4, 5));
	}
	
	/////////Even width/////
	
	//delta = 2/499 
	PanoramaParameters even = new PanoramaParameters(new GeoPoint(0, 0), 0, 1, 2, 200, 500, 10);
	
	@Test
	public void testAzimuthForX1even(){
		assertEquals(1, even.azimuthForX(249.5), 0);
	}
	
	@Test
	public void testAzimuthForX2even(){
		assertEquals(0, even.azimuthForX(0), 0.0000000001);
	}
	
	@Test
	public void testAzimuthForX3even(){
		assertEquals(1+249.5*2.0/499, even.azimuthForX(499), 0);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void IllegalxForAzimuthFromXeven(){
		even.azimuthForX(-1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void IllegalxForAzimuthFromX1even(){
		even.azimuthForX(500);
	}
	
	@Test 
	public void AzimuthForX4even(){
		assertEquals(1 + 2.0/499, even.azimuthForX(250.5), 0);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void xForAzimuthTest1even(){
		even.xForAzimuth(2.001);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void xForAzimuthTest2even(){
		even.xForAzimuth(-0.0001);
	}
	
	@Test 
	public void xForAzimuthTest3even(){
		assertEquals(249.5, even.xForAzimuth(1), 0);
	}
	
	@Test 
	public void xForAzimuthTest4even(){
		assertEquals(0, even.xForAzimuth(0), 0.00000001);
	}
	
	@Test 
	public void xForAzimuthTest5even(){
		assertEquals(498, even.xForAzimuth(2-(2.0/499)), 0);
	}
	
	@Test
	public void testAltitudeForY1even(){
		assertEquals(0, even.altitudeForY(4.5), 0);
	}
	
	@Test
	public void testAltitudeForY2even(){
		assertEquals(4.5*2.0/499, even.altitudeForY(0), 0.0000001);
	}
	
	@Test
	public void testAltitudeForY3even(){
		assertEquals(-4.5*2.0/499, even.altitudeForY(9), 0.000001);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void IllegalxForAltituderomYeven(){
		even.altitudeForY(-0.0001);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void IllegalxForAltitudeFromY1even(){
		even.altitudeForY(10);
	}
	
	@Test 
	public void TestAltitudeForY4even(){
		assertEquals(-3.5*2.0/499, even.altitudeForY(8), 0.000001);
	}
	
	//vh = 2/500 * 9 = 9/250 -> from -9/500 to +9/500
	
	@Test (expected = IllegalArgumentException.class)
	public void yForAltitudeTest1even(){
		even.yForAltitude(9.0/499 +0.0001);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void yForAltitudeTest2even(){
		even.yForAltitude(-9.0/499-0.0001);
	}
	
	@Test 
	public void yForAltitudeTest3even(){
		assertEquals(4.5, even.yForAltitude(0), 0);
	}
	
	@Test 
	public void yForAltitudeTest4even(){
		assertEquals(0, even.yForAltitude(4.5 * 2.0/499), 0.00000001);
	}
	
	@Test 
	public void yForAltitudeTest5even(){
		assertEquals(8, even.yForAltitude(-9.0/499 + (2.0/499)), 0.000001);
	}
	
	@Test
	public void TestIsValidIndexeven(){
		assertTrue(even.isValidSampleIndex(0, 0));
	}
	
	@Test
	public void TestIsValidIndex0even(){
		assertTrue(even.isValidSampleIndex(499, 0));
	}
	
	@Test
	public void TestIsValidIndex1even(){
		assertTrue(even.isValidSampleIndex(0, 9));
	}
	
	@Test
	public void TestIsValidIndex2even(){
		assertTrue(even.isValidSampleIndex(499, 9));
	}
	
	@Test
	public void TestIsValidIndex3even(){
		assertTrue(even.isValidSampleIndex(50, 1));
	}
	
	@Test
	public void TestIsValidIndex4even(){
		assertFalse(even.isValidSampleIndex(500, 9));
	}
	
	@Test
	public void TestIsValidIndex5even(){
		assertFalse(even.isValidSampleIndex(500, 10));
	}
	
	@Test
	public void TestIsValidIndex6even(){
		assertFalse(even.isValidSampleIndex(50, 10));
	}
	
	@Test
	public void TestIsValidIndex7even(){
		assertFalse(even.isValidSampleIndex(50, -1));
	}
	
	@Test
	public void TestIsValidIndex8even(){
		assertFalse(even.isValidSampleIndex(-1, -1));
	}
	
	@Test
	public void TestIsValidIndex9even(){
		assertFalse(even.isValidSampleIndex(-1, 1));
	}
	
	@Test
	public void TestIsValidIndex10even(){
		assertFalse(even.isValidSampleIndex(-123, 123));
	}
	
	@Test
	public void TestLinearSampleeven(){
		assertEquals(0, even.linearSampleIndex(0, 0));
	}
	
	@Test
	public void TestLinearSample1even(){
		assertEquals(1, even.linearSampleIndex(1, 0));
	}
	
	@Test
	public void TestLinearSample2even(){
		assertEquals(500*5 +4, even.linearSampleIndex(4, 5));
	}
	
}
