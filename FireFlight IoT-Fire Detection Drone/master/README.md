    Contents:
    - Azure: Python scripts responsible for cloud interaction, including fire detection through Azure Custom Vision and telemetry reporting to IoT Central. Provides the DroneController, which integrates drone control, sensor data processing, fire confirmation, and cloud communication.
    - dht11_temp_humid: Python script for the Raspberry Pi that reads temperature and humidity data from a DHT11 sensor, used by the main script for fire-detection logic and cloud processing. The final implementation is in "dht11_final.py", the other scripts are prototypes.
    - motor: Python scripts for an optional servo motor implementation, not currently used in "master_script.py".
    - mq7_gas: Python scripts for the Raspberry Pi that read CO concentration to determine air safety. The final implementation is in "mq_7_final.py", the other scripts are prototypes.
    - master_script.py: Main script that coordinates the general fire detection logic. It reads temperature and humidity from the DHT11 sensor, monitors CO levels with the MQ-7 sensor, and uses the DroneController for fire confirmation and cloud telemetry.

    Note: For the python script "drone_fire_detection.py" in the Azure directory, Azure credentials are provided via environment variables and are not stored in the repository for security purposes.