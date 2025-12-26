// Imported packages from library
#include <dht_nonblocking.h>
#include <SD.h>
#include <SPI.h>

// Initialising pins used on Arduino and global variables in program
#define DHT_SENSOR_TYPE DHT_TYPE_11

static const int DHT_SENSOR_PIN = 2;
int CS_PIN = 10;

DHT_nonblocking dht_sensor(DHT_SENSOR_PIN, DHT_SENSOR_TYPE);
File file;
char filename[] = "data.csv";

/*
 * Initialize the serial port.
 */
void setup() 
{
  Serial.begin(9600);
  
  // Creating data.txt file
  initializeSD();
  createFile(filename);
}

/*
 * Poll for a measurement, keeping the state machine alive.  Returns
 * true if a measurement is available.
 */
static bool measure_environment( float *temperature, float *humidity )
{
  static unsigned long measurement_timestamp = millis( );

  /* Measure once every 10 minutes. */
  if (millis( ) - measurement_timestamp > 600000ul)  // 5000 milliseconds = 5 seconds for testing purposes
  {
    if( dht_sensor.measure( temperature, humidity ) == true )
    {
      measurement_timestamp = millis( );
      return( true );
    }
  }

  return( false );
}

/* Main program for the Automated Greenhouse Monitoring System.
 * Runs until Arduino is removed from power souce.
 */
void loop() 
{
  float temperature;
  float humidity;

  /* Measure temperature and humidity.  If the functions returns
    true, then a measurement is available. */
  if(measure_environment(&temperature, &humidity) == true) 
  {
    // Displays temperature and humidity on the Arduino IDE serial monitor (console)
    Serial.print("Temperature = ");
    Serial.print(temperature, 1);
    Serial.print(" deg. C, Humidity = ");
    Serial.print(humidity, 1);
    Serial.println("%");

    // Writes the data into the "data.csv" file
    writeToFile(temperature, humidity, filename);
  }
}

/*
 * Function responsible for initializing the SD card.
 * Also checks if it was successfully initialized.
 */
void initializeSD()
{
  Serial.println("Initializing SD card...");
  pinMode(CS_PIN, OUTPUT);

  if(SD.begin())
  {
    Serial.println("SD card is ready to use.");
  } 
  else
  {
    Serial.println("SD card initialization failed");
    return;
  }
}

/*
 * Function responsible for creating the "data.csv" file.
 * Also checks if it was successfully created.
 */
int createFile(char filename[]) 
{
  // Check if the file already exists
  if (SD.exists(filename)) 
  {
    Serial.println("File already exists.");
    return 1;
  }

  // Opens the "data.csv" file in SD card for writing (create it if it doesn't exist)
  file = SD.open(filename, FILE_WRITE);

  if (file) 
  {
    // Write the header for the first time the file is created
    file.println("meantemp,humidity");  // Header line

    Serial.println("File created successfully.");
    return 1;
  } 
  else 
  {
    Serial.println("Error while creating file.");
    return 0;
  }
}

/*
 * Function responsible for writting the data logged into "data.csv" file.
 * Also checks if it was successfully written.
 */
int writeToFile(float temperature, float humidity, char name[])
{
  file = SD.open(name, FILE_WRITE);
  if(file)
  {
    // Data written on "data.csv" file using the following format
    file.print(temperature);
    file.print(",");
    file.println(humidity);
    file.close();

    // Confirmation prompt on serial monitor (console)
    Serial.println("Data logged.");
    return 1;
  } 
  else
  {
    Serial.println("Couldn't write to file");
    return 0;
  }
}