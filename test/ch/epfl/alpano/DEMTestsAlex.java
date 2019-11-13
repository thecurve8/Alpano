package ch.epfl.alpano;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;

public class DEMTestsAlex {

	@Test
	public void IndexOfZero (){
		assertEquals(0, DiscreteElevationModel.sampleIndex(0),0);
	}
	
	@Test
	public void IndexOfOneDegree(){
		assertEquals(DiscreteElevationModel.SAMPLES_PER_DEGREE, DiscreteElevationModel.sampleIndex(Math.toRadians(1)),0);
	}
	
	@Test
	public void IndexOfOneRadian(){
		assertEquals(DiscreteElevationModel.SAMPLES_PER_RADIAN, DiscreteElevationModel.sampleIndex(1),0);
	}
	
	@Test
	public void IndexOfNegativeOneRadian(){
		assertEquals((-1)*DiscreteElevationModel.SAMPLES_PER_RADIAN, DiscreteElevationModel.sampleIndex(-1),0);
	}
	
	@Test
	public void IndexOfNegativeALotOfRadians(){
		assertEquals((-17.546)*DiscreteElevationModel.SAMPLES_PER_RADIAN, DiscreteElevationModel.sampleIndex(-17.546),0);
	}
	
	@Test
	public void IndexOfNegativeALotOfDegrees(){
		assertEquals((-17.546)*DiscreteElevationModel.SAMPLES_PER_DEGREE, DiscreteElevationModel.sampleIndex(Math.toRadians(-17.546)),0);
	}
	
	
	private Interval1D i1 = new Interval1D(-10, 10);
	private Interval1D i2 = new Interval1D(10, 30);
	
	final class DemZero implements DiscreteElevationModel {
		  private final Interval2D extent;

		  public DemZero(Interval2D extent) {
		    this.extent = extent;
		  }

		  @Override
		  public void close() throws Exception { }

		  @Override
		  public Interval2D extent() { return extent; }

		  @Override
		  public double elevationSample(int x, int y) {
		    return 0;
		  }
	}
	
	DemZero a =  new DemZero(new Interval2D(i1, i2));
	ContinuousElevationModel b = new ContinuousElevationModel(a);
	
	@Test
	public void HorizontalDemAltitude(){
		
		assertEquals(0, b.elevationAt(new GeoPoint(0, 20/DiscreteElevationModel.SAMPLES_PER_RADIAN)), 0);
	}
	
	@Test
	public void HorizontalDemSlope(){
		for(int i = -11; i<11; ++i){
			for(int j = 9; j<31 ; j++){
				assertEquals(0, b.slopeAt(new GeoPoint(i/DiscreteElevationModel.SAMPLES_PER_RADIAN, j/DiscreteElevationModel.SAMPLES_PER_RADIAN)), 0);
			}
		}
	}
	
	final class DemTwenty implements DiscreteElevationModel {
		  private final Interval2D extent;

		  public DemTwenty(Interval2D extent) {
		    this.extent = extent;
		  }

		  @Override
		  public void close() throws Exception { }

		  @Override
		  public Interval2D extent() { return extent; }

		  @Override
		  public double elevationSample(int x, int y) {
		    return 20;
		  }
	}

	
	DemTwenty c =  new DemTwenty(new Interval2D(i1, i2));
	ContinuousElevationModel d = new ContinuousElevationModel(c);
	
	@Test
	public void AltitudeOutOfTwenty(){
		for(double i = -12; i<-11; i+=0.1){
			for(double j = 8 ; j<9; j+=0.1){
				assertEquals(0, d.elevationAt(new GeoPoint(i/DiscreteElevationModel.SAMPLES_PER_RADIAN, j/DiscreteElevationModel.SAMPLES_PER_RADIAN)),0);
			}
		}
	}
	
	@Test
	public void AltitudeInTwenty(){
		for(double i = -10; i<=10; i+=0.1){
			for(double j = 10 ; j<=30; j+=0.1){
				assertEquals(20, d.elevationAt(new GeoPoint(i/DiscreteElevationModel.SAMPLES_PER_RADIAN, j/DiscreteElevationModel.SAMPLES_PER_RADIAN)),0);
			}
		}
	}
	
	@Test
	public void AltitudeAroundTwenty(){
		assertEquals(10, d.elevationAt(new GeoPoint(-10.5/DiscreteElevationModel.SAMPLES_PER_RADIAN, 22/DiscreteElevationModel.SAMPLES_PER_RADIAN)), 0.001);
		assertEquals(10, d.elevationAt(new GeoPoint(-10.5/DiscreteElevationModel.SAMPLES_PER_RADIAN, 10/DiscreteElevationModel.SAMPLES_PER_RADIAN)), 0.001);
		assertEquals(10, d.elevationAt(new GeoPoint(10.5/DiscreteElevationModel.SAMPLES_PER_RADIAN, 16/DiscreteElevationModel.SAMPLES_PER_RADIAN)), 0.001);
		assertEquals(10, d.elevationAt(new GeoPoint(-5/DiscreteElevationModel.SAMPLES_PER_RADIAN, 9.5/DiscreteElevationModel.SAMPLES_PER_RADIAN)), 0.001);
		assertEquals(5, d.elevationAt(new GeoPoint(-10.5/DiscreteElevationModel.SAMPLES_PER_RADIAN, 9.5/DiscreteElevationModel.SAMPLES_PER_RADIAN)), 0.001);
	}
	
	@Test
	public void SlopeTestInsidetwenty(){
		for(double i = -9; i<=9; i+=0.1){
			for(double j = 11 ; j<=29; j+=0.1){
				assertEquals(0, d.slopeAt(new GeoPoint(i/DiscreteElevationModel.SAMPLES_PER_RADIAN, j/DiscreteElevationModel.SAMPLES_PER_RADIAN)),0.0001);
			}
		}
	}
	
	@Test (expected = NullPointerException.class)
	public void NullInConstructor(){
		new ContinuousElevationModel(null);
	}
	
	final class DemDiagonal implements DiscreteElevationModel {
		  private final Interval2D extent;

		  public DemDiagonal(Interval2D extent) {
		    this.extent = extent;
		  }

		  @Override
		  public void close() throws Exception { }

		  @Override
		  public Interval2D extent() { return extent; }

		  @Override
		  public double elevationSample(int x, int y) {
		    return x;
		  }
	}

	DemDiagonal di = new DemDiagonal(new Interval2D(i1, i2));
	ContinuousElevationModel diag = new ContinuousElevationModel(di);
	
	@Test
	public void AltitudeTestDiagonal(){
		for(double i = -5; i<=9; i+=0.1){
			for(double j = 11 ; j<=29; j+=0.1){
				assertEquals(i, diag.elevationAt(new GeoPoint(i/DiscreteElevationModel.SAMPLES_PER_RADIAN, j/DiscreteElevationModel.SAMPLES_PER_RADIAN)),0.1);
			}
		}
	}
	
	@Test
	public void SlopeTestDiagonal(){
		for(double i = -5; i<=9; i+=0.1){
			for(double j = 11 ; j<=29; j+=0.1){
				assertEquals(1/Distance.toMeters(1/DiscreteElevationModel.SAMPLES_PER_RADIAN), diag.slopeAt(new GeoPoint(i/DiscreteElevationModel.SAMPLES_PER_RADIAN, j/DiscreteElevationModel.SAMPLES_PER_RADIAN)),0.1);
			}
		}
	}
}
