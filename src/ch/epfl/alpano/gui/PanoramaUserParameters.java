/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.gui;

import static ch.epfl.alpano.gui.UserParameter.CENTER_AZIMUTH;
import static ch.epfl.alpano.gui.UserParameter.HEIGHT;
import static ch.epfl.alpano.gui.UserParameter.HORIZONTAL_FIELD_OF_VIEW;
import static ch.epfl.alpano.gui.UserParameter.MAX_DISTANCE;
import static ch.epfl.alpano.gui.UserParameter.OBSERVER_ELEVATION;
import static ch.epfl.alpano.gui.UserParameter.OBSERVER_LATITUDE;
import static ch.epfl.alpano.gui.UserParameter.OBSERVER_LONGITUDE;
import static ch.epfl.alpano.gui.UserParameter.SUPER_SAMPLING_EXPONENT;
import static ch.epfl.alpano.gui.UserParameter.WIDTH;
import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;

/**
 * Panorama parameters represented from the user side
 */
public final class PanoramaUserParameters {
	private final static int MAXIMUM_LIMIT_VERTICAL_FIELD_OF_VIEW = 170;
	private final static double RATIO_USER_ANGLE_TO_DEGRRES = 10_000;
	private final static int RATIO_USER_DISTANCE_TO_METERS = 1000;
	
	private final Map<UserParameter, Integer> parametersMap;
	
	/**
	 * PanoramaUserParameters Constructor taking a Map as parameter
	 * @param parametersMap
	 */
	public PanoramaUserParameters(Map<UserParameter, Integer> parametersMap){
		requireNonNull(parametersMap);
		//we replace every entry with its sanitized version
		parametersMap.replaceAll((x,y) -> x.sanitize(y));
		//if the given height is taller than max height, we set max height
		double maximumHeight = ((MAXIMUM_LIMIT_VERTICAL_FIELD_OF_VIEW * (parametersMap.get(WIDTH) - 1) / parametersMap.get(HORIZONTAL_FIELD_OF_VIEW))+ 1);
		if(parametersMap.get(HEIGHT) > maximumHeight){
			parametersMap.put(HEIGHT, (int)Math.floor(maximumHeight));
		}
		this.parametersMap = Collections.unmodifiableMap(new EnumMap<>(parametersMap));
	}
	
	/**
	 * PanoramaUserParameters Constructor taking all the separate parameters
	 * @param observerLongitude
	 * @param observerLatitude
	 * @param observerElevation
	 * @param centerAzimuth
	 * @param horizontalFieldOfView
	 * @param maxDistance
	 * @param width
	 * @param height
	 * @param superSamplingExponent
	 */
	public PanoramaUserParameters(int observerLongitude, int observerLatitude, int observerElevation, int centerAzimuth, int horizontalFieldOfView, int maxDistance, int width, int height, int superSamplingExponent){
		this(createMapFromParemeters(observerLongitude, observerLatitude, observerElevation, centerAzimuth, horizontalFieldOfView, maxDistance, width, height, superSamplingExponent));
	}
	
	/**
	 * Static function that creates a map based on the given parameters
	 * @param observerLongitude
	 * @param observerLatitude
	 * @param observerElevation
	 * @param centerAzimuth
	 * @param horizontalFieldOfView
	 * @param maxDistance
	 * @param width
	 * @param height
	 * @param superSamplingExponent
	 * @return Map<UserParameter, Integer> the map created with these parameters
	 */
	private static Map<UserParameter, Integer> createMapFromParemeters(int observerLongitude, int observerLatitude, int observerElevation, int centerAzimuth, int horizontalFieldOfView, int maxDistance, int width, int height, int superSamplingExponent){
		Map<UserParameter, Integer> parametersMap = new EnumMap<>(UserParameter.class);
		parametersMap.put(OBSERVER_LONGITUDE, observerLongitude);
		parametersMap.put(OBSERVER_LATITUDE, observerLatitude);
		parametersMap.put(OBSERVER_ELEVATION, observerElevation);
		parametersMap.put(CENTER_AZIMUTH, centerAzimuth);
		parametersMap.put(HORIZONTAL_FIELD_OF_VIEW, horizontalFieldOfView);
		parametersMap.put(MAX_DISTANCE, maxDistance);
		parametersMap.put(WIDTH, width);
		parametersMap.put(HEIGHT, height);
		parametersMap.put(SUPER_SAMPLING_EXPONENT, superSamplingExponent);
		return new EnumMap<>(parametersMap);
	}
	
	/**
	 * Function that returns the value associated to a given userParameter
	 * @param userParameter given parameter of type UserParamater
	 * @return int value associated to the given userParameter
	 */
	public int get(UserParameter userParameter){
		return parametersMap.get(userParameter);
	}
	
	/**
	 * ObserverLongitude 
	 * @return int the value of the observer's longitude
	 */
	public int observerLongitude(){
		return parametersMap.get(OBSERVER_LONGITUDE);
	}
	
	/**
	 * Value of OBSERVER_LATITUDE of the parameters map
	 * @return int the value of the observer's latitude
	 */
	public int observerLatitude(){
		return parametersMap.get(OBSERVER_LATITUDE);
	}
	
	/**
	 * Value of OBSERVER_ELEVATION of the parameters map
	 * @return int the value of the observer's elevation
	 */
	public int observerElevation(){
		return parametersMap.get(OBSERVER_ELEVATION);
	}
	
	/**
	 * Value of CENTER_AZIMUTH of the parameters map
	 * @return int the value of the center azimuth
	 */
	public int centerAzimuth(){
		return parametersMap.get(CENTER_AZIMUTH);
	}
	
	/**
	 * Value of HORIZONTAL_FIELD_OF_VIEW of the parameters map
	 * @return int the value of the horizontal field of view
	 */
	public int horizontalFieldOfView(){
		return parametersMap.get(HORIZONTAL_FIELD_OF_VIEW);
	}
	
	/**
	 * Value of MAX_DISTANCE of the parameters map
	 * @return int maximum distance
	 */
	public int maxDistance(){
		return parametersMap.get(MAX_DISTANCE);
	}
	
	/**
	 * Value of WIDTH of the parameters map
	 * @return int width
	 */
	public int width(){
		return parametersMap.get(WIDTH);
	}
	
	/**
	 * Value of HEIGHT of the parameters map
	 * @return int height
	 */
	public int height(){
		return parametersMap.get(HEIGHT);
	}
	
	/**
	 * Value of SUPER_SAMPLING_EXPONENT of the parameters map
	 * @return int super sampling exponent
	 */
	public int superSamplingExponent(){
		return parametersMap.get(SUPER_SAMPLING_EXPONENT);
	}
	
	/**
	 * Corresponding PanoramaParameters of the parameters Map, as they will be calculated (super sampling into account)
	 * @return PanoramaParameters corresponding PanoramaParameters
	 */
	public PanoramaParameters panoramaParameters(){
		return new PanoramaParameters(new GeoPoint(Math.toRadians(observerLongitude() / RATIO_USER_ANGLE_TO_DEGRRES), Math.toRadians(observerLatitude() / RATIO_USER_ANGLE_TO_DEGRRES)), observerElevation(), Math.toRadians(centerAzimuth()), Math.toRadians(horizontalFieldOfView()), maxDistance() * RATIO_USER_DISTANCE_TO_METERS, (int)Math.pow(2, superSamplingExponent())*width(), (int)Math.pow(2, superSamplingExponent())*height());
	}
	
	/**
	 * Corresponding PanoramaParameters of the parameters Map (as they will be displayed)
	 * @return PanoramaParameters corresponding PanoramaParameters
	 */
	public PanoramaParameters panoramaDisplayParameters(){
		return new PanoramaParameters(new GeoPoint(Math.toRadians(observerLongitude() / RATIO_USER_ANGLE_TO_DEGRRES), Math.toRadians(observerLatitude() / RATIO_USER_ANGLE_TO_DEGRRES)), observerElevation(), Math.toRadians(centerAzimuth()), Math.toRadians(horizontalFieldOfView()), maxDistance() * RATIO_USER_DISTANCE_TO_METERS, width(), height());
	}
	
	@Override
	public int hashCode(){
		//returns the hashCode of the parameters map
		return parametersMap.hashCode();
	}
	
	@Override 
	public boolean equals(Object that){
		//checks if the parameters map are the same
		return ((that instanceof PanoramaUserParameters) && (((PanoramaUserParameters)that).parametersMap.equals(this.parametersMap)));
	}
}
