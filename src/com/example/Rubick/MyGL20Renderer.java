package com.example.Rubick;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;


public class MyGL20Renderer implements GLSurfaceView.Renderer {

    //private Cub[] cubs;
    private Rubick cube;

    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }




    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        cube = new Rubick();
		chosenEdge = cube.edges[5];
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glEnable(GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glClearDepthf(1f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        Matrix.setIdentityM(MVPMatrix,0);
        Matrix.setIdentityM(ProjMatrix,0);
        Matrix.setIdentityM(VMatrix, 0);
        Matrix.setIdentityM(rotateMat, 0);
        //cubs initialization
    }

    long time;
    private float[] rotateMat = new float[16];
    private final float[] ProjMatrix = new float[16];
    private final float[] VMatrix = new float[16];
    private final float[] MVPMatrix = new float[16];
	private float[] cameraPos = {-1, 0, 0};
	private float cameraDistance = 7;
	private float[] cameraTop = {0, 1, 0};
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        Matrix.setLookAtM(VMatrix, 0, cameraPos[0]*cameraDistance, cameraPos[1]*cameraDistance, cameraPos[2]*cameraDistance, 0f, 0f, 0f, cameraTop[0], cameraTop[1], cameraTop[2]);
		time = SystemClock.uptimeMillis() % 4000L;
        Matrix.multiplyMM(MVPMatrix, 0, ProjMatrix, 0, VMatrix, 0);
        float[] tempMatrix = Arrays.copyOf(MVPMatrix, 16);
        Matrix.multiplyMM(MVPMatrix, 0, tempMatrix, 0,rotateMat, 0);
        cube.setMatrix(MVPMatrix);
        cube.draw();
    }
	private int surfaceWidth, surfaceHeight; float ratio;
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
		surfaceWidth = width;
		surfaceHeight = height;
        ratio = (float) width / height;


        Matrix.frustumM(ProjMatrix, 0, -ratio, ratio, -1, 1, 1, 9);
    }



	public void chooseCubeByPixel(int x, int y){
		for(int i = 0; i < cube.cubes.length; i++)
			cube.cubes[i].setColor(new float[]{i*20/255f,0,0});
		onDrawFrame(null);
		ByteBuffer buf = ByteBuffer.allocateDirect(3);
		buf.order(ByteOrder.nativeOrder());
		GLES20.glReadPixels(x, y, 1, 1, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, buf);
		byte b[] = new byte[3];
		buf.get(b);
		Integer i = Math.round((b[0]&0xFF)/20f);
		Log.d("RUB","chosenCube: " + i.toString());
		chosenCube = cube.cubes[i];
		for(CUBE _cube: cube.cubes)
			_cube.resetColors();
	}
	private CUBE chosenCube = null;
	private Edge chosenEdge = null;
	public boolean snapToEdge(){
		return chosenEdge.snapToEdge();
	}
	public void rotateEdge(float dx, float dy){
		dx = dx*6*ratio/((float)surfaceWidth);
		dy = dy*6/((float)surfaceHeight);
		float[] top = cameraTop.clone(), right = new float[3], shift = new float[3], touch = {dx, dy, 0};
//		LinearUtils.cross(cameraPos, top, right);
//		LinearUtils.normalize(right);
//		LinearUtils.scalarMultiply(top, dy);
//		LinearUtils.scalarMultiply(right, dx);
//		LinearUtils.plus(top, right, shift);
		if(chosenEdge == null){
			//векторно умножаем позицию камеры на нормаль грани
			//потом скалярно на вектор касания
			//ищем максимальный по модулю
			float max = 0;
			for(Edge edge: chosenCube.belongsTo){
				float[] cross = new float[3];
				LinearUtils.cross(cameraPos, edge.getNormal(), cross);
				float i = Math.abs(LinearUtils.dot(cross, touch));
				if(i > max){
					chosenEdge = edge;
					max = i;
				}
			}
		}

		float[] cross = new float[3];
		LinearUtils.cross(cameraPos, chosenEdge.getNormal(), cross);
		float i = LinearUtils.dot(cross, touch);
		float angle = (float)Math.atan((cube.space)/i);
		chosenEdge.rotate(angle);
	}
	//renderer.haltEdgeRotation();
	public void rotateCameraPos(float dx, float dy){
		dx = dx*6*ratio/((float)surfaceWidth);
		dy = dy*6/((float)surfaceHeight);
		float[] top = cameraTop.clone(), right = new float[3], tempRight = new float[3];
		LinearUtils.cross(cameraPos, top, right);
		LinearUtils.cross(cameraPos, top, tempRight);
		LinearUtils.normalize(right);
		LinearUtils.scalarMultiply(top, dy);
		LinearUtils.scalarMultiply(right, dx);
		LinearUtils.plus(cameraPos, top, null);
		LinearUtils.cross(tempRight, cameraPos,  cameraTop);
		LinearUtils.plus(cameraPos, right, null);
		LinearUtils.normalize(cameraPos);
		LinearUtils.normalize(cameraTop);
		//Log.d("RUB",new StringBuilder("camerapos: ").append(Float.toString(cameraPos[0])).append(" ").append(Float.toString(cameraPos[1])).append(" ").append(Float.toString(cameraPos[2])).toString());

	}
	//renderer.stopCamera();
	public void rotateCameraTop(float angle){
		float dx = 4*(float) Math.sin(angle);
		float dy = 4*(float) Math.cos(angle);
		float[] top = cameraTop.clone(), right = new float[3];
		LinearUtils.cross(cameraPos, top, right);
		LinearUtils.scalarMultiply(top, dy);
		LinearUtils.scalarMultiply(right, dx);
		LinearUtils.plus(cameraTop, top, null);
		LinearUtils.plus(cameraTop, right, null);
		LinearUtils.normalize(cameraTop);
	}
	public float getCameraDistance(){
		return 1/cameraDistance;
	}
	public void setCameraDistance(float d){
		//Log.d("RUB",Float.toString(d)+ " " + Float.toString(cameraDistance));
		if(1/d > 4 && 1/d < 8.5)
			cameraDistance = 1/d;
	}

}
