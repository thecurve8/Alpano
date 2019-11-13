/**
 * Interface Distance
 * 
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano;

public interface Distance {
	public final static double EARTH_RADIUS = 6371000;
	
	/**
	 * Gives angle corresponding to the arc of given length with earth radius
	 * 
	 * @param distanceInMeters length of arc on earth
	 * @return Corresponding angle
	 * 
	 */
	public static double toRadians(double distanceInMeters){
		
		return (distanceInMeters / EARTH_RADIUS);
	}
	/**
	 * Gives length of arc corresponding given angle of arc with earth radius
	 * 
	 * @param distanceInRadians Angle of arc on earth
	 * @return Corresponding length of arc
	 * 
	 */
	public static double toMeters(double distanceInRadians){
		return (distanceInRadians * EARTH_RADIUS);
	}
}
