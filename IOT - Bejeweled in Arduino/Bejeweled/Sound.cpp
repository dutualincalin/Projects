#include "Sound.hh"

/*
 * I mean, it is a song source,
 * it's pretty obvious what these do
 */


void GameOfThronesSong()
{
  int tempo = 85;
  int notes = sizeof(GameOfThrones) / sizeof(GameOfThrones[0]) / 2;
  int wholenote = (60000 * 4) / tempo;
  int divider = 0, noteDuration = 0;

  for (int thisNote = 0; thisNote < notes * 2; thisNote = thisNote + 2) {
    divider = GameOfThrones[thisNote + 1];
    if (divider > 0) {
      noteDuration = (wholenote) / divider;
    } else if (divider < 0) {
      noteDuration = (wholenote) / abs(divider);
      noteDuration *= 1.5;
    }

    tone(BUZZER, GameOfThrones[thisNote], noteDuration * 0.9);
    delay(noteDuration);
    noTone(BUZZER);
  }
}

void RickRollSong() 
{
  int tempo = 114;
  int notes = sizeof(RickRoll) / sizeof(RickRoll[0]) / 2;
  int wholenote = (60000 * 4) / tempo;
  int divider = 0, noteDuration = 0;

  for (int thisNote = 0; thisNote < notes * 2; thisNote = thisNote + 2) {
    divider = RickRoll[thisNote + 1];
    if (divider > 0) {
      noteDuration = (wholenote) / divider;
    } else if (divider < 0) {
      noteDuration = (wholenote) / abs(divider);
      noteDuration *= 1.5;
    }

    tone(BUZZER, RickRoll[thisNote], noteDuration * 0.9);
    delay(noteDuration);
    noTone(BUZZER);
  }
}
