package com.example.Rubick;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * User: vlastachu
 * Date: 26.04.13
 * Time: 20:09
 */
public class Edge {
    List<CUBE> cubes = new ArrayList<CUBE>();
    private float dirX, dirY, dirZ;
    Edge(float directionX, float directionY, float directionZ){
        dirX = directionX;
        dirY = directionY;
        dirZ = directionZ;
    }

    public Edge add(CUBE cube){
        cubes.add(cube);
        cube.belongsTo.add(this);
        return this;
    }
	private float lastRotationDirection = 0.5f;
    public void rotate(float angle){
        for(CUBE cube: cubes){
            cube.rotate(angle, dirX, dirY, dirZ);
        }
		lastRotationDirection *= angle > 0? 1: -1;
    }

	public boolean snapToEdge(){
		//Log.d("RUB", "scalarShift: " + Float.toString(scalarShift));
			for (CUBE cube: cubes)
				if(cube.getAngle()%90 > lastRotationDirection)
					cube.rotate(lastRotationDirection, dirX, dirY, dirZ);
				else
					cube.rotate(lastRotationDirection - cube.getAngle()%90f, dirX, dirY, dirZ);
		return cubes.get(0).getAngle()%90<0.01;
	}

    public void linkToEdge(){

    }


}
