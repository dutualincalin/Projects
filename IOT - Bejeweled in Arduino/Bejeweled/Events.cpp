#include "Events.hh"

// Ends a casual level, shows score
// on the display and saves it, if
// it is the case
void TimeUp(unsigned int score)
{
  display.clearDisplay();
  display.setTextSize(2);
  display.setTextColor(BLACK);
  
  display.setCursor(0, 0);
  display.print(F("Time Up"));

  display.setTextSize(1);
  display.setCursor(14, 39);
  display.print(F("Score: "));
  display.print(score);

  display.display();
  updateCasualScores(score);
  GameOfThronesSong();
}


// Ends a puzzle level if there are
// gems remaining and no moves left.
// Shows score and saves it
void gameOver(unsigned int score)
{
  display.clearDisplay();
  display.setTextSize(2);
  display.setTextColor(BLACK);
  
  display.setCursor(22, 0);
  display.print(F("Game\n"));
  display.setCursor(22, 16);
  display.print(F("Over"));
  
  display.setTextSize(1);
  display.setCursor(14, 39);
  display.print(F("Score: "));
  display.print(score);

  display.display();
  updatePuzzleScores(score);
  RickRollSong();
}


// Ends the puzzle gamemode if
// all the levels are finished
// successfully
void gameWon(unsigned int score)
{
  display.clearDisplay();
  display.setTextSize(2);
  display.setTextColor(BLACK);
  
  display.setCursor(0, 9);
  display.print(F("  Well"));
  display.setCursor(12, 24);
  display.print(F("Played"));
  display.display();
  delay(3000);
  
  display.clearDisplay();
  display.setTextSize(1);
  display.setCursor(0, 0);
  display.println(F("  Puzzle mode"));
  
  display.setTextSize(2);
  
  display.setCursor(25, 10);
  display.println(F("won"));
  display.setTextSize(1);
  display.setCursor(14, 39);
  display.print(F("Score: "));
  display.print(score);
  display.display();
  GameOfThronesSong();
  updatePuzzleScores(score);
}


// Ends a puzzle level if there are
// no gems remaining. Shows score only.
void levelWon(unsigned int score, uint8_t level)
{
  display.clearDisplay();
  display.setTextSize(2);
  display.setTextColor(BLACK);
  
  display.setCursor(0, 0);
  display.print(F("Level "));
  display.println(level);
  display.print(F("  won"));
  
  display.setTextSize(1);
  display.setCursor(14, 39);
  display.print(F("Score: "));
  display.print(score);
  display.display();
  delay(3000);
}


// Sets the next puzzle level
void setNextLevel(uint8_t gems[][5], uint8_t level)
{
  display.clearDisplay();
  display.setTextSize(2);
  display.setTextColor(BLACK);
  
  display.setCursor(0, 9);
  display.print(F("Loading\nLevel "));
  display.print(level);
  display.display();

  // Loads level's matrix
  switch(level){
    case 2:
      copyMatrix(gems, puzzle1);
      break;
      
    case 3:
      copyMatrix(gems, puzzle2);
      break;
      
    case 4:
      copyMatrix(gems, puzzle3);
      break;
      
    case 5:
      copyMatrix(gems, puzzle4);
  }
  
  delay(2000);
}


// Checks if the level was finished
bool checkWin(uint8_t gems[][5])
{
  uint8_t i;

  // Checks the last row if there are gems left
  for(i = 0; i < 5; i++){
    if(gems[4][i] != 5){
      return false;
    }
  }

  return true;
}


// Checks if there are no moves
bool checkIfNoMoves(uint8_t gems[][5])
{
  uint8_t i, j;

  for(i = 0 ; i < 5; i++){
    for(j = 0; j < 5; j++){

      if(gems[i][j] == 5){
        continue;
      }

      // Checks moves if actual gem is moved up
      if(gems[i - 1][j] != 5 && i - 1 > -1){
        if(checkGemUp(gems, gems[i][j], i - 1, j) >= 2 || 
          checkGemLeft(gems, gems[i][j], i - 1, j) + checkGemRight(gems, gems[i][j], i - 1, j) >= 2){
          
          return false;
        } 
      }

      // Checks moves if actual gem is moved down
      if(gems[i + 1][j] != 5 && i + 1 < 5){
        if(checkGemDown(gems, gems[i][j], i + 1, j) >= 2 ||
          checkGemLeft(gems, gems[i][j], i + 1, j) + checkGemRight(gems, gems[i][j], i + 1, j) >= 2){
          
          return false;
        }
      }

      // Checks moves if actual gem is moved left
      if(gems[i][j - 1] != 5 && j - 1 > -1){
        if(checkGemLeft(gems, gems[i][j], i, j - 1) >= 2 ||
          checkGemUp(gems, gems[i][j], i, j - 1) + checkGemDown(gems, gems[i][j], i, j - 1) >= 2){
          
          return false;    
        }
      }

      // Checks moves if actual gem is moved right
      if(gems[i][j + 1] != 5 && j + 1 < 5){
        if(checkGemRight(gems, gems[i][j], i, j + 1) >= 2 ||
           checkGemUp(gems, gems[i][j], i, j + 1) + checkGemDown(gems, gems[i][j], i, j + 1) >= 2){
             
          return false;    
        }
      }
    }
  }

  return true;
}
