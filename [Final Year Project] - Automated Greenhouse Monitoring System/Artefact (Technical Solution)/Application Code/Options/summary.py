"""

@author: Daniel
"""

import sys
import os
import numpy as np
import pyqtgraph as pg
from PyQt5.QtWidgets import QApplication, QWidget, QPushButton, QVBoxLayout, QHBoxLayout, QSpacerItem, QSizePolicy, QLabel, QFrame
from PyQt5.QtCore import Qt  # Import Qt for alignment

import csv
from PyQt5.QtWidgets import QLabel

class climateSummary(QWidget):
    def __init__(self):
        super().__init__()

        # Get screen's resolution (width and height)
        screen_geometry = QApplication.desktop().availableGeometry()

        # Calculate center position
        x = (screen_geometry.width() - self.width()) // 2
        y = (screen_geometry.height() - self.height()) // 2

        # Set up the window
        self.setWindowTitle("Climate Summary")
        self.setGeometry(x, y, 600, 500)

        # Lock the window size (prevent resizing)
        self.setFixedSize(self.size())

        # Initialize temperature unit (Celsius by default)
        self.is_celsius = True

        # Create labels to display the statistics
        self.avg_temp_label = QLabel("Average Temperature: --", self)
        self.avg_humidity_label = QLabel("Average Humidity: --", self)

        # Title for average values
        self.avg_title_label = QLabel("Average Temperature and Humidity Values", self)
        self.avg_title_label.setAlignment(Qt.AlignCenter)

        # First separator
        separator1 = QFrame(self)
        separator1.setFrameShape(QFrame.HLine)
        separator1.setFrameShadow(QFrame.Sunken)

        self.high_temp_label = QLabel("Highest Temperature: --", self)
        self.high_humidity_label = QLabel("Highest Humidity: --", self)

        # Title for highest values
        self.high_title_label = QLabel("Highest Temperature and Humidity Values", self)
        self.high_title_label.setAlignment(Qt.AlignCenter)

        # Second separator
        separator2 = QFrame(self)
        separator2.setFrameShape(QFrame.HLine)
        separator2.setFrameShadow(QFrame.Sunken)

        self.low_temp_label = QLabel("Lowest Temperature: --", self)
        self.low_humidity_label = QLabel("Lowest Humidity: --", self)

        # Title for lowest values
        self.low_title_label = QLabel("Lowest Temperature and Humidity Values", self)
        self.low_title_label.setAlignment(Qt.AlignCenter)

        # Create exit button
        self.exit_button = QPushButton("Exit", self)
        self.exit_button.setFixedSize(100, 30)
        self.exit_button.clicked.connect(self.close)

        # Create conversion button (Celsius/Fahrenheit toggle)
        self.convert_button = QPushButton("Convert to Fahrenheit", self)
        self.convert_button.setFixedSize(200, 30)
        self.convert_button.clicked.connect(self.convertTemperature)

        # Layout
        main_layout = QVBoxLayout()
        stacked_layout = QVBoxLayout()
        bottom_layout = QHBoxLayout()

        # Add widgets to layout
        stacked_layout.addWidget(self.avg_title_label)
        stacked_layout.addWidget(self.avg_temp_label)
        stacked_layout.addWidget(self.avg_humidity_label)

        stacked_layout.addWidget(separator1)  # First separator

        stacked_layout.addWidget(self.high_title_label)
        stacked_layout.addWidget(self.high_temp_label)
        stacked_layout.addWidget(self.high_humidity_label)

        stacked_layout.addWidget(separator2)  # Second separator

        stacked_layout.addWidget(self.low_title_label)
        stacked_layout.addWidget(self.low_temp_label)
        stacked_layout.addWidget(self.low_humidity_label)

        # Bottom layout with the exit and convert buttons
        bottom_layout.addWidget(self.convert_button, alignment=Qt.AlignLeft)
        bottom_layout.addWidget(self.exit_button, alignment=Qt.AlignRight)

        # Add layouts to the main layout
        main_layout.addLayout(stacked_layout)
        main_layout.addLayout(bottom_layout)

        # Set the layout of the window
        self.setLayout(main_layout)

        # Call readData() after setting up the GUI
        self.readData()

    # Reads temperature and humidity data from DATA.csv
    def readData(self):
        temperature_data = []
        humidity_data = []

        # Move up from Options/ and then go to Data/ directory to read DATA.csv
        file_path = os.path.join(os.path.dirname(__file__), "..", "Data", "DATA.csv")

        try:
            with open(file_path, mode='r') as file:
                csv_reader = csv.reader(file)

                for row in csv_reader:
                    try:
                        temperature = float(row[0])  # First column: Temperature
                        humidity = float(row[1])  # Second column: Humidity

                        temperature_data.append(temperature)
                        humidity_data.append(humidity)

                    except ValueError:
                        continue  # Skip rows with invalid data

            # Update GUI labels
            if temperature_data and humidity_data:
                self.temperature_data = temperature_data
                self.humidity_data = humidity_data

                self.updateLabels()
            else:
                self.showError("No valid data found!")

        except FileNotFoundError:
            self.showError("DATA.csv not found!")

    def updateLabels(self):
        # Update the labels with the calculated statistics
        temperature_data = self.temperature_data
        humidity_data = self.humidity_data

        # Calculate statistics
        avg_temp = sum(temperature_data) / len(temperature_data)
        avg_humidity = sum(humidity_data) / len(humidity_data)
        highest_temp = max(temperature_data)
        lowest_temp = min(temperature_data)
        highest_humidity = max(humidity_data)
        lowest_humidity = min(humidity_data)

        # Update the labels with the current temperature unit (Celsius or Fahrenheit)
        if self.is_celsius:
            self.avg_temp_label.setText(f"Average Temperature: {avg_temp:.2f}°C")
            self.high_temp_label.setText(f"Highest Temperature: {highest_temp:.2f}°C")
            self.low_temp_label.setText(f"Lowest Temperature: {lowest_temp:.2f}°C")
        else:
            # Convert to Fahrenheit
            avg_temp = self.celsiusToFahrenheit(avg_temp)
            highest_temp = self.celsiusToFahrenheit(highest_temp)
            lowest_temp = self.celsiusToFahrenheit(lowest_temp)

            self.avg_temp_label.setText(f"Average Temperature: {avg_temp:.2f}°F")
            self.high_temp_label.setText(f"Highest Temperature: {highest_temp:.2f}°F")
            self.low_temp_label.setText(f"Lowest Temperature: {lowest_temp:.2f}°F")

        self.avg_humidity_label.setText(f"Average Humidity: {avg_humidity:.2f}%")
        self.high_humidity_label.setText(f"Highest Humidity: {highest_humidity:.2f}%")
        self.low_humidity_label.setText(f"Lowest Humidity: {lowest_humidity:.2f}%")

    def convertTemperature(self):
        # Toggle between Celsius and Fahrenheit
        self.is_celsius = not self.is_celsius

        # Update the button text based on the current unit
        if self.is_celsius:
            self.convert_button.setText("Convert to Fahrenheit")
        else:
            self.convert_button.setText("Convert to Celsius")

        # Recalculate and update labels
        self.updateLabels()

    # Calculation formula to convert Celsius into Fahrenheit
    def celsiusToFahrenheit(self, celsius):
        return (celsius * 9/5) + 32

    # Customizable error message to be displayed when error catched
    # Updates all labels with an error message
    def showError(self, message):
        self.avg_temp_label.setText(f"Error: {message}")
        self.avg_humidity_label.setText(f"Error: {message}")
        self.high_temp_label.setText(f"Error: {message}")
        self.low_temp_label.setText(f"Error: {message}")
        self.high_humidity_label.setText(f"Error: {message}")
        self.low_humidity_label.setText(f"Error: {message}")
