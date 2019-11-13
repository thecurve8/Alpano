/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.dem;	

import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;

/**
 * Represents the union of two DEMs
 */
final class CompositeDiscreteElevationModel implements DiscreteElevationModel{
	private final DiscreteElevationModel dem1;
	private final DiscreteElevationModel dem2;
	private final Interval2D extent;
	
	/**
	 * Constructor of the union of two DEMs
	 * IllegalArgumentException is thrown by requireNonNull() if one of the two DEMs is null
	 * 
	 * @param dem1 DiscreteElevationModel first DEMs
	 * @param dem2 DiscreteElevationModel second DEMs
	 */
	CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2){
		this.dem1 = requireNonNull(dem1);
		this.dem2 = requireNonNull(dem2);
		this.extent = dem1.extent().union(dem2.extent());
	}
	
	
	/**
	 * Returns the extent, represented by an Interval2D on which the CompositeDiscreteElevationModel is defined
	 */
	public Interval2D extent(){
		return extent;
	}
	
	/**
	 * return the elevation of the sample at index (x,y)
	 * @param x int first coordinate of the index
	 * @param y int second coordinate of the index
	 * @throws IllegalArgumentException if the index is not contained in either DEMs
	 * @return elevation of the sample
	 */
	public double elevationSample(int x, int y){
		Preconditions.checkArgument(this.extent().contains(x, y), "CDEM doesn't contain sample");
		
		//Looks for the DEM containing the given index and gives the elevation in this one
		if(dem1.extent().contains(x, y)){
			return dem1.elevationSample(x, y);
		}else{
			return dem2.elevationSample(x, y);
		}
	}

	@Override
	/**
	 * Closes all associated ressources
	 */
	public void close() throws Exception {
		dem1.close();
		dem2.close();
	}
}