/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.gui;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

public final class Alpano extends Application {
	//HGT FILES TO USE FOR THE PROGRAM
	private final static File HGT_FILE1 = new File("N45E006.hgt");
	private final static File HGT_FILE2 = new File("N45E007.hgt");
	private final static File HGT_FILE3 = new File("N45E008.hgt");
	private final static File HGT_FILE4 = new File("N45E009.hgt");
	private final static File HGT_FILE5 = new File("N46E006.hgt");
	private final static File HGT_FILE6 = new File("N46E007.hgt");
	private final static File HGT_FILE7 = new File("N46E008.hgt");
	private final static File HGT_FILE8 = new File("N46E009.hgt");

	//Constants for the position in the GridPane
	private static final int COLUMN_COUNT_LONG_AND_LAT = 7;
	private static final int COLUMN_COUNT_ELEV_WIDTH_HEIGHT = 4;
	private static final int COLUMN_COUNT_AZI_FIELD_VISIB = 3;
	private static final int FONT_SIZE = 40;
	private static final int DISTANCE_PROGRESSBAR_TEXT = 80;
	
	private PanoramaComputerBean computerBean;
	private PanoramaParametersBean parametersBean;
	
	
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception{
		
		//------Initializing the PanoramParametersBean with default value at Alpes du Jura-------//
		parametersBean = new PanoramaParametersBean(PredefinedPanoramas.ALPES_DU_JURA);
		
		//------Initializing the PanoramComputerBean with no default value-------//
		ContinuousElevationModel cDEM = loadHGTFiles();
		File alps = new File("alps.txt");
		List<Summit> summits = GazetteerParser.readSummitsFrom(alps);
		computerBean = new PanoramaComputerBean(cDEM, summits);
	
		//Text Area with information about the point the cursor indicates
		TextArea mouseInfo = new TextArea();
		mouseInfo.setEditable(false);
		mouseInfo.setPrefRowCount(3);
		mouseInfo.setPrefColumnCount(20);
		
		//Image of the panorama
		ImageView panoView = new ImageView();
		panoView.imageProperty().bind(computerBean.imageProperty());
		panoView.fitWidthProperty().bind(parametersBean.widthProperty());
		panoView.setPreserveRatio(true);
		panoView.setSmooth(true);
		
		//Shows in the mouseInfo TextArea information about the point which the cursor indicates
		panoView.setOnMouseMoved((mouseEvent) -> {
				int x = (int)mouseEvent.getX()*(int)Math.pow(2, parametersBean.superSamplingExponentProperty().get());
	        	int y = (int)mouseEvent.getY()*(int)Math.pow(2, parametersBean.superSamplingExponentProperty().get());
	        	double latitude = Math.toDegrees(computerBean.getPanorama().latitudeAt(x, y));
	        	double longitude =  Math.toDegrees(computerBean.getPanorama().longitudeAt(x, y));
	        	Locale l = null;
	        	
	        	StringBuilder positionBuilder = new StringBuilder("Position : ");
	        	positionBuilder.append(String.format(l, "%.4f°", latitude));
	        	positionBuilder.append((longitude >= 0) ? "N" : "S");
	        	positionBuilder.append(String.format(l, " %.4f°", longitude));
	        	positionBuilder.append((longitude >= 0) ? "E" : "W");
	        	
	        	StringBuilder distanceBuilder = new StringBuilder("Distance : ");
	        	distanceBuilder.append(String.format(l, " %.1f km", computerBean.getPanorama().distanceAt(x, y) / 1_000));
	        	
	        	StringBuilder elevationBuilder = new StringBuilder("Altitude : ");
	        	elevationBuilder.append(String.format(l, " %.0f m", computerBean.getPanorama().elevationAt(x, y)));
	        	
	        	StringBuilder azimuthBuilder = new StringBuilder("Azimut : ");
	        	double azimut = computerBean.getParameters().panoramaParameters().azimuthForX(x);
	        	azimuthBuilder.append(String.format(l, " %.1f°", Math.toDegrees(azimut)));
	        	azimuthBuilder.append(" (" + Azimuth.toOctantString(azimut, "N", "E", "S", "W") + ")");
	        	
	        	StringBuilder altitudeBuilder = new StringBuilder("Elévation : ");
	        	double altitude = computerBean.getParameters().panoramaParameters().altitudeForY(y);
	        	altitudeBuilder.append(String.format(l, " %.1f°", Math.toDegrees(altitude)));
	        	
	        	mouseInfo.setText(positionBuilder.toString() + "\n"
	        						+ distanceBuilder.toString() + "\n" +
	        						elevationBuilder.toString() + "\n" + 
	        						azimuthBuilder.toString() + "\t" + altitudeBuilder.toString());
	    });
		
		//Opens OpenStreetMap to show a map of the surrounding area of the point where the user clicked on the panorama
		panoView.setOnMouseClicked((mouseEvent) -> {
				int x = (int)mouseEvent.getX()*(int)Math.pow(2, parametersBean.superSamplingExponentProperty().get());
				int y = (int)mouseEvent.getY()*(int)Math.pow(2, parametersBean.superSamplingExponentProperty().get());
				double latitude = Math.toDegrees(computerBean.getPanorama().latitudeAt(x, y));
				double longitude =  Math.toDegrees(computerBean.getPanorama().longitudeAt(x, y));
	
				String qy = String.format((Locale)null, "mlat=%.5f&mlon=%.5f", latitude, longitude);
				String fg = String.format((Locale)null, "map=15/%.5f/%.5f", latitude, longitude);
				URI osmURI = null;
				try {
					osmURI = new URI("http", "www.openstreetmap.org", "/", qy, fg);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				try {
					java.awt.Desktop.getDesktop().browse(osmURI);
				} catch (IOException e) {
					System.err.println("Le programme n'a pas réussi a se connecter à un navigateur Internet ou à l'URL correspondant");;
				}
			}
		);
		
		//Labels of the panorama
		Pane labelsPane = new Pane();
		labelsPane.prefWidthProperty().bind(panoView.fitWidthProperty());
		labelsPane.prefHeightProperty().bind(panoView.fitHeightProperty());
		Bindings.bindContent(labelsPane.getChildren(), computerBean.labels());
		labelsPane.setMouseTransparent(true);
		

		//Information when loading and cancel Button
		StackPane loadingPanorama = new StackPane();
		loadingPanorama.setBackground(new Background(new BackgroundFill(Color.color(1, 1, 1, 0.9), CornerRadii.EMPTY, Insets.EMPTY)));
		loadingPanorama.visibleProperty().bind(computerBean.isComputingProperty());
		
		ProgressIndicatorBar progressBar = new ProgressIndicatorBar((computerBean.percentageOfComputing()));
		progressBar.translateYProperty().set(DISTANCE_PROGRESSBAR_TEXT/2.0);
		
		Text loadingText =  new Text("Calcul du nouveau panorama");
		loadingText.setFont(new Font(FONT_SIZE));
		loadingText.setTextAlignment(TextAlignment.CENTER);
		loadingText.translateYProperty().set(-(DISTANCE_PROGRESSBAR_TEXT/2.0));
		
		RemainingTime remainingTime = new RemainingTime(computerBean.startingTime(), computerBean.percentageOfComputing());
		
		Button cancelLoading = new Button("Annuler");
		cancelLoading.setTranslateY(DISTANCE_PROGRESSBAR_TEXT);
		
		cancelLoading.setOnMouseClicked((event) -> {
			computerBean.setStopComputing(true);
			computerBean.setIsComputing(false);
		});
	
		loadingPanorama.getChildren().addAll(progressBar, loadingText, remainingTime, cancelLoading);
		
		
		//Map
		MapWithPosition map = new MapWithPosition(parametersBean);
		ScrollPane scrollMap =  new ScrollPane(map);
		
		//Update Notice when the panorama shown doesn't correspond to the panorama parameters entered by the user
		StackPane updateNotice = new StackPane();
		updateNotice.setBackground(new Background(new BackgroundFill(Color.color(1, 1, 1, 0.9), CornerRadii.EMPTY, Insets.EMPTY)));
		Text updateText = new Text("Les paramètres du panorama ont changé.\nCliquez ici pour mettre le dessin à jour.");
		updateText.setFont(new Font(FONT_SIZE));
		updateText.setTextAlignment(TextAlignment.CENTER);
		BooleanExpression parametersAreNotEqual = computerBean.parameterProperty().isNotEqualTo(parametersBean.parametersProperty());
		updateNotice.visibleProperty().bind(parametersAreNotEqual.and(computerBean.isComputingProperty().not()).or(computerBean.stopComputingProperty()));
		updateNotice.setOnMouseClicked((event) -> {
				computerBean.setParameters(parametersBean.parametersProperty().get());
			}		
		);
		updateNotice.getChildren().add(updateText);
		
		//StackPane with the panorama and the labels
		StackPane panoGroup = new StackPane();
		panoGroup.getChildren().addAll(panoView, labelsPane);
		
		//ScrollPane with the stackPane containing the panorama and the labels
		ScrollPane panoScrollPane = new ScrollPane(panoGroup);
			
		StackPane panoPane = new StackPane();

		panoPane.getChildren().addAll(panoScrollPane, updateNotice, loadingPanorama);
		
		//Grid Pane with panorama parameters and innformation aboout the point where the cursor is
		GridPane paramsGrid = new GridPane();
		paramsGrid.setAlignment(Pos.CENTER);
		paramsGrid.setHgap(10);
		paramsGrid.setVgap(3);
		paramsGrid.setPadding(new Insets(7, 5, 5, 5));
		
		//First Row
		Label latitudeLabel = new Label("Latitude (°) : ");
		TextField latitudeField = new TextField();
		settingAlignementAndPositionAndEdition(latitudeLabel, latitudeField, COLUMN_COUNT_LONG_AND_LAT);
		StringConverter<Integer> stringConverter = new FixedPointStringConverter(4);
		bindTextFieldWithParameter(latitudeField, parametersBean.observerLatitudeProperty(), stringConverter);
		
		Label longitudeLabel = new Label("Longitude (°) : ");
		TextField longitudeField = new TextField();
		settingAlignementAndPositionAndEdition(longitudeLabel, longitudeField, COLUMN_COUNT_LONG_AND_LAT);
		bindTextFieldWithParameter(longitudeField, parametersBean.observerLongitudeProperty(), stringConverter);
		
		Label altitudeLabel = new Label("Altitude (m) : ");
		TextField altitudeField = new TextField();
		settingAlignementAndPositionAndEdition(altitudeLabel, altitudeField, COLUMN_COUNT_ELEV_WIDTH_HEIGHT);
		bindTextFieldWithParameter(altitudeField, parametersBean.observerElevationProperty(), new IntegerStringConverter());
		
		paramsGrid.addRow(0, latitudeLabel, latitudeField, longitudeLabel, longitudeField, altitudeLabel, altitudeField);
		
		//second row
		Label azimuthLabel = new Label("Azimut (°) : ");
		TextField azimuthField = new TextField();
		settingAlignementAndPositionAndEdition(azimuthLabel, azimuthField, COLUMN_COUNT_AZI_FIELD_VISIB);
		bindTextFieldWithParameter(azimuthField, parametersBean.centerAzimuthProperty(), new IntegerStringConverter());
		
		Label angleLabel = new Label("Angle de vue (°) : ");
		TextField angleField = new TextField();
		settingAlignementAndPositionAndEdition(angleLabel, angleField, COLUMN_COUNT_AZI_FIELD_VISIB);
		bindTextFieldWithParameter(angleField, parametersBean.horizontalFieldOfViewProperty(), new IntegerStringConverter());
		
		Label visibilityLabel = new Label("Visibilité (km) : ");
		TextField visibilityField = new TextField();
		settingAlignementAndPositionAndEdition(visibilityLabel, visibilityField, COLUMN_COUNT_AZI_FIELD_VISIB);
		bindTextFieldWithParameter(visibilityField, parametersBean.maxDistanceProperty(), new IntegerStringConverter());
		
		paramsGrid.addRow(1, azimuthLabel, azimuthField, angleLabel, angleField, visibilityLabel, visibilityField);
		
		//Third row
		Label largeurLabel = new Label("Largeur (px) : ");
		TextField largeurField = new TextField();
		settingAlignementAndPositionAndEdition(largeurLabel, largeurField, COLUMN_COUNT_ELEV_WIDTH_HEIGHT);
		bindTextFieldWithParameter(largeurField, parametersBean.widthProperty(), new IntegerStringConverter());
		
		Label hauteurLabel = new Label("Hauteur (px) : ");
		TextField hauteurField = new TextField();
		settingAlignementAndPositionAndEdition(hauteurLabel, hauteurField, COLUMN_COUNT_ELEV_WIDTH_HEIGHT);
		bindTextFieldWithParameter(hauteurField, parametersBean.heightProperty(), new IntegerStringConverter());
		
		Label superSamplingLabel = new Label("Suréchentillonage : ");
		GridPane.setHalignment(superSamplingLabel, HPos.RIGHT);
		ChoiceBox<Integer> superSamplingChoice = new ChoiceBox<Integer>(FXCollections.observableArrayList(0, 1, 2));
		superSamplingChoice.disableProperty().bind(computerBean.isComputingProperty());
		StringConverter<Integer> listConverter = new LabeledListStringConverter("non", "2x", "4x");
		superSamplingChoice.setConverter(listConverter);
		superSamplingChoice.valueProperty().bindBidirectional(parametersBean.superSamplingExponentProperty());
		
		paramsGrid.addRow(2,  largeurLabel, largeurField, hauteurLabel, hauteurField, superSamplingLabel, superSamplingChoice);
		
		//Adding Predefined Panoramas in fifth row (4th row is empty)
		Button niesen = new Button("Niesen");
		niesen.disableProperty().bind(computerBean.isComputingProperty());
		niesen.setOnMouseClicked((event) -> {
			setPredefinedUserParameters(PredefinedPanoramas.NIESEN);
		});
		Button alpesDuJura = new Button("Alpes du Jura");
		alpesDuJura.disableProperty().bind(computerBean.isComputingProperty());
		alpesDuJura.setOnMouseClicked((event) -> {
			setPredefinedUserParameters(PredefinedPanoramas.ALPES_DU_JURA);
		});
		Button montRacine = new Button("Mont Racine");
		montRacine.disableProperty().bind(computerBean.isComputingProperty());
		montRacine.setOnMouseClicked((event) -> {
			setPredefinedUserParameters(PredefinedPanoramas.MONT_RACINE);
		});
		Button finsteraarhorn = new Button("Finsteraarhorn");
		finsteraarhorn.disableProperty().bind(computerBean.isComputingProperty());
		finsteraarhorn.setOnMouseClicked((event) -> {
			setPredefinedUserParameters(PredefinedPanoramas.FINSTERAARHORN);
		});
		Button sauvabelin = new Button("Tour de Sauvabelin");
		sauvabelin.disableProperty().bind(computerBean.isComputingProperty());
		sauvabelin.setOnMouseClicked((event) -> {
			setPredefinedUserParameters(PredefinedPanoramas.TOUR_DE_SAUVABELIN);
		});
		Button pelican = new Button("Plage du Pelican");
		pelican.disableProperty().bind(computerBean.isComputingProperty());
		pelican.setOnMouseClicked((event) -> {
			setPredefinedUserParameters(PredefinedPanoramas.PLAGE_DU_PELICAN);
		});
		paramsGrid.addRow(4, niesen, alpesDuJura, montRacine, finsteraarhorn, sauvabelin, pelican);
		
		//Adding TextArea with mouse info
		paramsGrid.add(mouseInfo, 6, 0, 1, 3);
		scrollMap.setPrefSize(250, 150);
		paramsGrid.add(scrollMap, 7, 0, 1, 5);
		
		//Putting everything together
		BorderPane root = new BorderPane();
		root.setCenter(panoPane);
		root.setBottom(paramsGrid);
		Scene scene = new Scene(root);
		
		//Changing value of the Cursor when the panorama is computing and when mouse hovers the cancel button
		computerBean.isComputingProperty().addListener((p, oldV, newV) -> {
			if(newV.booleanValue() && !cancelLoading.hoverProperty().get()){
				scene.setCursor(Cursor.WAIT);
			}else{
				scene.setCursor(Cursor.DEFAULT);
			}
		});
		cancelLoading.hoverProperty().addListener((prop, oldV, newV) -> {
			if(newV){
				scene.setCursor(Cursor.DEFAULT);
			} else if(computerBean.isComputing()) {
				scene.setCursor(Cursor.WAIT);
			}
		});

		primaryStage.setTitle("Alpano-bonus");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 * Continuous Elevation model given by the 8 HGT files
	 * @return ContinuousElevationModel representing the 8 HGT files
	 * @throws Exception if HGT files can't be read or united
	 */
	@SuppressWarnings("resource")
	private ContinuousElevationModel loadHGTFiles() throws Exception{
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
	
	/**
	 * Binds bidirectionally a given TextField with a given Property using a given string converter
	 * @param field TextField to be bound with a property
	 * @param property ObjectProperty<Integer> to be bound with the TextField
	 * @param converter StringConverter<Integer> converter to use between the field and the property
	 */
    private void bindTextFieldWithParameter(TextField field, ObjectProperty<Integer> property, StringConverter<Integer> converter){
		TextFormatter<Integer> stringFormatter = new TextFormatter<>(converter);
		stringFormatter.valueProperty().bindBidirectional(property);
		field.setTextFormatter(stringFormatter);
    }
	/**
	 * Setting H alignement of the given Label to the right, alignement of the given TextField to Center_Right and its PrefcolumnCount to the given columnCount
	 * @param label Label to set
	 * @param text TextField to set
	 * @param columnCount int columnCount used  
	 */
    private void settingAlignementAndPositionAndEdition(Label label, TextField text, int columnCount){
		GridPane.setHalignment(label, HPos.RIGHT);
		text.setAlignment(Pos.CENTER_RIGHT);
		text.setPrefColumnCount(columnCount);
		text.disableProperty().bind(computerBean.isComputingProperty());
	}
    
    private void setPredefinedUserParameters(PanoramaUserParameters parameters){
    	parametersBean.observerLatitudeProperty().set(parameters.observerLatitude());
    	parametersBean.observerLongitudeProperty().set(parameters.observerLongitude());
    	parametersBean.observerElevationProperty().set(parameters.observerElevation());
    	parametersBean.centerAzimuthProperty().set(parameters.centerAzimuth());
    	parametersBean.horizontalFieldOfViewProperty().set(parameters.horizontalFieldOfView());
    	parametersBean.maxDistanceProperty().set(parameters.maxDistance());
    	parametersBean.widthProperty().set(parameters.width());
    	parametersBean.heightProperty().set(parameters.height());
    	parametersBean.superSamplingExponentProperty().set(parameters.superSamplingExponent());
    }
}
