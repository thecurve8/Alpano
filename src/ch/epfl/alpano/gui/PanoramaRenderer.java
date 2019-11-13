/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.gui;

import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.Panorama;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * PanoramaRenderer Class that renders a Panorama Image
 */
public interface PanoramaRenderer {
	
	/**
	 * Static method that renders a WritableImage representing the given Panorama
	 * @param panorama The panorama to base the image on
	 * @param painter The painter to render the image with
	 * @return WritableImage an image representing the panorama
	 * @throws NullPointerException if Panorama or ImagePainter is null
	 */
	public static WritableImage renderPanorama(Panorama panorama, ImagePainter painter){
		requireNonNull(panorama);
		requireNonNull(painter);
		
		WritableImage image = new WritableImage(panorama.parameters().width(), panorama.parameters().height());
		PixelWriter writer = image.getPixelWriter();
		for(int x=0;x<panorama.parameters().width();x++){
			for(int y=0;y<panorama.parameters().height();y++){
				writer.setColor(x, y, painter.colorAt(x, y));
			}
		}
		return image;
	}
}
