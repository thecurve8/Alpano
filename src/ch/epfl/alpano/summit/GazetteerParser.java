/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.summit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.alpano.GeoPoint;

import static java.lang.Math.toRadians;

/**
 * Reader of files describing summits
 */
public class GazetteerParser {
	//Empty and private constructor to make class non instantiable
	private GazetteerParser(){}
	
	/**
	 * File reader
	 * @param file File to read summits from
	 * @return List<> of Summits
	 * @throws IOException
	 */
	public static List<Summit> readSummitsFrom(File file) throws IOException{
		ArrayList<Summit> summitList = new ArrayList<Summit>();
		
		//read the file in ASCII, line by line, and add every valid summit to the ArrayList
		//FileInputStream reads the file in bytes, InputStreamReader converts them to 
		//characters in ASCII representation, and BufferedReader allows us to read them line by line
		try(BufferedReader b = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.US_ASCII))){
			String nextLine;
			while((nextLine = b.readLine()) != null){
				if(checkLine(nextLine)){
					summitList.add(createSummit(nextLine));
				}else{
					throw new IOException("Wrong line format");
				}
			}
		}catch(IOException e){
			e.printStackTrace();
			throw new IOException("Exception while reading file");
		}
		
		//return an unmodifiable copy of the ArrayList
		return Collections.unmodifiableList(summitList);
	}
	
	/**
	 * Checks if the given line is compatible with our summit standards
	 * @param line String line to check
	 * @return boolean true iff standards are met
	 */
	private static boolean checkLine(String line){
		String[] spaces = new String[3];
		if(line.length() < 37){
			return false;
		}
		spaces[0] = line.substring(0, 9);
		spaces[1] = line.substring(10, 18);
		spaces[2] = line.substring(19, 24);
		//check three components of the longitude
		String[] longitudeDoublePoints = spaces[0].split(":");
		if(longitudeDoublePoints.length != 3){
			return false;
		}
		for(int j=0;j<3;j++){
			try{
				//first component has to be an integer in [-180, 180]
				Integer i = Integer.parseInt(longitudeDoublePoints[j].trim());
				if(j == 0 && (i < -180 || i > 180)){
					return false;
				}
				//second and third components have to be integers in interval [0, 60[
				if(j != 0 && (i < 0 || i > 59)){
					return false;
				}
			}catch(NumberFormatException err){
				err.printStackTrace();
				return false;
			}
		}
		
		//check three latitude components
		String[] latitudeDoublePoints = spaces[1].split(":");
		if(latitudeDoublePoints.length != 3){
			return false;
		}
		for(int j=0;j<3;j++){
			try{
				//first component has to be an integer in interval ]-90;90[
				Integer i = Integer.parseInt(latitudeDoublePoints[j].trim());
				if(j == 0 && (i <= -90 || i >= 90)){
					return false;
				}
				//second and third components have to be integers in interval [0, 60[
				if(j != 0 && (i < 0 || i > 59)){
					return false;
				}
			}catch(NumberFormatException err){
				return false;
			}
		}
		
		//check if elevation is an integer (summits can be negative too, i.e Netherlands)
		try{
			Integer.parseInt(spaces[2].trim());
		}catch(NumberFormatException err){
			return false;
		}
		
		return true;
	}
	
	/**
	 * Converts the latitude/longitude from HH:MM:SS format to radians
	 * @param line String representing an angle in format HH:MM:SS
	 * @return double angle in radians
	 */
	private static double convertToRadians(String line){
		String[] doublePoints = line.split(":");
		//if the degrees are negative, we must consider the minutes and seconds as negative too
		if(Integer.parseInt(doublePoints[0].trim()) < 0){
			return toRadians(Integer.parseInt(doublePoints[0].trim()) + Integer.parseInt(doublePoints[1].trim()) / -60d +  Integer.parseInt(doublePoints[2].trim()) / -3600d);
		}
		return toRadians(Integer.parseInt(doublePoints[0].trim()) + Integer.parseInt(doublePoints[1].trim()) / 60d +  Integer.parseInt(doublePoints[2].trim()) / 3600d);
	}
	
	/**
	 * Creates an object of type Summit from a given line
	 * @param line String describing the summit
	 * @return Summit created object of type Summit
	 */
	private static Summit createSummit(String line){
		String[] spaces = new String[4];
		spaces[0] = line.substring(0, 9);
		spaces[1] = line.substring(10, 18);
		spaces[2] = line.substring(19, 24);
		spaces[3] = line.substring(36);
		return new Summit(spaces[3], new GeoPoint(convertToRadians(spaces[0]), convertToRadians(spaces[1])), Integer.parseInt(spaces[2].trim()));
	}
	
	
}
