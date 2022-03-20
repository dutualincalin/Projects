#pragma once

#include "components/simple_scene.h"
#include "lab_m1/Tema2/Camera.h"
#include "lab_m1/Tema2/PrimitiveCreator3D.h"
#include "components/text_renderer.h"


namespace m1
{
    class Tema2 : public gfxc::SimpleScene
    {
    public:
         struct Player
         {
             Player() : posX(0), posY(0), posZ(0) {};
             Player(float posX, float posY, float posZ) : posX(posX), posY(posY), posZ(posZ) {};
             float posX;
             float posY;
             float posZ;
         };

         struct Shell {
             Shell() : posX(0), posY(0), posZ(0), angleY(0), angleZ(0), time(0) {}
             Shell(float posX, float posY, float posZ, float angleY, float angleZ, double time) : posX(posX), posY(posY), posZ(posZ), angleY(angleY), angleZ(angleZ), time(time) {}
             float posX;
             float posY;
             float posZ;
             float angleY;
             float angleZ;
             double time;
         };

         struct Enemy {
             Enemy() : coordX(0), coordY(0), coordZ(0), posX(0), posY(0), posZ(0), angle(0), deathTime(0), noise(0) {}
             Enemy(float coordX, float coordY, float coordZ, float angle) : coordX(coordX), coordY(coordY), coordZ(coordZ),
                   posX(0), posY(0), posZ(0), angle(angle), deathTime(-1), noise(0) {}

             float coordX;
             float coordY;
             float coordZ;
             float posX;
             float posY;
             float posZ;
             float angle;
             double deathTime;
             float noise;
         };

    public:
         Tema2();
        ~Tema2();

        void Init() override;

    private:
        // Player
        glm::mat4 ComputePlayerHeadMatrix(Player* player);
        glm::mat4 ComputePlayerMatrix(Player* player);
        void DrawPlayer(Player *player);


        // Shell
        void DrawShell(Shell* shell, float deltaTimeSeconds);
        void ManageShells(float deltaTimeSeconds);


        // Maze
        void SetMaze();
        void DrawMaze();
        void DrawPillar(float posX, float posZ);
        void DrawHorizontalWall(float posX, float posZ);
        void DrawVerticalWall(float posX, float posZ);
        void DrawFloor();
        void DrawExit(float posX, float posZ);


        // Enemies
        void ManageEnemies(float deltaTimeSeconds);
        void DrawEnemy(Enemy *enemy, float deltaTimeSeconds);

        
        // Collisions
        bool PlayerMazeCollision(Player* player, int i, int j);
        bool ShellMazeCollision(Shell* shell, int i, int j);
        bool PlayerEnemyCollision(Player* player, Enemy* enemy);
        bool ShellEnemyCollision(Shell* shell, Enemy* enemy);
        

        // HUD elements
        void DrawHPBar();
        void DrawTimeBar();
        void DrawHelpText();

        // Framework
        void FrameStart() override;
        void Update(float deltaTimeSeconds) override;
        void FrameEnd() override;

        // Rendering
        void RenderMesh_color(Mesh* mesh, Shader* shader, const glm::mat4& modelMatrix, const glm::vec3 &color, bool HUD);
        void RenderMesh_enemy(Mesh* mesh, Shader* shader, const glm::mat4& modelMatrix, const glm::vec3& color, float noise);
        void RenderMesh(Mesh* mesh, Shader* shader, const glm::mat4& modelMatrix) override;


        // Action based functions
        void OnInputUpdate(float deltaTime, int mods) override;
        void OnKeyPress(int key, int mods) override;
        void OnKeyRelease(int key, int mods) override;
        void OnMouseMove(int mouseX, int mouseY, int deltaX, int deltaY) override;
        void OnMouseBtnPress(int mouseX, int mouseY, int button, int mods) override;
        void OnMouseBtnRelease(int mouseX, int mouseY, int button, int mods) override;
        void OnMouseScroll(int mouseX, int mouseY, int offsetX, int offsetY) override;
        void OnWindowResize(int width, int height) override;

    protected:
        Player player;
        std::vector <Shell> Shells;
        std::vector <Enemy> Enemies;
        Camera::Camera *camera;
        glm::mat4 projectionMatrix;
        std::pair <int, int> finish;

        gfxc::TextRenderer* textRenderer;
        const glm::vec3 kTextColor = NormalizedRGB(255, 250, 250);
        const glm::vec3 kBackgroundColor = NormalizedRGB(41, 45, 62);

        // TODO(student): If you need any other class variables, define them here.
        float left = 0;
        float right = 10;
        float up = 10;
        float bottom = 0;
        float zNear = 0.01f;
        float zFar = 200;
        float FOV = 60;

        char maze[21][21];

        double reloadTime, deathTime, hpTime;
        float translateX, translateZ, oldTranslateX, oldTranslateZ;
        float rotationY, rotationZ, rotateExit;
        float speed, shellSpeed, enemySpeed;
        bool fire_mode, isReloading, win, lose_time, enable_HUD;
        int healthPart;
    };
}   // namespace m1
