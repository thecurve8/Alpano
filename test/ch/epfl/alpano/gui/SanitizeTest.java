package ch.epfl.alpano.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public final class SanitizeTest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
    	PanoramaParametersBean bean = new PanoramaParametersBean(PredefinedPanoramas.NIESEN);
    	
    	bean.heightProperty().set(800000000);
    	System.out.println(1);
		int a = (int)Math.floor((170 * (2500 - 1) / 110)+ 1);
		System.out.println(2);
		System.out.println("Expected : " + a);
		System.out.println(3);
		System.out.println("Actual : " + bean.heightProperty().get());
		System.out.println(4);
		if(a == bean.heightProperty().get())
			System.out.println("");

        Platform.exit();
    }
}