/**
 * Class Interval2D
 * 
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano;
import java.util.Objects;

import ch.epfl.alpano.Interval1D;

/**
 * Bidimensional interval of integers
 */
public final class Interval2D {
	private final Interval1D iX;
	private final Interval1D iY;
	
	/**
	 * Constructor of Interval2D Class
	 * @param iX first interval
	 * @param iY second interval
	 * @throws NullPointerException if one of the two given is null 
	 */
	public Interval2D(Interval1D iX, Interval1D iY){
		if(iX == null || iY == null){
			throw new NullPointerException("One of the two intervals is null");
		}
		this.iX = iX;
		this.iY = iY;
	}
	
	/**
	 * @return Interval1D first interval of the bidimensional interval
	 */
	public Interval1D iX(){
		return iX;
	}
	
	/**
	 * @return Interval1D second interval of the bidimensional interval
	 */
	public Interval1D iY(){
		return iY;
	}
	
	/**
	 * Checks if the bidimensional interval contains a pair of integers
	 * @param x first integer to check
	 * @param y second integer to check
	 * @return boolean true iff bimensional interval contains the pair x,y
	 */
	public boolean contains(int x, int y){
		return(this.iX.contains(x) && this.iY.contains(y));
	}
	
	/**
	 * @return int number of pairs contained in the bidimensional interval, i.e carthesian product
	 */
	public int size(){
		return (this.iX.size() * this.iY.size());
	}
	
	/**
	 * Returns the size of the intersection of two Interval2D
	 * @param that interval to check the size with
	 * @return int size of the bidimensional intersection of two Interval2D intervals, 0 if no intersection
	 */
	public int sizeOfIntersectionWith(Interval2D that){
		return (this.iX.sizeOfIntersectionWith(that.iX) * this.iY.sizeOfIntersectionWith(that.iY));
	}
	
	/**
	 * Creates a new bounding union of two bidimensional intervals
	 * @param that other interval to bound with
	 * @return Interval2D bounding union of the two intervals
	 */
	public Interval2D boundingUnion(Interval2D that){
		return new Interval2D(this.iX.boundingUnion(that.iX), this.iY.boundingUnion(that.iY));
	}
	
	/**
	 * Checks if two bidimensional intervals are unionable
	 * @param that other interval to check with
	 * @return true iff this and that are unionable
	 */
	public boolean isUnionableWith(Interval2D that){
		return ((this.size() + that.size() - this.sizeOfIntersectionWith(that)) == ((this.boundingUnion(that)).size()));
	}
	
	/**
	 * Creates the union interval from the two given intervals
	 * @param that other interval
	 * @return Interval2D the union of the two intervals if they are unionable
	 * @throws IllegalArgumentException if the two intervals aren't unionable
	 */
	public Interval2D union(Interval2D that){
		Preconditions.checkArgument(isUnionableWith(that), "Intervals aren't unionable");
		return boundingUnion(that);
	}
	
	@Override
	/**
	 * Overrides equals method
	 * @return true if the intervals contain the exact same integers, false otherwise
	 */
	public boolean equals(Object thatO){
		return (thatO != null &&
				thatO.getClass() == getClass() &&
				((Interval2D)thatO).iX.equals(this.iX) && 
				((Interval2D)thatO).iY.equals(this.iY));
	}
	
	@Override
	/**
	 * Overrides the hash method using the start and the end of the interval
	 * @return int hash code of the interval
	 */
	public int hashCode() {
		return Objects.hash(iX, iY);
	}
	
	/**
	 * String representation of the interval, displaying only the first integer and the last one
	 */
	@Override
	public String toString(){
		return (iX.toString() + 'x' + iY.toString());
	}

}