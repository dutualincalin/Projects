#include <Adafruit_PCD8544.h>

#include "Sound.hh"
#include "MainMenu.hh"
#include "Exit.hh"
#include "Games.hh"
#include "Leaderboard.hh"
#include "Save.hh"

// Display
Adafruit_PCD8544 display = Adafruit_PCD8544(2, 3, 5, 7, 6);

// Scores saved in EEPROM
int score[6];

// Casual save
bool casual_save;
uint8_t casual_game_save[5][5];
unsigned long casual_time_save;
int casual_score_save;

// Puzzle save
bool puzzle_save;
uint8_t puzzle_game_save[5][5];
uint8_t puzzle_level_save;
uint8_t puzzle_score_save;

// State variables
uint8_t oldState;
uint8_t state = 0;
int8_t option = 0;

// Debouncing variables
volatile unsigned long lastDebounceTime = 0;
volatile  unsigned long debounceDelay = 200;

// Setting up the LCD
void setupLCD()
{
    display.begin();
    display.setContrast(50);

    display.display();
    delay(1000);
}


void setup()
{
  
  Serial.begin(9600);
  while (!Serial) {;}

  // Setting the pins and the interrupts
  DDRC &= ~(63 << PC0);
  PORTC |= (63 << PC0);
  
  PCICR |= (1 << PCIE1);
  PCMSK1 |= (63 << PCINT8);
  sei();

  // Setting the pin for the buzzer
  pinMode(8, OUTPUT);

  // Setting up the display
  setupLCD();

  // Loading the saves
  loadSaves();
}


ISR(PCINT1_vect)
{
  // Up
  if((PINC & (1 << PC0)) == 0){
    if ((millis() - lastDebounceTime) > debounceDelay + 5){
      lastDebounceTime = millis();
      tone(BUZZER, 3850, 50);
      
      switch (state){
        case 0:
          //move up
          option = option - 1 + 4 * (option == 0);
          break;

        case 1:
        case 2:
        case 6:
          // Move up (1, 2) or move the gem up(6)
          option = 1;
          break;

        case 5:
        case 7:
          // Move up
          option = option - 1 + 3 * (option == 0);
          break;
      }
    }

    return;
  }

  
  // Down
  if((PINC & (1 << PC3)) == 0){
    if ((millis() - lastDebounceTime) > debounceDelay){
      lastDebounceTime = millis();
      tone(BUZZER, 3850, 50);
      
      switch (state){
        case 0:
          // Move down
          option = (option + 1) % 4;
          break;

        case 1:
        case 2:
        case 6:
          // Move down (1, 2) or move the gem down(6)
          option = 2;
          break;

        case 5:
        case 7:
          // Move down
          option = (option + 1) % 3;
          break;
      }
    }
    
    return;
  }


  // Left
  if((PINC & (1 << PC2)) == 0){
    if ((millis() - lastDebounceTime) > debounceDelay){
      lastDebounceTime = millis();
      tone(BUZZER, 3850, 50);
      
      switch (state){
        case 1:
        case 2:
        case 6:
          // Move left or move the gem left
          option = 3;
          break;

        case 3:
          // Switch between leaderbords
          option = 1 * (option == 0);
          break;
      }
    }

    return;
  }


  // Right
  if((PINC & (1 << PC1)) == 0){
    if ((millis() - lastDebounceTime) > debounceDelay){
      lastDebounceTime = millis();
      tone(BUZZER, 3850, 50);
      
      switch (state){
        // Move right or move the gem right
        case 1:
        case 2:
        case 6:
          option = 4;
          break;

        case 3:
          // Switch between leaderbords
          option = 1 * (option == 0);
          break;
      }
    }
    
    return;
  }


  // Select
  if((PINC & (1 << PC4)) == 0){
    if ((millis() - lastDebounceTime) > debounceDelay){
      lastDebounceTime = millis();
      tone(BUZZER, 3850, 50);
      
      switch (state){
        case 0:
        case 7:
          // Selects an option
          state = option + 1;
          break;

        case 1:
        case 2:
          // Selects a gem
          oldState = state;
          state = 6;
          option = 0;
          break;

        case 5:
          switch (option){
            case 0:
              // Resume game
              state = oldState;
              option = 0;
              break;
           
            case 1:
              // Save game
              state = oldState;
              option = 1;
              break;
    
            case 2:
              // Exit game
              state = 0;
              option = 1;
              break;
          }

          break;

        case 6:
          // Deselects gem
          state = oldState;
          option = 0;
          break;
      }
    }
    
    return;
  }


  // Esc
  if((PINC & (1 << PC5)) == 0){
    if ((millis() - lastDebounceTime) > debounceDelay){
      lastDebounceTime = millis();
      tone(BUZZER, 3000, 50);
      
      switch (state){
        case 0:
          // Moves to option 3 in Main Menu (Exit)
          option = 3;
          break;

        case 1:
        case 2:
          // Swithces to Submenu
          oldState = state;
          state = 5;
          option = 0;
          break;

        case 3:
          // Exits the Leaderboard to the Main Menu
          state = 0;
          option = 0;
          break;

        case 5:
          // Exits submenu and goes back to the game
          state = oldState;
          option = 0;
          break;
      }
    }
    
    return;
  }
}

/*
 * State:
 * 1 - Casual
 * 2 - Puzzle
 * 3 - Leaderboard
 * 4 - Exit
 */
void loop()
{
  switch (state){
    case 0:
      MainMenu(option);
      break;

    case 1:
    case 2:
      option = 0;
      ChooseSave(state);
      break;

    case 3:
      option = 0;
      showLeaderboards();
      break;
      
    case 4:
      exitState();
  }
}
