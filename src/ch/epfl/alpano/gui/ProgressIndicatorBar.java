package ch.epfl.alpano.gui;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * ProgressBar with percentage text on it
 */
public final class ProgressIndicatorBar extends StackPane {
	  final private ReadOnlyDoubleProperty percentageDone;
	  final private ProgressBar bar = new ProgressBar();
	  final private Text text = new Text();

	  final private static double TOTAL_WORK = 1.0;
	  final private static int DEFAULT_LABEL_PADDING = 5;
	  final private static int WIDTH = 150;

	  /**
	   * Constructor of the ProgressIndicator bar
	   * @param workDone percentage already done in [0, 1]
	   */
	  public ProgressIndicatorBar(final ReadOnlyDoubleProperty workDone) {
	    this.percentageDone  = workDone;

	    syncProgress();
	    workDone.addListener((prop, oldV, newV) -> {
	        syncProgress();
	    });

	    bar.setMaxWidth(WIDTH); 
	    getChildren().setAll(bar, text);
	  }

	  // synchronizes the progress indicated with the work done.
	  private void syncProgress() {
	    if (percentageDone == null) {
	      text.setText("");
	      bar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
	    } else {
	      text.setText(String.format(" %.0f%%", (percentageDone.get()/ TOTAL_WORK)*100));
	      bar.setProgress(percentageDone.get() / TOTAL_WORK);
	    }
	    if(text.getBoundsInLocal() != null){
		    bar.setMinHeight(text.getBoundsInLocal().getHeight() + DEFAULT_LABEL_PADDING * 2);
		    bar.setMinWidth (text.getBoundsInLocal().getWidth()  + DEFAULT_LABEL_PADDING * 2);
	    }
	  }
	}