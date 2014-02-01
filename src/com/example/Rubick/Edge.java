package com.example.Rubick;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: vlastachu
 * Date: 26.04.13
 * Time: 20:09
 */
public class Edge {
    List<CUBE> cubes = new ArrayList<CUBE>();

    public float[] getNormal() {
        return Arrays.copyOf(dir, dir.length);
    }

    private float[] dir = new float[3];
    Edge(float directionX, float directionY, float directionZ){
        dir[0] = directionX;
        dir[1] = directionY;
        dir[2] = directionZ;
    }
    public Edge add(CUBE cube){
        cubes.add(cube);
        cube.belongsTo.add(this);
        return this;
    }
	public void clean(){           //rename to clear
		for(CUBE cube: cubes)
			cube.belongsTo.clear();
		cubes.clear();
	}
	private float lastRotationDirection = 0.6f;
	private float defaultAngle = 2.5f;
    public void rotate(float angle){
        for(CUBE cube: cubes){
            cube.rotate(angle, dir[0], dir[1], dir[2]);
        }
		if(Math.abs(lastRotationDirection + angle) < 0.5)
			lastRotationDirection *= -1;
		else if(Math.abs(lastRotationDirection + angle) < 5)
			lastRotationDirection += angle;
    }

	public boolean snapToEdge(){
			for (CUBE cube: cubes)
				if(Math.abs(cube.getAngle())%90 > defaultAngle)
					cube.rotate(lastRotationDirection > 0? defaultAngle: -defaultAngle, dir[0], dir[1], dir[2]);
				else
					cube.rotate( - cube.getAngle()%90f, dir[0], dir[1], dir[2]);
		return Math.abs(cubes.get(0).getAngle())%90<0.1;
	}



}
