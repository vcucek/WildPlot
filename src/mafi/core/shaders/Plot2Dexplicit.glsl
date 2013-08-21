/**
 * Author: Vito Cucek
 */

const int NUM_ITERATIONS = 10;
uniform mat4 viewProjInverse;
uniform float pixelWidth;
uniform float pixelHeight;
varying vec2 coords;

#ifdef _VERTEX_

void main() {
    vec4 worldCoords = viewProjInverse * gl_Vertex;
	coords = worldCoords.xy;
    gl_Position = gl_Vertex;
}

#else

float calcDensity(){
	float hit = 0.0;
	float start = coords.x - pixelWidth/2.0;
	float fac = pixelWidth / float(NUM_ITERATIONS);
	for(int i=0; i<NUM_ITERATIONS; i++){
		float funcValue = function((start + fac * float(i)));
		if(coords.y>0.0){
			hit = hit + step(coords.y , funcValue);
		}
		else{
			hit = hit + step(funcValue, coords.y);
		}
	}

	return (hit/float(NUM_ITERATIONS));
}

void main() {
	//float value = coords.x * coords.x;
	//float value = sin(1.0/coords.x);

	float density = calcDensity();
    gl_FragColor = vec4(0.5, 0.5,1.0, 0.6 * density);
}

#endif