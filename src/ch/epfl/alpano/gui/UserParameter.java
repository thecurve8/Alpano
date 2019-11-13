/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.gui;

/**
 * Enumeration of the parameters of the panorama
 */
public enum UserParameter {
	OBSERVER_LONGITUDE(60_000, 120_000),
	OBSERVER_LATITUDE(450_000, 480_000),
	OBSERVER_ELEVATION(300, 10_000),
	CENTER_AZIMUTH(0, 359),
	HORIZONTAL_FIELD_OF_VIEW(1, 360),
	MAX_DISTANCE(10, 600),
	WIDTH(30, 16_000),
	HEIGHT(10, 4_000),
	SUPER_SAMPLING_EXPONENT(0, 2);
	
	private int min;
	private int max;
	/**
	 * Private constructor of the parameters of the enumeration
	 * @param min int minimum value
	 * @param max int maximal value
	 */
	private UserParameter(int min, int max) {
		//no conditions needed for min and max since the constructor is private
		this.min = min;
	    this.max = max;
	}
	
	/**
	 * Returns closest valid value of the parameter to the value given to the function
	 * @param parameter int value to sanitize
	 * @return int closest valid value
	 */
	public int sanitize(int parameter){
		if(parameter > this.max){
			return this.max;
		}else if(parameter < this.min){
			return this.min;
		}else{
			return parameter;
		}
	}
	
}
