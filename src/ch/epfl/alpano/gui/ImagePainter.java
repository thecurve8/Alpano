/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.gui;
import javafx.scene.paint.Color;

/**
 * Functional Interface representing an image painter, based on multiple ChannelPainters
 */
@FunctionalInterface
public interface ImagePainter {
	/**
	 * Color of the Painter at a given point
	 * @param x int horizontal coordinate
	 * @param y int vertical coordinate
	 * @return Color the color at the given coordinates
	 */
	public abstract Color colorAt(int x, int y);
	
	/**
	 * Generates an ImagePainter based on 4 distinct ChannelPainters, including Opacity
	 * @param hue Represents the hue -- i.e the color appearance degree (0 to 359)
	 * @param saturation Represents the purity of the color (0 to 1)
	 * @param brightness Represents the brightness of the color (0 to 1)
	 * @param opacity Represents the opacity  (0 to 1)
	 * @return ImagePainter using the hsb static method in Color to mix these 4 parameters
	 */
	public static ImagePainter hsb(ChannelPainter hue, ChannelPainter saturation, ChannelPainter brightness, ChannelPainter opacity){
		return (x, y) -> Color.hsb(hue.valueAt(x, y), saturation.valueAt(x, y), brightness.valueAt(x, y), opacity.valueAt(x, y));
	}
	
	/**
	 * Generates an ImagePainter based on 3 distinct ChannelPainters, with opacity set to 1 by default
	 * @param hue Represents the hue -- i.e the color appearance degree (0 to 359)
	 * @param saturation Represents the purity of the color (0 to 1)
	 * @param brightness Represents the brightness of the color (0 to 1)
	 * @return ImagePainter using the hsb static method in Color to mix these 3 parameters
	 */
	public static ImagePainter hsb(ChannelPainter hue, ChannelPainter saturation, ChannelPainter brightness){
		return (x, y) -> Color.hsb(hue.valueAt(x, y), saturation.valueAt(x, y), brightness.valueAt(x, y));
	}
	
	/**
	 * Generates an ImagePainter based on two distinct ChannelPainters
	 * @param gray Represents the gray scale of the color (0 to 1)
	 * @param opacity Represents the opacity  (0 to 1)
	 * @return ImagePainter using the gray static method in Color to mix these 2 parameters
	 */
	public static ImagePainter gray(ChannelPainter gray, ChannelPainter opacity){
		return (x, y) -> Color.gray(gray.valueAt(x, y), opacity.valueAt(x, y));
	}
	
	/**
	 * Generates an ImagePainter based on the gray scale only (opacity set to 1 by default)
	 * @param gray Represents the gray scale of the color (0 to 1)
	 * @return ImagePainter using the gray static method in Color
	 */
	public static ImagePainter gray(ChannelPainter gray){
		return (x, y) -> Color.gray(gray.valueAt(x, y));
	}
}
