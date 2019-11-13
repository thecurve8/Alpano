/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano;
import static java.util.Objects.requireNonNull;

import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;

/**
 * Class PanoramaComputer computes Panorama of given DEM and parameters
 */
public final class PanoramaComputer {
	private final ContinuousElevationModel dem;
	private final static double REFRACTION_CONSTANT = 0.13;
	private final static double INTERVAL_SIZE_FOR_ROOT_SEARCH = 64.0;
	private final static double INTERVAL_SIZE_FOR_IMPROVED_ROOT_SEARCH = 4.0;
	private final static double ONE_OVER_TWO_TIMES_EFFECTIVE_RADIUS = ((1-REFRACTION_CONSTANT) / (2*Distance.EARTH_RADIUS));

	/**
	 * PanoramaComputer Constructor
	 * @param dem ContinousElevationModel used to construct Panorama
	 */
	public PanoramaComputer(ContinuousElevationModel dem){
		this.dem = requireNonNull(dem);
	}

	/**
	 * Creates a Panorama based on given parameters and DEM
	 * @param parameters PanoramaParameters given parameters
	 * @return Panorama created Panorama
	 */
	public Panorama computePanorama(PanoramaParameters parameters, DoubleProperty percentage, DoubleProperty startingTime, BooleanProperty stopComputing){
		long startTime = System.nanoTime();
		startingTime.set(startTime);
		
		Panorama.Builder pano = new Panorama.Builder(parameters);
		
		//loop over all x (horizontal coordinate of pixels from left to right) values to find their elevation, distance, longitude, latitude, slope
		for(int x=0;x<parameters.width();x++){
			if(!stopComputing.get()){
				if(x % 100 == 0){
					double currentPercentage = (double)x / (double)parameters.width();
					percentage.set(currentPercentage);
				}
				ElevationProfile eProfile = new ElevationProfile(dem, parameters.observerPosition(), parameters.azimuthForX(x), parameters.maxDistance());
				
				double minX = 0;
				//minX is the last improvedRootInterval we have found
				
				//we test all pixels in that column starting from the bottom and if minX is Positive_INFINITY, we pass to the next X
				for(int height = parameters.height(); height > 0; --height){
					if(minX == Double.POSITIVE_INFINITY){
						break;
					}
				
					//vertical index of the y pixel (since we are starting from bottom but indexation starts from top)
					int relativeY = height-1;
					
					//create the function that will allow us to find the points of intersection between the ray and the profile
					DoubleUnaryOperator func = rayToGroundDistance(eProfile, parameters.observerElevation(), Math.tan(parameters.altitudeForY(relativeY)));
					
					double firstIntervalContainingRoot = Math2.firstIntervalContainingRoot(func, minX, parameters.maxDistance(), INTERVAL_SIZE_FOR_ROOT_SEARCH);
					
					if(firstIntervalContainingRoot != Double.POSITIVE_INFINITY){
						
						double improvedRootInterval = Math2.improveRoot(func, firstIntervalContainingRoot, firstIntervalContainingRoot + INTERVAL_SIZE_FOR_ROOT_SEARCH, INTERVAL_SIZE_FOR_IMPROVED_ROOT_SEARCH);
						
						//compute the distance from the observer to the ground
						float distance = (float)(improvedRootInterval / Math.cos(parameters.altitudeForY(relativeY)));
						
						GeoPoint positionOfRoot = eProfile.positionAt(improvedRootInterval);
						//set all the values in the Panorama Builders
						pano.setDistanceAt(x, relativeY, distance);
						pano.setSlopeAt(x, relativeY, (float)dem.slopeAt(positionOfRoot));
						pano.setLongitudeAt(x, relativeY, (float)positionOfRoot.longitude());
						pano.setLatitudeAt(x, relativeY, (float)positionOfRoot.latitude());
						pano.setElevationAt(x, relativeY, (float)dem.elevationAt(positionOfRoot));
						
						minX = improvedRootInterval;
						
					}else{
						minX = Double.POSITIVE_INFINITY;
					}
				}
			}else{
				break;
			}
		}
		return pano.build();
	}
	
	/**
	 * Creates the function that represents the ray to ground distance based on given ElevationProfile
	 * @param profile ElevationProfile profile to base the function on
	 * @param ray0 Elevation where the ray starts
	 * @param raySlope Slope of the ray (tangent of the elevation angle)
	 * @return DoubleUnaryOperator function representing the ray to ground distance
	 */
	public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope){
		return x -> (ray0 + x * raySlope - profile.elevationAt(x) + ONE_OVER_TWO_TIMES_EFFECTIVE_RADIUS * Math2.sq(x));
	}
}
