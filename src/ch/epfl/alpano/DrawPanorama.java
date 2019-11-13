/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano;

import java.io.File;

import javax.imageio.ImageIO;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.gui.ChannelPainter;
import ch.epfl.alpano.gui.ImagePainter;
import ch.epfl.alpano.gui.PanoramaRenderer;
import ch.epfl.alpano.gui.PredefinedPanoramas;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

final class DrawPanorama {
	final static File HGT_FILE1 = new File("N46E007.hgt");
	final static File HGT_FILE2 = new File("N46E008.hgt");
	final static File HGT_FILE3 = new File("N46E006.hgt");
	
	//-----------AJOUTER D'AUTRES HGT FILES SI NECESSAIRE----------------------//
//	final static File HGT_FILE4 = new File("N45E007.hgt");
//	final static File HGT_FILE5 = new File("N47E007.hgt");
	//---------------------------------------------------------------------------//



	//-------------------A UTILISER POUR LES PANORAMA PREDEFINIS-----------------//
	final static PanoramaParameters PARAMS = PredefinedPanoramas.ALPES_DU_JURA.panoramaParameters(); 
	//---------------------------------------------------------------------------//
	
	//-------------------A UTILISER POUR LES PANORAMAS SPECIFIQUES---------------//
//	final static double ORIGIN_LON = toRadians(7.65);
//	final static double ORIGIN_LAT = toRadians(46.73);
//	final static int ELEVATION = 600;
//	final static double CENTER_AZIMUTH = toRadians(180);
//	final static double HORIZONTAL_FOV = toRadians(60);
//	final static int MAX_DISTANCE = 100_000;
//	final static int IMAGE_WIDTH = 500;
//	final static int IMAGE_HEIGHT = 200;
	
//	final static PanoramaParameters PARAMS =
//			new PanoramaParameters(new GeoPoint(ORIGIN_LON,
//					ORIGIN_LAT),
//					ELEVATION,
//					CENTER_AZIMUTH,
//					HORIZONTAL_FOV,
//					MAX_DISTANCE,
//					IMAGE_WIDTH,
//					IMAGE_HEIGHT);
	//---------------------------------------------------------------------------//
	
	public static void main(String[] as) throws Exception {
		try (DiscreteElevationModel dDEM1 =
				new HgtDiscreteElevationModel(HGT_FILE1);
			DiscreteElevationModel dDEM2 = 
					new HgtDiscreteElevationModel(HGT_FILE2);
			DiscreteElevationModel dDEM3 = 
					new HgtDiscreteElevationModel(HGT_FILE3);
				)	
				{
			DiscreteElevationModel dDEM = dDEM1.union(dDEM2).union(dDEM3);
			ContinuousElevationModel cDEM =
					new ContinuousElevationModel(dDEM);
			Panorama p = new PanoramaComputer(cDEM)
					.computePanorama(PARAMS, new SimpleDoubleProperty(), new SimpleDoubleProperty(), new SimpleBooleanProperty());

					ChannelPainter distance = p::distanceAt;
					ChannelPainter slope = p::slopeAt;
					ChannelPainter opacity =
					  distance.map(d -> d == Float.POSITIVE_INFINITY ? 0 : 1);
					
		//--------------------PANORAMA EN COULEUR------------------------------------//
					ChannelPainter hue = distance.div(100000).cycling().mul(360);
					ChannelPainter sat = distance.div(200000).clamped().inverted();
					ChannelPainter bright = slope.mul(2).div(Math.PI).inverted().mul(0.7).add(0.3);
					ImagePainter l = ImagePainter.hsb(hue, sat, bright, opacity);
		//---------------------------------------------------------------------------//			
					
					
		//--------------------PANORAMA EN NOIR & BLANC ------------------------------//
//			ChannelPainter gray =
//					  ChannelPainter.maxDistanceToNeighbors(p)
//					  .sub(500)
//					  .div(4500)
//					  .clamped()
//					  .inverted();
//					ImagePainter l = ImagePainter.gray(gray, opacity);
		//---------------------------------------------------------------------------//


					Image i = PanoramaRenderer.renderPanorama(p, l);
					ImageIO.write(SwingFXUtils.fromFXImage(i, null),
					              "png",
					              new File("pelican.png"));
		}
	}
}

