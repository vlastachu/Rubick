package com.example.Rubick;

import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: vlastachu
 * Date: 26.04.13
 * Time: 16:33
 */
public class CUBE implements Drawable {
    private QUAD[] quads;
    private float[] matrix = new float[16];
    private float[] rotateMatrix = new float[16];
    private float[] translateMatrix = new float[16];
    public List<Edge> belongsTo = new ArrayList<Edge>();

    public void setMatrix(float[] matrix){
        this.matrix = Arrays.copyOf(matrix,16);
        //Matrix.multiplyMM(this.matrix, 0, matrix, 0, rotateMatrix, 0);
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

    public CUBE(float startPosX, float startPosY, float startPosZ, float[] colorFront, float[] colorBack, float[] colorRight, float[] colorLeft,float[] colorTop, float[] colorBottom ) {
        Matrix.setIdentityM(rotateMatrix, 0);
        Matrix.setIdentityM(translateMatrix, 0);
        quads = new QUAD[6];
        //front
        quads[0] = new QUAD(new float[]{0.5f, 0.5f, 0.5f}, new float[]{-0.5f, -0.5f, 0.5f}, colorFront);
        //back
        quads[1] = new QUAD(new float[]{0.5f, 0.5f, -0.5f}, new float[]{-0.5f, -0.5f, -0.5f}, colorBack);
        //right
        quads[2] = new QUAD(new float[]{0.5f, 0.5f, -0.5f}, new float[]{0.5f, -0.5f, 0.5f}, colorRight);
        //left
        quads[3] = new QUAD(new float[]{-0.5f, 0.5f, -0.5f}, new float[]{-0.5f, -0.5f, 0.5f}, colorLeft);
        //bottom
        quads[4] = new QUAD(new float[]{0.5f, -0.5f, -0.5f},new float[]{0.5f, -0.5f, 0.5f}, new float[]{-0.5f, -0.5f, -0.5f},new float[]{-0.5f, -0.5f, 0.5f} , colorBottom);
        //top
        quads[5] = new QUAD(new float[]{0.5f, 0.5f, -0.5f},new float[]{0.5f, 0.5f, 0.5f}, new float[]{-0.5f, 0.5f, -0.5f},new float[]{-0.5f, 0.5f, 0.5f}, colorTop);
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
