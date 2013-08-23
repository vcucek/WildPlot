/**
 * Author: Vito Cucek
 */

#version 130

uniform mat4 modelViewProj;
uniform mat4 modelView;
uniform sampler2D heightMap;
uniform sampler2D colorMap;
uniform vec3 sunDirection;

varying vec4 tCoords;

#ifdef _VERTEX_

void main() {
    vec4 textureCoords = gl_Vertex;
	tCoords = gl_Vertex;
	vec4 normalDisp = texture(heightMap, textureCoords.xy);

	float disp = normalDisp.a;

	gl_Position = modelViewProj * vec4(gl_Vertex.xy-0.5, disp-0.5, gl_Vertex.w);
}

#else

void main() {
	vec4 normalDisp = texture(heightMap, tCoords.xy);
	vec3 normal = 2.0 * (normalDisp.xyz - 0.5);

	//vec4 sunDir = sunDirection;

	//vec4 baseColor = texture(colorMap, tCoords.xy);
	vec4 baseColor = normalDisp;
	float diffuse = max(dot(normalize(normal), normalize(sunDirection)),0.0);
	vec3 colorShaded = baseColor.rgb * (diffuse * vec3(1,1,1) + vec3(0.2,0.2,0.2));

	gl_FragColor = vec4(colorShaded.rgb, 1.0);
	//gl_FragColor = vec4(normalDisp.xyz, 1.0);
    //gl_FragColor = vec4(0.5,0.5,1.0, 1.0);
}

#endif