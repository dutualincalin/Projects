#ifndef _EVENTS_HH_
#define _EVENTS_HH_

#include <Arduino.h>
#include <Adafruit_PCD8544.h>
#include "Sound.hh"
#include "Games.hh"
#include "Gems.hh"


// Variables
extern Adafruit_PCD8544 display;


// Functions
void TimeUp(unsigned int score);

void gameOver(unsigned int score);

void gameWon(unsigned int score);

void levelWon(unsigned int score, uint8_t level);

void setNextLevel(uint8_t gems[][5], uint8_t level);

bool checkWin(uint8_t gems[][5]);

bool checkIfNoMoves(uint8_t gems[][5]);

#endif
