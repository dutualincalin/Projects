#include <PicoMQTT.h>
#include <Firebase_ESP_Client.h>
#include "addons/TokenHelper.h"
#include "addons/RTDBHelper.h"

#if __has_include("config.h")
#include "config.h"
#endif

#ifndef WIFI_SSID
#define WIFI_SSID "Pixel 6 PRO"
#endif

#ifndef WIFI_PASSWORD
#define WIFI_PASSWORD "PixelCalin"
#endif

// Insert Firebase API Key and Database URL
#define API_KEY "AIzaSyBnp0pmGpRj07Fb8Wn64Q6Ea9RfzD6WQkA"
#define DATABASE_URL "https://msama-us-default-rtdb.firebaseio.com/"

// Firebase Variables
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;
bool signupOK = false;

// MQTT
PicoMQTT::Client mqtt;

String ipToString(IPAddress ip) {
  return String(ip[0]) + "." + String(ip[1]) + "." + String(ip[2]) + "." + String(ip[3]);
}

void setup() {
  Serial.begin(115200);

  // Wifi
  Serial.printf("Connecting to WiFi %s\n", WIFI_SSID);
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  while (WiFi.status() != WL_CONNECTED) { 
    delay(1000);
    Serial.print(".");
  }

  Serial.printf("\nWiFi connected. IP: %s\n", WiFi.localIP().toString().c_str());

  // Firebase
  config.api_key = API_KEY;
  config.database_url = DATABASE_URL;
 
  if (Firebase.signUp(&config, &auth, "", "")){
    Serial.println("ok");
    signupOK = true;
  }
  else{
    Serial.println("Nok");
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }
 
  config.token_status_callback = tokenStatusCallback;
 
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);

  // MQTT
  IPAddress mqttBrokerIp = WiFi.localIP();
  mqttBrokerIp[3] = 250;
  Serial.printf("\nComputed Broker IP: %s\n", mqttBrokerIp.toString().c_str());
  mqtt.host = ipToString(mqttBrokerIp);
  mqtt.client_id = "FirebaseModule";

  mqtt.subscribe("MSAMA/firebase", [](const char * topic, const char * payload) {
    Serial.printf("Received message in topic %s: %s\n", topic, payload);
    
    if (Firebase.ready() && signupOK) {
      FirebaseJson firebaseJson;
      firebaseJson.setJsonData(payload);
      if (Firebase.RTDB.setJSON(&fbdo, "MSAMA", &firebaseJson)) {
        Serial.println("PASSED");
        Serial.println("PATH: " + fbdo.dataPath());
        Serial.println("TYPE: " + fbdo.dataType());
      } else {
        Serial.println("FAILED");
        Serial.println("REASON: " + fbdo.errorReason());
      }
    }
  });

  mqtt.connected_callback = [] {
    Serial.println("MQTT connected");
  };

  mqtt.connection_failure_callback = [] {
    Serial.println("Failed to connect. Attempting to reconnect");
  };

  mqtt.begin();
}

void loop() {
  mqtt.loop();
}
