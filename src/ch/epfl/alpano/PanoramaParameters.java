/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano;

import static java.util.Objects.requireNonNull;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static ch.epfl.alpano.Math2.angularDistance;

/**
 * Class containing needed paramters for the representation of panorama
 */
public final class PanoramaParameters {
	private final GeoPoint observerPosition;
	private final int observerElevation;
	private final double centerAzimuth;
	private final double horizontalFieldOfView;
	private final double verticalFieldOfView;
	private final int maxDistance;
	private final int width;
	private final int height;
	
	/**
	 * Constructor of the class
	 * @param observerPosition GeoPoint position of the observer of the panorama
	 * @param observerElevation int elevation of the observer of the panorama (same as elevation of central point of the panorama)
	 * @param centerAzimuth double azimuth at central point of the panorama
	 * @param horizontalFieldOfView double horizontal extent of the panorama in radians 
	 * @param maxDistance int maximum visible distance of the panorama
	 * @param width int width of the panorama
	 * @param height int height of the panorama
	 */
	public PanoramaParameters(GeoPoint observerPosition, int observerElevation, double centerAzimuth, double horizontalFieldOfView, int maxDistance, int width, int height){
		checkArgument(Azimuth.isCanonical(centerAzimuth), "Azimuth is not canonical");
		checkArgument(horizontalFieldOfView > 0 && horizontalFieldOfView <= Math2.PI2, "horizontal field of view not between 0 (excluded) and 2PI (included)");
		checkArgument(width > 0, "width not positive");
		checkArgument(height > 0, "height not positive");
		checkArgument(maxDistance > 0, "max distance not positive");
		
		this.observerPosition = requireNonNull(observerPosition);
		this.observerElevation = observerElevation;
		this.centerAzimuth = centerAzimuth;
		this.horizontalFieldOfView = horizontalFieldOfView;
		this.verticalFieldOfView = horizontalFieldOfView * (height - 1) / (width - 1);
		this.maxDistance = maxDistance;
		this.width = width;
		this.height = height;
	}

	/**
	 * Position of the observer of the panorama
	 * @return GeoPoint observerPosition
	 */
	public GeoPoint observerPosition() {
		return observerPosition;
	}

	/**
	 * Elevation of the observer (and the central point) of the panoram
	 * @return int observerElevation
	 */
	public int observerElevation() {
		return observerElevation;
	}

	/**
	 * Azimuth of the center of the panorama 
	 * @return double centerAzimuth
	 */
	public double centerAzimuth() {
		return centerAzimuth;
	}

	
	/**
	 * horizontal extent of the panorama in radians 
	 * @return double horizontalFieldOfView
	 */
	public double horizontalFieldOfView() {
		return horizontalFieldOfView;
	}

	/**
	 * Maximum visible distance of the panorama
	 * @return int maxDistance
	 */
	public int maxDistance() {
		return maxDistance;
	}
	
	/**
	 * Width of the panorama
	 * @return int width
	 */
	public int width() {
		return width;
	}

	/**
	 * Height of the panorama
	 * @return int height
	 */
	public int height() {
		return height;
	}
	
	/**
	 * Vertical extent of the panorama in radians 
	 * @return double vertical field of view
	 */
	public double verticalFieldOfView(){
		return verticalFieldOfView;
	}
	
	/**
	 * Azimuth corresponding to a the horizontal index of a pixel
	 * @param x double horizontal index of a pixel
	 * @return double azimuth of the a pixel with horizontal index x
	 */
	public double azimuthForX(double x){
		checkArgument(x >= 0 && x <= (this.width - 1), "x is not in range");
		
		double indexOfCentralAzimuth = (this.width - 1) / 2.0;
		double difference = x - indexOfCentralAzimuth;
		double delta = this.horizontalFieldOfView / (this.width - 1);
		return Azimuth.canonicalize(this.centerAzimuth + difference * delta);
	}
	
	/**
	 * Horizontal index of a pixel at a given azimuth
	 * @param a double azimuth of a pixel
	 * @return double horizontal index of a pixel at the given azimuth
	 */
	public double xForAzimuth(double a){
		double anglediff = angularDistance(centerAzimuth, a);
		checkArgument(anglediff <= this.horizontalFieldOfView/2 && anglediff>=-this.horizontalFieldOfView/2, "Azimuth not in range");
		
		double indexOfCentralAzimuth = (this.width - 1) / 2.0;
		double delta = this.horizontalFieldOfView / (this.width - 1);
		double numberOfIndexesFromCentral = (anglediff) / delta;
		return indexOfCentralAzimuth + numberOfIndexesFromCentral;
	}
	
	/**
	 * Altitude corresponding to the vertical index of a pixel
	 * @param y double vertical index of a pixel
	 * @return altitude in radians corresponding to the given vertical index
	 */
	public double altitudeForY(double y){
		checkArgument(y >= 0 && y <= (this.height - 1), "y is not in range");
		
		double indexOfCentralAzimuth = (this.height - 1) / 2.0;
		double difference = indexOfCentralAzimuth -  y;
		double delta = this.horizontalFieldOfView / (this.width - 1);
		return difference * delta;
	}
	
	/**
	 * Vertical index of a pixel at a given altitude
	 * @param a double altitude of a pixel in radians
	 * @return double vertical index of a pixel at the given altitude
	 */
	public double yForAltitude(double a){
		double maximumAltitude = verticalFieldOfView() / 2;
		double anglediff = angularDistance(0, a);
		double epsilon = 1E-8;
		checkArgument(anglediff <= maximumAltitude + epsilon && anglediff >= -(maximumAltitude + epsilon), "Altitude not in range");
		double indexOfCentralAzimuth = (this.height - 1) / 2.0;
		double delta = this.horizontalFieldOfView / (this.width - 1);
		double numberOfIndexesFromCentral = anglediff / delta;
		return indexOfCentralAzimuth - numberOfIndexesFromCentral;
	}
	
	/**
	 * Checks if given horizontal and vertical indexes are valid
	 * @param x int horizontal index to check
	 * @param y int vertical index to check
	 * @return true iff the index (x,y) are in the panorama
	 */
	boolean isValidSampleIndex(int x, int y){
		return x >= 0 && x <= (this.width - 1) && y >= 0 && y <= (this.height - 1);
	}
	
	/**
	 * Linear index of the index (x,y)
	 * @param x int horizontal index
	 * @param y int vertical index
	 * @return int linear index corresponding to the index (x,y)
	 */
	int linearSampleIndex(int x, int y){
		assert isValidSampleIndex(x, y);
		return (y * this.width) + x;
	}

}
