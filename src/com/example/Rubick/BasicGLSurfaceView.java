/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.Rubick;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

class BasicGLSurfaceView extends GLSurfaceView {
	private MyGL20Renderer renderer;
	private float beginX = -1, beginY = -1, beginAngle = 0, beginDistance = -1;

	private float getAngle(MotionEvent e){
		//law of cosines
		float d = getDistance(e);
		float a = getDistance(e.getX(0) - e.getX(1), e.getY(0) - e.getY(1), d, 0);
		if(e.getY(0) - e.getY(1) > 0)
			return (float)Math.acos(1 - a*a/(2*d*d));
		else
			return -(float)Math.acos(1 - a*a/(2*d*d));
	}

	private float getDistance(float x1, float y1, float x2, float y2){
		return (float)Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2));
	}

	private float getDistance(MotionEvent e){
		return getDistance(e.getX(0),e.getY(0),e.getX(1),e.getY(1));
	}

	public BasicGLSurfaceView(Context context) {
		super(context);
		setEGLContextClientVersion(2);
		renderer = new MyGL20Renderer();
		setRenderer(renderer);
	}
	private float x, y, dx, dy;             float beginCameraDistance = -1;
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if (e.getPointerCount() == 1){
			x = e.getX(0);
			y = e.getY(0);
			dx = x - beginX;
			dy = y - beginY;
			switch (e.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//renderer.chooseCubeByPixel((int)x, (int)y);
					break;
				case MotionEvent.ACTION_MOVE:
					if(beginX != -1 && beginY != -1)
						renderer.rotateEdge(dx, dy);
					requestRender();
					break;
				case MotionEvent.ACTION_UP:
					beginDistance = -1;
					beginX = -1;
					beginY = -1;
//					while(renderer.snapToEdge()){
//						requestRender();
//					}
				case MotionEvent.ACTION_CANCEL:
					beginDistance = -1;
					beginX = -1;
					beginY = -1;
			}
		}else {
			x = (e.getX(0) + e.getX(1))/2;
			y = (e.getY(0) + e.getY(1))/2;
			dx = x - beginX;
			dy = y - beginY;
			float angle = getAngle(e);
			float distance = getDistance(e);

			if(beginDistance == -1){
				beginDistance = distance;
				beginCameraDistance = renderer.getCameraDistance();
			}
			switch (e.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//if(renderer.isEdgeRotate())...;
					beginAngle = angle;
					break;
				case MotionEvent.ACTION_MOVE:
					renderer.rotateCameraPos(dx, dy);
					renderer.rotateCameraTop(angle - beginAngle);
					renderer.setCameraDistance((distance)/((beginDistance+distance)/(2*beginCameraDistance)));
					requestRender();
					break;
				case MotionEvent.ACTION_UP:
					beginDistance = -1;
					//beginX = -1;
					//beginY = -1;
					angle = 0;
					distance = 1;
					//renderer.stopCamera();
				case MotionEvent.ACTION_CANCEL:
					beginDistance = -1;
			}
				beginAngle = angle;
		}
			//if (beginX == -1) {
				beginY = y;
				beginX = x;
			//}
		return true;
	}
}

