package com.example.Rubick;

/**
 * User: vlastachu
 * Date: 26.04.13
 * Time: 17:11
 */
class Rubick implements Drawable {
    CUBE[] cubes = new CUBE[26];
    public Edge[] edges = new Edge[9];
    final float back[] = {186/255f, 198/255f, 54/255f, 1f};//green
	final float front[] = {227/255f, 118/255f, 59/255f, 1f};//yellow
	final float top[] = {201/255f, 60/255f, 88/255f, 1f};//violet
	final float bottom[] = {150/255f, 12/255f, 7/255f, 1f};  //red
	//final float cyan[] = {0.0f, 0.9f, 0.9f, 1.0f};
	final float left[] = {35/255f, 112/255f, 176/255f, 1f};//blue
	final float inner[] = {82/255f,84/255f, 92/255f, 1f};//white
	final float right[] = {70/255f, 60/255f, 88/255f, 1f};//black
    private float[] matrix;
	public final float space = 1.1f;
    Rubick() {
		//TODO BYDLOCODE
        cubes[0] = new CUBE(space, 0, 0, inner, inner, back, inner, inner, inner);
        cubes[1] = new CUBE(space, space, 0, inner, inner, back, inner, top, inner);
        cubes[2] = new CUBE(space, -space, 0, inner, inner, back, inner, inner, bottom);
        cubes[3] = new CUBE(space, 0, space, right, inner, back, inner, inner, inner);
        cubes[4] = new CUBE(space, space, space, right, inner, back, inner, top, inner);
        cubes[5] = new CUBE(space, -space, space, right, inner, back, inner, inner, bottom);
        cubes[6] = new CUBE(space, 0, -space, inner, left, back, inner, inner, inner);
        cubes[7] = new CUBE(space, space, -space, inner, left, back, inner, top, inner);
        cubes[8] = new CUBE(space, -space, -space, inner, left, back, inner, inner, bottom);

        cubes[9] = new CUBE(-space, 0, 0, inner, inner, inner, front, inner, inner);
        cubes[10] = new CUBE(-space, space, 0, inner, inner, inner, front, top, inner);
        cubes[11] = new CUBE(-space, -space, 0, inner, inner, inner, front, inner, bottom);
        cubes[12] = new CUBE(-space, 0, space, right, inner, inner, front, inner, inner);
        cubes[13] = new CUBE(-space, space, space, right, inner, inner, front, top, inner);
        cubes[14] = new CUBE(-space, -space, space, right, inner, inner, front, inner, bottom);
        cubes[15] = new CUBE(-space, 0, -space, inner, left, inner, front, inner, inner);
        cubes[16] = new CUBE(-space, space, -space, inner, left, inner, front, top, inner);
        cubes[17] = new CUBE(-space, -space, -space, inner, left, inner, front, inner, bottom);

        cubes[18] = new CUBE(0, 0, space, right, inner, inner, inner, inner, inner);
        cubes[19] = new CUBE(0, space, space, right, inner, inner, inner, top, inner);
        cubes[20] = new CUBE(0, -space, space, right, inner, inner, inner, inner, bottom);
        cubes[21] = new CUBE(0, 0, -space, inner, left, inner, inner, inner, inner);
        cubes[22] = new CUBE(0, space, -space, inner, left, inner, inner, top, inner);
        cubes[23] = new CUBE(0, -space, -space, inner, left, inner, inner, inner, bottom);
        cubes[24] = new CUBE(0, space, 0, inner, inner, inner, inner, top, inner);
        cubes[25] = new CUBE(0, -space, 0, inner, inner, inner, inner, inner, bottom);

        edges[0] = new Edge(1, 0, 0);
        edges[1] = new Edge(1, 0, 0);
        edges[2] = new Edge(1, 0, 0);
        edges[3] = new Edge(0, 1, 0);
        edges[4] = new Edge(0, 1, 0);
        edges[5] = new Edge(0, 1, 0);
        edges[6] = new Edge(0, 0, 1);
        edges[7] = new Edge(0, 0, 1);
        edges[8] = new Edge(0, 0, 1);
		resetEdges();
    }

	public void resetEdges(){
		for(CUBE cube: cubes){
			cube.correctPosition();
		}
		for(Edge edge: edges)
			edge.clean();
        for(CUBE cube: cubes){
			cube.resetColors();
			float[] pos = cube.getPosition();
			for(int i = 0; i < 3; i++)
				if(pos[i] <= space + 0.1 && pos[i] >= space -0.1){
					edges[i*3].add(cube);
				}
				else if(pos[i] <= -space + 0.1 && pos[i] >= -space -0.1){
					edges[i*3+1].add(cube);
				}
				else if(pos[i] <= 0.1 && pos[i] >= -0.1){
					edges[i*3+2].add(cube);
				}
		}
		cubes[17].say();
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
