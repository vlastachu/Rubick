package com.example.Rubick;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.example.Rubick.MyGL20Renderer.loadShader;

/**
 * User: vlastachu
 * Date: 26.04.13
 * Time: 16:28
 */



class QUAD implements Drawable{


    private float[] matrix;

    public void setMatrix(float[] matrix){
        this.matrix = matrix;
    }

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

    private FloatBuffer vertexBuffer;
    private int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 4;
    float coords[] = { // in counterclockwise order:
            -0.3f, -0.311004243f, 0.0f, 1.0f,   // left bottom
            -0.5f, 0.622008459f, 0.0f, 1.0f,  // left top
            0.5f, -0.311004243f, 0.0f, 1.0f,   // right bottom
            0.5f, 0.622008459f, 0.0f ,1.0f     // right top
    };
    private final int vertexCount = coords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // bytes per vertex

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    public QUAD(float[] rightTop, float[] leftBottom, float[] color) {
        init(rightTop,leftBottom,color);
        postInit();
    }
    private void init(float[] rightTop, float[] leftBottom, float[] color) {
        this.color = color;
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

    private void postInit(){
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
    public QUAD(float[] leftBottom, float[] leftTop, float[] rightBottom, float[] rightTop,    float[] color){
        init(rightTop,leftBottom,color);

        coords[4] = leftTop[0];
        coords[5] = leftTop[1];
        coords[6] = leftTop[2];
        coords[8] = rightBottom[0];
        coords[9] = rightBottom[1];
        coords[10] = rightBottom[2];
        postInit();
    }

    public void draw() {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, matrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
