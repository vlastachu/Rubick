package com.example.Rubick;

import android.opengl.Matrix;
import android.util.Log;

/**
 * User: vlastachu
 * Date: 30.10.13
 * Time: 0:04
 */
public class Corner extends Mesh {
	final int QUADS_PER_CORNER = 2;
	public Corner(float[] center_vec3, float[] center2_vec3, float[] radius_vec3) {
		float[] radius_vec4 = {radius_vec3[0], radius_vec3[1], radius_vec3[2], 1};
		float[] center_vec4 = {center_vec3[0], center_vec3[1], center_vec3[2], 1};
		float[] center2_vec4 = {center2_vec3[0], center2_vec3[1], center2_vec3[2], 1};

		Log.d("RUB", "radiusvec4[" + radius_vec4[0] + ", " + radius_vec4[1] + ", " +radius_vec4[2] + ", " + radius_vec4[3]);
		float[] centerDir = new float[3];
		LinearUtils.minus(center2_vec3, center_vec3, centerDir);
		LinearUtils.normalize(centerDir);
		float[] tempRotateMatrix = new float[16];
		Matrix.setRotateM(tempRotateMatrix, 0, 90.0f/QUADS_PER_CORNER, centerDir[0], centerDir[1], centerDir[2]);

		coords = new float[COORDS_PER_VERTEX*2*(QUADS_PER_CORNER+1)];
		float[] temp = new float[4];
		for(int i = 0; i <= QUADS_PER_CORNER; i++){
			LinearUtils.plus(center_vec4,radius_vec4,temp);
			for(int j = 0; j < 4;j++) coords[j + i*COORDS_PER_VERTEX*2] = temp[j];
			LinearUtils.plus(center2_vec4,radius_vec4,temp);
			for(int j = 4; j < 8;j++) coords[j + i*COORDS_PER_VERTEX*2] = temp[j-4];
			radius_vec4 = LinearUtils.multiply(tempRotateMatrix, radius_vec4);
		}
		Log.d("RUB", "radiusvec4[" + radius_vec4[0] + ", " + radius_vec4[1] + ", " +radius_vec4[2] + ", " + radius_vec4[3]);
		for(float f: coords)
			Log.d("RUB","f: "+f);
		postInit();
	}
}
