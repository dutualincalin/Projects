#include <PicoMQTT.h>
#include <regex>

#define COMM_SERIAL Serial2

#if __has_include("config.h")
#include "config.h"
#endif

#ifndef WIFI_SSID
#define WIFI_SSID "Pixel 6 PRO"
#endif

#ifndef WIFI_PASSWORD
#define WIFI_PASSWORD "PixelCalin"
#endif

#define BUTTON_PIN 4

#define DEBOUNCE_DELAY 50

#define pattern "[0-9]+\\.{1}[0-9]{2}"


bool initializationComplete = false;

// Processing data
String front_proxy_distance = "";
String back_proxy_distance = "";
String co_emmisions = "";
String tire_pressure = "";
String tire_temperature = "";
String altitude = "";
String inside_temperature = "";
String camera_connection = "";
volatile int gear_mode = 0;

// Firebase variables
bool firebaseReady = false;
unsigned long firebaseDelay = 5000;
unsigned long lastFirebaseRequestTime = 0;

// Button debouncer variables
unsigned long lastDebounceTime = 0;
int lastSteadyState = LOW;
int lastFlickerableState = LOW;
int currentState;

// MQTT broker custom implementation
class MyMQTTBroker : public PicoMQTT::Server {
public:
  using PicoMQTT::Server::Server;

protected:
  virtual void on_connected(const char * client_id) {
    Serial.print("New client connected: ");
    Serial.println(client_id);

    if(strcmp(client_id, "FirebaseModule") == 0) {
      firebaseReady = true;
    }
  }

  virtual void on_disconnected(const char * client_id) {
    Serial.print("Client Disconnected: ");
    Serial.println(client_id);

    if(strcmp(client_id, "FirebaseModule") == 0) {
        firebaseReady = false;
    }

    if(strcmp(client_id, "ProximityModule") == 0) {
      front_proxy_distance = "";
      back_proxy_distance = "";
      return;
    }

    if(strcmp(client_id, "PressureModule") == 0) {
      tire_pressure = "";
      tire_temperature = "";
      altitude = "";
      return;
    }

    if(strcmp(client_id, "PollutionModule") == 0) {
      co_emmisions = "";
      return;
    }

    if(strcmp(client_id, "CameraModule") == 0) {
      camera_connection = "";
      return;
    }
  }
};

MyMQTTBroker mqtt;

void setup() {
    // Setup serial
    Serial.begin(115200);
    COMM_SERIAL.begin(9600);

    // Button
    pinMode(BUTTON_PIN, INPUT_PULLUP);

    // Wifi
    Serial.printf("Connecting to WiFi %s\n", WIFI_SSID);
    WiFi.mode(WIFI_STA);
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

    while (WiFi.status() != WL_CONNECTED) { 
      delay(1000);
      Serial.print(".");
    }

    Serial.print("\n");

    IPAddress staticIp = WiFi.localIP();
    IPAddress gateway = WiFi.gatewayIP();
    IPAddress subnet = WiFi.subnetMask();
    staticIp[3] = 250;

    WiFi.disconnect();
    WiFi.config(staticIp, WiFi.gatewayIP(), WiFi.subnetMask());
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    while (WiFi.status() != WL_CONNECTED) { 
      delay(1000);
      Serial.print(".");
    }

    Serial.printf("WiFi connected, IP: %s\n", WiFi.localIP().toString().c_str());

    // MQTT
    mqtt.subscribe("MSAMA/co_emmisions", [](const char * topic, const char * payload) {
        Serial.printf("[Receive | '%s']: %s\n", topic, payload);
        co_emmisions = String(payload);
    });

    mqtt.subscribe("MSAMA/tire_pressure", [](const char * topic, const char * payload) {
        Serial.printf("[Receive | '%s']: %s\n", topic, payload);
        tire_pressure = String(payload);
    });

    mqtt.subscribe("MSAMA/tire_temperature", [](const char * topic, const char * payload) {
        Serial.printf("[Receive | '%s']: %s\n", topic, payload);
        tire_temperature = String(payload);
    });

    mqtt.subscribe("MSAMA/altitude", [](const char * topic, const char * payload) {
        Serial.printf("[Receive | '%s']: %s\n", topic, payload);
        altitude = String(payload);
    });

    mqtt.subscribe("MSAMA/front_proximity", [](const char * topic, const char * payload) {
        Serial.printf("[Receive | '%s']: %s\n", topic, payload);
        front_proxy_distance = String(payload);
    });

    mqtt.subscribe("MSAMA/back_proximity", [](const char * topic, const char * payload) {
        Serial.printf("[Receive | '%s']: %s\n", topic, payload);
        back_proxy_distance = String(payload);
    });

    mqtt.subscribe("MSAMA/inside_temperature", [](const char * topic, const char * payload) {
        Serial.printf("[Receive | '%s']: %s\n", topic, payload);
        inside_temperature = String(payload);
    });

    mqtt.subscribe("MSAMA/camera_connection", [](const char * topic, const char * payload) {
        Serial.printf("[Receive | '%s']: %s\n", topic, payload);
        camera_connection = String(payload);
    });

    mqtt.socket_timeout_millis = 5000;
    mqtt.keep_alive_tolerance_millis = 10000;

    mqtt.begin();
    initializationComplete = true;
}

void loop() {
  mqtt.loop();

  currentState = digitalRead(BUTTON_PIN);

  // Button press implemented with debounceer
  if (currentState != lastFlickerableState) {
    lastDebounceTime = millis();
    lastFlickerableState = currentState;
  }

  if ((millis() - lastDebounceTime) > DEBOUNCE_DELAY) {
    if(lastSteadyState == HIGH && currentState == LOW) {
      gear_mode = (gear_mode + 1) % 2;
      String message = String(gear_mode);
      Serial.printf("[Send | MSAMA/gear_mode]: %s\n", message);
      mqtt.publish("MSAMA/gear_mode", message);
    }

    lastSteadyState = currentState;
  }

  // UART communication with TemperatureModule
  if (COMM_SERIAL.available() > 0 && initializationComplete) {
    String arduinoData = COMM_SERIAL.readStringUntil('\n');
    Serial.println(arduinoData);
    std::string arduinoRegexData = std::string(arduinoData.c_str());
    std::smatch match;

    if (regex_search(arduinoRegexData, match, std::regex(pattern))) {
      Serial.printf("Received inside_temperature data from Arduino: %s\n", arduinoData);
      inside_temperature = arduinoData;
    }
  }

  // Firebase
  if(gear_mode == 1) {
    firebaseDelay = 250;
  } else {
    firebaseDelay = 5000;
  }

  if(firebaseReady && millis() - lastFirebaseRequestTime > firebaseDelay) {
    String message = "{\"front_proxy_distance\": \"" + front_proxy_distance + "\", ";
    message += "\"back_proxy_distance\": \"" + back_proxy_distance + "\", ";
    message += "\"gear_mode\": \"" + String(gear_mode) + "\", ";
    message += "\"co_emmisions\": \"" + co_emmisions + "\", ";
    message += "\"tire_pressure\": \"" + tire_pressure + "\", ";
    message += "\"tire_temperature\": \"" + tire_temperature + "\", ";
    message += "\"altitude\": \"" + altitude + "\", ";
    message += "\"inside_temperature\": \"" + inside_temperature + "\", ";
    message += "\"camera_connection\": \"" + camera_connection + "\"}";
    
    Serial.printf("Publishing message in topic MSAMA/firebase\n");
    mqtt.publish("MSAMA/firebase", message);
    
    lastFirebaseRequestTime = millis();
  }
}
