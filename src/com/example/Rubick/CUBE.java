package com.example.Rubick;

import android.opengl.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: vlastachu
 * Date: 26.04.13
 * Time: 16:33
 */
public class CUBE implements Drawable {
	private float[] color = new float[4];
	private final float[][] defaultColors;
    private QUAD[] quads;
    private float[] matrix = new float[16];
	private float[] rotateMatrixConst = new float[16];
	private float[] rotateMatrix = new float[16];
    private float[] translateMatrix = new float[16];
	private float bSize = 0.0f; //0.1 for corners
    public List<Edge> belongsTo = new ArrayList<Edge>();
	private Corner[] corners;
	private float[] cornerColor = {82/255f,84/255f, 92/255f, 1f};
    public void setMatrix(float[] matrix){
        this.matrix = Arrays.copyOf(matrix,16);
        for(QUAD quad: quads){
            quad.setMatrix(this.matrix);
        }
		for(Corner corner: corners)
			corner.setMatrix(this.matrix);
    }
	private float _angle = 0;
	public float getAngle(){
		return _angle;
	}
    public void rotate(float angle, float dirX, float dirY, float dirZ){
		_angle += angle;
		if (_angle > 360 || _angle < -360) _angle %= 360;
        Matrix.setRotateM(rotateMatrix, 0, _angle, dirX, dirY, dirZ);
    }

	public float[] getColor() {
		return color;
	}

	public void setColor(float[] color) {
		this.color = color;
		for(QUAD quad: quads){
			quad.setColor(color);
		}
		for(Corner corner: corners){
			corner.setColor(color);
		}
	}

	public void resetColors(){
		for(int i = 0; i < 6; i++){
			quads[i].setColor(defaultColors[i]);
		}
		for(Corner corner: corners){
			corner.setColor(cornerColor);
		}
	}

	float[] position;

	public float[] getPosition() {
        //float [] res1 = LinearUtils.multiply(translateMatrix, new float[]{0,0,0,1});
		float [] res = LinearUtils.multiply(rotateMatrixConst, position);
		return res;
	}

	//methods for debug:
	public void say(){
		float [] res = LinearUtils.multiply(rotateMatrix, position);
		//Log.d("RUB","pos[0]: " + matrix[3] + ",pos[1] " + matrix[7] + ",pos[2] " + matrix[11] + ",pos[3] " + matrix[15]);
		Log.d("RUB","pos[0]: " + res[0] + ",pos[1] " + res[1] + ",pos[2] " + res[2] + ",pos[3] " + res[3]);

	}

	public void mark(){
		for(int i = 0; i < 6; i++){
			float[] col = {0.3f,0.3f,0.3f,1};

			quads[i].setColor(col);
		}
	}
	public void correctPosition(){
		float[] temp = Arrays.copyOf(rotateMatrixConst,16);
		Matrix.multiplyMM(rotateMatrixConst,0,rotateMatrix,0,temp,0);
		_angle = 0;
		rotate(0,0,1,0);
	}

	public float[] getTopColor(){
		return defaultColors[5];
	}

	//two non-efficient methods for quads initialization
	private float[] vplus(float[] a, float[] b){
		float[] res = new float[a.length];
		for(int i = 0; i < a.length; i++) res[i] = a[i] + b[i];
		return res;
	}
	private void rotateAroundZ(float[] a){
		float temp = a[0];
		a[0] = -a[1];
		a[1] = temp;
	}
    public CUBE(float startPosX, float startPosY, float startPosZ, float[] colorFront, float[] colorBack, float[] colorRight, float[] colorLeft,float[] colorTop, float[] colorBottom ) {
		Matrix.setIdentityM(rotateMatrixConst, 0);
		Matrix.setIdentityM(rotateMatrix, 0);
        Matrix.setIdentityM(translateMatrix, 0);
		defaultColors = new float[][]{colorRight, colorTop,  colorLeft, colorBottom, colorFront, colorBack};
		quads = new QUAD[6];
		corners = new Corner[0];    //no corners in this realise
		/*quads[2] = new QUAD(new float[]{0.5f, 0.5f, -0.5f}, new float[]{0.5f, -0.5f, 0.5f});
		//left
		quads[3] = new QUAD(new float[]{-0.5f, 0.5f, -0.5f}, new float[]{-0.5f, -0.5f, 0.5f});
		//bottom
		quads[4] = new QUAD(new float[]{0.5f, -0.5f, -0.5f},new float[]{0.5f, -0.5f, 0.5f}, new float[]{-0.5f, -0.5f, -0.5f},new float[]{-0.5f, -0.5f, 0.5f});
		//top
		quads[5] = new QUAD(new float[]{0.5f, 0.5f, -0.5f},new float[]{0.5f, 0.5f, 0.5f}, new float[]{-0.5f, 0.5f, -0.5f},new float[]{-0.5f, 0.5f, 0.5f});
*/
		float[][] rot = {{0.5f, 0.5f, -0.5f}, {0.5f, 0.5f, 0.5f}, {0.5f, -0.5f, -0.5f}, {0.5f, -0.5f, 0.5f}};
		float[] d = {bSize, 0f, 0f};
		//corners[0] = new Corner(rot[0], rot[1], d);
		for (int i = 0; i < 4; i++){
			quads[i] = new QUAD(vplus(rot[0], d), vplus(rot[1], d), vplus(rot[2], d), vplus(rot[3], d));
			//corners[3*i+1] = new Corner(rot[1], rot[3], d);
			//corners[3*i+2] = new Corner(rot[3], rot[2], d);
			for(int j = 0; j < 4; j++) rotateAroundZ(rot[j]);
			rotateAroundZ(d);
		}
		quads[4] = new QUAD(new float[]{0.5f, 0.5f, 0.5f + bSize}, new float[]{-0.5f, -0.5f, 0.5f + bSize});
		quads[5] = new QUAD(new float[]{0.5f, 0.5f, -0.5f - bSize}, new float[]{-0.5f, -0.5f, -0.5f - bSize});
        resetColors();
		position = new float[]{startPosX, startPosY, startPosZ, 1f};
		Matrix.translateM(translateMatrix, 0, startPosX, startPosY, startPosZ);
    }

    public void draw() {
        float[] tempMatrix = Arrays.copyOf(matrix, 16);
		Matrix.multiplyMM(matrix, 0, tempMatrix, 0,rotateMatrix, 0);
		tempMatrix = Arrays.copyOf(matrix, 16);
		Matrix.multiplyMM(matrix, 0, tempMatrix, 0,rotateMatrixConst, 0);
        for (QUAD quad : quads){
            quad.setMatrix(matrix);
            quad.draw();
		}
		for (Corner corner : corners){
			corner.setMatrix(matrix);
			corner.draw();
		}
    }
    public void draw(float[] MVP){
        Matrix.multiplyMM(matrix,0,MVP,0,rotateMatrix,0);
		float[] tempMatrix = Arrays.copyOf(matrix, 16);
		Matrix.multiplyMM(matrix, 0, tempMatrix, 0,rotateMatrixConst, 0);
        tempMatrix = Arrays.copyOf(matrix, 16);
        Matrix.multiplyMM(matrix, 0, tempMatrix, 0,translateMatrix, 0);

        for (QUAD quad : quads){
            quad.setMatrix(matrix);
            quad.draw();
        }
		for (Corner corner : corners){
			corner.setMatrix(matrix);
			corner.draw();
		}
    }
}
