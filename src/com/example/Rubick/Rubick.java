package com.example.Rubick;

import java.util.ArrayList;
import java.util.List;

/**
 * User: vlastachu
 * Date: 26.04.13
 * Time: 17:11
 */
class Rubick implements Drawable {
    CUBE[] cubes = new CUBE[26];
    public Edge[] edges = new Edge[9];
    float green[] = {0.53671875f, 0.76953125f, 0.22265625f, 1.0f};
    float yellow[] = {0.76671875f, 0.76953125f, 0.22265625f, 1.0f};
    float red[] = {0.8f, 0.2f, 0.2f, 1.0f};
    float blue[] = {0.2f, 0.2f, 0.9f, 1.0f};
    float cyan[] = {0.0f, 0.9f, 0.9f, 1.0f};
    float violet[] = {0.9f, 0.2f, 0.9f, 1.0f};
    float white[] = {1f, 1f, 1f, 1f};
    float black[] = {0f, 0f, 0f, 1f};
    private float[] matrix;
	public final float space = 1.05f;
    Rubick() {

        cubes[0] = new CUBE(space, 0, 0, white, white, green, white, white, white);
        cubes[1] = new CUBE(space, space, 0, white, white, green, white, red, white);
        cubes[2] = new CUBE(space, -space, 0, white, white, green, white, white, blue);
        cubes[3] = new CUBE(space, 0, space, black, white, green, white, white, white);
        cubes[4] = new CUBE(space, space, space, black, white, green, white, red, white);
        cubes[5] = new CUBE(space, -space, space, black, white, green, white, white, blue);
        cubes[6] = new CUBE(space, 0, -space, white, violet, green, white, white, white);
        cubes[7] = new CUBE(space, space, -space, white, violet, green, white, red, white);
        cubes[8] = new CUBE(space, -space, -space, white, violet, green, white, white, blue);

        cubes[9] = new CUBE(-space, 0, 0, white, white, white, yellow, white, white);
        cubes[10] = new CUBE(-space, space, 0, white, white, white, yellow, red, white);
        cubes[11] = new CUBE(-space, -space, 0, white, white, white, yellow, white, blue);
        cubes[12] = new CUBE(-space, 0, space, black, white, white, yellow, white, white);
        cubes[13] = new CUBE(-space, space, space, black, white, white, yellow, red, white);
        cubes[14] = new CUBE(-space, -space, space, black, white, white, yellow, white, blue);
        cubes[15] = new CUBE(-space, 0, -space, white, violet, white, yellow, white, white);
        cubes[16] = new CUBE(-space, space, -space, white, violet, white, yellow, red, white);
        cubes[17] = new CUBE(-space, -space, -space, white, violet, white, yellow, white, blue);

        cubes[18] = new CUBE(0, 0, space, black, white, white, white, white, white);
        cubes[19] = new CUBE(0, space, space, black, white, white, white, red, white);
        cubes[20] = new CUBE(0, -space, space, black, white, white, white, white, blue);
        cubes[21] = new CUBE(0, 0, -space, white, violet, white, white, white, white);
        cubes[22] = new CUBE(0, space, -space, white, violet, white, white, red, white);
        cubes[23] = new CUBE(0, -space, -space, white, violet, white, white, white, blue);
        cubes[24] = new CUBE(0, space, 0, white, white, white, white, red, white);
        cubes[25] = new CUBE(0, -space, 0, white, white, white, white, white, blue);

        edges[0] = new Edge(1, 0, 0);
        edges[1] = new Edge(1, 0, 0);
        edges[2] = new Edge(1, 0, 0);
        for (int i = 0; i < 9; i++) edges[0].add(cubes[i]);
        for (int i = 9; i < 18; i++) edges[1].add(cubes[i]);
        for (int i = 18; i < 26; i++) edges[2].add(cubes[i]);
        edges[3] = new Edge(0, 1, 0);
        edges[4] = new Edge(0, 1, 0);
        edges[5] = new Edge(0, 1, 0);
        edges[6] = new Edge(0, 0, 1);
        edges[7] = new Edge(0, 0, 1);
        edges[8] = new Edge(0, 0, 1);
        for (int i = 0; i < 24; i++) {
            if (i % 3 == 0)
                edges[3].add(cubes[i]);
            if (i % 3 == 1)
                edges[4].add(cubes[i]);
            if (i % 3 == 2)
                edges[5].add(cubes[i]);
            if (i % 9 == 0)
                edges[6].add(cubes[i]).add(cubes[i + 1]).add(cubes[i + 2]);
            if (i % 9 == 3)
                edges[7].add(cubes[i]).add(cubes[i + 1]).add(cubes[i + 2]);
            if (i % 9 == 6)
                edges[8].add(cubes[i]).add(cubes[i + 1]).add(cubes[i + 2]);
        }
        edges[4].add(cubes[24]);
        edges[5].add(cubes[25]);

        edges[8].add(cubes[24]).add(cubes[25]);
    }

    public void setMatrix(float[] matrix) {
        this.matrix = matrix;
        for (CUBE cube : cubes) {
            cube.setMatrix(matrix);
        }
    }

    public void draw() {
        for (CUBE cube : cubes)
            cube.draw(matrix);
    }
}
