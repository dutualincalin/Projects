#include <Wire.h>
#include <SPI.h>
#include <Adafruit_BMP280.h>
#include <PicoMQTT.h>

#if __has_include("config.h")
#include "config.h"
#endif

#ifndef WIFI_SSID
#define WIFI_SSID "Pixel 6 PRO"
#endif

#ifndef WIFI_PASSWORD
#define WIFI_PASSWORD "PixelCalin"
#endif

#define BMP_SCK  (13)
#define BMP_MISO (12)
#define BMP_MOSI (11)
#define BMP_CS   (10)

// MQTT variables
PicoMQTT::Client mqtt;
bool mqtt_connected;

Adafruit_BMP280 bmp;

String ipToString(IPAddress ip) {
  return String(ip[0]) + "." + String(ip[1]) + "." + String(ip[2]) + "." + String(ip[3]);
}

void setup() {
  Serial.begin(115200);

  while ( !Serial ) {
    delay(100);
  }

  Serial.println(F("BMP280 test"));
  unsigned status;
  status = bmp.begin();
  if (!status) {
    Serial.println(F("Could not find a valid BMP280 sensor, check wiring or "
                      "try a different address!"));
    Serial.print("SensorID was: 0x"); Serial.println(bmp.sensorID(),16);
    Serial.print("        ID of 0xFF probably means a bad address, a BMP 180 or BMP 085\n");
    Serial.print("   ID of 0x56-0x58 represents a BMP 280,\n");
    Serial.print("        ID of 0x60 represents a BME 280.\n");
    Serial.print("        ID of 0x61 represents a BME 680.\n");
    while (1) delay(10);
  }

  /* Default settings from datasheet. */
  bmp.setSampling(Adafruit_BMP280::MODE_NORMAL,     /* Operating Mode. */
                  Adafruit_BMP280::SAMPLING_X2,     /* Temp. oversampling */
                  Adafruit_BMP280::SAMPLING_X16,    /* Pressure oversampling */
                  Adafruit_BMP280::FILTER_X16,      /* Filtering. */
                  Adafruit_BMP280::STANDBY_MS_500); /* Standby time. */

  // Wi-Fi connection
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

   Serial.printf("\nWiFi connected. IP: %s\n", WiFi.localIP().toString().c_str());
  
  // MQTT
  mqtt_connected = false;
  IPAddress mqttBrokerIp = WiFi.localIP();
  mqttBrokerIp[3] = 250;
  Serial.printf("\nComputed Broker IP: %s\n", mqttBrokerIp.toString().c_str());
  mqtt.host = ipToString(mqttBrokerIp);
  mqtt.client_id = "PressureModule";

  mqtt.connected_callback = [] {
    mqtt_connected = true;
    Serial.println("MQTT connected");
  };

  mqtt.connection_failure_callback = [] {
    Serial.println("Failed to connect. Attempting to reconnect");
  };

  mqtt.begin();
}

void loop() {
  mqtt.loop();

  if(mqtt_connected) {
    String message = String(bmp.readTemperature());
    Serial.printf("[MQTT] MSAMA/tire_temperature: %s\n", message.c_str());
    mqtt.publish("MSAMA/tire_temperature", message);
    
    delay(1500);

    message = String(bmp.readPressure() * 0.00014503773773);
    Serial.printf("[MQTT] MSAMA/tire_pressure: %s\n", message.c_str());
    mqtt.publish("MSAMA/tire_pressure", message);

    delay(1500);

    message = String(bmp.readAltitude(1013.25));
    Serial.printf("[MQTT] MSAMA/altitude: %s\n", message.c_str());
    mqtt.publish("MSAMA/altitude", message);

    delay(2000);
  }
}
