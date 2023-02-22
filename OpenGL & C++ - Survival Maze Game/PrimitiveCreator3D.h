#pragma once

#include <string>

#include "core/gpu/mesh.h"
#include "utils/glm_utils.h"
#include "utils/math_utils.h"


namespace PrimitiveCreator3D{
	// Draws a 2D rectangle
	Mesh* Create2DRectangle(bool fill);

	// Draws a 3D D
	Mesh* CreateD();

	// Draws a 3D C
	Mesh* CreateC();

	// Draws a rhomb
	Mesh* CreateRomb();
}

