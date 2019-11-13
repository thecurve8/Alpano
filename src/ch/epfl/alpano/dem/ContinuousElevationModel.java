/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.dem;
import static ch.epfl.alpano.Math2.bilerp;
import static ch.epfl.alpano.Math2.sq;
import static ch.epfl.alpano.dem.DiscreteElevationModel.sampleIndex;
import static java.lang.Math.acos;
import static java.lang.Math.floor;
import static java.lang.Math.sqrt;
import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;

/**
 * Continuous DEM - defined everywhere
 */
public final class ContinuousElevationModel {
	//Constant distance between two sample points of the DEM (same distance vertically and horizontally)
	private final static double DISTANCE_BETWEEN =  Distance.toMeters(1/DiscreteElevationModel.SAMPLES_PER_RADIAN);
	
	private final DiscreteElevationModel dem;
	
	/**
	 * Constructor of the Continuous DEM, requiring a non-null DEM, IllegalArgumentException thrown otherwise
	 * @param dem DEM used for the Continuous DEM
	 */
	public ContinuousElevationModel(DiscreteElevationModel dem){
		this.dem = requireNonNull(dem);
	}
	
	/**
	 * Elevation at a given GeoPoint
	 * @param p given GeoPoint
	 * @return approximation of the elevation of the given GeoPoint, calculated with bidimensional linear interpolation  
	 */
	public double elevationAt(GeoPoint p){
		
		//Floored indexes of the GeoPoint p
		int indexLongGeo = (int)floor(sampleIndex(p.longitude()));
		int indexLatGeo = (int)floor(sampleIndex(p.latitude()));

		//Elevation of the four closest points of the GeoPoint p 
		double elevation1 = elevationAtInExtension(indexLongGeo, indexLatGeo);
		double elevation2 = elevationAtInExtension(indexLongGeo + 1, indexLatGeo);
		double elevation3 = elevationAtInExtension(indexLongGeo, indexLatGeo + 1);
		double elevation4 = elevationAtInExtension(indexLongGeo + 1, indexLatGeo + 1);

		//Fractional part of the Index of the GeoPoint. Needed for the bilerp method which takes a value between 0 and 1 
		double x = getFractionalPartForBilerp(sampleIndex(p.longitude()));
		double y = getFractionalPartForBilerp(sampleIndex(p.latitude()));
				 
		return bilerp(elevation1, elevation2, elevation3, elevation4, x, y);
	}
	
	/**
	 * Slope at a given GeoPoint
	 * @param p given GeoPoint
	 * @return approximation of the slope of the given GeoPoint, calculated with bidimensional linear interpolation  
	 */
	public double slopeAt(GeoPoint p){

		//Floored indexes of the GeoPoint p
		int indexLongGeo = (int)floor(sampleIndex(p.longitude()));
		int indexLatGeo = (int)floor(sampleIndex(p.latitude()));

		//Slope of the four closest points of the GeoPoint p 
		double slope1 = slopeAtInExtension(indexLongGeo, indexLatGeo);
		double slope2 = slopeAtInExtension(indexLongGeo + 1, indexLatGeo);
		double slope3 = slopeAtInExtension(indexLongGeo, indexLatGeo + 1);
		double slope4 = slopeAtInExtension(indexLongGeo + 1, indexLatGeo + 1);
		
		//Fractional part of the Index of the GeoPoint. Needed for the bilerp method which takes a value between 0 and 1 
		double x = getFractionalPartForBilerp(sampleIndex(p.longitude()));
		double y = getFractionalPartForBilerp(sampleIndex(p.latitude()));
		
		return bilerp(slope1, slope2, slope3, slope4, x, y);	
	}
	
	/**
	 * Elevation at any given index 
	 * @param x int first coordinate of the index
	 * @param y int second coordinate of the index
	 * @return elevation if index lies in dem's extent, 0 otherwise
	 */
	private double elevationAtInExtension(int x, int y){
		if(dem.extent().contains(x, y)){
			return dem.elevationSample(x, y);
		}else{
			return 0;
		}
	}
	
	/**
	 * Slope at any given index 
	 * @param x int first coordinate of the index
	 * @param y int second coordinate of the index
	 * @return slope if index lies in dem's extent (formula gives 0 if outside of dem's extent)
	 */
	private double slopeAtInExtension(int x, int y){
		double delta1 = elevationAtInExtension(x+1, y) - elevationAtInExtension(x, y);
		double delta2 = elevationAtInExtension(x, y+1) - elevationAtInExtension(x, y);
		return acos(DISTANCE_BETWEEN / (sqrt(sq(delta1) + sq(delta2) + sq(DISTANCE_BETWEEN))));
	}
	
	/**
	 * Returns fractional part of a double, if the number is negative, returns 1- (the fractional part)
	 * @param n double 
	 * @return fractional part of n
	 */
	private static double getFractionalPartForBilerp(double n) {     
	        return n - floor(n);
	}
}
