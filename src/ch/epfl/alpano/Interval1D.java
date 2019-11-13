/**
 * Classe Interval1D
 * 
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano;

import java.util.Locale;
import java.util.Objects;

/**
 * Interval1D Class that represents an interval of integers
 *
 */
public final class Interval1D {
	private final int includedFrom;
	private final int includedTo;
	
	/**
	 * Constructor of Interval1D
	 * @param includedFrom first integer of the interval
	 * @param includedTo last integer of the interval
	 * @throws IllegalArgumentException if includedFrom is strictly superior to includedTo
	 */
	public Interval1D(int includedFrom, int includedTo){
		Preconditions.checkArgument(includedTo >= includedFrom, "IncludedTo has to be strictly superior to IncludedFrom");
		this.includedFrom = includedFrom;
		this.includedTo = includedTo;
	}
	
	/**
	 * includedFrom()
	 * @return first integer of the interval
	 */
	public int includedFrom(){
		return this.includedFrom;
	}
	
	/**
	 * includedTo()
	 * @return last integer of the interval
	 */
	public int includedTo(){
		return this.includedTo;
	}
	
	/**
	 * Checks if an integer is contained in the interval
	 * @param v integer to be checked
	 * @return true if integer is contained, false otherwise
	 */
	public boolean contains(int v){
		return(v >= includedFrom() && v <= includedTo());
	}
	
	/**
	 * size()
	 * @return number of integers in the interval
	 */
	public int size(){
		return (includedTo() - includedFrom() + 1);
	}
	
	/**
	 * Gives the size of the intersection of two intervals
	 * @param that other interval to intersect
	 * @return 0 if there is no intersection, size of the intersection otherwise
	 */
	public int sizeOfIntersectionWith(Interval1D that){
		int intervalSize = Math.min(this.includedTo(), that.includedTo()) - Math.max(this.includedFrom(), that.includedFrom()) + 1;
		if(intervalSize > 0){
			return intervalSize;
		}else{
			return 0;
		}
	}
	
	/**
	 * Creates a bounding union between two intervals
	 * @param that other interval for the bounding union
	 * @return Interval1D the bounding union
	 */
	public Interval1D boundingUnion(Interval1D that){
		int minFrom = Math.min(this.includedFrom(), that.includedFrom());
		int maxTo = Math.max(this.includedTo(), that.includedTo());
		return new Interval1D(minFrom, maxTo);
	}
	
	/**
	 * Checks if two intervals are unionable
	 * @param that other interval to check with
	 * @return true if they are unionable, false otherwise
	 */
	public boolean isUnionableWith(Interval1D that){
		return ((this.size() + that.size() - this.sizeOfIntersectionWith(that)) == (this.boundingUnion(that)).size());
	}
	
	/**
	 * Creates the union interval from the two given intervals
	 * @param that other interval
	 * @return Interval1D the union of the two intervals if they are unionable
	 * @throws IllegalArgumentException if the two intervals aren't unionable
	 */
	public Interval1D union(Interval1D that){
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
				((Interval1D)thatO).includedFrom() == this.includedFrom() &&
				((Interval1D)thatO).includedTo() == this.includedTo());
	}
	
	@Override
	/**
	 * Overrides the hash method using the start and the end of the interval
	 * @return int hash code of the interval
	 */
	public int hashCode() {
		return Objects.hash(includedFrom(), includedTo());
	}
	
	/**
	 * String representation of the interval, displaying only the first integer and the last one
	 */
	@Override
	public String toString(){
		Locale l = null;
		return String.format(l, "[%d..%d]", this.includedFrom(), this.includedTo());
	}
}
