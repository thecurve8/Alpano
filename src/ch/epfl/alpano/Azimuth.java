/**
 * Interface Azimuth
 * 
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano;

public interface Azimuth {
	/**
	   * Checks if given azimuth angle is canonical ([0;2PI[)
	   *
	   * @param azimuth azimuth angle in radians
	   * @return true if canonical, false otherwise
	   * 
	   */
	public static boolean isCanonical(double azimuth){
		return (azimuth >= 0 && azimuth < Math2.PI2);
	}
	
	/**
	 * Canonicalizes given azimuth angle
	 * 
	 * @param azimuth azimuth angle in radians
	 * @return canonicalized azimuth angle
	 * 
	 */
	public static double canonicalize(double azimuth){
		return Math2.floorMod(azimuth, Math2.PI2);
	}
	
	/**
	 * Converts clockwise azimuth angle to anti-clockwise mathematical angle (both in radians)
	 * 
	 * @param azimuth clockwise canonical azimuth angle
	 * @return correspondent anti-clockwise angle in radians
	 * @throws IllegalArgumentException if azimuth angle is not canonical
	 *  
	 */
	public static double toMath(double azimuth){
		Preconditions.checkArgument(isCanonical(azimuth), "Azimuth is not canonical");
		if(azimuth == 0){
			//if azimuth in radians is 0, mathemetical anti-clockwise angle is 0 too (only case where 360 - angle doesn't work)
			return 0;
		}else{
			//in all other cases, we convert azimuth in radians to mathematical angle, then substract it from 360 to get anticlockwise
			return (Math2.PI2 - azimuth);
		}
	}
	
	/**
	 * Converts anti-clockwise mathematical angle to clockwise azimuth angle (both in radians)
	 * 
	 * @param angle anti-clockwise canonical mathematical angle
	 * @return correspondent clockwise angle in radians
	 * @throws IllegalArgumentException if azimuth angle is not canonical
	 *  
	 */
	public static double fromMath(double angle){
		Preconditions.checkArgument(angle >= 0 && angle < Math2.PI2, "Angle is not in interval 0 to 2PI (excluded)");
		if(angle == 0){
			return 0;
		}else{
			return (Math2.PI2 - angle);
		}
	}
	
	/**
	 * Gives the name of the octant in which the azimuth angle lies
	 * 
	 * @param azimuth given angle in radians
	 * @param n String corresponding to north
	 * @param e String corresponding to east
	 * @param s String corresponding to south
	 * @param w String corresponding to west
	 * @return String describing the octant corresponding to given azimuth angle
	 * @throws IllegalArgumentException if azimuth angle is not canonical
	 * 
	 */
	public static String toOctantString(double azimuth, String n, String e, String s, String w){
		Preconditions.checkArgument(isCanonical(azimuth), "Azimuth is not canonical");
		int numberOfOctant = (int) ((azimuth + (Math.PI / 8)) / (Math.PI / 4));
		switch(numberOfOctant){
		case 0:
			return n;
		case 1:
			return n+e;
		case 2:
			return e;
		case 3:
			return s+e;
		case 4:
			return s;
		case 5:
			return s+w;
		case 6:
			return w;
		case 7:
			return n+w;
		case 8:
			return n;
		default:
			//return error if not in this cases
			throw new Error();
		}
	}
}
