#include "DHT.h"
#include <AltSoftSerial.h>

#define DHTPIN 4
#define DHTTYPE DHT11

AltSoftSerial commSerial;

DHT dht(DHTPIN, DHTTYPE);

void setup() {
  Serial.begin(9600);
  commSerial.begin(9600);

  dht.begin();
}

void loop() {
  delay(4000);

  float temperature = dht.readTemperature();

  if (isnan(temperature)) {
    Serial.println(F("Failed to read from DHT sensor!"));
    return;
  }

  commSerial.print(temperature);
  Serial.print("Sent new temperature measurement: ");
  Serial.println(temperature);
}