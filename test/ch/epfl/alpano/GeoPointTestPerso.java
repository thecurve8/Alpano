package ch.epfl.alpano;

import static org.junit.Assert.*;
import ch.epfl.alpano.GeoPoint;

import org.junit.Test;

public class GeoPointTestPerso {
	
	@Test (expected = IllegalArgumentException.class)
	public void LongitudeTooSmall(){
		new GeoPoint(-3.15, 0.8118);

	}
	
	@Test (expected = IllegalArgumentException.class)
	public void LongitudeTooBig(){
		new GeoPoint(3.15, 0.8118);

	}
	
	@Test (expected = IllegalArgumentException.class)
	public void LatitudeTooSmall(){
		new GeoPoint(-2.15, -2.15);

	}
	
	@Test (expected = IllegalArgumentException.class)
	public void LatitudeTooBig(){
		new GeoPoint(-2.15, 2.15);

	}
	
	@Test
	public void ZeroPoint(){
		new GeoPoint(0,0);
	}
	
	@Test
	public void CheckLongitude(){
		GeoPoint a = new GeoPoint(1.154, 0.9876);
		assertEquals(1.154, a.longitude(), 0);
	}
	
	@Test
	public void CheckLatitude(){
		GeoPoint a = new GeoPoint(1.154, 0.9876);
		assertEquals(0.9876, a.latitude(), 0);
	}
	
	@Test
	public void distanceRolexEiger(){
		GeoPoint Rolex = new GeoPoint(0.114620484, 0.811888596);
		GeoPoint Eiger = new GeoPoint(0.139720064, 0.81293178);
		
		double expected = 110176.923;
		
		assertEquals(expected, Rolex.distanceTo(Eiger), 0.1);
	}

	@Test
	public void AzimuthSameLongitude(){
		GeoPoint Rolex = new GeoPoint(46.517282*Math2.PI2/360, 6.56724*Math2.PI2/360);
		GeoPoint Jet = new GeoPoint(46.517282*Math2.PI2/360, 6.15615*Math2.PI2/360);
		
		assertEquals(0, Jet.azimuthTo(Rolex), 0.001);
	}
	
	@Test
	public void AzimuthSameLatitude(){
		GeoPoint Rolex = new GeoPoint(46.517282*Math2.PI2/360, 6.56724*Math2.PI2/360);
		GeoPoint Jet = new GeoPoint(46.20741*Math2.PI2/360, 6.56724*Math2.PI2/360);
		
		assertEquals(90*Math2.PI2/360, Jet.azimuthTo(Rolex), 0.001);
	}
	
	@Test
	public void AzimuthRolexEiger(){

		GeoPoint Rolex = new GeoPoint(0.114620484, 0.811888596);
		GeoPoint Eiger = new GeoPoint(0.139720064, 0.81293178);
		
		double claculALaMain = 1.501330685; //Result found with the same formulas, coherent with the data on the Internet 
		
		assertEquals(claculALaMain, Rolex.azimuthTo(Eiger), 0.0000001);
	}

	@Test
	public void AzimuthToTestLausMoskow(){
		GeoPoint Laus = new GeoPoint(0.11573278, 0.811944621);
		GeoPoint Moskow = new GeoPoint(0.656645, 0.973073);
		
		assertEquals(52.95*Math2.PI2/360, Laus.azimuthTo(Moskow), 0.0001);
	}
	
	@Test
	public void DistanceToTestLausMoskow(){
		GeoPoint Laus = new GeoPoint(0.11573278, 0.811944621);
		GeoPoint Moskow = new GeoPoint(0.656645, 0.973073);
		
		assertEquals(2370000, Laus.distanceTo(Moskow), 8000);
	}
	
	@Test
	public void DistanceToSame(){
		GeoPoint Laus = new GeoPoint(0.1157, 0.8118);
		
		assertEquals(0, Laus.distanceTo(Laus), 0);
	}
	
	@Test
	public void AzimuthSouthPoleToNorthPole(){
		GeoPoint south = new GeoPoint(1, -Math.PI/2+0.001);
		GeoPoint north = new GeoPoint(1, Math.PI/2-0.001);
		
		assertEquals(0, south.azimuthTo(north), 0.0001);
		assertEquals(Math.PI, north.azimuthTo(south), 0.0001);
	}

	@Test
	public void DistanceSouthNorthPole(){
		GeoPoint south = new GeoPoint(1, -Math.PI/2);
		GeoPoint north = new GeoPoint(1, Math.PI/2);
		
		double expected = Math.PI*Distance.EARTH_RADIUS;
		
		assertEquals(expected, south.distanceTo(north), 1);
		assertEquals(expected, north.distanceTo(south), 1);
	}
	
	@Test
	public void StringOfGeoPoint(){
		GeoPoint a = new GeoPoint(10*Math2.PI2/360, -20*Math2.PI2/360);
		
		assertEquals("(10.0000,-20.0000)", a.toString());
	}
	
	@Test
	public void StringOfGeoPoint1(){
		GeoPoint a = new GeoPoint(10.112233*Math2.PI2/360, -20.112233*Math2.PI2/360);
		
		assertEquals("(10.1122,-20.1122)", a.toString());
	}
	
	@Test
	public void StringOfGeoPoint2(){
		GeoPoint a = new GeoPoint(0*Math2.PI2/360, 0*Math2.PI2/360);
		
		assertEquals("(0.0000,0.0000)", a.toString());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void StringOfIllegalPoint(){
		GeoPoint a = new GeoPoint(0*Math2.PI2/360, 100*Math2.PI2/360);
		a.toString();
	}
}
