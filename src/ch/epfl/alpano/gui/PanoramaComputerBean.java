package ch.epfl.alpano.gui;

import static java.util.Objects.requireNonNull;

import java.util.List;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
/**
 * JavaFX bean containing the necessary Properties to draw the final image as shown in the interface
 * Containing 4 Observable properties:
 *  	> 1 read and write property containing the PanoramaUserparameters
 *  	> 3 read only properties for the panorama, its image and the list of the labels
 *
 */
public final class PanoramaComputerBean {
	
	//Read and write property
	private final ObjectProperty<PanoramaUserParameters> panoramaUserParametersProperty;
	
	//Properties which are given to the outside as ReadyOnlyObjectPropoerties
	//But stored here as ObjectPropoerties to be able to set in generateAllProperties()
	private final ObjectProperty<Panorama> panoramaProperty;
	private final ObjectProperty<Image> imageProperty;
	private final ObservableList<Node> labels;
	private final BooleanProperty isComputing;
	private final DoubleProperty percentageOfComputing;
	private final DoubleProperty startingTime;
	private final BooleanProperty stopComputing;
	
	private final List<Summit> summits;
	private final ContinuousElevationModel cem;

	/**
	 * Constructor of the PanoramaComputerBean. Initialising all parameters and adding a listener to panoramaUserParametersProperty
	 * to make sure all other properties are up-to-date with the parameters.
	 * @param cem ContinuousElevationModel used for the panorama
	 * @param summits List of Summit that can be shown on the panorama
	 * @throws NullPointerException if one of the two arguments is null
	 */
	public PanoramaComputerBean(ContinuousElevationModel cem, List<Summit> summits) {
		this.cem = requireNonNull(cem);
		this.summits = requireNonNull(summits);
		
		panoramaProperty = new SimpleObjectProperty<>();
		imageProperty = new SimpleObjectProperty<>();
		labels = FXCollections.observableArrayList();
		panoramaUserParametersProperty = new SimpleObjectProperty<>();
		
		panoramaUserParametersProperty.addListener((prop, oldV, newV) ->
			synchronizeAllProperties());
		isComputing = new SimpleBooleanProperty();
		percentageOfComputing = new SimpleDoubleProperty();
		startingTime = new SimpleDoubleProperty();
		stopComputing = new SimpleBooleanProperty();
	}
	
	/**
	 * Getter for the read and write property containing the panoramaUserParameters
	 * @return ObjectProperty<PanoramaUserParameters> property containing the panoramaUserParameters 
	 */
	public ObjectProperty<PanoramaUserParameters> parameterProperty(){
		return panoramaUserParametersProperty;
	}
	
	/**
	 * Getter for the panoramaUserParameters contained by the corresponding property 
	 * @return PanoramaUserParameters contained in the corresponding property
	 */
	public PanoramaUserParameters getParameters(){
		return panoramaUserParametersProperty.get();
	}
	
	/**
	 * Setter for the property containing the PanoramaUserParameters
	 * @param newParameters new PanoramaUserParameters to put in the corresponding property
	 * @throws NullPointerException if the newParameters are null
	 */
	public void setParameters(PanoramaUserParameters newParameters){
		requireNonNull(newParameters);
		if(newParameters.equals(getParameters())){
			//to allow users to reload the panorama with the same parameters after a cancel
			synchronizeAllProperties();
		}
		panoramaUserParametersProperty.set(newParameters);
	}
	
	/**
	 * Getter for the read only property containing the panorama
	 * @return ReadOnlyObjectProperty<Panorama> property containing the panorama 
	 */
	public ReadOnlyObjectProperty<Panorama> panoramaProperty(){
		return panoramaProperty;
	}
	
	/**
	 * Getter for the Panorama contained by the corresponding property 
	 * @return Panorama contained in the corresponding property
	 */
	public Panorama getPanorama(){
		return panoramaProperty.get();
	}
	
	/**
	 * Getter for the read only property containing the Image of the panorama
	 * @return ReadOnlyObjectProperty<Image> property containing the image of the panorama 
	 */
	public ReadOnlyObjectProperty<Image> imageProperty(){
		return imageProperty;
	}
	
	/**
	 * Getter for the Image of the panorama contained by the corresponding property 
	 * @return Image of the panorama contained in the corresponding property
	 */
	public Image getImage(){
		return imageProperty.get();
	}
	
	/**
	 * Getter for the read only property containing the ObervableList of all the labels
	 * @return ObservableList<Node> containing all the labels
	 */
	public ObservableList<Node> labels(){
		return labels;
	}
	
	public boolean isComputing(){
		return this.isComputing.get();
	}
	
	public BooleanProperty isComputingProperty(){
		return this.isComputing;
	}
	
	public void setIsComputing(boolean computing){
		this.isComputing.set(computing);
	}
	
	public boolean stopComputing(){
		return this.stopComputing.get();
	}
	
	public void setStopComputing(boolean stop){
		this.stopComputing.set(stop);
	}
	
	public BooleanProperty stopComputingProperty(){
		return this.stopComputing;
	}
	
	public DoubleProperty percentageOfComputing(){
		return this.percentageOfComputing;
	}
	
	public DoubleProperty startingTime(){
		return this.startingTime;
	}
	

	/**
	 * Synchronization of all the properties to be up-to-date with the PanoramaUserParameters
	 */
	private void synchronizeAllProperties(){
	isComputing.set(true);
	stopComputing.set(false);
	new Thread(() -> {
		panoramaProperty.set(new PanoramaComputer(cem).computePanorama(getParameters().panoramaParameters(), percentageOfComputing, startingTime, stopComputing));
		if(!stopComputing()){
			List<Node> nodes = new Labelizer(cem, summits).labels(getParameters().panoramaDisplayParameters());
			Platform.runLater(() -> labels.setAll(nodes));
			
			//Defining the ImagePainter
			ChannelPainter distance = getPanorama()::distanceAt;
			ChannelPainter slope = getPanorama()::slopeAt;
			ChannelPainter opacity =
					distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);
			ChannelPainter hue = distance.div(100000).cycling().mul(360);
			ChannelPainter sat = distance.div(200000).clamped().inverted();
			ChannelPainter bright = slope.mul(2).div(Math.PI).inverted().mul(0.7).add(0.3);
			ImagePainter painter = ImagePainter.hsb(hue, sat, bright, opacity);
			imageProperty.set(PanoramaRenderer.renderPanorama(getPanorama(), painter));
			isComputing.set(false);
		}
		}).start();
	}
}
