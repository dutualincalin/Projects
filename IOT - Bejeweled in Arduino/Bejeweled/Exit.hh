#ifndef _EXIT_HH_
#define _EXIT_HH_

#include <Adafruit_PCD8544.h>
#include <EEPROM.h>
#include "Save.hh"


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


// Functions
void exitState();

#endif
