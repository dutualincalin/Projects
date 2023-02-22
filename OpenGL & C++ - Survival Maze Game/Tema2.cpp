#include "lab_m1/Tema2/Tema2.h"

#include <vector>
#include <string>
#include <iostream>
#include <fstream>
#include <cmath>

using namespace std;
using namespace m1;


/*
 *  To find out more about `FrameStart`, `Update`, `FrameEnd`
 *  and the order in which they are called, see `world.cpp`.
 */


Tema2::Tema2()
{
}


Tema2::~Tema2()
{
}

// Finds maximum value
float max(float a, float b) {
    if (a > b) {
        return a;
    }

    return b;
}

// Finds minimum value
float min(float a, float b) {
    if (a < b) {
        return a;
    }

    return b;
}

// Reads maze from file
void Tema2::SetMaze() {
    int i, j;
    string path;
    
    // Setting a path for a maze selected randomly
    path = "src/lab_m1/Tema2/mazes/";
    path += std::to_string(rand() % 11);
    path.append(".txt");

    ifstream f(path);

    // reading the maze elements
    for (i = 0; i < 21; i++) {
        for (j = 0; j < 21; j++) {
            f >> std::noskipws >> maze[i][j];

            // Setting finish point
            if (maze[i][j] == 'F') {
                finish.first = i;
                finish.second = j;
            }
            
            // Setting the enemies
            if (maze[i][j] == 'E') {
                Enemies.push_back(Enemy(j * 2.5f, 0, i * 2.5f, 0));
            }

            if (maze[i][j] == '\n') {
                j--;
            }
        }
    }

    f >> std::skipws >> rotateExit;

    f.close();
}


void Tema2::Init()
{
    int i, j;
    camera = new Camera::Camera();
    
    // Setting the maze
    SetMaze();

    // Player initialization
    do {
        i = rand() % 21;
        j = rand() % 21;
    } while ((abs(i - finish.first) < 5 || abs(j - finish.second) < 5) || (maze[i][j] != ' ') ||
            (maze[i][j - 1] == '+') || (maze[i][j + 1] == '+') || (maze[i - 1][j] == '+') || (maze[i + 1][j] == '+'));

    player = Player(j * 2.5f, 1.165f, i * 2.5f);
    
    // Time setting
    reloadTime = 0;

    // Translation variables
    translateX = 0;
    translateZ = 0;
    oldTranslateX = 0;
    oldTranslateZ = 0;
    
    // Speed variables
    speed = 5;
    shellSpeed = 20;
    enemySpeed = 10;

    // Rotation variables
    rotationZ = 0;
    rotationY = 0;

    // Boolean variables
    fire_mode = false;
    isReloading = false;
    win = false;
    lose_time = false;
    enable_HUD = false;

    // HUD variables
    healthPart = 5;
    deathTime = 180;
    hpTime = 0;

    // Camera definition
    camera->Set(glm::vec3(player.posX - 2 * cos(rotationY), player.posY + 1, player.posZ + 2 * sin(rotationY)),
        glm::vec3(player.posX, player.posY, player.posZ),
        glm::vec3(0, player.posY + 10, 0));
       
    // Meshes
    {
        Mesh* mesh = new Mesh("D-Mesh");
        mesh = PrimitiveCreator3D::CreateD();
        AddMeshToList(mesh);
    }

    {
        Mesh* mesh = new Mesh("C-Mesh");
        mesh = PrimitiveCreator3D::CreateC();
        AddMeshToList(mesh);
    }

    {
        Mesh* mesh = new Mesh("Romb");
        mesh = PrimitiveCreator3D::CreateRomb();
        AddMeshToList(mesh);
    }

    {
        Mesh* mesh = new Mesh("Lined2DRectangular");
        mesh = PrimitiveCreator3D::Create2DRectangle (false);
        AddMeshToList(mesh);
    }

    {
        Mesh* mesh = new Mesh("Filled2DRectangular");
        mesh = PrimitiveCreator3D::Create2DRectangle(true);
        AddMeshToList(mesh);
    }

    {
        Mesh* mesh = new Mesh("box");
        mesh->LoadMesh(PATH_JOIN(window->props.selfDir, RESOURCE_PATH::MODELS, "primitives"), "box.obj");
        meshes[mesh->GetMeshID()] = mesh;
    }

    {
        Mesh* mesh = new Mesh("sphere");
        mesh->LoadMesh(PATH_JOIN(window->props.selfDir, RESOURCE_PATH::MODELS, "primitives"), "sphere.obj");
        meshes[mesh->GetMeshID()] = mesh;
    }

    {
        Mesh* mesh = new Mesh("plane");
        mesh->LoadMesh(PATH_JOIN(window->props.selfDir, RESOURCE_PATH::MODELS, "primitives"), "plane50.obj");
        meshes[mesh->GetMeshID()] = mesh;
    }

    {
        Mesh* mesh = new Mesh("bunny");
        mesh->LoadMesh(PATH_JOIN(window->props.selfDir, RESOURCE_PATH::MODELS, "animals"), "bunny.obj");
        meshes[mesh->GetMeshID()] = mesh;
    }

    // Text
    glm::ivec2 resolution = window->GetResolution();
    textRenderer = new gfxc::TextRenderer(window->props.selfDir, resolution.x, resolution.y);
    textRenderer->Load(PATH_JOIN(window->props.selfDir, RESOURCE_PATH::FONTS, "Hack-Bold.ttf"), 18);

    // Player Shader
    {
        Shader* shader = new Shader("PlayerShader");
        shader->AddShader(PATH_JOIN(window->props.selfDir, SOURCE_PATH::M1, "Tema2", "shaders", "Player_VertexShader.glsl"), GL_VERTEX_SHADER);
        shader->AddShader(PATH_JOIN(window->props.selfDir, SOURCE_PATH::M1, "Tema2", "shaders", "Player_FragmentShader.glsl"), GL_FRAGMENT_SHADER);
        shader->CreateAndLink();
        shaders[shader->GetName()] = shader;
    }

    // Enemy shader
    {
        Shader* shader = new Shader("EnemyShader");
        shader->AddShader(PATH_JOIN(window->props.selfDir, SOURCE_PATH::M1, "Tema2", "shaders", "Enemy_VertexShader.glsl"), GL_VERTEX_SHADER);
        shader->AddShader(PATH_JOIN(window->props.selfDir, SOURCE_PATH::M1, "Tema2", "shaders", "Enemy_FragmentShader.glsl"), GL_FRAGMENT_SHADER);
        shader->CreateAndLink();
        shaders[shader->GetName()] = shader;
    }

    projectionMatrix = glm::perspective(RADIANS(FOV), window->props.aspectRatio, 0.01f, 200.0f);

}


void Tema2::FrameStart()
{
    // Clears the color buffer (using the previously set color) and depth buffer
    glClearColor(0, 0, 0, 1);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    glm::ivec2 resolution = window->GetResolution();
    // Sets the screen area where to draw
    glViewport(0, 0, resolution.x, resolution.y);
}


//*******************************************************************
// Collisions
//*******************************************************************

// Player - Maze
bool Tema2::PlayerMazeCollision(Player *player, int i, int j) {
    float varX, varZ, coordX, coordZ;

    if (maze[i][j] == '+') {
        varX = 0.5f;
        varZ = 0.5f;
    }

    else if (maze[i][j] == '-') {
        varX = 2;
        varZ = 0.5f;
    }

    else if (maze[i][j] == '|'){
        varX = 0.5f;
        varZ = 2;
    }

    else {
        varX = 1;
        varZ = 1;
    }

    coordX = j * 2.5f;
    coordZ = i * 2.5f;

    return (player->posX - 0.15f <= coordX + varX && player->posX + 0.15f >= coordX - varX) &&
            (player->posZ - 0.45f <= coordZ + varZ && player->posZ + 0.45f >= coordZ - varZ);
}

// Shell - Maze
bool Tema2::ShellMazeCollision(Shell* shell, int i, int j) {
    float varX, varY, varZ, x, y, z, coordY, coordX, coordZ, distance;

    if (maze[i][j] == '+') {
        varX = 0.5f;
        varY = 1.5f;
        varZ = 0.5f;
        coordX = j * 2.5f;
        coordZ = i * 2.5f;
        coordY = 1.5f;
    }

    else if (maze[i][j] == '-') {
        varX = 2;
        varY = 1;
        varZ = 0.5f;
        coordX = j * 2.5f;
        coordZ = i * 2.5f;
        coordY = 1;
    }

    else if (maze[i][j] == '|'){
        varX = 0.5f;
        varY = 1;
        varZ = 2;
        coordX = j * 2.5f;
        coordZ = i * 2.5f;
        coordY = 1;
    }

    else {
        varX = 25;
        varY = 0.005f;
        varZ = 25;
        coordX = 25;
        coordZ = 25;
        coordY = 0.005f;
    }

    x = max(coordX - varX, min(shell->posX, coordX + varX));
    y = max(coordY - varY, min(shell->posY, coordY + varY));
    z = max(coordZ - varZ, min(shell->posZ, coordZ + varZ));

    distance = sqrtf(powf((x - shell->posX), 2) + powf((y - shell->posY), 2) + powf((z - shell->posZ), 2));

    return distance <  0.3f;
}

// Player - Enemy
bool Tema2::PlayerEnemyCollision(Player* player, Enemy* enemy) {
    return (player->posX - 0.15f <= enemy->posX + 2 && player->posX + 0.15f >= enemy->posX) &&
        (player->posZ - 0.45f <= enemy->posZ + 0.5f && player->posZ + 0.45f >= enemy->posZ - 0.5f);
}

// Shell - Enemy
bool Tema2::ShellEnemyCollision(Shell* shell, Enemy* enemy) {
    float x, y, z, distance;

    x = max(enemy->posX + 0.5f, min(shell->posX, enemy->posX + 1.5f));
    y = max(enemy->posY + 0.5f, min(shell->posY, enemy->posY + 1.5f));
    z = max(enemy->posZ - 0.25f, min(shell->posZ, enemy->posZ + 0.5f));

    distance = sqrtf(powf((x - shell->posX), 2) + powf((y - shell->posY), 2) + powf((z - shell->posZ), 2));

    return distance < 0.3f;
}

//*******************************************************************
// Player
//*******************************************************************

// Computes matrix used for drawing player's head
glm::mat4 Tema2::ComputePlayerHeadMatrix(Player *player) {
    glm::mat4 headMatrix;
    headMatrix = glm::mat4(1);
    headMatrix = glm::translate(headMatrix, glm::vec3(player->posX, player->posY, player->posZ));
    headMatrix = glm::rotate(headMatrix, rotationY, glm::vec3(0, 1, 0));
    headMatrix = glm::rotate(headMatrix, rotationZ, glm::vec3(0, 0, 1));

    return headMatrix;
}

// Compute matrix used for player's body parts
glm::mat4 Tema2::ComputePlayerMatrix(Player* player) {
    glm::mat4 modelMatrix = glm::mat4(1);
    modelMatrix = glm::translate(modelMatrix, glm::vec3(player->posX, player->posY, player->posZ));
    modelMatrix = glm::rotate(modelMatrix, rotationY, glm::vec3(0, 1, 0));

    return modelMatrix;
}

// Draws the player
void Tema2::DrawPlayer(Player* player) {
    int i, j, intersection;
    glm::mat4 modelMatrix, headMatrix, playerMatrix;
    float increaseX, increaseZ;

    increaseX = ((translateX - oldTranslateX) * cos(rotationY) + (translateZ - oldTranslateZ) * sin(rotationY));
    increaseZ = (-(translateX - oldTranslateX) * sin(rotationY) + (translateZ - oldTranslateZ) * cos(rotationY));

    player->posX += increaseX;
    intersection = 0;

    // checks for collison with a maze element on Ox axis
    for (i = 0; i < 21; i++) {
        for (j = 0; j < 21; j++) {
            if (maze[i][j] == '+' || maze[i][j] == '-' || maze[i][j] == '|') {
                if (PlayerMazeCollision(player, i, j)) {
                    intersection = 1;
                    break;
                }
            }
        }

        if (intersection == 1) {
            break;
        }
    }

    if (intersection == 1) {
        player->posX -= increaseX;

        if (!fire_mode) {
            camera->Set(glm::vec3(player->posX - 2 * cos(rotationY), player->posY + 1, player->posZ + 2 * sin(rotationY)),
                glm::vec3(player->posX, player->posY, player->posZ),
                glm::vec3(0, player->posY + 10, 0));
        }

        else {
            camera->Set(glm::vec3(player->posX, player->posY, player->posZ),
                glm::vec3(player->posX + 20 * cos(rotationY), player->posY, player->posZ - 20 * sin(rotationY)),
                glm::vec3(0, player->posY + 5, 0));
        }

        rotationZ = 0;
    }


    player->posZ += increaseZ;
    intersection = 0;
    
    // checks for collison with a maze element on Oz axis
    for (i = 0; i < 21; i++) {
        for (j = 0; j < 21; j++) {
            if (maze[i][j] == '+' || maze[i][j] == '-' || maze[i][j] == '|') {
                if (PlayerMazeCollision(player, i, j)) {
                    intersection = 1;
                    break;
                }
            }
        }

        if (intersection == 1) {
            break;
        }
    }

    if (intersection == 1) {
        player->posZ -= increaseZ;

        if (!fire_mode) {
            camera->Set(glm::vec3(player->posX - 2 * cos(rotationY), player->posY + 1, player->posZ + 2 * sin(rotationY)),
                glm::vec3(player->posX, player->posY, player->posZ),
                glm::vec3(0, player->posY + 10, 0));
        }

        else {
            camera->Set(glm::vec3(player->posX, player->posY, player->posZ),
                glm::vec3(player->posX + 20 * cos(rotationY), player->posY, player->posZ - 20 * sin(rotationY)),
                glm::vec3(0, player->posY + 5, 0));
        }

        rotationZ = 0;
    }

    // Computes player's matrixes
    playerMatrix = ComputePlayerMatrix(player);
    headMatrix = ComputePlayerHeadMatrix(player);

    // Feet
    {
        // Piciorul stang
        modelMatrix = playerMatrix;
        modelMatrix = glm::translate(modelMatrix, glm::vec3(0, -0.98f, -0.12f));
        modelMatrix = glm::scale(modelMatrix, glm::vec3(0.2f, 0.366f, 0.2f));
        RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(0, 0, 0.545f), false);

        //Picorul drept
        modelMatrix = glm::translate(modelMatrix, glm::vec3(0, 0, 1.2f));
        RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(0, 0, 0.545f), false);
    }


    // body
    {
        modelMatrix = playerMatrix;
        modelMatrix = glm::translate(modelMatrix, glm::vec3(0, -0.465f, 0));
        modelMatrix = glm::scale(modelMatrix, glm::vec3(0.3f, 0.65f, 0.5f));
        RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(0.3f, 0.1f, 0.5f), false);
    }


    // T-shirt making
    {
        // D
        modelMatrix = playerMatrix;
        modelMatrix = glm::translate(modelMatrix, glm::vec3(+0.155f, -0.565f, -0.17f));
        modelMatrix = glm::rotate(modelMatrix, RADIANS(-90), glm::vec3(0, 1, 0));

        modelMatrix = glm::scale(modelMatrix, glm::vec3(0.075f, 0.075f, 3.1f));
        RenderMesh_color(meshes["D-Mesh"], shaders["PlayerShader"], modelMatrix, glm::vec3(1, 0.55f, 0), false);

        // C
        modelMatrix = playerMatrix;
        modelMatrix = glm::translate(modelMatrix, glm::vec3(+0.172f, -0.685f, -0.07f));
        modelMatrix = glm::rotate(modelMatrix, RADIANS(-90), glm::vec3(0, 1, 0));

        modelMatrix = glm::scale(modelMatrix, glm::vec3(0.125f, 0.1f, 3.35f));
        RenderMesh_color(meshes["C-Mesh"], shaders["PlayerShader"], modelMatrix, glm::vec3(1, 0.55f, 0), false);
    }


    // Arms
    {
        // Bratul stang
        modelMatrix = playerMatrix;
        modelMatrix = glm::translate(modelMatrix, glm::vec3(0, -0.315f, -0.356f));
        modelMatrix = glm::scale(modelMatrix, glm::vec3(0.2f, 0.35f, 0.2f));
        RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(0.3f, 0.1f, 0.5f), false);

        // Bratul drept
        modelMatrix = glm::translate(modelMatrix, glm::vec3(0, 0, 3.56f));
        RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(0.3f, 0.1f, 0.5f), false);
    }


    // Hands
    {
        // Mana stanga
        modelMatrix = playerMatrix;
        modelMatrix = glm::translate(modelMatrix, glm::vec3(0, -0.605f, -0.356f));
        modelMatrix = glm::scale(modelMatrix, glm::vec3(0.2f, 0.21f, 0.2f));
        RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(1, 0.64f, 0.4f), false);

        // Mana dreapta
        modelMatrix = glm::translate(modelMatrix, glm::vec3(0, 0, 3.56f));
        RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(1, 0.64f, 0.4f), false);
    }


    // Head
    if (!fire_mode) {
        // Forma capului
        modelMatrix = headMatrix;
        modelMatrix = glm::scale(modelMatrix, glm::vec3(0.26f, 0.26f, 0.26f));
        RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(1, 0.64f, 0.4f), false);


        // Ochiul drept
        modelMatrix = headMatrix;
        modelMatrix = glm::translate(modelMatrix, glm::vec3(0.117f, 0.045f, -0.0725f));
        modelMatrix = glm::scale(modelMatrix, glm::vec3(0.03f, 0.03f, 0.03f));
        RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(1, 1, 1), false);

        modelMatrix = glm::translate(modelMatrix, glm::vec3(0, 0, 1));
        RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(0.053f, 0.29f, 0.657f), false);

        // Ochiul stang
        modelMatrix = glm::translate(modelMatrix, glm::vec3(0, 0, 3));
        RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(0.053, 0.29f, 0.657f), false);

        modelMatrix = glm::translate(modelMatrix, glm::vec3(0, 0, 1));
        RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(1, 1, 1), false);


        // Mustata
        modelMatrix = headMatrix;
        modelMatrix = glm::translate(modelMatrix, glm::vec3(0.117f, -0.005f, 0));
        modelMatrix = glm::scale(modelMatrix, glm::vec3(0.03f, 0.03f, 0.08f));
        RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(0.7f, 0.4196f, 0.2627f), false);


        // Gura
        modelMatrix = glm::translate(modelMatrix, glm::vec3(0, -2, 0));
        modelMatrix = glm::scale(modelMatrix, glm::vec3(1, 1.5f, 2));
        RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(0.93f, 0.11f, 0.14f), false);
    }

    // Sphere of doom (fire mode)
    if (fire_mode) {
        modelMatrix = glm::mat4(1);
        modelMatrix = glm::translate(modelMatrix, camera->GetTargetPosition());
        modelMatrix = glm::scale(modelMatrix, glm::vec3(0.1f));
        RenderMesh(meshes["sphere"], shaders["VertexNormal"], modelMatrix);
    }

    // Checks collision with exit
    if (PlayerMazeCollision(player, finish.first, finish.second)) {
        win = true;
    }
}

//*******************************************************************
// Shell
//*******************************************************************

// Draws shells
void Tema2::DrawShell(Shell* shell, float deltaTimeSeconds) {
    shell->posX += shellSpeed * cos(shell->angleY) * deltaTimeSeconds;
    shell->posY += shellSpeed * sin(shell->angleZ) * deltaTimeSeconds;
    shell->posZ -= shellSpeed * sin(shell->angleY) * deltaTimeSeconds;

    glm::mat4 modelMatrix = glm::mat4(1);
    modelMatrix *= glm::translate(modelMatrix, glm::vec3(shell->posX, shell->posY, shell->posZ));
    modelMatrix = glm::scale(modelMatrix, glm::vec3(0.3f));

    RenderMesh_color(meshes["sphere"], shaders["PlayerShader"], modelMatrix, glm::vec3(0, 0.3725f, 0.38f), false);
}

// Manages shell activity
void Tema2::ManageShells(float deltaTimeSeconds) {
    // Reloading gun
    if (isReloading) {
        if (glfwGetTime() - reloadTime > 0.5f) {
            isReloading = false;
        }
    }

    // Checks and draws shells
    auto ammo = Shells.begin();
    bool remove = false;
    double shellTime;

    while (ammo != Shells.end()) {
        DrawShell(&(*ammo), deltaTimeSeconds);
        shellTime = ammo->time;
        
        if (glfwGetTime() - shellTime > 3) {
            remove = true;
        }
        
        if (!remove) {
            for (int i = 0; i < 21; i++) {
                for (int j = 0; j < 21; j++) {
                    if (ShellMazeCollision(&(*ammo), i, j)) {
                        remove = true;
                        break;
                    }
                }

                if (remove) {
                    break;
                }

            }
        }

        if (remove) {
            ammo = Shells.erase(ammo);
            remove = false;
        }

        else ++ammo;
    }
}

//*******************************************************************
// Maze
//*******************************************************************

// Draws a maze pillar
void Tema2::DrawPillar(float posX, float posZ) {
    glm::mat4 modelMatrix = glm::mat4(1);
    modelMatrix = glm::translate(modelMatrix, glm::vec3(posX, 1.5f, posZ));
    modelMatrix = glm::scale(modelMatrix, glm::vec3(1, 3, 1));
    RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(0.82f, 0.82f, 0.82f), false);
}

// Draws a maze horinzontal wall
void Tema2::DrawHorizontalWall(float posX, float posZ) {
    glm::mat4 modelMatrix = glm::mat4(1);
    modelMatrix = glm::translate(modelMatrix, glm::vec3(posX, 1, posZ));
    modelMatrix = glm::scale(modelMatrix, glm::vec3(4, 2, 0.5f));
    RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(1, 0.549f, 0), false);
}

// Draws a maze vertical wall
void Tema2::DrawVerticalWall(float posX, float posZ) {
    glm::mat4 modelMatrix = glm::mat4(1);
    modelMatrix = glm::translate(modelMatrix, glm::vec3(posX, 1, posZ));
    modelMatrix = glm::scale(modelMatrix, glm::vec3(0.5f, 2, 4));
    RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(1, 0.549f, 0), false);
}

// Draws maze floor
void Tema2::DrawFloor() {
    glm::mat4 modelMatrix = glm::mat4(1);
    modelMatrix = glm::translate(modelMatrix, glm::vec3(25, 0.01f, 25));
    RenderMesh_color(meshes["plane"], shaders["PlayerShader"], modelMatrix, glm::vec3(0.4941f, 0.2157f, 0.047f), false);
}

// Draws maze exit
void Tema2::DrawExit(float posX, float posZ) {
    // Drawing the pillar
    glm::mat4 modelMatrix = glm::mat4(1);
    modelMatrix = glm::translate(modelMatrix, glm::vec3(posX, 0.5f, posZ));
    modelMatrix = glm::scale(modelMatrix, glm::vec3(0.5f, 1, 0.5f));
    RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(0.82f, 0.82f, 0.82f), false);

    // Drawing the stand
    modelMatrix = glm::mat4(1);
    modelMatrix = glm::translate(modelMatrix, glm::vec3(posX, 1, posZ));
    modelMatrix = glm::scale(modelMatrix, glm::vec3(0.7f, 0.2f, 0.7f));
    RenderMesh_color(meshes["box"], shaders["PlayerShader"], modelMatrix, glm::vec3(0.82f, 0.82f, 0.82f), false);

    // Drawing the key treasure
    modelMatrix = glm::mat4(1);
    modelMatrix = glm::translate(modelMatrix, glm::vec3(posX, 1.3f, posZ));
    modelMatrix = glm::rotate(modelMatrix, RADIANS(rotateExit), glm::vec3(0, 1, 0));
    modelMatrix = glm::scale(modelMatrix, glm::vec3(0.02f));
    RenderMesh(meshes["bunny"], shaders["VertexNormal"], modelMatrix);
}

// Draws the maze in positive XoZ
void Tema2::DrawMaze() {
    int i, j;

    for (i = 0; i < 21; i++) {
        for (j = 0; j < 21; j++) {
            if (maze[i][j] == '+') {
                DrawPillar(j * 2.5f, i * 2.5f);
            }

            if (maze[i][j] == '-') {
                DrawHorizontalWall(j * 2.5f, i * 2.5f);
            }

            if (maze[i][j] == '|') {
                DrawVerticalWall(j * 2.5f, i * 2.5f);
            }

            if (maze[i][j] == 'F') {
                DrawExit(j * 2.5f, i * 2.5f);
            }
        }
    }

    DrawFloor();
}

//*******************************************************************
// Enemy
//*******************************************************************

// Draws an enemy
void Tema2::DrawEnemy(Enemy* enemy, float deltaTimeSeconds) {
    // Sets new positions if not in death animation
    if (enemy->deathTime == -1) {
        enemy->posX = enemy->coordX - 1 - 1.5f * cos(enemy->angle);
        enemy->posZ = enemy->coordZ - 1.5f * sin(enemy->angle);
        enemy->posY = enemy->coordY;
    }

    glm::mat4 modelMatrix = glm::mat4(1);
    modelMatrix = glm::translate(modelMatrix, glm::vec3(enemy->posX, enemy->posY, enemy->posZ));

    // Gives enemy movement if not dead
    if (enemy->deathTime == -1) {
        modelMatrix = glm::translate(modelMatrix, glm::vec3(1, 0, 0));
        modelMatrix = glm::rotate(modelMatrix, enemy->angle, glm::vec3(0, 1, 0));
        modelMatrix = glm::translate(modelMatrix, glm::vec3(-1, 0, 0));

        enemy->angle += enemySpeed * deltaTimeSeconds;
    }

    RenderMesh_enemy(meshes["Romb"], shaders["EnemyShader"], modelMatrix, glm::vec3(0.6078f, 0.06666f, 0.1176f), enemy->noise);
}

// Manages all the enemies
void Tema2::ManageEnemies(float deltaTimeSeconds) {
    auto nemesis = Enemies.begin();

    while (nemesis != Enemies.end()) {
        DrawEnemy(&(*nemesis), deltaTimeSeconds);

        if (nemesis->deathTime == -1) {
            // checks collision with player
            if (PlayerEnemyCollision(&player, (&(*nemesis))) && glfwGetTime() > hpTime + 0.7f) {
                healthPart--;
                hpTime = glfwGetTime();
            }

            // checks collision with a shell
            auto ammo = Shells.begin();

            while (ammo != Shells.end()) {
                if (ShellEnemyCollision(&(*ammo), &(*nemesis))) {
                    // sets deathTime - time between death and erase
                    nemesis->deathTime = glfwGetTime();
                    nemesis->noise = 0;
                    ammo = Shells.erase(ammo);
                    break;
                }

                ++ammo;
            }
        }
        
        if (nemesis->deathTime != -1) {
            // Gives enemy some noise for its death animation
            nemesis->noise += deltaTimeSeconds / 5;

            // erases the enemy after death animation ended
            if (glfwGetTime() - nemesis->deathTime > 2) {
                nemesis = Enemies.erase(nemesis);
                continue;
            }

            else {
                nemesis++;
                continue;
            }
        }

        else
            ++nemesis;
    }
}

//*******************************************************************
// HUD
//*******************************************************************

// Draws HP Bar
void Tema2::DrawHPBar() {
    glm::mat4 modelMatrix = glm::mat4(1);
    glm::mat4 outlineModelMatrix;

    // sets rotations
    modelMatrix = glm::translate(modelMatrix, camera->GetPosition());
    modelMatrix = glm::rotate(modelMatrix, rotationY + RADIANS(90), glm::vec3(0, 1, 0));
    modelMatrix = glm::rotate(modelMatrix, -rotationZ, glm::vec3(1, 0, 0));
    
    if (!fire_mode) {
        // sets position and for hp bar
        modelMatrix = glm::translate(modelMatrix, glm::vec3(0.98f, -0.5075f, 3));
        outlineModelMatrix = modelMatrix;
        modelMatrix = glm::scale(modelMatrix, glm::vec3(-0.06f * healthPart, 0.1f, 0.25f));
        RenderMesh_color(meshes["Filled2DRectangular"], shaders["PlayerShader"], modelMatrix, glm::vec3(1, 0, 0), true);

        // sets position and scale for wireframe
        outlineModelMatrix = glm::translate(outlineModelMatrix, glm::vec3(0.005f, -0.009f, 0));
        outlineModelMatrix = glm::scale(outlineModelMatrix, glm::vec3(-0.307f, 0.12f, 0.25f));
        RenderMesh_color(meshes["Lined2DRectangular"], shaders["PlayerShader"], outlineModelMatrix, glm::vec3(1, 0, 0), true);
    }

    else {
        // sets position and for hp bar
        modelMatrix = glm::translate(modelMatrix, glm::vec3(0.98f, 0.89f, 3));
        outlineModelMatrix = modelMatrix;
        modelMatrix = glm::scale(modelMatrix, glm::vec3(-0.06f * healthPart, 0.089f, 0.25f));
        RenderMesh_color(meshes["Filled2DRectangular"], shaders["PlayerShader"], modelMatrix, glm::vec3(1, 0, 0), true);

        // sets position and scale for wireframe
        outlineModelMatrix = glm::translate(outlineModelMatrix, glm::vec3(0.005f, -0.01f, 0));
        outlineModelMatrix = glm::scale(outlineModelMatrix, glm::vec3(-0.307f, 0.1075f, 0.25f));
        RenderMesh_color(meshes["Lined2DRectangular"], shaders["PlayerShader"], outlineModelMatrix, glm::vec3(1, 0, 0), true);
    }
}

// Draws Time bar
void Tema2::DrawTimeBar() {
    glm::mat4 modelMatrix = glm::mat4(1);
    glm::mat4 outlineModelMatrix;

    // sets rotations
    modelMatrix = glm::translate(modelMatrix, camera->GetPosition());
    modelMatrix = glm::rotate(modelMatrix, rotationY + RADIANS(90), glm::vec3(0, 1, 0));
    modelMatrix = glm::rotate(modelMatrix, -rotationZ, glm::vec3(1, 0, 0));

    if (!fire_mode) {
        // sets position and for time bar
        modelMatrix = glm::translate(modelMatrix, glm::vec3(0.98f, -0.65f, 3));
        outlineModelMatrix = modelMatrix;
        modelMatrix = glm::scale(modelMatrix, glm::vec3(-0.3f * (180 - (float)glfwGetTime())/180, 0.1f, 0.25f));
        RenderMesh_color(meshes["Filled2DRectangular"], shaders["PlayerShader"], modelMatrix, glm::vec3(1, 1, 1), true);

        // sets position and scale for wireframe
        outlineModelMatrix = glm::translate(outlineModelMatrix, glm::vec3(0.005f, -0.009f, 0));
        outlineModelMatrix = glm::scale(outlineModelMatrix, glm::vec3(-0.307f, 0.12f, 0.25f));
        RenderMesh_color(meshes["Lined2DRectangular"], shaders["PlayerShader"], outlineModelMatrix, glm::vec3(1, 1, 1), true);
    }

    else {
        // sets position and for time bar
        modelMatrix = glm::translate(modelMatrix, glm::vec3(0.98f, 0.75f, 3));
        outlineModelMatrix = modelMatrix;
        modelMatrix = glm::scale(modelMatrix, glm::vec3(-0.3f * (180 - (float)glfwGetTime()) / 180, 0.089f, 0.25f));
        RenderMesh_color(meshes["Filled2DRectangular"], shaders["PlayerShader"], modelMatrix, glm::vec3(1, 1, 1), true);

        // sets position and scale for wireframe
        outlineModelMatrix = glm::translate(outlineModelMatrix, glm::vec3(0.005f, -0.01f, 0));
        outlineModelMatrix = glm::scale(outlineModelMatrix, glm::vec3(-0.307f, 0.1075f, 0.25f));
        RenderMesh_color(meshes["Lined2DRectangular"], shaders["PlayerShader"], outlineModelMatrix, glm::vec3(1, 1, 1), true);
    }
}

// Draws text hud
void Tema2::DrawHelpText()
{
    const float kTopY = 100.f;
    const float kRowHeight = 25.f;

    int rowIndex = 0;
    std::string polygonModeText = "";

    textRenderer->RenderText("w, a, s, d : movement", 5.0f, kTopY + kRowHeight * rowIndex++, 1.0f, kTextColor);
    textRenderer->RenderText("ctrl : switch to normal/fire mode", 5.0f, kTopY + kRowHeight * rowIndex++, 1.0f, kTextColor);
    textRenderer->RenderText("left click : fire(in fire mode)", 5.0f, kTopY + kRowHeight * rowIndex++, 1.0f, kTextColor);

    if (fire_mode) {
        textRenderer->RenderText("Current mode: fire mode", 5.0f, kTopY + kRowHeight * rowIndex++, 1.0f, kTextColor);
    }

    else textRenderer->RenderText("Current mode: normal mode", 5.0f, kTopY + kRowHeight * rowIndex++, 1.0f, kTextColor);
}

//*******************************************************************

void Tema2::Update(float deltaTimeSeconds)
{
    // Disables cursor
    window->DisablePointer();

    // Draws the maze
    DrawMaze();

    // Draws the player
    DrawPlayer(&player);

    // Draws the shells
    ManageShells(deltaTimeSeconds);

    // Draws the enemies
    ManageEnemies(deltaTimeSeconds);

    // Draws HUD elements
    DrawHPBar();
    DrawTimeBar();

    // Remembers old frames translations to compute player's
    // coordinates for the next frame
    oldTranslateX = translateX;
    oldTranslateZ = translateZ;

    // checks if time is up
    if (glfwGetTime() > deathTime) {
        lose_time = true;
    }
}

void Tema2::FrameEnd()
{
    if (enable_HUD) {
        DrawHelpText();
    }

    if (win) {
        window->Close();
        printf("\n\n");
        printf("*********************************************************************************\n");
        printf("You grabbed the rabbit and an entrance has opened in front of you. You go through\n");
        printf("the entrance and found yourself in a tunnel with a strong lighting at the end of \n");
        printf("       it. You easily passed the tunnel and found your way out of the maze.      \n");
        printf("                                   GOOD ENDING                                   \n");
        printf("*********************************************************************************\n");
        printf("                                     YOU WIN!                                    \n");
        printf("\n\n");
    }

    if (lose_time) {
        window->Close();
        printf("\n\n");
        printf("***********************************************************************************\n");
        printf("The shiny exit has just dissapeared and you can feel it is gone forever. Just then,\n");
        printf(" you hear enemies approaching and they are getting closer. You found a pretty nice \n");
        printf("hideout in a hard to reach corner and decided to spend the night there. At midnight\n");
        printf("you can hear the enemies being really close. You get up just to realise that they  \n");
        printf("spotted you. You start to run for your life, but not even 5 seconds passed and you \n");
        printf("reached a deadend. There is no way to escape so you decided to fight, but there are\n");
        printf("just too many of them...Running out of shells, you feel the death approaching......\n");
        printf("                                    BAD ENDING                                     \n");
        printf("***********************************************************************************\n");
        printf("                                    GAME OVER!                                     \n");
        printf("\n\n");
    }

    if (healthPart == 0) {
        window->Close();
        printf("\n\n");
        printf("***********************************************************************************\n");
        printf(" As you fight yourself with your spinning enemy, your sense of distance fooled you \n");
        printf(" as your enemy has cornered you getting ready for his final move. Even if your will\n");
        printf("wants to fight, your body is so wounded, you can barely move. As you lose more and \n");
        printf("more blood while the enemy is spinning around you, you fell the death approaching..\n");
        printf("                                   WORST ENDING                                    \n");
        printf("***********************************************************************************\n");
        printf("                                    GAME OVER!                                     \n");
        printf("\n\n");
    }
}

//*******************************************************************

// Render function with coloring and ortographic projection switch
void Tema2::RenderMesh_color(Mesh *mesh, Shader *shader, const glm::mat4 & modelMatrix, const glm::vec3 &color, bool HUD)
{
    if (!mesh || !shader || !shader->program)
        return;

    // Render an object using the specified shader and the specified position

    shader->Use();
    glUniformMatrix4fv(shader->loc_view_matrix, 1, GL_FALSE, glm::value_ptr(camera->GetViewMatrix()));

    if (!HUD) {
        glUniformMatrix4fv(shader->loc_projection_matrix, 1, GL_FALSE, glm::value_ptr(projectionMatrix));
    }

    else {
        glm::ivec2 res = window->GetResolution();
        glUniformMatrix4fv(shader->loc_projection_matrix, 1, GL_FALSE, glm::value_ptr(glm::ortho(-1.f, 1.f, -1.f, 1.f, 0.001f, 100.f)));
    }

    glUniformMatrix4fv(shader->loc_model_matrix, 1, GL_FALSE, glm::value_ptr(modelMatrix));
    glUniform3fv(glGetUniformLocation(shader->program, "Object_color"), 1, glm::value_ptr(color));

    mesh->Render();
}

// Render function with noise application
void Tema2::RenderMesh_enemy(Mesh *mesh, Shader *shader, const glm::mat4& modelMatrix, const glm::vec3 &color, float noise) {
    if (!mesh || !shader || !shader->program)
        return;

    // Render an object using the specified shader and the specified position
    shader->Use();
    glUniformMatrix4fv(shader->loc_view_matrix, 1, GL_FALSE, glm::value_ptr(camera->GetViewMatrix()));
    glUniformMatrix4fv(shader->loc_projection_matrix, 1, GL_FALSE, glm::value_ptr(projectionMatrix));
    glUniformMatrix4fv(shader->loc_model_matrix, 1, GL_FALSE, glm::value_ptr(modelMatrix));
    glUniform3fv(glGetUniformLocation(shader->program, "Object_color"), 1, glm::value_ptr(color));
    glUniform1f(glGetUniformLocation(shader->program, "noise"), noise);

    mesh->Render();
}

// Normal RenderMesh
void Tema2::RenderMesh(Mesh* mesh, Shader* shader, const glm::mat4& modelMatrix)
{
    if (!mesh || !shader || !shader->program)
        return;

    // Render an object using the specified shader and the specified position
    shader->Use();
    glUniformMatrix4fv(shader->loc_view_matrix, 1, GL_FALSE, glm::value_ptr(camera->GetViewMatrix()));
    glUniformMatrix4fv(shader->loc_projection_matrix, 1, GL_FALSE, glm::value_ptr(projectionMatrix));
    glUniformMatrix4fv(shader->loc_model_matrix, 1, GL_FALSE, glm::value_ptr(modelMatrix));

    mesh->Render();
}


/*
 *  These are callback functions. To find more about callbacks and
 *  how they behave, see `input_controller.h`.
 */


void Tema2::OnInputUpdate(float deltaTime, int mods)
{
    if (window->KeyHold(GLFW_KEY_W)) {
        // forward
		translateX += speed * deltaTime;
		camera->MoveForward(deltaTime * speed);
	}

	if (window->KeyHold(GLFW_KEY_A)) {
        // left
		camera->TranslateRight(-deltaTime * speed);
		translateZ -= deltaTime * speed;
	}

	if (window->KeyHold(GLFW_KEY_S)) {
        // backwards
		camera->MoveForward(-deltaTime * speed);
		translateX -= speed * deltaTime;
	}

	if (window->KeyHold(GLFW_KEY_D)) {
        // right
		camera->TranslateRight(deltaTime * speed);
		translateZ += deltaTime * speed;
	}
}


void Tema2::OnKeyPress(int key, int mods)
{
    // Switches modes depending of the current state
    if (window->GetSpecialKeyState() & GLFW_MOD_CONTROL) {
        if (fire_mode) {
            fire_mode = false;
            camera->Set(glm::vec3(player.posX - 2 * cos(rotationY), player.posY + 1, player.posZ + 2 * sin(rotationY)),
                glm::vec3(player.posX, player.posY, player.posZ),
                glm::vec3(0, player.posY + 10, 0));
        }

        else {
            fire_mode = true;
            camera->Set(glm::vec3(player.posX, player.posY, player.posZ),
               glm::vec3(player.posX + 20 * cos(rotationY), player.posY, player.posZ - 20 * sin(rotationY)),
               glm::vec3(0, player.posY + 5, 0));
        }

        rotationZ = 0;
    }

    // Enables helping text
    if (key == GLFW_KEY_H) {
        if (!enable_HUD) {
            enable_HUD = true;
        }

        else enable_HUD = false;
    }

}


void Tema2::OnKeyRelease(int key, int mods)
{
    // Add key release event
}


void Tema2::OnMouseMove(int mouseX, int mouseY, int deltaX, int deltaY)
{
   float sensitivityOX = 0.001f;
   float sensitivityOY = 0.001f;
   float camera_rotation;

   // Sets camera and rotation for
   // First person view
   if (fire_mode) {
	   rotationY += -deltaX * sensitivityOX;

	   camera_rotation = -deltaY * sensitivityOY;

	   if (rotationZ + camera_rotation > RADIANS(60)) {
		   camera_rotation = RADIANS(60) - rotationZ;
		   rotationZ = RADIANS(60);
	   }

	   else if (rotationZ + camera_rotation < RADIANS(-60)) {
		   camera_rotation = RADIANS(-60) - rotationZ;
		   rotationZ = RADIANS(-60);
	   }

	   else {
		   rotationZ += -deltaY * sensitivityOY;
	   }

	   camera->RotateFirstPerson_OY(-deltaX * sensitivityOX);
	   camera->RotateFirstPerson_OX(camera_rotation);
   }

   // Sets camera and rotation for
   // Third person view
   if (!fire_mode) {
	   rotationY += -deltaX * sensitivityOX;
	   camera_rotation = -deltaY * sensitivityOY;

	   if (rotationZ + camera_rotation > RADIANS(60)) {
		   camera_rotation = RADIANS(60) - rotationZ;
		   rotationZ = RADIANS(60);
	   }

	   else if (rotationZ + camera_rotation < RADIANS(-60)) {
		   camera_rotation = RADIANS(-60) - rotationZ;
		   rotationZ = RADIANS(-60);
	   }

	   else {
		   rotationZ += -deltaY * sensitivityOY;
	   }

	   camera->RotateThirdPerson_OY(-deltaX * sensitivityOX);
	   camera->RotateThirdPerson_OX(camera_rotation);
   }
}


void Tema2::OnMouseBtnPress(int mouseX, int mouseY, int button, int mods)
{
    // Creates a new shell which will be launched from the sphere of doom
    // and then marks the gun as in reload
    if (!isReloading && IS_BIT_SET(button, GLFW_MOUSE_BUTTON_LEFT) && fire_mode) {
        reloadTime = glfwGetTime();
        glm::vec3 doomPos = camera->GetTargetPosition();
        Shells.push_back(Shell(doomPos[0], doomPos[1], doomPos[2], rotationY, rotationZ, reloadTime));
        isReloading = true;
    }
}


void Tema2::OnMouseBtnRelease(int mouseX, int mouseY, int button, int mods)
{
    // Add mouse button release event
}


void Tema2::OnMouseScroll(int mouseX, int mouseY, int offsetX, int offsetY)
{
}


void Tema2::OnWindowResize(int width, int height)
{
}
