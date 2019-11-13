/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.gui;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.Math2;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Claas labelising summits of the panorama
 */
public final class Labelizer {
	private final ContinuousElevationModel cem;
	private final List<Summit> summits;
	private final static double MARGIN_ALLOWED = 200;
	private final static double INTERVAL_SIZE_FOR_ROOT_SEARCH = 64;
	private final static int VERTICAL_LIMIT_FOR_SUMMIT_LABELIZING = 170;
	private final static int HORIZONTAL_LIMIT_FOR_SUMMIT_LABELIZING = 20;
	private final static int SPACE_BETWEEN_HIGHEST_SUMMIT_AND_LABELS = 22;
	private final static int SPACE_BETWEEN_LINE_AND_LABEL = 2;
	private final static int ROTATION_ANGLE = -60;
	
	/**
	 * Constructor of the labelizer
	 * @param cem ContinuousElevationModel from which the panorama is created
	 * @param summits List<Summits> list of summits that might be in the panorama
	 * @throws NullPointerException if the cem or the list with the summits is null
	 */
	public Labelizer(ContinuousElevationModel cem, List<Summit> summits){
		this.cem = requireNonNull(cem);
		requireNonNull(summits);
		this.summits = Collections.unmodifiableList(new ArrayList<>(summits));
	}
	
	/**
	 * Creates the list of Nodes of the Labels of the visible and labelisable summits of the panorama
	 * @param parameters PanoramaParameters parameters of the panorama
	 * @return List<Node> list of the nodes of the labels of the labelisable summits in the panorama
	 */
	public List<Node> labels(PanoramaParameters parameters){
		List<Node> nodes = new ArrayList<>();
		List<VisibleSummit> availableSummits = summitsAvailableForLabelizing(visibleSummits(parameters), parameters);
		//yIndex where the labels are positioned
		int horizontalLineForLabels = 0;
		
		//availableSummits might be empty, so get(0) might not be usable
		if(availableSummits.size() > 0){
			//we take the first summit from the list as the highest because it was sorted in summitsAvailableForLabelizing
			horizontalLineForLabels =  availableSummits.get(0).yIndex - SPACE_BETWEEN_HIGHEST_SUMMIT_AND_LABELS;
		}
		
		for(VisibleSummit s: availableSummits){
			//Line starts two pixels under the horizontalLineForLabels and ends at the summit
			Line summitLine = new Line(s.xIndex, horizontalLineForLabels + SPACE_BETWEEN_LINE_AND_LABEL, s.xIndex, s.yIndex);
			
			//Name Labels are on the horizontalLineForLabels and rotated 60 degrees to the left 
			Text summitName = new Text(s.summit.name() + " (" + s.summit.elevation() + "m)");
			summitName.getTransforms().addAll(new Translate(s.xIndex, horizontalLineForLabels), new Rotate(ROTATION_ANGLE, 0, 0));
			
			nodes.add(summitName);
			nodes.add(summitLine);
		}
		
		return Collections.unmodifiableList(new ArrayList<>(nodes));
	}
	 
	/**
	 * Creates a List of summits which will be labelized
	 * @param summits List<VisibleSummit> list of the visible summits in the panorama
	 * @param parameters PanoramaParameters parameters of the panorama
	 * @return List<VisibleSummit> list of all the visible summits wich are labelizable
	 */
	private List<VisibleSummit> summitsAvailableForLabelizing(List<VisibleSummit> summits, PanoramaParameters parameters){
		//remove the summits which are less than 170 pixels away from the top
		summits.removeIf(s -> {
			return (s.yIndex < VERTICAL_LIMIT_FOR_SUMMIT_LABELIZING);
		});
		
		//sort the summits first according to the y-coordinate of their pixel and if they are equal according to this criteria, then sort according to their elevation
		Collections.sort(summits, 
                (s1, s2) -> {
                	//sort increasingly according to y-coordinate
                  	Integer intermediateComparison = new Integer(s1.yIndex).compareTo(s2.yIndex);
                	if(intermediateComparison == 0){
                		//sort decreasingly according to the elevation
                		return new Integer(s2.summit.elevation()).compareTo(s1.summit.elevation());
                	}
                	return intermediateComparison;
                });
		
		//BitSet representing all available columns of the panorama where a label can be added
		//Since all x-coordinates are rounded each entry of the BitSet represents the column at a x-coordinate
		BitSet availablePositionSet = new BitSet(parameters.width());
		
		//Since the labels can't be added at less than 20 pixels of the sides of the panorama, the BitSet only represents the columns from 20 to (width-20)
		//Check if the 'to' is strictly superior to 'from', else we don't set
		if(parameters.width() - HORIZONTAL_LIMIT_FOR_SUMMIT_LABELIZING + 1 > HORIZONTAL_LIMIT_FOR_SUMMIT_LABELIZING){
			availablePositionSet.set(HORIZONTAL_LIMIT_FOR_SUMMIT_LABELIZING, parameters.width() - HORIZONTAL_LIMIT_FOR_SUMMIT_LABELIZING + 1);	
		}
		
		List<VisibleSummit> labelizableSummits = new ArrayList<>();
		for(VisibleSummit s: summits){
			if(availablePositionSet.get(s.xIndex)){
				labelizableSummits.add(s);
				//making the position of the new Summit unavailable and the 19 positions to the left and 19 to the right
				//no -1 for the to because exclusive

				availablePositionSet.clear(s.xIndex-(HORIZONTAL_LIMIT_FOR_SUMMIT_LABELIZING - 1), s.xIndex + (HORIZONTAL_LIMIT_FOR_SUMMIT_LABELIZING));
			}
		}
		//since method is private no need to make the list unmodifiable
		return labelizableSummits;
	}
	
	/**
	 * VisibleSummits on the panorama
	 * @param parameters PanoramaParameters on which we want to know the visible summits 
	 * @return List<Summit> list with all visible summits
	 */
	private List<VisibleSummit> visibleSummits(PanoramaParameters parameters){
		
		List<VisibleSummit> visibleSummits = new ArrayList<>();
		for(Summit s: summits){
			double distanceToSummit = s.position().distanceTo(parameters.observerPosition());
			//Checking if summit not further than MaxDistance of the panorama
			if(distanceToSummit > parameters.maxDistance()){
				continue;
			}
			double summitAzimuth = parameters.observerPosition().azimuthTo(s.position());
			double angularDistance = Math2.angularDistance(summitAzimuth, parameters.centerAzimuth());
			
			//Checking if summit is in horizontalFieldOfView
			if(Math.abs(angularDistance) > (parameters.horizontalFieldOfView() / 2)){
				continue;
			}
			
			ElevationProfile profile = new ElevationProfile(cem, parameters.observerPosition(), summitAzimuth, distanceToSummit);
			//RayToGroundDistance evaluated at distanceToSummit distance with raySlope 0 gives us the elevation of the summit inversed (the ray is "in the ground"), so we add '-' in front
			double summitElevation = -PanoramaComputer.rayToGroundDistance(profile, parameters.observerElevation(), 0).applyAsDouble(distanceToSummit);
			
			double raySlope = Math.atan2(summitElevation, distanceToSummit);
			//Checking if summit is in VerticalFieldOfView
			if(Math.abs(raySlope) > (parameters.verticalFieldOfView() / 2)){
				continue;
			}
			
			//Checking if summit is visible to the observer (with a margin of MARGIN_ALLOWED)
			DoubleUnaryOperator zeroFunction = PanoramaComputer.rayToGroundDistance(profile, parameters.observerElevation(), raySlope);
			double firstIntervalContainingRoot = Math2.firstIntervalContainingRoot(zeroFunction, 0, (distanceToSummit - MARGIN_ALLOWED), INTERVAL_SIZE_FOR_ROOT_SEARCH);
			
			
			if(firstIntervalContainingRoot == Double.POSITIVE_INFINITY){
				visibleSummits.add(new VisibleSummit(s, parameters, summitElevation, distanceToSummit, summitAzimuth));
			}
			
		}
		//since the method is private, no need to make the list unmodifiable
		return visibleSummits;
	}
	
	/**
	 * Nested class that pairs a Summit with its xIndex and yIndex to optimize the program
	 */
	private final static class VisibleSummit{
		private final Summit summit;
		//coordinates of the pixel where the summit is drawn
		private final int xIndex, yIndex;
		
		/**
		 * Constructor of the class creating the pairing
		 * @param summit Summit to pair indexes to
		 * @param parameters PanoramaParameters used to compute the indexes
		 * @param elevation elevation of the summit as seen from the observer (calculated with ray)
		 * @param distanceToSummit distance from the observer to the summit
		 * @param summitAzimuth azimuth from the observer to the summit
		 */
		private VisibleSummit(Summit summit, PanoramaParameters parameters, double elevation, double distanceToSummit, double summitAzimuth){
			this.summit = summit;
			this.yIndex = (int)Math.round(parameters.yForAltitude(Math.atan2(elevation, distanceToSummit)));
			this.xIndex = (int)Math.round(parameters.xForAzimuth(summitAzimuth));
		}
	}
}