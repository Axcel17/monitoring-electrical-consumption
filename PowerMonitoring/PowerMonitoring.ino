#include <WiFi.h>
#include <FirebaseESP32.h>
#include <stdlib.h>

#define FIREBASE_HOST "controle-0db-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "GNu0WxLRwKrm3-6xjr1QEsqRaFPtzp7gF2hOVQuKr28" // Reemplaza con la clave privada (token) generada en Firebase
#define WIFI_SSID "NETLIFE-ESPINOZA"
//"Xiaomi"
#define WIFI_PASSWORD "0931457345"
//sebas123

FirebaseData firebaseData;

void setup() {
  Serial.begin(115200);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  randomSeed(analogRead(0));
}

void loop() {
  if (Firebase.ready()) {

    // Ejemplo: enviar datos a Firebase cada 5 segundos
    static unsigned long previousMillis = 0;
    const long interval = 5000;

    unsigned long currentMillis = millis();
    if (currentMillis - previousMillis >= interval) {
      previousMillis = currentMillis;

      float SWM1 = (random(51)*1.0/100)+0.5;
      Serial.println(SWM1);
      Firebase.setDouble(firebaseData, "sensors/SWM1/measure", SWM1);
      delay(50);

      float XYZ2 = (random(21)*1.0/100)+3.4;
      Firebase.setDouble(firebaseData, "sensors/XYZ2/measure", XYZ2);
      delay(50); 

      float ABC3 = (random(81)*1.0/100)+12.3;
      Firebase.setDouble(firebaseData, "sensors/ABC3/measure", ABC3);
      delay(1900); 

    }
  } else{
    Firebase.reconnectWiFi(true); 
    Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
    delay(2000); 
  }
}
