package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class PanoramaTestsAlex {

	@Test
	public void test() {
		Panorama.Builder pB = new Panorama.Builder(new PanoramaParameters(new GeoPoint(0, 0), 100, 1, 1, 100, 100, 100));
		int n = 0;
		for(int i = 0; i<100; ++i){
			for(int j = 0; j<100; ++j){
				pB.setDistanceAt(i, j, n);
				++n;
			}
		}
		Panorama pm = pB.build();
		
		int m=0;
		for(int i = 0; i<100; ++i){
			for(int j = 0; j<100; ++j){
				assertEquals(m, pm.distanceAt(i, j),0);
				++m;
			}
		}
	}
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void testIndexOutOfBounds() {
		Panorama.Builder pB = new Panorama.Builder(new PanoramaParameters(new GeoPoint(0, 0), 100, 1, 1, 100, 100, 100));
		int n = 0;
		for(int i = 0; i<100; ++i){
			for(int j = 0; j<100; ++j){
				pB.setDistanceAt(i, j, n);
				++n;
			}
		}
		pB.setDistanceAt(100, 101, 123);
		
		
	}
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void testIndexOutOfBounds1() {
		Panorama.Builder pB = new Panorama.Builder(new PanoramaParameters(new GeoPoint(0, 0), 100, 1, 1, 100, 100, 100));
		int n = 0;
		for(int i = 0; i<100; ++i){
			for(int j = 0; j<100; ++j){
				pB.setDistanceAt(i, j, n);
				++n;
			}
		}
		pB.setDistanceAt(101, 100, 123);		
	}
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void testIndexOutOfBounds2() {
		Panorama.Builder pB = new Panorama.Builder(new PanoramaParameters(new GeoPoint(0, 0), 100, 1, 1, 100, 100, 100));
		int n = 0;
		for(int i = 0; i<100; ++i){
			for(int j = 0; j<100; ++j){
				pB.setDistanceAt(i, j, n);
				++n;
			}
		}
		pB.setDistanceAt(-1, 100, 123);		
	}
	
	@Test (expected = IllegalStateException.class)
	public void testAlreadyBuilt() {
		Panorama.Builder pB = new Panorama.Builder(new PanoramaParameters(new GeoPoint(0, 0), 100, 1, 1, 100, 100, 100));
		int n = 0;
		for(int i = 0; i<100; ++i){
			for(int j = 0; j<100; ++j){
				pB.setDistanceAt(i, j, n);
				++n;
			}
		}
		pB.setDistanceAt(1, 99, 123);
		pB.build();
		pB.setDistanceAt(1, 1, 1);
	}
	
	@Test (expected = IllegalStateException.class)
	public void testAlreadyBuilt1() {
		Panorama.Builder pB = new Panorama.Builder(new PanoramaParameters(new GeoPoint(0, 0), 100, 1, 1, 100, 100, 100));
		int n = 0;
		for(int i = 0; i<100; ++i){
			for(int j = 0; j<100; ++j){
				pB.setDistanceAt(i, j, n);
				++n;
			}
		}
		pB.setDistanceAt(1, 99, 123);
		pB.build();
		pB.build();
	}
	
	@Test (expected = IllegalStateException.class)
	public void testAlreadyBuilt2() {
		Panorama.Builder pB = new Panorama.Builder(new PanoramaParameters(new GeoPoint(0, 0), 100, 1, 1, 100, 100, 100));
		int n = 0;
		for(int i = 0; i<100; ++i){
			for(int j = 0; j<100; ++j){
				pB.setDistanceAt(i, j, n);
				++n;
			}
		}
		pB.setDistanceAt(1, 99, 123);
		pB.build();
		pB.setLatitudeAt(1, 1, 1);
	}

}
