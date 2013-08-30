/*
 * (C) Copyright 2013 Vito Čuček.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * @author Vito Čuček <vito.cucek@xlab.si>
 */

const int NUM_ITERATIONS = 6;
uniform mat4 viewProjInverse;


uniform float pixelWidth;
uniform float pixelHeight;
uniform float res;

uniform float minZ;
uniform float maxZ;

varying vec4 screenCoords;
varying vec2 worldCoords;

#ifdef _VERTEX_

void main() {
	screenCoords = gl_Vertex;
	gl_Position = gl_Vertex;
}

#else

float tToN(float param){
	return ((param - minZ)/(maxZ-minZ));
}

float calcMax(vec2 wC, float pWidth, float pHeight){
	float maxV = -10000000.0;
	float minV = 10000000.0;

	float facX = pWidth / float(NUM_ITERATIONS);
	float facY = pHeight / float(NUM_ITERATIONS);
	float startX = wC.x - (pWidth/2.0);
	float startY = wC.y - (pHeight/2.0);

	for(int i=0; i<NUM_ITERATIONS; i++){
		float sY = startY + facY * float(i);
		for(int j=0; j<NUM_ITERATIONS; j++){
			float sX = startX + (facX/float(NUM_ITERATIONS)) * float(i);
			maxV = max(function((sX + facX * float(j)), (sY + (facY/float(NUM_ITERATIONS)) * float(j))), maxV);
			minV = min(function((sX + facX * float(j)), (sY + (facY/float(NUM_ITERATIONS)) * float(j))), minV);
		}
	}

	//float outVal = 0.0;

	//TODO: figure out this problem!!!!!!!!!
	//if()

	return tToN(maxV);
}

vec3 calcNormal(vec2 wC, float maxV, float pWidth, float pHeight){
	vec3 up = vec3(0.0, 1.0/res, tToN(function(wC.x, wC.y + pHeight))-maxV);
	vec3 down = vec3(0.0, -1.0/res, tToN(function(wC.x, wC.y - pHeight))-maxV);
	vec3 left = vec3(-1.0/res, 0.0, tToN(function(wC.x - pWidth, wC.y))-maxV);
	vec3 right = vec3(1.0/res, 0.0, tToN(function(wC.x + pWidth, wC.y))-maxV);

	vec3 n1 = normalize(cross(right, up));
	//vec3 n2 = normalize(cross(up, left));
	//vec3 n3 = normalize(cross(left, down));
	//vec3 n4 = normalize(cross(down, right));

	//return (normalize(n1+n2+n3+n4));
	return (normalize(n1));
}

void main() {
	vec4 wC = viewProjInverse * screenCoords;
	float maxV = calcMax(wC.xy, pixelWidth, pixelHeight);
	//vec3 normalV = calcNormal(wC.xy, maxV, 2.0 * pixelWidth, 2.0 * pixelHeight);
	vec3 normalV = calcNormal(wC.xy, maxV, 2.0 * pixelWidth, 2.0 * pixelHeight);
    gl_FragColor = vec4((normalV/2.0)+0.5, maxV);
}

#endif