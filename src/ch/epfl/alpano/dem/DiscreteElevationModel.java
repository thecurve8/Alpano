/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.Preconditions;

/**
 * Abstract representation associating a set of points with their elevation 
 */
public interface DiscreteElevationModel extends AutoCloseable{
	static final int SAMPLES_PER_DEGREE = 3600;
	static final double SAMPLES_PER_RADIAN = SAMPLES_PER_DEGREE * 360 / Math2.PI2;
	
	/**
	 * Index corresponding to an angle in radians
	 * @param angle given angle in radians
	 * @return index of the given angle
	 */
	static double sampleIndex(double angle){
		return angle * SAMPLES_PER_RADIAN;
	}
	
	/**
	 * Abstract method returning the bidimensional interval representing the DEM's extent
	 * @return Interval2D of the DEMS's extent
	 */
	Interval2D extent();
	
	/**
	 * Abstract method returning the elevation of a point on a given index on the DEM
	 * @param x int first coordinate of the index of the point
	 * @param y int second coordinate of the index of the point
	 * @return elevation of the point at index (x,y)
	 */
	double elevationSample(int x, int y);
	
	/**
	 * Default method returning the union with another DiscreteElevationModel
	 * @param that second DiscreteElevationModel 
	 * @return the union of the two DEMs
	 * @throws IllegalArgumentException if the extents of the two DEMs are not unionable
	 */
	default DiscreteElevationModel union(DiscreteElevationModel that){
		Preconditions.checkArgument(this.extent().isUnionableWith(that.extent()), "The two DEM are not unionable");
		return new CompositeDiscreteElevationModel(this, that);
	}
}
