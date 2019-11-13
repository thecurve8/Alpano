/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.dem;

import java.awt.image.BufferedImage;
import java.io.File;
import static java.lang.Math.toRadians;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.max;
import static java.lang.Math.min;

import javax.imageio.ImageIO;

import ch.epfl.alpano.GeoPoint;

final class DrawHgtDEM {
	final static File HGT_FILE = new File("N46E006.hgt");
	final static double ORIGIN_LON = toRadians(Integer.parseInt(HGT_FILE.getName().substring(4, 7))+0.25);
	final static double ORIGIN_LAT = toRadians(Integer.parseInt(HGT_FILE.getName().substring(1, 3))+0.25);
	final static double WIDTH = toRadians(0.5);
	final static int IMAGE_SIZE = 300;
	final static double MIN_ELEVATION = 200;
	final static double MAX_ELEVATION = 1_500;

	public static void main(String[] as) throws Exception {
		DiscreteElevationModel dDEM =
				new HgtDiscreteElevationModel(HGT_FILE);
		ContinuousElevationModel cDEM =
				new ContinuousElevationModel(dDEM);

		double step = WIDTH / (IMAGE_SIZE - 1);
		BufferedImage i = new BufferedImage(IMAGE_SIZE,
				IMAGE_SIZE,
				TYPE_INT_RGB);
		for (int x = 0; x < IMAGE_SIZE; ++x) {
			double lon = ORIGIN_LON + x * step;
			for (int y = 0; y < IMAGE_SIZE; ++y) {
				double lat = ORIGIN_LAT + y * step;
				GeoPoint p = new GeoPoint(lon, lat);
				double el =
						(cDEM.elevationAt(p) - MIN_ELEVATION)
						/ (MAX_ELEVATION - MIN_ELEVATION);
				i.setRGB(x, IMAGE_SIZE - 1 - y, gray(el));
			}
		}
		dDEM.close();

		ImageIO.write(i, "png", new File("dem.png"));
	}
	
	private static int gray(double v) {
		double clampedV = max(0, min(v, 1));
		int gray = (int) (255.9999 * clampedV);
		return (gray << 16) | (gray << 8) | gray;
	}
}