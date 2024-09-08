#include "Leaderboard.hh"

// Shows the Casual Leaderboard
void showCasualLeaderboard()
{
  int i;
  
  display.clearDisplay();
  display.setTextSize(2);
  display.setTextColor(BLACK);
  
  display.setCursor(8,0);
  display.println(F("Casual"));
  
  display.setTextSize(1);
  display.setCursor(0, 20);

  for(i = 0; i < 3; i++){
    display.print(F("       "));
    display.println(String(i + 1) + F(")") + String(score[i]));
  }

  display.drawBitmap(0, 16, casual, 40, 32, 1);
  
  display.display();
  delay(300);
}


// Shows the Puzzle Leaderboard
void showPuzzleLeaderboard()
{
  int i;
  
  display.clearDisplay();
  display.setTextSize(2);
  display.setTextColor(BLACK);
  display.setCursor(8,0);
  display.println(F("Puzzle"));
  display.setTextSize(1);
  display.setCursor(0, 20);

  for(i = 0; i < 3; i++){
    display.print(F(" "));
    display.println(String(i + 1) + F(")") + String(score[i + 3]));
  }
  
  display.drawBitmap(43, 16, puzzle, 40, 32, 1);
  
  display.display();
  delay(300);
}


// Adds a score to the casual
// leaderboard if it is big enough
void updateCasualScores(int newScore)
{
  int i;
  
  for(i = 0; i < 3; i++){
    if(newScore > score[i]){
      score[i] = newScore;
      break;
    }
  }
}


// Adds a score to the puzzle
// leaderboard if it is big enough
void updatePuzzleScores(int newScore)
{
  int i;
  
  for(i = 0; i < 3; i++){
    if(newScore > score[i + 3]){
      score[i + 3] = newScore;
      break;
    }
  }
}


// Manages the leaderboards
void showLeaderboards()
{
  int lastOpt = -1;
  
  while(state == 3){
    // Casual Leaderboard
    if(option == 0 && lastOpt != 0){
      showCasualLeaderboard();
      lastOpt = 0;
    }

    // Puzzle Leaderboard
    else if(option == 1 && lastOpt != 1){
      showPuzzleLeaderboard();
      lastOpt = 1;
    }
  }
}
