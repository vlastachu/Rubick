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
    private float[] rotateMatrix = new float[16];
    private float[] translateMatrix = new float[16];
    public List<Edge> belongsTo = new ArrayList<Edge>();

    public void setMatrix(float[] matrix){
        this.matrix = Arrays.copyOf(matrix,16);
        for(QUAD quad: quads){
            quad.setMatrix(this.matrix);
        }
    }
	private float _angle = 0;
	public float getAngle(){
		return _angle;
	}
    public void rotate(float angle, float dirX, float dirY, float dirZ){
		_angle += angle;
		if (_angle > 360 || _angle < -360)_angle = 0;
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
	}

	public void resetColors(){
		for(int i = 0; i < 6; i++){
			quads[i].setColor(defaultColors[i]);
		}
	}

	float[] position;

	public float[] getPosition() {
		float[] res = new float[4];
		return LinearUtils.multiply(rotateMatrix, position);
	}
    public CUBE(float startPosX, float startPosY, float startPosZ, float[] colorFront, float[] colorBack, float[] colorRight, float[] colorLeft,float[] colorTop, float[] colorBottom ) {
        Matrix.setIdentityM(rotateMatrix, 0);
        Matrix.setIdentityM(translateMatrix, 0);
		defaultColors = new float[][]{colorFront, colorBack, colorRight, colorLeft, colorBottom, colorTop};
		quads = new QUAD[6];
        //front
        quads[0] = new QUAD(new float[]{0.5f, 0.5f, 0.5f}, new float[]{-0.5f, -0.5f, 0.5f});
        //back
        quads[1] = new QUAD(new float[]{0.5f, 0.5f, -0.5f}, new float[]{-0.5f, -0.5f, -0.5f});
        //right
        quads[2] = new QUAD(new float[]{0.5f, 0.5f, -0.5f}, new float[]{0.5f, -0.5f, 0.5f});
        //left
        quads[3] = new QUAD(new float[]{-0.5f, 0.5f, -0.5f}, new float[]{-0.5f, -0.5f, 0.5f});
        //bottom
        quads[4] = new QUAD(new float[]{0.5f, -0.5f, -0.5f},new float[]{0.5f, -0.5f, 0.5f}, new float[]{-0.5f, -0.5f, -0.5f},new float[]{-0.5f, -0.5f, 0.5f});
        //top
        quads[5] = new QUAD(new float[]{0.5f, 0.5f, -0.5f},new float[]{0.5f, 0.5f, 0.5f}, new float[]{-0.5f, 0.5f, -0.5f},new float[]{-0.5f, 0.5f, 0.5f});
        resetColors();
		position = new float[]{startPosX, startPosY, startPosZ, 1f};
		Matrix.translateM(translateMatrix, 0, startPosX, startPosY, startPosZ);
    }

    public void draw() {
        float[] tempMatrix = Arrays.copyOf(matrix, 16);
        Matrix.multiplyMM(matrix, 0, tempMatrix, 0,rotateMatrix, 0);
        for (QUAD quad : quads){
            quad.setMatrix(matrix);
            quad.draw();
        }
    }
    public void draw(float[] MVP){
        Matrix.multiplyMM(matrix,0,MVP,0,rotateMatrix,0);
        float[] tempMatrix = Arrays.copyOf(matrix, 16);
        Matrix.multiplyMM(matrix, 0, tempMatrix, 0,translateMatrix, 0);

        for (QUAD quad : quads){
            quad.setMatrix(matrix);
            quad.draw();
        }
    }
}
