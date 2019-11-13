/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.dem;

import static ch.epfl.alpano.Distance.toRadians;
import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Math2.floorMod;
import static ch.epfl.alpano.Math2.lerp;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.asin;
import static java.lang.Math.ceil;
import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.scalb;
import static java.lang.Math.sin;
import static java.lang.Math.PI;
import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.GeoPoint;

/**
 * ElevationProfile Elevation of an interval of a given length in meters, starting at a given origin.
 */
public final class ElevationProfile {

	private static final int DISTANCE_BETWEEN_MESURES = 4096;

	private final ContinuousElevationModel cem;
	private final double length;
	
	private final double[] importantMesures;
	private final int LENGTH_HALF_LIST;

	/**
	 * Constructor of the class, initializes important values and creates an array of important latitude/longitude for optimization
	 * @param elevationModel ContinuousElevation from which th ElevationProfile is created
	 * @param origin GeoPoint from which the ElevationProfile starts
	 * @param azimuth double direction of the ElevationProfile from the origin
	 * @param length double length of the ElevationProfile
	 */
	public ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint origin, double azimuth, double length) {
		//Various checks
		checkArgument(Azimuth.isCanonical(azimuth), "Azimuth is not canonical");
		checkArgument(length>0, "Length is not strictly positive");
		requireNonNull(origin);

		this.cem = requireNonNull(elevationModel);
		this.length = length;
		
		//Amount of measures to cover length
		LENGTH_HALF_LIST = (int)ceil(((length/DISTANCE_BETWEEN_MESURES) + 1));
		
		//Creating a list of doubles. The first part contains all the longitudes and the second half all the latitudes of the needed points separated by a DISTANCE_BETWEEN_MEASURES distance. 
		//Uses a single dimension array to improve performances
		importantMesures = new double[(LENGTH_HALF_LIST * 2)];
		
		for(int i=0; i<LENGTH_HALF_LIST; ++i){
			importantMesures[i] = getLongitudeForElevationProfile(i * DISTANCE_BETWEEN_MESURES, origin, azimuth);
			importantMesures[i+LENGTH_HALF_LIST] = getLatitudeForElevationProfile(i * DISTANCE_BETWEEN_MESURES, origin, azimuth);
		}
	}
	
	/**
	 * Returns the elevation at a point at distance x in meters from the origin
	 * @param x the distance in meters
	 * @return double the elevation at the point at distance x from the origin
	 */
	public double elevationAt(double x){
		checkArgument(x <= length && x >= 0, "Position is not in interval");
		return cem.elevationAt(positionAt(x));
	}
	
	/**
	 * Returns the GeoPoint position at a distance x in meters from the origin using linear interpolation with the array initialized in the constructor
	 * @param x the distance in meters
	 * @return GeoPoint the position (latitude/longitude) at distance x from the origin
	 */
	public GeoPoint positionAt(double x){
		checkArgument(x <= length && x >= 0, "Position is not in interval");
		
		// x/4096 = scalb(x, -12)
		double indexOfX = scalb(x, -12);
		int indexBeforeX = (int)(floor(indexOfX));
		double restOfX = indexOfX - indexBeforeX;

		// we take the two closest measures we already have and interpolate between them
		double longitude = lerp(importantMesures[indexBeforeX], importantMesures[indexBeforeX+1], restOfX);
		double latitude = lerp(importantMesures[indexBeforeX + LENGTH_HALF_LIST], importantMesures[indexBeforeX + LENGTH_HALF_LIST + 1], restOfX);
			
		return new GeoPoint(longitude, latitude);
	}
	
	/**
	 * Returns the GeoPoint position at a distance x in meters from the origin using linear interpolation with the array initialized in the constructor
	 * @param x the distance in meters
	 * @return double the slope at the point at distance x from the origin
	 */
	public double slopeAt(double x){
		checkArgument(x <= length && x >= 0, "Position is not in interval");
		return cem.slopeAt(positionAt(x));	
	}
	
	/**
	 * Returns the latitude at a point at distance meters from the origin
	 * @param meters the distance from the origin
	 * @param origin GeoPoint the origin 
	 * @param azimuth double azimuth from the origin to the point
	 * @return double the latitude
	 */
	private double getLatitudeForElevationProfile(double meters, GeoPoint origin, double azimuth){
		double radian = toRadians(meters);
		return asin(sin(origin.latitude()) * cos(radian) + cos(origin.latitude()) * sin(radian) * cos(Azimuth.toMath(azimuth)));
	}
	
	/**
	 * Returns the longitude at a point at distance meters from the origin
	 * @param meters the distance from the origin
	 * @param origin GeoPoint the origin 
	 * @param azimuth double azimuth from the origin to the point
	 * @return double the longitude
	 */
	private double getLongitudeForElevationProfile(double meters, GeoPoint origin, double azimuth){
		
		double radian = toRadians(meters);
		double intermediateASin = asin((sin(Azimuth.toMath(azimuth)) * sin(radian)) / cos(getLatitudeForElevationProfile(meters, origin, azimuth)));
		
		return floorMod((origin.longitude() - intermediateASin + PI), PI2) - PI;
	}

}
