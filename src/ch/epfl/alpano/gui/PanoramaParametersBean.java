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
import static javafx.application.Platform.runLater;
import static java.util.Objects.requireNonNull;

import java.util.EnumMap;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * JavaFX bean containing the PanoramaUserParameter of the Panorama
 * Containing 10 Observable properties:
 *  	> 1 read only property containing the PanoramaUserParameters
 *  	> 9 read and write property for each individual parameter of the panorama (ObserverLongitude, ObserverLatitude...) 
 */
public final class PanoramaParametersBean{
	
	//parametersProperty is only returned to the outside by the corresponding
	//method (parametersProperty()) as a ReadOnlyObjectProperty
	private final ObjectProperty<PanoramaUserParameters> parametersProperty;
	//Each parameter of the panorama is mapped to a property
	private final Map<UserParameter, ObjectProperty<Integer>> propertyMap = new EnumMap<>(UserParameter.class);
	
	/**
	 * Constructor of the panoramaParametersBean
	 * @param parameters PanoramaUserParameters used to set the bean
	 * @throws NullPointerException if parameters are null
	 */
	public PanoramaParametersBean(PanoramaUserParameters parameters){
		requireNonNull(parameters);
		parametersProperty = new SimpleObjectProperty<>(parameters);
		
		//fills the map with the properties  with values given by the parameters
		for(UserParameter parameter: UserParameter.values()){
			ObjectProperty<Integer> property = new SimpleObjectProperty<Integer>(parameters.get(parameter));
			
			//each value is "listened" by the PanoramaParameterBean and when
			//the value of the property changes, the PanoramaParameterBean is
			//synchronized
			property.addListener((prop, oldV, newV) ->
			  runLater(this::synchronizeParameters));
			
			propertyMap.put(parameter, property);
		}
	}
	
	/**
	 * Synchronization of the ReadOnlyObjectProperty of the PanoramaParametersBean 
	 * containing the PanoramaUserParameters with the ObjectProperty<Integer> containing 
	 * the individual parameters of the panorama
	 */
	private void synchronizeParameters(){
		//Using the new ObjectProperty<Integer> containing the individual parameters of the panorama
		//to construct a sanitized version of the PanoramaUserParameters to store in the corresponding 
		//property
		PanoramaUserParameters sanitizedParameters = new PanoramaUserParameters(observerLongitudeProperty().get(), observerLatitudeProperty().get(), observerElevationProperty().get(), centerAzimuthProperty().get(), horizontalFieldOfViewProperty().get(), maxDistanceProperty().get(), widthProperty().get(), heightProperty().get(), superSamplingExponentProperty().get());
		parametersProperty.set(sanitizedParameters);
		//since the parameters might have been unvalid we change the values contained by the properties in the EnumMap
		//with the one which were just sanitized
		propertyMap.forEach((k, v) -> v.set(parametersProperty.get().get(k)));
	}
	
	/**
	 * The ReadOnlyObjectProperty of the PanoramaParametersBean containing the PanoramaUserParameters
	 * @return ReadOnlyObjectProperty<PanoramaUserParameters> containing the PanoramaUserParameters
	 */
	public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty(){
		return parametersProperty;
	}
	
	/**
	 * ObjectProperty containing the observerLongitude of the PanoramaUserParameters
	 * @return ObjectProperty<Integer> containing the observerLongitude
	 */
	public ObjectProperty<Integer> observerLongitudeProperty(){
		return propertyMap.get(OBSERVER_LONGITUDE);
	}
	
	/**
	 * ObjectProperty containing the observerLatitude of the PanoramaUserParameters
	 * @return ObjectProperty<Integer> containing the observerLatitude
	 */
	public ObjectProperty<Integer> observerLatitudeProperty(){
		return propertyMap.get(OBSERVER_LATITUDE);
	}
	
	/**
	 * ObjectProperty containing the observerElevation of the PanoramaUserParameters
	 * @return ObjectProperty<Integer> containing the observerElevation
	 */
	public ObjectProperty<Integer> observerElevationProperty(){
		return propertyMap.get(OBSERVER_ELEVATION);
	}
	
	/**
	 * ObjectProperty containing the centerAzimuth of the PanoramaUserParameters
	 * @return ObjectProperty<Integer> containing the centerAzimuth
	 */
	public ObjectProperty<Integer> centerAzimuthProperty(){
		return propertyMap.get(CENTER_AZIMUTH);
	}
	
	/**
	 * ObjectProperty containing the horizontalFieldOfView of the PanoramaUserParameters
	 * @return ObjectProperty<Integer> containing the horizontalFieldOfView
	 */
	public ObjectProperty<Integer> horizontalFieldOfViewProperty(){
		return propertyMap.get(HORIZONTAL_FIELD_OF_VIEW);
	}
	
	/**
	 * ObjectProperty containing the maxDistance of the PanoramaUserParameters
	 * @return ObjectProperty<Integer> containing the maxDistance
	 */
	public ObjectProperty<Integer> maxDistanceProperty(){
		return propertyMap.get(MAX_DISTANCE);
	}
	
	/**
	 * ObjectProperty containing the width of the PanoramaUserParameters
	 * @return ObjectProperty<Integer> containing the width of the PanoramaUserParameters
	 */
	public ObjectProperty<Integer> widthProperty(){
		return propertyMap.get(WIDTH);
	}
	
	/**
	 * ObjectProperty containing the height of the PanoramaUserParameters
	 * @return ObjectProperty<Integer> containing the height of the PanoramaUserParameters
	 */
	public ObjectProperty<Integer> heightProperty(){
		return propertyMap.get(HEIGHT);
	}
	
	/**
	 * ObjectProperty containing the superSamplingExponent of the PanoramaUserParameters
	 * @return ObjectProperty<Integer> containing the superSamplingExponent
	 */
	public ObjectProperty<Integer> superSamplingExponentProperty(){
		return propertyMap.get(SUPER_SAMPLING_EXPONENT);
	}
	
}
