package ch.epfl.alpano.gui;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class PanoramParametersBeanTest {

	ArrayList<Integer> parameters = new ArrayList<>(Arrays.asList(76500, 467300, 600, 180, 110, 300, 2500, 800, 0));
	PanoramaParametersBean bean = new PanoramaParametersBean(PredefinedPanoramas.NIESEN);
	

	@Test (expected = NullPointerException.class)
	public void NullTest(){
		new PanoramaParametersBean(null);
	}

	@Test
	public void ParametersSame(){
		assertEquals(PredefinedPanoramas.NIESEN, bean.parametersProperty().get());
	}
	
	@Test
	public void ValuesStoredCorrectly() {
		assertEquals(parameters.get(0), bean.observerLongitudeProperty().get());
		assertEquals(parameters.get(1), bean.observerLatitudeProperty().get());
		assertEquals(parameters.get(2), bean.observerElevationProperty().get());
		assertEquals(parameters.get(3), bean.centerAzimuthProperty().get());
		assertEquals(parameters.get(4), bean.horizontalFieldOfViewProperty().get());
		assertEquals(parameters.get(5), bean.maxDistanceProperty().get());
		assertEquals(parameters.get(6), bean.widthProperty().get());
		assertEquals(parameters.get(7), bean.heightProperty().get());
		assertEquals(parameters.get(8), bean.superSamplingExponentProperty().get());
	}
	
	@Test
	public void changesEffectsAreCorrectTest(){
		bean.observerLongitudeProperty().set(76000);
		assertEquals((Integer)76000, bean.observerLongitudeProperty().get());
	}
	
	/* This test is made in SanitizeTest class -> Application needed
		@Test
		public void sanitizeCorrectTest(){
			bean.heightProperty().set(800000);
			int a = (int)Math.floor((170 * (2500 - 1) / 110)+ 1);
			assertEquals((Integer)a, bean.heightProperty().get());
		}
	 * 
	 */
	

}

