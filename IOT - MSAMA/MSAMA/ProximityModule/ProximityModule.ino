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

#define BACK_ECHO_PIN 25
#define BACK_TRIG_PIN 26

#define FRONT_ECHO_PIN 18
#define FRONT_TRIG_PIN 5

#define SOUND_SPEED 0.034

long duration;
float distance;

// MQTT variables
PicoMQTT::Client mqtt;
bool mqtt_connected;
int gear_mode = 0;

String ipToString(IPAddress ip) {
  return String(ip[0]) + "." + String(ip[1]) + "." + String(ip[2]) + "." + String(ip[3]);
}

void setup() {
  Serial.begin(115200); // Starts the serial communication
  pinMode(FRONT_TRIG_PIN, OUTPUT); // Sets the trigPin as an Output
  pinMode(FRONT_ECHO_PIN, INPUT); // Sets the echoPin as an Input

  pinMode(BACK_TRIG_PIN, OUTPUT); // Sets the trigPin as an Output
  pinMode(BACK_ECHO_PIN, INPUT); // Sets the echoPin as an Input

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
  mqtt.client_id = "ProximityModule";

  mqtt.subscribe("MSAMA/gear_mode", [](const char * topic, const char * payload) {
      Serial.printf("Received message in topic '%s': %s\n", topic, payload);
      gear_mode = atoi(payload);
  });

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

  if(mqtt_connected && gear_mode == 1) {
    digitalWrite(FRONT_TRIG_PIN, LOW);
    delayMicroseconds(2);

    digitalWrite(FRONT_TRIG_PIN, HIGH);
    delayMicroseconds(10);
    digitalWrite(FRONT_TRIG_PIN, LOW);

    duration = pulseIn(FRONT_ECHO_PIN, HIGH);
    distance = duration * SOUND_SPEED/2;
    String message = String(distance);

    Serial.printf("[MQTT] MSAMA/front_proximity: %s\n", message.c_str());
    mqtt.publish("MSAMA/back_proximity", message);
    

    digitalWrite(BACK_TRIG_PIN, LOW);
    delayMicroseconds(2);

    digitalWrite(BACK_TRIG_PIN, HIGH);
    delayMicroseconds(10);
    digitalWrite(BACK_TRIG_PIN, LOW);

    duration = pulseIn(BACK_ECHO_PIN, HIGH);
    distance = duration * SOUND_SPEED/2;
    message = String(distance);

    Serial.printf("[MQTT] MSAMA/back_proximity: %s\n", message.c_str());
    mqtt.publish("MSAMA/front_proximity", message);
    
    delay(750);
  }
}