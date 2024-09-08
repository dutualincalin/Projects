#ifndef _SAVE_HH_
#define _SAVE_HH_

#include <Adafruit_PCD8544.h>
#include <EEPROM.h>
#include "Games.hh"


// Variables
extern Adafruit_PCD8544 display;
extern int score[6];
extern bool casual_save;
extern uint8_t casual_game_save[5][5];
extern unsigned long casual_time_save;
extern int casual_score_save;
extern bool puzzle_save;
extern uint8_t puzzle_game_save[5][5];
extern uint8_t puzzle_level_save;
extern uint8_t puzzle_score_save;
extern uint8_t oldState;
extern uint8_t state;
extern int8_t option;


// Functions
void ifSavedOptions(int options);

void ChooseSave(int chosen_state);

void loadSaves();

void Save();

void saveCasual(uint8_t gems[][5], unsigned int score, unsigned long timer);

void savePuzzle(uint8_t gems[][5], unsigned int score, unsigned long level);

#endif
