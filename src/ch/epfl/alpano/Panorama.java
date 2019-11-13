/**
 * @author Yannick Bloem (262179)
 * @author Alexander Apostolov (271798)
 *
 */

package ch.epfl.alpano;

import java.util.Arrays;
import static java.util.Objects.requireNonNull;


/**
 * Information representing a panorama
 */
public final class Panorama {

	//Arrays with information corresponding to their name. Information at index i is the information of the pixel of the panorama at linear index i
	private final PanoramaParameters parameters;
	private final float[] distance;
	private final float[] longitude;
	private final float[] latitude;
	private final float[] elevation;
	private final float[] slope;
	
	/**
	 * Constructor of the Panorama
	 * @param parameters PanoramaParameters parameters of the panorama
	 * @param distance float[] Array with information about distance of each pixel to observer
	 * @param longitude float[] Array with information about longitude of each pixel
	 * @param latitude float[] Array with information about latitude of each pixel
	 * @param elevation float[] Array with information about elevation of each pixel
	 * @param slope float[] Array with information about slope of each pixel
	 */
	private Panorama(PanoramaParameters parameters, float[] distance, float[] longitude, float[] latitude, float[] elevation, float[] slope){
		this.parameters = parameters;
		this.distance = distance;
		this.longitude = longitude;
		this.latitude = latitude;
		this.elevation = elevation;
		this.slope = slope;
	}

	/**
	 * Parameters of the panorama
	 * @return PanoramaParameters parameters of the panorama
	 */
	public PanoramaParameters parameters(){
		return this.parameters;
	}
	
	/**
	 * Distance to observer at given pixel index 
	 * @param x int horizontal coordinate of the pixel
	 * @param y int vertical coordinate of the pixel
	 * @return float distance to observer
	 */
	public float distanceAt(int x, int y){
		checkIfIndexIsInBounds(x, y);
		return distance[parameters.linearSampleIndex(x, y)];
	}
	
	/**
	 * Longitude of a given pixel index
	 * @param x int horizontal coordinate of the pixel
	 * @param y int vertical coordinate of the pixel
	 * @return float longitude of the pixel
	 */
	public float longitudeAt(int x, int y){
		checkIfIndexIsInBounds(x, y);
		return longitude[parameters.linearSampleIndex(x, y)];
	}
	
	/**
	 * Latitude of a given pixel index
	 * @param x int horizontal coordinate of the pixel
	 * @param y int vertical coordinate of the pixel
	 * @return float latitude of the pixel
	 */
	public float latitudeAt(int x, int y){
		checkIfIndexIsInBounds(x, y);
		return latitude[parameters.linearSampleIndex(x, y)];
	}
	
	/**
	 * Elevation of a given pxel index
	 * @param x int horizontal coordinate of the pixel
	 * @param y int vertical coordinate of the pixel
	 * @return float elevation of the pixel
	 */
	public float elevationAt(int x, int y){
		checkIfIndexIsInBounds(x, y);
		return elevation[parameters.linearSampleIndex(x, y)];
	}
	
	/**
	 * Slope of a given pixel index
	 * @param x int horizontal coordinate of the pixel
	 * @param y int vertical coordinate of the pixel
	 * @return float slope of the pixel
	 */
	public float slopeAt(int x, int y){
		checkIfIndexIsInBounds(x, y);
		return slope[parameters.linearSampleIndex(x, y)];
	}
	
	/**
	 * Distance at given pixel index with default distance if pixel index is out index
	 * @param x int horizontal coordinate of the pixel
	 * @param y int vertical coordinate of the pixel
	 * @param d float default distance
	 * @return float distance of the pixel (default distance if pixel out of bounds
	 */
	public float distanceAt(int x, int y, float d){
		if(!parameters.isValidSampleIndex(x, y)){
			return d;
		}else{
			return distanceAt(x, y);
		}
	}
	
	/**
	 * Check if given pixel index is inside the panorama
	 * @param x int horizontal coordinate of the pixel
	 * @param y int vertical coordinate of the pixel
	 * @throws IndexOutOfBoundsEsception if not
	 */
	private void checkIfIndexIsInBounds(int x, int y){
		if(!parameters.isValidSampleIndex(x, y)){
			throw new IndexOutOfBoundsException("Index ("+x +", "+y+") is not in range");
		}
	}
	
	/**
	 * Builder of the panorama
	 */
	public final static class Builder{
		
		//Arrays with information corresponding to their name. Information at index i is the information of the pixel of the panorama at linear index i
		private float[] distance;
		private float[] longitude;
		private float[] latitude;
		private float[] elevation;
		private float[] slope;
		private boolean hasBuilt;
		private final PanoramaParameters parameters;
		
		/**
		 * Constructor of the Panorama.Builder
		 * @param parameters PanoramaParameters parameters of the panorama
		 */
		public Builder(PanoramaParameters parameters){
			this.parameters = requireNonNull(parameters);
			int size = parameters.width() * parameters.height();
			
			distance = new float[size];
			longitude = new float[size];
			latitude = new float[size];
			elevation = new float[size];
			slope = new float[size];
			
			//Filling Array with default values all other arrays are filled with 0 automatically
			Arrays.fill(distance, Float.POSITIVE_INFINITY);
		}
		
		/**
		 * Check if index is in bounds and that the panorama has yet not been built
		 * @param x int horizontal coordinate of the pixel
		 * @param y int vertical coordinate of the pixel
		 * @throws IllegalStateException if panorama has already been built
		 * @throws IndexOutOfBoundsException if pixel index is out of bounds of the panorama
		 */
		private void checkIfIndexIsInBoundsAndHasNotBuilt(int x, int y){
			if(hasBuilt){
				throw new IllegalStateException("Panorama already built");
			}
			if(!parameters.isValidSampleIndex(x, y)){
				throw new IndexOutOfBoundsException("Index ("+x +", "+y+") is not in range");
			}
		}
		
		/**
		 * Setting distance at given pixel index
		 * @param x int horizontal coordinate of the pixel
		 * @param y int vertical coordinate of the pixel
		 * @param distance float distance at the given pixel index
		 * @return this Panorama.Builder, the current state of the builder
		 */
		public Builder setDistanceAt(int x, int y, float distance){
			checkIfIndexIsInBoundsAndHasNotBuilt(x, y);
			this.distance[parameters.linearSampleIndex(x, y)] = distance;
			return this;
		}
		
		/**
		 * Setting longitude at given pixel index
		 * @param x int horizontal coordinate of the pixel
		 * @param y int vertical coordinate of the pixel
		 * @param longitude float longitude at the given pixel index
		 * @return this Panorama.Builder, the current state of the builder
		 */
		public Builder setLongitudeAt(int x, int y, float longitude){
			checkIfIndexIsInBoundsAndHasNotBuilt(x, y);
			this.longitude[parameters.linearSampleIndex(x, y)] = longitude;
			return this;
		}
		
		/**
		 * Setting latitude at given pixel index
		 * @param x int horizontal coordinate of the pixel
		 * @param y int vertical coordinate of the pixel
		 * @param latitude float latitude at the given pixel index
		 * @return this Panorama.Builder, the current state of the builder
		 */
		public Builder setLatitudeAt(int x, int y, float latitude){
			checkIfIndexIsInBoundsAndHasNotBuilt(x, y);
			this.latitude[parameters.linearSampleIndex(x, y)] = latitude;
			return this;
		}
		
		/**
		 * Setting elevation at given pixel index
		 * @param x int horizontal coordinate of the pixel
		 * @param y int vertical coordinate of the pixel
		 * @param elevation float elevation at the given pixel index
		 * @return this Panorama.Builder, the current state of the builder
		 */
		public Builder setElevationAt(int x, int y, float elevation){
			checkIfIndexIsInBoundsAndHasNotBuilt(x, y);
			this.elevation[parameters.linearSampleIndex(x, y)] = elevation;
			return this;
		}
		
		/**
		 * Setting slope at given pixel index
		 * @param x int horizontal coordinate of the pixel
		 * @param y int vertical coordinate of the pixel
		 * @param slope float slope at the given pixel index
		 * @return this Panorama.Builder, the current state of the builder
		 */
		public Builder setSlopeAt(int x, int y, float slope){
			checkIfIndexIsInBoundsAndHasNotBuilt(x, y);
			this.slope[parameters.linearSampleIndex(x, y)] = slope;
			return this;
		}
		
		/**
		 * Building the panorama
		 * @return Panorama panorama built with the Builder
		 */
		public Panorama build(){
			if(hasBuilt){
				throw new IllegalStateException("Panorama already built");
			}
			hasBuilt = true;
			Panorama panorama = new Panorama(parameters, distance, longitude, latitude, elevation, slope);
			
			//Putting all arrays to null to free memory space
			distance = null;
			longitude = null;
			latitude = null;
			elevation = null;
			slope = null;
			
			return panorama;
		}
		
	}
}
