/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;

@SuppressWarnings("unused")
public class GazetteerParserTestYB {
	
	/*
	//Change checkLine, convertToRadians and createSummit in GazetteerParser from private to public for tests

	@Test
	public void readSummitsFromAlpsFile() {
		File alps = new File("alps.txt");
		try {
			List<Summit> summits = GazetteerParser.readSummitsFrom(alps);
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	@Test
	public void testSpecificFalseLineElevationError(){
		assertFalse(GazetteerParser.checkLine("  7:25:12 45:08:25  13f25  R0 E07 BA MONTE CURT"));
	}
	
	@Test
	public void testSpecificFalseLineLatitudeError(){
		assertFalse(GazetteerParser.checkLine("  a7:25:12 45:08:25  1325  R0 E07 BA MONTE CURT"));
	}
	
	@Test
	public void testSpecificFalseLineLongitudeError(){
		assertFalse(GazetteerParser.checkLine("  a7:25:12 45:0s8:25  1325  R0 E07 BA MONTE CURT"));
	}
	
	@Test
	public void testSpecificCorrectLine(){
		assertTrue(GazetteerParser.checkLine("-07:25:12 45:08:25  1325  R0 E07 BA MONTE CURT"));
	}
	
	@Test
	public void testSpecificFalseLineWeirdLatitude(){
		assertFalse(GazetteerParser.checkLine(" 190:25:12 45:08:25  1325  R0 E07 BA MONTE CURT"));
	}
	
	@Test
	public void testSpecificFalseLineWeirdLatitude2(){
		assertFalse(GazetteerParser.checkLine(" 19::12 45:08:25  1325  R0 E07 BA MONTE CURT"));
	}
	
	@Test
	public void testSpecificFalseLineWeirdLatitude3(){
		assertFalse(GazetteerParser.checkLine(" 19:12:90 45:08:25  1325  R0 E07 BA MONTE CURT"));
	}
	
	@Test
	public void testConvertToRadians(){
		assertEquals(1.5885405657, GazetteerParser.convertToRadians(" 90:60:60"), 0.01);
	}
	
	@Test
	public void testConvertNegativeToRadians(){
		assertEquals(-1.5885405657, GazetteerParser.convertToRadians(" -90:60:60"), 0.01);
	}
	
	@Test
	public void testSummitToString(){
		Summit summit = GazetteerParser.createSummit("  7:25:12 45:08:25  1325  R0 E07 BA MONTE CURT");
		assertEquals("MONTE CURT (7.0000,45.0000) 1325", summit.toString());
	}
	*/
}
