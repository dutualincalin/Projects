#include "Games.hh"

// Shows the screen while in level
void showScore(unsigned int score)
{
  display.setRotation(1);
  display.setTextSize(2);
  display.setTextColor(BLACK);
  display.setCursor(0, 3);
  display.print(score);
  display.setRotation(0);
}


// Display the level on the screen (for puzzle)
void showLevel(uint8_t gems[][5], unsigned int score, uint8_t pos_x, uint8_t pos_y)
{
  // Clear the display
  display.clearDisplay();
  
  // Grid
  display.drawBitmap(23, 1, grid, 61, 46, 1);
  display.drawBitmap(21, 0, grid_line, 1, 48, 1);

  // Score
  showScore(score);

  // Selection area
  if(state == 6){
    display.drawBitmap(pos_y * 12 + 24, pos_x  * 9 + 2, pos_s, 11, 8, 1);
  }

  else display.drawBitmap(pos_y * 12 + 24, pos_x  * 9 + 2, pos_u, 11, 8, 1);

  // Show gems
  setGems(gems);

  // Display everything
  display.display();
}


// Display the level on the screen (for casual)
void showLevel(uint8_t gems[][5], unsigned int score, uint8_t pos_x, uint8_t pos_y, unsigned long timer)
{
  // Clear the display
  display.clearDisplay();
  
  // Grid
  display.drawBitmap(23, 1, grid, 61, 46, 1);

  // Timer Grid
  display.drawBitmap(18, 48 * timer / TIME_LIMIT, grid_line, 1, 48 * (TIME_LIMIT - timer) / TIME_LIMIT, 1);
  display.drawBitmap(19, 48 * timer / TIME_LIMIT, grid_line, 1, 48 * (TIME_LIMIT - timer) / TIME_LIMIT, 1);
  display.drawBitmap(20, 48 * timer / TIME_LIMIT, grid_line, 1, 48 * (TIME_LIMIT - timer) / TIME_LIMIT, 1);
  display.drawBitmap(21, 48 * timer / TIME_LIMIT, grid_line, 1, 48 * (TIME_LIMIT - timer) / TIME_LIMIT, 1);
  
  // Score
  showScore(score);

  if(state == 6){
    display.drawBitmap(pos_y * 12 + 24, pos_x  * 9 + 2, pos_s, 11, 8, 1);
  }

  else display.drawBitmap(pos_y * 12 + 24, pos_x  * 9 + 2, pos_u, 11, 8, 1);

  // Show gems
  setGems(gems);

  // Display everything
  display.display();
}


/*
 * Taskes place when player selected a gem (for Casual)
 * 
 * Options
 * 1 - move gem up
 * 2 - move gem down
 * 3 - move gem left
 * 4 - move gem right
 */
void selectedMove(uint8_t gems[][5], uint8_t options, unsigned int *score, uint8_t *pos_x, uint8_t *pos_y, unsigned long timer)
{
  uint8_t aux;
  bool matched = false;
  bool valid = false;

  switch(options){
    // move up
    case 1:
      if(*pos_x != 0 && gems[*pos_x - 1][*pos_y] != 5){
        swapGems(&gems[*pos_x][*pos_y], &gems[*pos_x - 1][*pos_y]);
        (*pos_x)--;
        valid = true;
      }
      
      break;

    // move down
    case 2:
      if(*pos_x != 4 && gems[*pos_x + 1][*pos_y] != 5){
        swapGems(&gems[*pos_x][*pos_y], &gems[*pos_x + 1][*pos_y]);
        (*pos_x)++;
        valid = true;
      }

      break;

    //move left
    case 3:
      if(*pos_y != 0 && gems[*pos_x][*pos_y - 1] != 5){
        swapGems(&gems[*pos_x][*pos_y], &gems[*pos_x][*pos_y - 1]);
        (*pos_y)--;
        valid = true;
      }

      break;

    // move right
    case 4:
      if(*pos_y != 4 && gems[*pos_x][*pos_y + 1] != 5){
        swapGems(&gems[*pos_x][*pos_y], &gems[*pos_x][*pos_y + 1]);
        (*pos_y)++;
        valid = true;
      }
  }

  // Show changes
  showLevel(gems, *score, *pos_x, *pos_y, timer);
  delay(300);
      
  while(1){
    // Compute score and check matches
    aux = checkGems(gems);

    // Show the level
    showLevel(gems, *score, *pos_x, *pos_y, timer);
    delay(300);
    
    if(aux == 0){
      if(!matched && valid){
        // Undo the move if it is not valid
        switch(options){      
          case 1:
            (*pos_x)++;
            swapGems(&gems[*pos_x][*pos_y], &gems[*pos_x - 1][*pos_y]);
            break;
      
          case 2:
            (*pos_x)--;
            swapGems(&gems[*pos_x][*pos_y], &gems[*pos_x + 1][*pos_y]);
            break;
      
          case 3:
            (*pos_y)++;
            swapGems(&gems[*pos_x][*pos_y], &gems[*pos_x][*pos_y - 1]);
            break;
      
          case 4:
            (*pos_y)--;
            swapGems(&gems[*pos_x][*pos_y], &gems[*pos_x][*pos_y + 1]);
        }
      }

      // Set parameters for the next state
      state = oldState;
      option = 0;
      return;
    }

    // Level the gems that are left
    // floating in the air
    levelGems(gems);
    showLevel(gems, *score, *pos_x, *pos_y, timer);
    delay(300);

    // Fill the empty spaces
    fillGems(gems);
    showLevel(gems, *score, *pos_x, *pos_y, timer);
    delay(300);

    // Set the score
    *score += aux;
    matched = true;
  }
}


/*
 * Taskes place when player selected a gem (for Puzzle)
 * 
 * Options
 * 1 - move gem up
 * 2 - move gem down
 * 3 - move gem left
 * 4 - move gem right
 */
void selectedMove(uint8_t gems[][5], uint8_t options, unsigned int *score, uint8_t *pos_x, uint8_t *pos_y)
{
  uint8_t aux;
  bool matched = false;
  bool valid = false;

  switch(options){
    // move up
    case 1:
      if(*pos_x != 0 && gems[*pos_x - 1][*pos_y] != 5){
        swapGems(&gems[*pos_x][*pos_y], &gems[*pos_x - 1][*pos_y]);
        (*pos_x)--;
        valid = true;
      }
      
      break;

    // move down
    case 2:
      if(*pos_x != 4 && gems[*pos_x + 1][*pos_y] != 5){
        swapGems(&gems[*pos_x][*pos_y], &gems[*pos_x + 1][*pos_y]);
        (*pos_x)++;
        valid = true;
      }

      break;

    //move left
    case 3:
      if(*pos_y != 0 && gems[*pos_x][*pos_y - 1] != 5){
        swapGems(&gems[*pos_x][*pos_y], &gems[*pos_x][*pos_y - 1]);
        (*pos_y)--;
        valid = true;
      }

      break;

    // move right
    case 4:
      if(*pos_y != 4 && gems[*pos_x][*pos_y + 1] != 5){
        swapGems(&gems[*pos_x][*pos_y], &gems[*pos_x][*pos_y + 1]);
        (*pos_y)++;
        valid = true;
      }
  }

  // Show changes
  showLevel(gems, *score, *pos_x, *pos_y);
  delay(300);
      
  while(1){
    // Compute score and check matches
    aux = checkGems(gems);

    // Show the level
    showLevel(gems, *score, *pos_x, *pos_y);
    delay(300);
    
    if(aux == 0){
      if(!matched && valid){
        // Undo the move if it is not valid
        switch(options){
          case 1:
            (*pos_x)++;
            swapGems(&gems[*pos_x][*pos_y], &gems[*pos_x - 1][*pos_y]);
            break;

          case 2:
            (*pos_x)--;
            swapGems(&gems[*pos_x][*pos_y], &gems[*pos_x + 1][*pos_y]);
            break;

          case 3:
            (*pos_y)++;
            swapGems(&gems[*pos_x][*pos_y], &gems[*pos_x][*pos_y - 1]);
            break;

          case 4:
            (*pos_y)--;
            swapGems(&gems[*pos_x][*pos_y], &gems[*pos_x][*pos_y + 1]);
        }
      }

      // Set parameters for the next state
      state = oldState;
      option = 0;
      return;
    }

    // Level the gems that are left
    // floating in the air
    levelGems(gems);
    showLevel(gems, *score, *pos_x, *pos_y);
    delay(300);

    // Set the score
    *score += aux;
    matched = true;
  }
}


// Show the options of the Submenu
void showSubOptions()
{
  display.clearDisplay();
  display.drawBitmap(0, 0, bejeweled_left_half, 23, 48, 1);
  display.drawBitmap(61, 0, bejeweled_right_half, 23, 48, 1);

  display.fillRect(23, option * 10 + 8, 38, 9, BLACK);
  
  display.setCursor(24, option * 10 + 9);
  display.setTextSize(1);
  display.setTextColor(WHITE);

  switch(option){
    case 0:
      display.print(F("Resume"));
      display.setTextColor(BLACK);
      display.setCursor(24, 19);
      display.print(F(" Save"));
      display.setCursor(24, 29);
      display.print(F(" Exit"));
      break;

    case 1:
      display.print(F(" Save"));
      display.setTextColor(BLACK);
      display.setCursor(24, 9);
      display.print(F("Resume"));
      display.setCursor(24, 29);
      display.print(F(" Exit"));
      break;

    case 2:
      display.print(F(" Exit"));
      display.setTextColor(BLACK);
      display.setCursor(24, 9);
      display.print(F("Resume"));
      display.setCursor(24, 19);
      display.print(F(" Save"));
      break;
  }

  display.display();  
}


// Manages the ingame Submenu
void SubMenu(uint8_t gems[][5], unsigned int score, unsigned long other)
{
  while(state == 5){
    showSubOptions();
  }

  // Save the game
  if(option == 1){
    // Casual game save
    if(state == 1){
      saveCasual(gems, score, other);
    }

    // Puzzle game save
    else if(state == 2){
      savePuzzle(gems, score, other);
    }
        
    option = 0;
  }
}


// Copies level matrix to gems
void copyMatrix(uint8_t gems[][5], uint8_t level[][5])
{
  int i, j;

  for(i = 0 ; i < 5; i++){
    for(j = 0; j < 5; j++){
      gems[i][j] = level[i][j];
    }
  }
}
