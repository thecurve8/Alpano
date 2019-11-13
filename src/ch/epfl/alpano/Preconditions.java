/**
 * Interface Preconditions
 * 
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano;

public interface Preconditions {
	/**
	 * Throws IllegalArgumentException if given boolean is false
	 * @param b given boolean
	 */
	static void checkArgument(boolean b){
		if(!b){
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Throws IllegalArgumentException with given message if given boolean is false
	 * @param b given boolean
	 * @param message given message for the exception
	 */
	static void checkArgument(boolean b, String message){
		if(!b){
			throw new IllegalArgumentException(message);
		}
	}
}
