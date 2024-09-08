#include "MainMenu.hh"

// Shows Main Menu options
void MainMenu(uint8_t options)
{
    display.clearDisplay();
    display.drawBitmap(0, 0,  bejeweled, 46, 48, 1);

    display.fillRect(47, options * 10 + 4, 38, 9, BLACK);
    
    display.setCursor(48, options * 10 + 5);
    display.setTextSize(1);
    display.setTextColor(WHITE);
    
    switch (options){
    case 0:
        display.print(F("Casual"));
        display.setTextColor(BLACK);
        display.setCursor(48, 15);
        display.print(F("Puzzle"));
        display.setCursor(48, 25);
        display.print(F("Scores"));
        display.setCursor(48, 35);
        display.print(F(" Exit"));
        break;

    case 1:
        display.print(F("Puzzle"));
        display.setTextColor(BLACK);
        display.setCursor(48, 5);
        display.print(F("Casual"));
        display.setCursor(48, 25);
        display.print(F("Scores"));
        display.setCursor(48, 35);
        display.print(F(" Exit"));
        break;

    case 2:
        display.print(F("Scores"));
        display.setTextColor(BLACK);
        display.setCursor(48, 5);
        display.print(F("Casual"));
        display.setCursor(48, 15);
        display.print(F("Puzzle"));
        display.setCursor(48, 35);
        display.print(F(" Exit"));
        break;

    case 3:
        display.print(F(" Exit"));
        display.setTextColor(BLACK);
        display.setCursor(48, 5);
        display.print(F("Casual"));
        display.setCursor(48, 15);
        display.print(F("Puzzle"));
        display.setCursor(48, 25);
        display.print(F("Scores"));
        break;
    }

    display.display();
}
