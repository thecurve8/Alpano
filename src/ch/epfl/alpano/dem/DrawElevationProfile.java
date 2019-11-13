/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.dem;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ch.epfl.alpano.GeoPoint;
import static java.lang.Math.toRadians;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

final class DrawElevationProfile {
	private final static File HGT_FILE1 = new File("N45E006.hgt");
	private final static File HGT_FILE2 = new File("N45E007.hgt");
	private final static File HGT_FILE3 = new File("N45E008.hgt");
	private final static File HGT_FILE4 = new File("N45E009.hgt");
	private final static File HGT_FILE5 = new File("N46E006.hgt");
	private final static File HGT_FILE6 = new File("N46E007.hgt");
	private final static File HGT_FILE7 = new File("N46E008.hgt");
	private final static File HGT_FILE8 = new File("N46E009.hgt");
	final static double MAX_ELEVATION = 4_000;
	  
	  private static GeoPoint summit = new GeoPoint(toRadians(7.41667), toRadians(45.53806));
	  
	  final static double LONGITUDE = toRadians(6.8087);
	  final static double LATITUDE = toRadians(47.0085);
	  final static int LENGTH = (int) new GeoPoint(LONGITUDE, LATITUDE).distanceTo(summit);
	  final static double AZIMUTH = new GeoPoint(LONGITUDE, LATITUDE).azimuthTo(summit);
	  final static int WIDTH = 800, HEIGHT = 100;

	  @SuppressWarnings("resource")
	  private static ContinuousElevationModel loadHGTFiles() throws Exception{
		  DiscreteElevationModel dDEM1 =
				  new HgtDiscreteElevationModel(HGT_FILE1);
		  DiscreteElevationModel dDEM2 = 
				  new HgtDiscreteElevationModel(HGT_FILE2);
		  DiscreteElevationModel dDEM3 = 
				  new HgtDiscreteElevationModel(HGT_FILE3);
		  DiscreteElevationModel dDEM4 = 
				  new HgtDiscreteElevationModel(HGT_FILE4);
		  DiscreteElevationModel dDEM5 = 
				  new HgtDiscreteElevationModel(HGT_FILE5);
		  DiscreteElevationModel dDEM6 = 
				  new HgtDiscreteElevationModel(HGT_FILE6);
		  DiscreteElevationModel dDEM7 = 
				  new HgtDiscreteElevationModel(HGT_FILE7);
		  DiscreteElevationModel dDEM8 = 
				  new HgtDiscreteElevationModel(HGT_FILE8);
		  
		  return new ContinuousElevationModel((dDEM1.union(dDEM2).union(dDEM3).union(dDEM4)).union(dDEM5.union(dDEM6).union(dDEM7).union(dDEM8)));
	  }
	  public static void main(String[] as) throws Exception {
		  
		  
		  
		  
	   
	    ContinuousElevationModel cDEM =
	     loadHGTFiles();
	    GeoPoint o =
	      new GeoPoint(LONGITUDE, LATITUDE);
	    ElevationProfile p =
	      new ElevationProfile(cDEM, o, AZIMUTH, LENGTH);

	    int BLACK = 0x00_00_00, WHITE = 0xFF_FF_FF;

	    BufferedImage i =
	      new BufferedImage(WIDTH, HEIGHT, TYPE_INT_RGB);
	    for (int x = 0; x < WIDTH; ++x) {
	      double pX = x * (double) LENGTH / (WIDTH - 1);
	      double pY = p.elevationAt(pX);
	      int yL = (int)((pY / MAX_ELEVATION) * (HEIGHT - 1));
	      for (int y = 0; y < HEIGHT; ++y) {
	        int color = y < yL ? BLACK : WHITE;
	        i.setRGB(x, HEIGHT - 1 - y, color);
	      }
	    }
//	    cDEM.close();

	    ImageIO.write(i, "png", new File("profile.png"));
	}
}
