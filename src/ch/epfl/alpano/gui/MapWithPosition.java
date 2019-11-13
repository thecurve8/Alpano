package ch.epfl.alpano.gui;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

/**
 * Map which shows which zone the observer is watching
 */
public final class MapWithPosition extends StackPane{
	//Parameters which depend on the image of the map used
	//image use here come from http://www.worldatlas.com/webimage/countrys/europe/outline/ch.gif
	final private static double PIXELS_PER_KILOMETER = 0.576368876;
	final private static double IMAGE_WIDTH = 200;
	final private static double IMAGE_HEIGHT = 88.5;
	final private static double PIXEL_PER_DEGREE = 44.0849;
	final private static double LEFT_LONGITUDE = 5.95;
	final private static double TOP_LATITUDE = 47.8186;

	final private ObjectProperty<Integer> maxDistance;
	final private ObjectProperty<Integer> horizontalFieldOfView;
	final private ObjectProperty<Integer> latitude;
	final private ObjectProperty<Integer> longitude;
	final private ObjectProperty<Integer> azimuth;

	final private ImageView imageView;
	final private Arc arc;
	/**
	 * Constructor of the stack pane with the text field with the estimated remaining time
	 * @param startTime ReadOnlyProperty containing the starts time of the computation of the new panorama
	 * @param percentage ReadOnlyProperty containing the percentage of the calculations done for the new panorama
	 */
	public MapWithPosition(PanoramaParametersBean bean) {

		this.maxDistance = bean.maxDistanceProperty();
		this.horizontalFieldOfView = bean.horizontalFieldOfViewProperty();
		this.azimuth = bean.centerAzimuthProperty();
		this.longitude = bean.observerLongitudeProperty();
		this.latitude = bean.observerLatitudeProperty();

		this.setBackground(new Background(new BackgroundFill(Color.color(1, 1, 1, 0.9), CornerRadii.EMPTY, Insets.EMPTY)));
		this.imageView = new ImageView(new Image("file:ch.gif", IMAGE_WIDTH, IMAGE_HEIGHT, false, true));
		this.setPadding(new Insets(20, 50, 50, 10));

		Group group = new Group();
		arc = new Arc();
		arc.setType(ArcType.ROUND);
		
		//Set properties of the arc at the beginning
		arc.setRadiusX(maxDistance.get() * PIXELS_PER_KILOMETER);
		arc.setRadiusY(maxDistance.get() * PIXELS_PER_KILOMETER);
		arc.setLength(horizontalFieldOfView.get());
		arc.setStartAngle((90-azimuth.get())%360 - (horizontalFieldOfView.get()/2));
		arc.setFill(Color.color(0.05, 0.55, 0.95, 0.6));
		group.getChildren().addAll(imageView, arc);
		arc.setCenterX((longitude.get()/10_000.0 - LEFT_LONGITUDE)*PIXEL_PER_DEGREE);
		arc.setCenterY(-((latitude.get()/10_000.0 - TOP_LATITUDE)*PIXEL_PER_DEGREE ));

		maxDistance.addListener((v, oldVal, newVal)->{
			arc.setRadiusX(newVal*PIXELS_PER_KILOMETER);
			arc.setRadiusY(newVal*PIXELS_PER_KILOMETER);
		});

		horizontalFieldOfView.addListener((v, oldVal, newVal)->{
			arc.setLength(newVal);
			arc.setStartAngle((90-azimuth.get())%360 - (newVal/2));
		});

		azimuth.addListener((v, oldVal, newVal)->{
			arc.setStartAngle((90-newVal)%360 - (horizontalFieldOfView.get()/2));
		});

		longitude.addListener((v, oldVal, newVal)->{
			arc.setCenterX((longitude.get()/10_000.0 - LEFT_LONGITUDE)*PIXEL_PER_DEGREE);
		});

		latitude.addListener((v, oldVal, newVal)->{
			arc.setCenterY(-((latitude.get()/10_000.0 - TOP_LATITUDE)*PIXEL_PER_DEGREE ));
		});
		
		getChildren().add(group);

	}
}
