#include "Gems.hh"

// Checks how many games of the same type
// are upwards the actual gem
unsigned int checkGemUp(uint8_t gems[][5], uint8_t gem, uint8_t i, uint8_t j)
{
  int8_t k = i - 1;
  uint8_t up = 0;

  while(k > -1){
    if(gems[k][j] == gem){
      k--;
      up++;
    }
    
    else break;
  }

  return up;
}


// Checks how many games of the same type
// are downwards the actual gem
unsigned int checkGemDown(uint8_t gems[][5], uint8_t gem, uint8_t i, uint8_t j)
{
   int8_t k = i + 1;
   uint8_t down = 0;
   
   while(k < 5){
    if(gems[k][j] == gem){
      k++;
      down++;
    }

    else break;
  }

  return down;
}


// Checks how many games of the same type
// are on the left side of the actual gem
unsigned int checkGemLeft(uint8_t gems[][5], uint8_t gem, uint8_t i, uint8_t j)
{
  int8_t k = j - 1;
  unsigned int left = 0;
      
  while(k > -1){
    if(gems[i][k] == gem){
      k--;
      left++;
    }

    else break;
  }

  return left;
}


// Checks how many games of the same type
// are on the right side of the actual gem
unsigned int checkGemRight(uint8_t gems[][5], uint8_t gem, uint8_t i, uint8_t j)
{
  int8_t k = j + 1;
  unsigned int right = 0;
  
  while(k < 5){
    if(gems[i][k] == gem){
      k++;
      right++;
    }

    else break;
  }
  
  return right;
}


// Computes the matches
unsigned int checkGems(uint8_t gems[][5])
{
  int8_t i, j, k;
  uint8_t up, down, left, right;
  unsigned int scored = 0;
  bool matches;
  bool sound = false;
  
  for(i = 0; i < 5; i++){
    for(j = 0; j < 5; j++){
      matches = false;
      
      if(gems[i][j] == 5){
        continue;
      }

      // Checks neighbourhood gems
      up = checkGemUp(gems, gems[i][j], i, j);
      down = checkGemDown(gems, gems[i][j], i, j);
      left = checkGemLeft(gems, gems[i][j], i, j);
      right = checkGemRight(gems, gems[i][j], i, j);      

      // Checks vertical combination
      if(up + down >= 2){
        for(k = i - up; k <= i + down ; k++){
          gems[k][j] = 5;
        }

        scored += up + down;
        matches = true;

        if(!sound){
          tone(BUZZER, 1000, 125);
          delay(200);
          tone(BUZZER, 1500, 125);
          delay(200);
          sound = true;
        }        
      }

      // Checks horizontal combination
      if(left + right >= 2){
        for(k = j - left; k <= j + right; k++){
          gems[i][k] = 5;
        }

        scored += left + right;
        matches = true;

        if(!sound){
          tone(BUZZER, 1000, 125);
          delay(200);
          tone(BUZZER, 1500, 125);
          delay(200);
          sound = true;
        }
      }

      // Adds actual gem to the score
      if(matches){
        scored++;
      }
    }
  }
  
  return scored;
}

// Sets mid-air gems on the floor
// or on the other gems
void levelGems(uint8_t gems[][5])
{
  int8_t i, j, k;
  
  for(i = 3; i >= 0; i--){
    for(j = 4; j >= 0; j--){
      if(gems[i][j] == 5){
        continue;
      }

      k = i;
      while(gems[k + 1][j] == 5){
        k++;
        
        if(k == 4){
          break; 
        }
      }

      if(k != i){
        gems[k][j] = gems[i][j];
        gems[i][j] = 5;
      }
    }
  }
}

// Shows one gem on the display
void showGem(uint8_t gem, uint8_t x, uint8_t y)
{
  switch(gem){
    case 0:
      display.drawBitmap(y, x, blue, 9, 6, 1);
      break;

    case 1:
      display.drawBitmap(y, x, pink, 9, 6, 1);
      break;

    case 2:
      display.drawBitmap(y, x, red, 9, 6, 1);
      break;

    case 3:
      display.drawBitmap(y, x, orange, 9, 6, 1);
      break;

    case 4:
      display.drawBitmap(y, x, yellow, 9, 6, 1);
  }
}

// Shows all the gems on the display
void setGems(uint8_t gems[][5])
{
  uint8_t i, j;
  for(i = 0; i < 5; i++){
    for(j = 0; j < 5; j++){
      showGem(gems[i][j], i * 9 + 3, j * 12 + 25);
    }
  }
}

// Swaps 2 gems
void swapGems(uint8_t *gem1, uint8_t *gem2)
{
  uint8_t aux;
  
  aux = *gem1;
  *gem1 = *gem2;
  *gem2 = aux;
}

// Fills gaps with gems
void fillGems(uint8_t gems[][5])
{
  uint8_t i, j;

  for(i = 0; i < 5; i++){
    for(j = 0; j < 5; j++){
      if(gems[i][j] != 5){
        continue;
      }

      // Fills the grid with random gems
      gems[i][j] = rand() % 5;
    }
  }

  // Checks if there are any moves.
  // If not, redoes the process.
  while(checkIfNoMoves(gems)){
    for(i = 0; i < 5; i++){
      for(j = 0; j < 5; j++){  
        gems[i][j] = rand() % 5;
      }
    }
  }
}
