#include "Games.hh"

// Runs a Casual level
void Casual()
{
  unsigned int score;
  uint8_t pos_x = 3;
  uint8_t pos_y = 2;
  unsigned long startTime;
  unsigned long holdTime;
  unsigned long holdingTime = 0;

  uint8_t gems[5][5] = {
    5, 5, 5, 5, 5,
    5, 5, 5, 5, 5,
    5, 5, 5, 5, 5,
    5, 5, 5, 5, 5,
    5, 5, 5, 5, 5,
  };

  // Checks if the saved game was chosen
  if(casual_save){
    score = casual_score_save;
    copyMatrix(gems, casual_game_save);
  }

  else{
    fillGems(gems);
    score = 0;
  }

  // Sets up the starting time
  startTime = millis();
  
  while(1){
    // The submenu (when pressing esc)
    if(state == 5){
      holdTime = millis();
      SubMenu(gems, score, holdTime - startTime - holdingTime);
      holdingTime += millis() - holdTime;
      option = 0;
    }

    // Time is up
    if(millis() - startTime - holdingTime + casual_time_save > TIME_LIMIT){
      TimeUp(score);
      state = 0;
      option = 0;
    }

    // Exited the game via submenu
    if(state == 0){
      return;
    }

    // Checks if gem was selected
    if(state == 6){
      // ... and if the gem exists
      if(gems[pos_x][pos_y] != 5 && option > 0){
        selectedMove(gems, option, &score, &pos_x, &pos_y, millis() - startTime - holdingTime + casual_time_save);
        option = 0;
      }

      else if(gems[pos_x][pos_y] == 5){
        state = oldState;
        option = 0;
      }
    }

    // Shows level's changes
    showLevel(gems, score, pos_x, pos_y, millis() - startTime - holdingTime + casual_time_save);

    // If no gem selected
    if(state != 6){
      // checks if selecting area needs to be moved
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
    }
  }
}
