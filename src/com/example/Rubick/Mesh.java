package com.example.Rubick;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.example.Rubick.MyGL20Renderer.loadShader;

/**
 * User: vlastachu
 * Date: 30.10.13
 * Time: 0:14
 * Basic class for real opengl triangle-set objects
 */
public class Mesh implements Drawable {

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

	private float color[] = new float[4];
	private int mProgram;
	private int mPositionHandle;
	private int mColorHandle;
	private int mMVPMatrixHandle;
	static final int COORDS_PER_VERTEX = 4;
	protected float[] coords;
	protected float[] matrix;
	protected FloatBuffer vertexBuffer;
	protected void postInit() {
		for(int i = 3;i < coords.length; i += 4)
			coords[i] = 1;
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

	public float[] getColor() {
		return color;
	}

	public void setColor(float[] color) {
		System.arraycopy(color, 0, this.color, 0, 4);
	}

	public void setMatrix(float[] matrix) {
		this.matrix = matrix;
	}

	public void draw() {
		GLES20.glUseProgram(mProgram);
		mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, coords.length / COORDS_PER_VERTEX,
				GLES20.GL_FLOAT, false,
				COORDS_PER_VERTEX * 4, vertexBuffer);
		mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
		GLES20.glUniform4fv(mColorHandle, 1, color, 0);
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, matrix, 0);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, coords.length / COORDS_PER_VERTEX);
		GLES20.glDisableVertexAttribArray(mPositionHandle);
	}
}
