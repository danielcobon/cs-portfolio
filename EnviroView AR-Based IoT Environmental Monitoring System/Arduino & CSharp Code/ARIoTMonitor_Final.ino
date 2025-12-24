// Auth Token and relevant address for the sensor data on my Blynk Cloud
#define BLYNK_TEMPLATE_ID "TMPL53IDnXyhr"
#define BLYNK_TEMPLATE_NAME "AR IoT Monitor"
#define BLYNK_AUTH_TOKEN "7kiBV0gu6iDtuDJB4YhsvqkjBfPYyOfy"
#define BLYNK_PRINT Serial

// Imported Libraries
#include <WiFi.h>
#include <WiFiClient.h>
#include <BlynkSimpleEsp32.h>
#include <DFRobot_DHT11.h>
#include "SR04.h"

// WiFi credentials (Will change depending on WiFi)
const char* ssid     = "VM6285908";
const char* password = "sh3kdWfrVyws";

// DHT11 Setup
DFRobot_DHT11 DHT;
#define DHT11_PIN 21

// SR04 Setup
#define TRIG_PIN 2
#define ECHO_PIN 4
SR04 sr04 = SR04(ECHO_PIN, TRIG_PIN);
long initialDepth = 0;

char auth[] = BLYNK_AUTH_TOKEN;
BlynkTimer timer;

void sendSensor()
{
  // Read DHT11
  DHT.read(DHT11_PIN);
  float t = DHT.temperature;
  float h = DHT.humidity;

  // Send to Blynk
  Blynk.virtualWrite(V0, t); // Temperature
  Blynk.virtualWrite(V1, h); // Humidity

  // Print locally
  Serial.print("Temperature : ");
  Serial.println(t);
  Serial.print("Humidity : ");
  Serial.println(h);

  // Read Ultrasonic Sensor
  long currentDepth = sr04.Distance();
  long depthDifference = initialDepth - currentDepth;
  float waterLevelPercent = ((float)depthDifference / initialDepth) * 100.0;

  // Constrain the % between 0 and 100
  waterLevelPercent = constrain(waterLevelPercent, 0, 100);

  // Send to Blynk
  Blynk.virtualWrite(V2, waterLevelPercent);

  // Print locally
  Serial.print("Water Level : ");
  Serial.print(waterLevelPercent);
  Serial.println("%");
}

void setup()
{   
  Serial.begin(115200);
  
  // Connect to WiFi and Blynk
  Blynk.begin(auth, ssid, password);

  // Wait for stable readings
  delay(2000);

  // Capture the initial depth as reference
  initialDepth = sr04.Distance();
  Serial.print("Initial Depth Recorded: ");
  Serial.print(initialDepth);
  Serial.println(" cm");

  // Set up repeating sensor sending (reads data ince every 3 seconds)
  timer.setInterval(3000L, sendSensor);
}

void loop()
{
  Blynk.run();
  timer.run();
}
