#include "Exit.hh"

// Exits the entire game.
// Saves everything in EEPROM
// and writes message on screen.
void exitState(){
  display.clearDisplay();
  display.setTextSize(2);
  display.setTextColor(BLACK);
  
  display.setCursor(5, 16);
  display.println(F("SAVING"));
  display.display();

  // Saves everything
  Save();
  delay(1000);
  display.clearDisplay();
  
  display.setCursor(3, 0);
  display.println(F(" Game "));
  display.setCursor(8, 20);
  display.println(F("closed"));
  display.display();
  delay(3500);
  
  display.clearDisplay();
  display.setTextSize(0);
  display.setCursor(0, 0);
  display.println(F("You can safelyremove the USBcable or pressreset button  to restart thegame"));
  display.display();
  while(1)
  {;}
}
