#include "Games.hh"
#include <avr/pgmspace.h>

#define MAX_LEVELS 5

/*
 * Manages a Puzzle level
 */
void Puzzle()
{
  uint8_t level;
  unsigned int score;
  uint8_t pos_x = 3;
  uint8_t pos_y = 2;
  
  uint8_t gems[5][5];

  // Loads the game if saved
  if(puzzle_save){
    level = puzzle_level_save;
    score = puzzle_score_save;
    copyMatrix(gems, puzzle_game_save);
  }

  else{
    level = 0;
    score = 0;
    copyMatrix(gems, puzzle0);
  }
  
  while(1){
    // Submenu
    if(state == 5){
      SubMenu(gems, score, level);
    }

    // Exits to the Main Menu via Submenu
    if(state == 0){
      return;
    }

    // Selects a gem
    if(state == 6){
      // and it is valid
      if(gems[pos_x][pos_y] != 5 && option > 0){
        selectedMove(gems, option, &score, &pos_x, &pos_y);
        option = 0;
      }

      else if(gems[pos_x][pos_y] == 5){
        state = oldState;
        option = 0;
      }
    }

    // Shows the level
    showLevel(gems, score, pos_x, pos_y);

    // Moves the selector
    if(state != 6){
      switch(option){
        case 1:
          pos_x -= (pos_x != 0);
          break;
          
        case 2:
          pos_x += (pos_x != 4);
          break;
          
        case 3:
          pos_y -= (pos_y != 0);
          break;
          
        case 4:
          pos_y += (pos_y != 4);
          break;
      }
      
      option = 0;

      if(checkWin(gems)){
        // Puzzle Solved
        levelWon(score, level + 1);
        level++;
        
        if(level == MAX_LEVELS){
          // Won the gamemode
          gameWon(score);
          state = 0;
          option = 0;
          break;
        }
  
        else {
          // Next level
          setNextLevel(gems, level + 1);
          pos_x = 3;
          pos_y = 2;
        }
      }
      
      else if(checkIfNoMoves(gems)){
        // Game Over
        gameOver(score);
        state = 0;
        option = 0;
        break;
      }
    }
 }
}
