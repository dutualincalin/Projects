#include "Save.hh"

// Shows saving menu options
void ifSavedOptions(int options)
{
  display.clearDisplay();
  display.drawBitmap(0, 0, bejeweled_left_half, 23, 48, 1);
  display.drawBitmap(61, 0, bejeweled_right_half, 23, 48, 1);

  display.fillRect(23, options * 10 + 8, 38, 9, BLACK);

  display.setTextSize(1);
  display.setTextColor(WHITE);

  switch(options){
    case 0:
      display.setCursor(33, 9);
      display.print(F("New"));
      display.setTextColor(BLACK);
      display.setCursor(27, 19);
      display.print(F("Saved"));
      display.setCursor(31, 29);
      display.print(F("Back"));
      break;

    case 1:
      display.setCursor(27, 19);
      display.print(F("Saved"));
      display.setTextColor(BLACK);
      display.setCursor(33, 9);
      display.print(F("New"));
      display.setCursor(31, 29);
      display.print(F("Back"));
      break;

    case 2:
      display.setCursor(31, 29);
      display.print(F("Back"));
      display.setTextColor(BLACK);
      display.setCursor(33, 9);
      display.print(F("New"));
      display.setCursor(27, 19);
      display.print(F("Saved"));
      break;
  }

  display.display();
}

// After choosing a gamemode, you will
// be greeted by a submenu in which you
// can choose if you want to play a new
// game or choose the saved one.
// This functions controls that submenu
void ChooseSave(int chosen_state)
{
  state = 7;

  while(1){
    ifSavedOptions(option);

    // New Game
    if(state == 1){
      if(chosen_state == 1){
        casual_save = false;
      }

      else if(chosen_state == 2){
        puzzle_save = false;
      }

      break;
    }

    // Saved Game
    else if(state == 2){
      if(chosen_state == 1){
        if(!casual_save){
          display.clearDisplay();
          display.setTextSize(1);
          display.setTextColor(BLACK);
          
          display.setCursor(15, 9);
          display.print(F("No Casual"));
          display.setCursor(24, 19);
          display.print(F(" Game"));
          display.setCursor(26, 29);
          display.print(F("Saved"));
          display.display();
          delay(1000); 

          state = 7;
        }

        else{
          break;
        }
      }
     
      else if(chosen_state == 2){
        if(!puzzle_save){
          // Scrie ca nu exista save
          display.clearDisplay();
          display.setTextSize(1);
          display.setTextColor(BLACK);
          display.setCursor(15, 9);
          display.print(F("No Puzzle"));
          display.setCursor(24, 19);
          display.print(F(" Game"));
          display.setCursor(26, 29);
          display.print(F("Saved"));
          display.display();
          delay(1000); 

          state = 7;
        }

        else{
          break;
        }
      }
    }

    // Back
    else if(state == 3){
      chosen_state = 0;
      break;
    }
  }

  option = 0;


  switch (chosen_state){
    case 0:
      state = 0;
      return;

    case 1:
      state = chosen_state;
      Casual();
      break;

    case 2:
      state = chosen_state;
      Puzzle();
  }
}


// Loads everything saved from EEPROM 
void loadSaves()
{
  casual_time_save = 0;
  int addr = 0;
  int i, j;

  for(i = 0; i < 6; i++){
    score[i] = EEPROM.read(addr) * 256;
    addr++;
    score[i] += EEPROM.read(addr);
    addr++;
  }

  casual_save = EEPROM.read(addr);
  addr++;

  if(casual_save){
    for(i = 0; i < 5; i++){
      for(j = 0; j < 5; j++){
         casual_game_save[i][j] = EEPROM.read(addr);
         addr++;
      }
    }

    casual_time_save = 256 * EEPROM.read(addr);
    addr++;
    casual_time_save += EEPROM.read(addr);
    addr++;

    casual_score_save = 256 * EEPROM.read(addr);
    addr++;
    casual_score_save += EEPROM.read(addr);
    addr++;
  }

  else addr += 29;

  puzzle_save = EEPROM.read(addr);
  addr++;

  if(puzzle_save){
    for(i = 0; i < 5; i++){
      for(j = 0; j < 5; j++){
         puzzle_game_save[i][j] = EEPROM.read(addr);
         addr++;
      }
    }

    puzzle_level_save = EEPROM.read(addr);
    addr++;
    puzzle_score_save = EEPROM.read(addr);
  }
}


// Saves a casual game
void saveCasual(uint8_t gems[][5], unsigned int score, unsigned long timer)
{
  display.clearDisplay();
  display.setTextSize(1);
  display.setTextColor(BLACK);
  
  display.setCursor(24, 9);
  display.print(F("Saving"));
  display.setCursor(24, 19);
  display.print(F("Casual"));
  display.setCursor(24, 29);
  display.print(F(" Game"));
  display.display();
  delay(500);
  
  casual_save = true;
  copyMatrix(casual_game_save, gems);
  casual_score_save = score;
  casual_time_save = timer;
  
  display.clearDisplay();
  display.setTextSize(1);
  display.setTextColor(BLACK);
  
  display.setCursor(24, 9);
  display.print(F("Casual"));
  display.setCursor(24, 19);
  display.print(F(" Game"));
  display.setCursor(27, 29);
  display.print(F("Saved"));
  display.display();
  delay(2000); 
}


// Saves a Puzzle game
void savePuzzle(uint8_t gems[][5], unsigned int score, unsigned long level)
{
  display.clearDisplay();
  display.setTextSize(1);
  display.setTextColor(BLACK);
  
  display.setCursor(24, 9);
  display.print(F("Saving"));
  display.setCursor(24, 19);
  display.print(F("Puzzle"));
  display.setCursor(24, 29);
  display.print(F(" Game"));
  display.display();
  delay(2000);
  
  puzzle_save = true;
  copyMatrix(puzzle_game_save, gems);
  puzzle_score_save = score;
  puzzle_level_save = level;
  
  display.clearDisplay();
  display.setTextSize(1);
  display.setTextColor(BLACK);
  
  display.setCursor(24, 9);
  display.print(F("Puzzle"));
  display.setCursor(24, 19);
  display.print(F(" Game"));
  display.setCursor(27, 29);
  display.print(F("Saved"));
  display.display();
  delay(2000); 
}


// Saves everything saved in EEPROM
void Save()
{
  int addr = 0;
  int i, j;

  for(i = 0; i < 6; i++){
    EEPROM.write(addr, score[i] / 256);
    addr++;
    
    EEPROM.write(addr, score[i] % 256);
    addr++;
  }

  EEPROM.write(addr, casual_save);
  addr++;

  if(casual_save){
    for(i = 0; i < 5; i++){
      for(j = 0; j < 5; j++){
         EEPROM.write(addr, casual_game_save[i][j]);
         addr++;
      }
    }

    EEPROM.write(addr, casual_time_save / 256);
    addr++;
    EEPROM.write(addr, casual_time_save % 256);
    addr++;

    EEPROM.write(addr, casual_score_save / 256);
    addr++;
    EEPROM.write(addr, casual_score_save % 256);
    addr++;
  }

  else addr += 29;

  EEPROM.write(addr, puzzle_save);
  addr++;

  if(puzzle_save){
    for(i = 0; i < 5; i++){
      for(j = 0; j < 5; j++){
         EEPROM.write(addr, puzzle_game_save[i][j]);
         addr++;
      }
    }

    EEPROM.write(addr, puzzle_level_save);
    addr++;
    EEPROM.write(addr, puzzle_score_save);
  }
}
