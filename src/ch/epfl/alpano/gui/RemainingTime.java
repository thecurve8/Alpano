package ch.epfl.alpano.gui;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * StackPane containing a text field with the estimated remaining time
 */
public final class RemainingTime extends StackPane {
	final private ReadOnlyDoubleProperty startTime;
	final private ReadOnlyDoubleProperty percentage;
	final private Text text = new Text();
	
	/**
	 * Constructor of the stack pane with the text field with the estimated remaining time
	 * @param startTime ReadOnlyProperty containing the starts time of the computation of the new panorama
	 * @param percentage ReadOnlyProperty containing the percentage of the calculations done for the new panorama
	 */
	 public RemainingTime(final ReadOnlyDoubleProperty startTime, ReadOnlyDoubleProperty percentage) {
		    this.startTime  = startTime;
		    this.percentage = percentage;

		    syncProgress();
		    
		    percentage.addListener((prop, oldV, newV) -> {
		        syncProgress();
		    });

		    getChildren().setAll(text);
		  }

		  // synchronizes the progress indicated with the work done.
		  private void syncProgress() {
		    if (percentage == null || startTime == null || percentage.get() == 0) {
		      text.setText("Estimation du temps restant : --");
		    } else {
		    	double elapsedTime = System.nanoTime() - startTime.get();
		    	double estimatedTotalTime = elapsedTime / percentage.get() ;
		    	double remainingTime = (estimatedTotalTime - elapsedTime)/(double)10e8;
		    	text.setText(String.format("Estimation du temps restant : %.0f sec", (remainingTime)));
		    	text.setTextAlignment(TextAlignment.LEFT);
		    }
		  }

}
