/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.gui;

import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.Math2;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.Preconditions;

/**
 * Functional Interface representing the painting of a channel
 */

@FunctionalInterface
public interface ChannelPainter {
	/**
	 * Value of the channel at a given point
	 * @param x int horizontal coordinate of the point of the channel
	 * @param y int vertical coordinate of the point of the chanel
	 * @return float value of the point of the channel at the given coordinates
	 */
	public abstract float valueAt(int x, int y);
	
	/**
	 * Channel painter whose values at a given point are the difference between the value of the panorama at that point and the neighboring (up, down left or right) which is farthest from the observer 
	 * @param panorama panorama from which the channel painter is created
	 * @return ChannelPainter with points whose values are the difference between that value of the panorama at that point and the value of the farthest neighboring point
	 */
	public static ChannelPainter maxDistanceToNeighbors(Panorama panorama){
		return (x, y) -> {
			return (Math.max(Math.max(panorama.distanceAt(x-1, y, 0), panorama.distanceAt(x+1, y, 0)), Math.max(panorama.distanceAt(x, y-1, 0), panorama.distanceAt(x, y+1, 0))) - panorama.distanceAt(x, y));
		};
	}
	
	/**
	 * Adds a constant to the value of every point of the channel
	 * @param constant double value to be added
	 * @return ChannelPainter whose values are the sum of the previous values of the ChannelPainter to which this method is applied and the given constant
	 */
	public default ChannelPainter add(double constant){
		return (x, y) -> valueAt(x, y) + (float)constant;
	}
	
	/**
	 * Subtracts a constant to the value of every point of the channel
	 * @param constant double value to be subtracted
	 * @return ChannelPainter whose values are the subtraction of the previous values of the ChannelPainter to which this method is applied and the given constant
	 */
	public default ChannelPainter sub(double constant){
		return (x, y) -> valueAt(x, y) - (float)constant;
	}
	
	/**
	 * Multiplies a constant to the value of every point of the channel
	 * @param constant double value to be multiplied
	 * @return ChannelPainter whose values are the multiplication of the previous values of the ChannelPainter to which this method is applied and the given constant
	 */
	public default ChannelPainter mul(double constant){
		return (x, y) -> valueAt(x, y) * (float)constant;
	}
	
	/**
	 * Divides a constant to the value of every point of the channel
	 * @param constant double value to divide with
	 * @return ChannelPainter whose values are the division of the previous values of the ChannelPainter to which this method is applied and the given constant
	 * @throws IllegalArgumentException if the given constant is 0
	 */
	public default ChannelPainter div(double constant){
		Preconditions.checkArgument(constant != 0, "Argument 'constant' is 0");
		return (x, y) -> valueAt(x, y) / (float)constant;
	}
	
	/**
	 * Changes every value of the ChannelPainter by applying to every value a DoubleUnaryOperator
	 * @param operator operator to apply
	 * @return ChannelPainter whose values are the result of the operator applied to the values of the ChannelPainter to which this method is applied
	 */
	public default ChannelPainter map(DoubleUnaryOperator operator){
		return (x, y) -> (float)operator.applyAsDouble(valueAt(x, y));
	}
	
	/**
	 * Changes all the values of the ChannelPainter to which this method is applied by 1- their value
	 * @return ChannelPainter in which all values are inverted compared to the ChannelPainter to which this method is applied
	 */
	public default ChannelPainter inverted(){
		return (x, y) -> 1 - valueAt(x, y);
	}
	
	/**
	 * Changes all the values of the ChannelPainter to which this method is applied by their remainder when divided by 1
	 * @return ChannelPainter in which all values are the ramainder mod1 of the values of the ChannelPainter to which this method is applied
	 */
	public default ChannelPainter cycling(){
		return (x, y) -> (float)Math2.floorMod(valueAt(x, y), 1);
	}
	
	/**
	 * Changes all the values of the ChannelPainter to which this method is applied by 0 if the value is negative, 1 if it is bigger or equal to 1 and doesn't change the value if they are in [0, 1]
	 * @return ChannelPainter whose values are clamped between 0 and 1, values outside this interval are reduced to 1 or brought up to 0;
	 */
	public default ChannelPainter clamped(){
		return (x, y) -> Math.max(0, Math.min(valueAt(x,y), 1));
	}
}
