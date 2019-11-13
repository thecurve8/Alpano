/**
 * Interface Math2
 * 
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano;
import java.util.function.DoubleUnaryOperator;

public interface Math2 {
	//frequently used constants
	public final static double PI2 = 2*Math.PI;
	
	/**
	 * Squares a given double
	 * 
	 * @param x given double to square
	 * @return squared double
	 */
	public static double sq(double x){
		return x*x;
	}
	
	/**
	 * Gives the remainder of the Euclidean division
	 * 
	 * @param x dividend
	 * @param y divisor
	 * @return remainder of the Euclidean division
	 */
	public static double floorMod(double x, double y){
		return (x - y * (Math.floor(x / y)));
	}
	
	/**
	 * Trigonometric function haversin defined as [sin(x/2)]^2
	 * 
	 * @param x angle to be used
	 * @return result of haversin with given angle
	 */
	public static double haversin(double x){
		return sq((Math.sin(x/2)));
	}
	
	/**
	 * Signed difference between two angles given in the interval [-pi:pi[
	 * 
	 * @param a1 first angle
	 * @param a2 second angle
	 * @return Signed difference between a1 and a2
	 */
	public static double angularDistance(double a1, double a2){
		return (floorMod((a2 - a1 + Math.PI), PI2) - Math.PI);
	}
	
	/**
	 * Linear interpolation 
	 * 
	 * @param y0 value of the function at 0
	 * @param y1 value of the function at 1
	 * @param x horizontal coordinate of the value to be interpolated 
	 * @return value found by interpolation at x
	 */
	public static double lerp(double y0, double y1, double x){
		return (y0 + (x * (y1 - y0)));
	}
	
	/**
	 * Bilinear interpolation
	 * 
	 * @param z00 value of the function at (0,0)
	 * @param z10 value of the function at (1,0)
	 * @param z01 value of the function at (0,1)
	 * @param z11 value of the function at (1,1)
	 * @param x first variable of the point we want to interpolate
	 * @param y second variable of the point we want to interpolate
	 * @return value found by interpolation at (x,y)
	 */
	public static double bilerp(double z00, double z10, double z01, double z11, double x, double y){
		double lerp1 = lerp(z00, z10, x);
		double lerp2 = lerp(z01, z11, x);
		return lerp(lerp1, lerp2, y);
	}
	
	/**
	 * Finds lower bound of the first interval of size dX containing a root of the given function
	 * 
	 * @param f given function 
	 * @param minX starting point for the research
	 * @param maxX ending point for the research
	 * @param dX interval size
	 * @return lower bound of first interval containing a root, Double.POSITIVE_INFINITy if no such interval found
	 */
	public static double firstIntervalContainingRoot(DoubleUnaryOperator f, double minX, double maxX, double dX){
		for(double currentX = minX; currentX + dX<=maxX; currentX += dX){
			if(f.applyAsDouble(currentX) * f.applyAsDouble(currentX + dX) <= 0){
				return currentX;
			}
		}
		return Double.POSITIVE_INFINITY;
	}
	
	/**
	 * Searches for interval smaller than epsilon containing root of the given function
	 * 
	 * @param f given function
	 * @param x1 lower bound of the interval 
	 * @param x2 upper bound of the interval
	 * @param epsilon size of wanted interval
	 * @return lower bound of the interval of size epsilon or root if found
	 * @throws IllegalArgumentException if both bounds have same sign i.e. may not contain a root 
	 */
	public static double improveRoot(DoubleUnaryOperator f, double x1, double x2, double epsilon){
		Preconditions.checkArgument((f.applyAsDouble(x1) * f.applyAsDouble(x2) <= 0), "Les images ont le même signe");
		double middleValue = (x1 + x2) / 2;
		if(f.applyAsDouble(middleValue) == 0){
			return middleValue;
		}else if((x2 - x1) <= epsilon){
			return x1;
		}else if(f.applyAsDouble(middleValue) * f.applyAsDouble(x1) <= 0){
			return improveRoot(f, x1, middleValue, epsilon);
		}else{
			return improveRoot(f, middleValue, x2, epsilon);
		}
	}
}
