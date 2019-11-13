package ch.epfl.alpano;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
public class HGTTestsAlex {
	
	
	@Test (expected = NullPointerException.class)
	public void NullFile() throws Exception {
		HgtDiscreteElevationModel d = new HgtDiscreteElevationModel(null);
		d.close();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void FileNameTooLong() throws Exception {
		File a = new File("abcdeghijklmno");
		HgtDiscreteElevationModel d = new HgtDiscreteElevationModel(a);
		d.close();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void FileNameIncorrect() throws Exception {
		File a = new File("0123456789A");
		HgtDiscreteElevationModel d = new HgtDiscreteElevationModel(a);
		d.close();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void FileNameTooShort() throws Exception {
		File a = new File("N46E007.hg");
		HgtDiscreteElevationModel d = new HgtDiscreteElevationModel(a);
		d.close();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void FileNameIncorrect1() throws Exception {
		File a = new File("W46E007.hgt");
		HgtDiscreteElevationModel d = new HgtDiscreteElevationModel(a);
		d.close();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void FileNameIncorrect2() throws Exception {
		File a = new File("n46E007.hgt");
		HgtDiscreteElevationModel d = new HgtDiscreteElevationModel(a);
		d.close();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void FileNameIncorrect3() throws Exception {
		File a = new File("N000E007.hgt");
		HgtDiscreteElevationModel d = new HgtDiscreteElevationModel(a);
		d.close();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void FileNameIncorrect4() throws Exception {
		File a = new File("N00e007.hgt");
		HgtDiscreteElevationModel d = new HgtDiscreteElevationModel(a);
		d.close();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void FileNameIncorrect5() throws Exception {
		File a = new File("N00N007.hgt");
		HgtDiscreteElevationModel d = new HgtDiscreteElevationModel(a);
		d.close();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void FileNameIncorrect6() throws Exception {
		File a = new File("N00W07.hgt");
		HgtDiscreteElevationModel d = new HgtDiscreteElevationModel(a);
		d.close();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void FileNameIncorrect7() throws Exception {
		File a = new File("N00W007,hgt");
		HgtDiscreteElevationModel d = new HgtDiscreteElevationModel(a);
		d.close();
	}
	
	@Test
	public void FileNameCorrect() throws Exception {
		File a = new File("N46E007.hgt");
		HgtDiscreteElevationModel d = new HgtDiscreteElevationModel(a);
		d.close();	
	}
	
	@Test
	public void getCorrectDateFromFile() throws Exception{
		final File HGT_FILE = new File("N46E006.hgt");
		HgtDiscreteElevationModel a = new HgtDiscreteElevationModel(HGT_FILE);
		
		int yRelatif =  1;
		int nbLignesEntieres = 3601 - yRelatif;
		
		int xRelatif = 3601;
		
		//counts all points before the given point at index (x,y) and retrieves it from the .hgt file
		//all full lines have 3601 elements each and the last line contains as many elements as columns at the left of the x index
		int pointFichier = 3601 * nbLignesEntieres + xRelatif;
		
		assertEquals(12967201, pointFichier, 0);
		a.close();
	}

}
