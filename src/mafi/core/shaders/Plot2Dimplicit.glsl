/**
 * Author: Vito Cucek
 */

const int NUM_ITERATIONS = 6;
uniform mat4 viewProjInverse;


uniform float pixelWidth;
uniform float pixelHeight;

varying vec4 screenCoords;
varying vec2 worldCoords;

#ifdef _VERTEX_

void main() {
	screenCoords = gl_Vertex;
	gl_Position = gl_Vertex;
}

#else

float calcDensity(vec2 wC, float pWidth, float pHeight){
	float hit = 0.0;
	float facX = pWidth / float(NUM_ITERATIONS);
	float facY = pHeight / float(NUM_ITERATIONS);
	float startX = wC.x - (pWidth/2.0);
	float startY = wC.y - (pHeight/2.0);

	/*
	for(int i=1; i<(NUM_ITERATIONS-1); i++){
		hit += function((startX + facX * float(i)), wC.y + pHeight/2.0);
		hit += function((startX + facX * float(i)), wC.y - pHeight/2.0);
	}

	for(int i=2; i<(NUM_ITERATIONS-2); i++){
		hit += function(wC.x + pWidth/2.0, (startY + facY * float(i)));
		hit += function(wC.x - pWidth/2.0, (startY + facY * float(i)));
	}
	*/

	for(int i=0; i<NUM_ITERATIONS; i++){
		float sY = startY + facY * float(i);
		for(int j=0; j<NUM_ITERATIONS; j++){
			float sX = startX + (facX/float(NUM_ITERATIONS)) * float(i);
			hit += function((sX + facX * float(j)), (sY + (facY/float(NUM_ITERATIONS)) * float(j)));
		}
	}

	return (hit/float(NUM_ITERATIONS * NUM_ITERATIONS));
}

void main() {
	vec4 wC = viewProjInverse * screenCoords;
	float density = calcDensity(wC.xy, pixelWidth, pixelHeight);
    gl_FragColor = vec4(0.5, 0.5,1.0, 0.6 * density);
}

#endif