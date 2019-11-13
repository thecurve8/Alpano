/**
 * Classe GeoPoint
 * 
 * @author Alexander Apostolov (271798)
 * @author Yannick Bloem (262179)
 *
 */

package ch.epfl.alpano;
import static ch.epfl.alpano.Math2.*;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;
import static java.lang.Math.PI;

import java.util.Locale;

/**
 * A point on the surface of Earth
 */
public final class GeoPoint {
	private final double longitude;
	private final double latitude;
	
	/**
	 * Constructor 
	 * 
	 * @param longitude longitude of the point
	 * @param latitude latitude of the point
	 * @throw IllegalArgumentException if longitude not in interval [-pi; pi]
	 * @throw IllegalArgumentException if latitude not in interval [-pi/2; pi/2]
	 * 
	 */
	public GeoPoint(double longitude, double latitude){
		Preconditions.checkArgument(longitude <= PI && longitude >= -PI, "Longitude has to be in the interval [-pi; pi]"); 
		Preconditions.checkArgument(latitude <= PI/2 && latitude >= -PI/2, "Latitude has to be in the interval [-pi/2; pi/2]"); 
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/**
	 * Longitude of the GeoPoint
	 * @return longitude of the point
	 */
	public double longitude(){
		return longitude;
	}
	
	/**
	 * Latitude of the GeoPoint
	 * @return latitude of the point
	 */
	public double latitude(){
		return latitude;
	}
	
	/**
	 * Distance to another GeoPoint
	 * @param that other GeoPoint
	 * @return distance in meters to the second point
	 */
	public double distanceTo(GeoPoint that){
		double differenceLatitude = this.latitude - that.latitude();
		double differenceLongitude  = this.longitude - that.longitude();
		return Distance.toMeters(2*asin(sqrt(haversin(differenceLatitude)+cos(this.latitude)*cos(that.latitude)*haversin(differenceLongitude))));
	}
	
	/**
	 * Azimuth in the direction of another GeoPoint
	 * @param that other GeoPoint
	 * @return The Azimuth in the direction of the other GeoPoint
	 */
	public double azimuthTo(GeoPoint that){
		double differenceLongitude  = this.longitude - that.longitude();
		
		double nominator = sin(differenceLongitude)*cos(that.latitude);
		double denominator = cos(this.latitude)*sin(that.latitude)-sin(this.latitude)*cos(that.latitude)*cos(differenceLongitude);
		return Azimuth.fromMath(Azimuth.canonicalize(atan2(nominator, denominator)));
	}
	
	/**
	 * String representing the longitude and latitude of the GeoPoint rounded to fourth decimal and in degrees
	 */
	@Override
	public String toString(){
		Locale l = null;
		return String.format(l, "(%.4f,%.4f)", Math.toDegrees(longitude), Math.toDegrees(latitude));		
	}
}
