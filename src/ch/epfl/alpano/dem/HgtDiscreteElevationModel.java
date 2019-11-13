/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.dem;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;

/**
 * HgtDiscreteElevationModel DEM taking its values in a .hgt file
 */
final public class HgtDiscreteElevationModel implements DiscreteElevationModel{
	private ShortBuffer b;
	private FileInputStream s;
	private final Interval2D extent;
	private final int startingX; //horizontal index of lower left point
	private final int startingY; //vertical index of lower left point
	private final static int FILE_LENGTH = 25934402;
	private final static int POINTS_PER_COLUMN_AND_LINE = 3601;
	
	/**
	 * HgtDiscreteElevationModel constructor checking and (if possible) reading a given .hgt file
	 * @param file .hgt file containing the elevation measures of a particular square area of dimensions 1degree x 1degree
	 */
	public HgtDiscreteElevationModel(File file){
		//File checks
		checkArgument(checkFileName(file), "File name is not correct");
		checkArgument(checkFileLength(file), "File length is not 25'934'402 octets");
		
		String filename = file.getName();
	    long l = file.length();
	    
	    //opening channel to map .hgt file
	    try {
			s = new FileInputStream(file);
			b = s.getChannel().map(MapMode.READ_ONLY, 0, l).asShortBuffer();
		} catch (IOException e) {
			throw new IllegalArgumentException("Error while opening file");
		}
	    
	    //Getting information from file name
	    String longitudeSubstring = filename.substring(4, 7);
		String latitudeSubstring = filename.substring(1, 3);
		
		//A check has already been made on these substrings to see if they are integers
		if(filename.charAt(0)=='S'){
			this.startingX = SAMPLES_PER_DEGREE * parseInt(longitudeSubstring)*-1;
		} else {
			this.startingX = SAMPLES_PER_DEGREE * parseInt(longitudeSubstring);
		}
		
		
		if(filename.charAt(3) == 'W'){
			this.startingY = SAMPLES_PER_DEGREE * parseInt(latitudeSubstring)*-1;
		} else {
			this.startingY = SAMPLES_PER_DEGREE * parseInt(latitudeSubstring);
		}
		
		extent = new Interval2D(new Interval1D(startingX, startingX + SAMPLES_PER_DEGREE), new Interval1D(startingY, startingY + SAMPLES_PER_DEGREE));
	}
	
	/**
	 * Checks if a given file name corresponds to the needed format
	 * @param file file to analyze
	 * @return true if file is compatible with our standards
	 */
	private boolean checkFileName(File file){
		String name = file.getName();
		if(name.length() != 11){
			return false;
		}
		
		if(name.charAt(0) != 'N' && name.charAt(0) != 'S'){
			return false;
		}
		
		String latitudeSubstring = name.substring(1, 3);
		String longitudeSubstring = name.substring(4, 7);
		try{
			Integer.parseInt(latitudeSubstring);
			Integer.parseInt(longitudeSubstring);
		}catch(NumberFormatException er){
			return false;
		}
		
		if(name.charAt(3) != 'W' && name.charAt(3) != 'E'){
			return false;
		}
		
		String extension = name.substring(7, 11);
		if(!extension.equals(".hgt")){
			return false;
		}
		
		return true;
	}
	
	/**
	 * Checks if the given file contains 25934402 octets
	 * @param file file to analyze
	 * @return true if the file is compatible with our standards
	 */
	private boolean checkFileLength(File file){
		long fileLength = file.length();
		if(fileLength != FILE_LENGTH){
			return false;
		}
		return true;
	}

	/**
	 * Closing the associated ressources by emptying (associating null) the buffer and closing the FileInputStream
	 */
	@Override
	public void close() throws Exception {
		//emptying buffer
		b = null;
		
		//closing FileInputStream
		s.close();
	}

	/**
	 * Returns the 2D Interval covered by the .hgt file (always 1degree x 1degree)
	 * @return Interval2D the extent as a 2D Interval
	 */
	@Override
	public Interval2D extent() {
		return this.extent;
	}

	@Override
	/**
	 * Returns the elevation at a given point at indexes (x,y)
	 * @param x int the first coordinate of the index
	 * @param y int the second coordinate of the index
	 * @return double the elevation at the given point
	 */
	public double elevationSample(int x, int y) {
		//first check to see if the given indexes are contained in the HGTDEM
		checkArgument(this.extent().contains(x, y), "HGTDEM doesn't contain these indexes");
		
		//counts all full lines above the y index (y line not included)
		int yRelatif = abs(y - startingY) + 1;
		int nbLignesEntieres = POINTS_PER_COLUMN_AND_LINE - yRelatif;
		
		//counts all columns on the left of the x index (including the x column)
		int xRelatif = abs(x - startingX);
		
		//counts all points before the given point at index (x,y) and retrieves it from the .hgt file
		//all full lines have 3601 elements each and the last line contains as many elements as columns at the left of the x index
		int pointFichier = POINTS_PER_COLUMN_AND_LINE * nbLignesEntieres + xRelatif;
		return b.get(pointFichier);
	}
}
