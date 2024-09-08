#ifndef _GEMS_HH_
#define _GEMS_HH_

#include <Adafruit_PCD8544.h>
#include "Events.hh"

// Variables
extern Adafruit_PCD8544 display;


// Constant vectors
static const unsigned char PROGMEM blue[]=
{
B01111111, B00000000,
B11111111, B10000000,
B01111111, B00000000,
B00111110, B00000000,
B00011100, B00000000,
B00001000, B00000000,
};

static const unsigned char PROGMEM pink[] =
{
  B00000000, B00000000,
  B00001000, B00000000,
  B00011100, B00000000,
  B00111110, B00000000,
  B01111111, B00000000,
  B11111111, B10000000,
};

static const unsigned char PROGMEM red[] =
{
  B01111111, B00000000,
  B01111111, B00000000,
  B01111111, B00000000,
  B01111111, B00000000,
  B01111111, B00000000,
  B01111111, B00000000,
};

static const unsigned char PROGMEM orange[] =
{
  B00011100, B00000000,
  B00111110, B00000000,
  B01111111, B00000000,
  B01111111, B00000000,
  B00111110, B00000000,
  B00011100, B00000000,
};

static const unsigned char PROGMEM yellow[] =
{
  B00001000, B00000000,
  B00011100, B00000000,
  B00111110, B00000000,
  B00111110, B00000000,
  B00011100, B00000000,
  B00001000, B00000000,
};


// Functions
unsigned int checkGemUp(uint8_t gems[][5], uint8_t gem, uint8_t i, uint8_t j);

unsigned int checkGemDown(uint8_t gems[][5], uint8_t gem, uint8_t i, uint8_t j);

unsigned int checkGemLeft(uint8_t gems[][5], uint8_t gem, uint8_t i, uint8_t j);

unsigned int checkGemRight(uint8_t gems[][5], uint8_t gem, uint8_t i, uint8_t j);

unsigned int checkGems(uint8_t gems[][5]);

void levelGems(uint8_t gems[][5]);

void showGem(uint8_t gem, uint8_t x, uint8_t y);

void setGems(uint8_t gems[][5]);

void swapGems(uint8_t *gem1, uint8_t *gem2);

void fillGems(uint8_t gems[][5]);

#endif
