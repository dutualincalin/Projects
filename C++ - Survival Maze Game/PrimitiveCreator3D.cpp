#include "PrimitiveCreator3D.h"

#include <vector>

#include "core/engine.h"
#include "utils/gl_utils.h"

using namespace std;

// Draws a 3D D
Mesh* PrimitiveCreator3D::CreateD() {
    vector<VertexFormat> vertices
    {
        VertexFormat(glm::vec3(0, 0, 0)),
        VertexFormat(glm::vec3(1, 1, 0)),
        VertexFormat(glm::vec3(2, 1, 0)),
        VertexFormat(glm::vec3(3, 0, 0)),
        VertexFormat(glm::vec3(4, 0.7f, 0)),
        VertexFormat(glm::vec3(3.2f, 1, 0)),
        VertexFormat(glm::vec3(3.2f, 3, 0)),
        VertexFormat(glm::vec3(4, 3.3f, 0)),
        VertexFormat(glm::vec3(2.5f, 5, 0)),
        VertexFormat(glm::vec3(2, 4, 0)),
        VertexFormat(glm::vec3(1, 4, 0)),
        VertexFormat(glm::vec3(0, 5, 0)),
        VertexFormat(glm::vec3(0, 0, 0.1f)),
        VertexFormat(glm::vec3(1, 1, 0.1f)),
        VertexFormat(glm::vec3(2, 1, 0.1f)),
        VertexFormat(glm::vec3(3, 0, 0.1f)),
        VertexFormat(glm::vec3(4, 0.7f, 0.1f)),
        VertexFormat(glm::vec3(3.2f, 1, 0.1f)),
        VertexFormat(glm::vec3(3.2f, 3, 0.1f)),
        VertexFormat(glm::vec3(4, 3.3f, 0.1f)),
        VertexFormat(glm::vec3(2.5f, 5, 0.1f)),
        VertexFormat(glm::vec3(2, 4, 0.1f)),
        VertexFormat(glm::vec3(1, 4, 0.1f)),
        VertexFormat(glm::vec3(0, 5, 0.1f)),
    };

    vector<unsigned int> indices =
    {
        0, 1, 2,
        0, 2, 3,
        3, 2, 5,
        3, 4, 5,
        5, 6, 7,
        5, 7, 4,
        6, 9, 8,
        6, 8, 7,
        9, 10, 11,
        9, 11, 8,
        10, 11, 0,
        10, 0, 1,

        12, 13, 14,
        12, 14, 15,
        15, 14, 17,
        15, 16, 17,
        17, 18, 19,
        17, 19, 16,
        18, 21, 20,
        18, 20, 19,
        21, 22, 23,
        21, 23, 20,
        22, 23, 12,
        22, 12, 13,

        0, 12, 11,
        23, 12, 11,
        15, 0, 3,
        12, 0, 15,
        3, 15, 16,
        16, 4, 3,
        4, 7, 19,
        19, 16, 4,
        7, 8, 19,
        20, 8, 19,
        8, 11, 20,
        23, 11, 20,

        1, 13, 10,
        22, 13, 10,
        14, 1, 2,
        13, 1, 14,
        2, 14, 17,
        17, 5, 2,
        5, 6, 18,
        18, 17, 5,
        6, 9, 18,
        21, 9, 18,
        8, 10, 21,
        22, 10, 21,
    };

    Mesh* D = new Mesh("D-Mesh");
    D->InitFromData(vertices, indices);
    return D;
}

// Draws a 3D C
Mesh* PrimitiveCreator3D::CreateC() {
    vector<VertexFormat> vertices
    {
        VertexFormat(glm::vec3(1, 0, 0)),
        VertexFormat(glm::vec3(2, 0, 0)),
        VertexFormat(glm::vec3(2, 1, 0)),
        VertexFormat(glm::vec3(1, 1, 0)),
        VertexFormat(glm::vec3(0.7f, 1.2f, 0)),
        VertexFormat(glm::vec3(0.7f, 2.7f, 0)),
        VertexFormat(glm::vec3(1, 3, 0)),
        VertexFormat(glm::vec3(2, 3, 0)),
        VertexFormat(glm::vec3(2, 4, 0)),
        VertexFormat(glm::vec3(1, 4, 0)),
        VertexFormat(glm::vec3(0, 3, 0)),
        VertexFormat(glm::vec3(0, 1, 0)),
        VertexFormat(glm::vec3(1, 0, 0.1f)),
        VertexFormat(glm::vec3(2, 0, 0.1f)),
        VertexFormat(glm::vec3(2, 1, 0.1f)),
        VertexFormat(glm::vec3(1, 1, 0.1f)),
        VertexFormat(glm::vec3(0.7f, 1.2f, 0.1f)),
        VertexFormat(glm::vec3(0.7f, 2.7f, 0.1f)),
        VertexFormat(glm::vec3(1, 3, 0.1f)),
        VertexFormat(glm::vec3(2, 3, 0.1f)),
        VertexFormat(glm::vec3(2, 4, 0.1f)),
        VertexFormat(glm::vec3(1, 4, 0.1f)),
        VertexFormat(glm::vec3(0, 3, 0.1f)),
        VertexFormat(glm::vec3(0, 1, 0.1f)),
    };

    vector<unsigned int> indices =
    {
        0, 1, 2,
        2, 3, 0,
        3, 4, 0,
        4, 11, 0,
        4, 5, 11,
        5, 10, 11,
        5, 9, 10,
        5, 6, 9,
        6, 8, 9,
        6, 7, 8,

        12, 13, 14,
        14, 15, 12,
        15, 16, 12,
        16, 23, 12,
        16, 17, 23,
        17, 22, 23,
        17, 21, 22,
        17, 18, 21,
        18, 20, 21,
        18, 19, 20,

        0, 12, 13,
        13, 1, 0,
        1, 2, 14,
        14, 13, 1,
        2, 3, 15,
        15, 14, 2,
        3, 4, 16,
        16, 15, 3,
        4, 5, 17,
        17, 16, 4,
        5, 6, 18,
        18, 17, 5,
        6, 7, 19,
        19, 18, 6,
        7, 8, 20,
        20, 19, 7,
        8, 9, 21,
        21, 20, 8,
        9, 10, 22,
        22, 21, 9,
        10 ,11, 23,
        23, 22, 10,
        11, 0, 12,
        12, 23, 11,
    };

    Mesh* C = new Mesh("C-Mesh");
    C->InitFromData(vertices, indices);
    return C;
}

// Draws a rhomb
Mesh* PrimitiveCreator3D::CreateRomb() {
    vector<VertexFormat> vertices
    {
        VertexFormat(glm::vec3(1, 0, 0), glm::vec3(-1, -1, 0)),
        VertexFormat(glm::vec3(2, 1, 0), glm::vec3(1, 0, 0)),
        VertexFormat(glm::vec3(1, 2, 0), glm::vec3(0, 1, 0)),
        VertexFormat(glm::vec3(0, 1, 0), glm::vec3(-1, 0, 0)),
        VertexFormat(glm::vec3(1, 1, 1), glm::vec3(0, 0, 1)),
        VertexFormat(glm::vec3(1, 1, -1), glm::vec3(0, 0, -1)),
    };

    vector<unsigned int> indices =
    {
        0, 1, 4,
        1, 2, 4,
        2, 3, 4,
        3, 0, 4,
        0, 1, 5,
        1, 2, 5,
        2, 3, 5,
        3, 0, 5,
    };

    Mesh* romb = new Mesh("Romb");
    romb->InitFromData(vertices, indices);
    return romb;
}

// Draws a 2D rectangle
Mesh* PrimitiveCreator3D::Create2DRectangle(bool fill) {
    vector<VertexFormat> vertices
    {
        VertexFormat(glm::vec3(0, 0, 0)),
        VertexFormat(glm::vec3(2, 0, 0)),
        VertexFormat(glm::vec3(2, 1, 0)),
        VertexFormat(glm::vec3(0, 1, 0)),
    };

    Mesh* rectangle;
    std::vector<unsigned int> indices = { 0, 1, 2, 3 };

    if (!fill) {
        rectangle = new Mesh("Lined2DRectangular");
        rectangle->SetDrawMode(GL_LINE_LOOP);
    }
    else {
        rectangle = new Mesh("Filled2DRectangular");
        indices.push_back(0);
        indices.push_back(2);
    }

    rectangle->InitFromData(vertices, indices);
    return rectangle;
}