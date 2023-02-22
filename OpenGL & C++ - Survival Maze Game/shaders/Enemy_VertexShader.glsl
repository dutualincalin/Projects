#version 330

// Vertex Shader for Enemy

// Input
layout(location = 0) in vec3 vertex_position;
layout(location = 3) in vec3 vertex_normal;
layout(location = 2) in vec2 vertex_coordinate;
layout(location = 1) in vec3 vertex_color;

// Uniform properties
uniform mat4 Model;
uniform mat4 View;
uniform mat4 Projection;
uniform vec3 Object_color;
uniform float noise;

// Output
out vec3 frag_position;
out vec3 frag_normal;
out vec2 frag_coordinate;
out vec3 frag_color;


void main()
{
    vec3 newPos;

    frag_position = vertex_position;
    frag_normal = vertex_normal;
    frag_coordinate = vertex_coordinate;
    frag_color = Object_color;

    if(noise > 0){
        newPos = vertex_position + vertex_normal * noise;
    }

    else newPos = vertex_position;

    gl_Position = Projection * View * Model * vec4(newPos, 1);
}
