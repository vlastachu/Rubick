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


class QUAD implements Drawable {


	// number of coordinates per vertex in this array
	static final int COORDS_PER_VERTEX = 4;
	private final String vertexShaderCode =
			"attribute vec4 vPosition;" +
					"uniform mat4 uMVPMatrix;" +
					"void main() {" +
					"  gl_Position =  uMVPMatrix*vPosition;" +
					"}";
	private final String fragmentShaderCode =
			"precision mediump float;" +
					"uniform vec4 vColor;" +
					"void main() {" +
					"  gl_FragColor = vColor;" +
					"}";
	float[] coords = new float[16];
	private final int vertexCount = coords.length / COORDS_PER_VERTEX;
	private final int vertexStride = COORDS_PER_VERTEX * 4; // bytes per vertex
	// Set color with red, green, blue and alpha (opacity) values
	float color[] = new float[4];
	private float[] matrix;
	private FloatBuffer vertexBuffer;
	private int mProgram;
	private int mPositionHandle;
	private int mColorHandle;
	private int mMVPMatrixHandle;

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

	public void setMatrix(float[] matrix) {
		this.matrix = matrix;
	}

	public float[] getColor() {
		return color;
	}

	public void setColor(float[] color) {
		System.arraycopy(color, 0, this.color, 0, 4);
		Log.d("RUB", Float.toString(this.color[3]));
	}

	private void init(float[] rightTop, float[] leftBottom) {
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

	private void postInit() {
		ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 4);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(coords);
		vertexBuffer.position(0);
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
		mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
		GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
		GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
		GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

	}

	public void draw() {
		GLES20.glUseProgram(mProgram);
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false,
				vertexStride, vertexBuffer);
		mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, matrix, 0);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
}
