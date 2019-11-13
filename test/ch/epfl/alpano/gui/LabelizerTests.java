/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano.gui;

import java.io.File;
import java.util.List;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.stage.Stage;

public final class LabelizerTests extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    	DiscreteElevationModel dem = new HgtDiscreteElevationModel(new File("N46E007.hgt"));
		ContinuousElevationModel cem = new ContinuousElevationModel(dem);
		List<Summit> summits = GazetteerParser.readSummitsFrom(new File("alps.txt"));
		Labelizer labelizer = new Labelizer(cem, summits);
		List<Node> nodes = labelizer.labels(PredefinedPanoramas.NIESEN.panoramaParameters());
		for(Node n : nodes){
			System.out.println(n);
		}
        Platform.exit();
    }
}