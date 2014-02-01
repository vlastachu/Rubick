package com.example.Rubick;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.example.Rubick.MyGL20Renderer.loadShader;

/**
 * User: vlastachu
 * Date: 26.04.13
 * Time: 16:28
 */


class QUAD extends Mesh {

	public QUAD(float[] rightTop, float[] leftBottom) {
		init(rightTop, leftBottom);
		postInit();
	}

	public QUAD(float[] leftBottom, float[] leftTop, float[] rightBottom, float[] rightTop) {
		init(rightTop, leftBottom);
		coords[4] = leftTop[0];
		coords[5] = leftTop[1];
		coords[6] = leftTop[2];
		coords[8] = rightBottom[0];
		coords[9] = rightBottom[1];
		coords[10] = rightBottom[2];
		postInit();
	}



	private void init(float[] rightTop, float[] leftBottom) {
		coords = new float[COORDS_PER_VERTEX * 4];
		coords[0] = leftBottom[0];
		coords[1] = leftBottom[1];
		coords[2] = leftBottom[2];
		coords[4] = leftBottom[0];
		coords[5] = rightTop[1];
		coords[6] = leftBottom[2];

		coords[8] = rightTop[0];
		coords[9] = leftBottom[1];
		coords[10] = rightTop[2];
		coords[12] = rightTop[0];
		coords[13] = rightTop[1];
		coords[14] = rightTop[2];
	}

}
