package ch.epfl.alpano.gui;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class UserParametersTest {

	@Test
	public void SanitizeWorksForGivenValues() {
		ArrayList<Integer> list = new ArrayList<>(Arrays.asList(-1, -60_000, 0, 59_000, 60_000, 60_001, 64_184, 100_000, 119_999, 120_000, 120_001, 123_345));
		ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(60_000, 60_000, 60_000, 60_000, 60_000, 60_001, 64_184, 100_000, 119_999, 120_000, 120_000, 120_000));
		for(int i=0; i<list.size(); ++i){
			assertEquals((int)expected.get(i), UserParameter.OBSERVER_LONGITUDE.sanitize(list.get(i)));
		}
	}

}
