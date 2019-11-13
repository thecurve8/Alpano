/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.summit;

import ch.epfl.alpano.GeoPoint;
import static java.util.Objects.requireNonNull;

/**
 * Representation of a Summit
 */
public final class Summit {
	private final String name;
	private final GeoPoint position;
	private final int elevation;
	
	/**
	 * Constructor of Summit
	 * @param name String of the summit
	 * @param position GeoPoint of the summit
	 * @param elevation int elevation of the summit
	 */
	public Summit(String name, GeoPoint position, int elevation){
		this.name = requireNonNull(name);
		this.position = requireNonNull(position);
		this.elevation = elevation;
	}

	/**
	 * Returns the summit name
	 * @return name String summit name
	 */
	public String name() {
		return name;
	}

	/**
	 * Returns the summit position
	 * @return position GeoPoint summit position
	 */
	public GeoPoint position() {
		return position;
	}

	/**
	 * Returns the summit elevation
	 * @return elevation int summit elevation
	 */
	public int elevation() {
		return elevation;
	}
	
	/**
	 * String representation of a summit
	 */
	public String toString(){
		return (this.name + ' ' + this.position.toString() + ' ' + this.elevation);
	}

}
