#include <MQ7.h>
#include <WiFi.h>
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
/*	
	MQ7_Example.ino

	Example sketch for MQ7 carbon monoxide detector.
		- connect analog input 
		- set A_PIN to the relevant pin
		- connect device ground to GND pin 
		- device VC to 5.0 volts

	Created by Fergus Baker
	22 August 2020
	Released into the public domain.
*/

#define A_PIN 36
#define VOLTAGE 5

// MQ7 variables
MQ7 mq7(A_PIN, VOLTAGE);
float ppm;

// MQTT variables
bool mqtt_connected;
PicoMQTT::Client mqtt;

String ipToString(IPAddress ip) {
  return String(ip[0]) + "." + String(ip[1]) + "." + String(ip[2]) + "." + String(ip[3]);
}

void setup() {
  // Serial
	Serial.begin(115200);

	while (!Serial) {
		;
	}

	Serial.println("");

	Serial.println("Calibrating MQ7");
	mq7.calibrate();
	Serial.println("Calibration done!");

  // Wi-Fi
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
  mqtt.client_id = "PollutionModule";

  mqtt.connected_callback = [] {
    mqtt_connected = true;
    Serial.println("MQTT connected");
  };

  mqtt.connection_failure_callback = [] {
    Serial.println("Failed to connect. Attemtping to reconnect");
  };

  mqtt.begin();
}
 
void loop() {
  mqtt.loop();
  
  if(mqtt_connected) {
    String message = String(mq7.readPpm());
    Serial.printf("[MQTT] MSAMA/co_emmisions: %s\n", message.c_str());
    mqtt.publish("MSAMA/co_emmisions", message);
    
    delay(5000);
  }
}
