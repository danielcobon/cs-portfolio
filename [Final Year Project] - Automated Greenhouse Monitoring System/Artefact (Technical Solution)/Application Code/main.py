"""

@author: Daniel
"""

from Options.summary import climateSummary
from Options.forecast import pressureForecast

import sys
import os
import numpy as np
import pyqtgraph as pg
from PyQt5.QtWidgets import QApplication, QWidget, QPushButton, QVBoxLayout, QHBoxLayout, QSpacerItem, QSizePolicy
from PyQt5.QtCore import Qt  # Import Qt for alignment

class mainMenu(QWidget):
    def __init__(self):
        super().__init__()

        # Initialize climate_window as None (Climate Summary Button)
        self.climate_window = None 
        self.forecast_window = None  # Initialize forecast window

        # Get screen's resolution (width and height)
        screen_geometry = QApplication.desktop().availableGeometry()

        # Calculate center position
        x = (screen_geometry.width() - self.width()) // 2
        y = (screen_geometry.height() - self.height()) // 2

        # Set up the window
        self.setWindowTitle("Automated Greenhouse Monitoring System")
        # Set the window's geometry to be centered
        self.setGeometry(x, y, 600, 500)

        # Lock the window size (prevent resizing)
        self.setFixedSize(self.size())  # Lock the window size

        # Create buttons
        self.button1 = QPushButton("Climate Summary", self)
        self.button2 = QPushButton("Pressure Forecast", self)
        self.exit_button = QPushButton("Exit", self)
        self.exit_button.setFixedSize(100, 30)  # Smaller exit button

        # Connect buttons to functions
        self.button1.clicked.connect(self.open_climate_summary)
        self.button2.clicked.connect(self.open_pressure_forecast)
        self.exit_button.clicked.connect(self.close)

        # Layout
        main_layout = QVBoxLayout()
        stacked_layout = QVBoxLayout()
        bottom_layout = QHBoxLayout()

        # Add buttons to the stacked layout
        stacked_layout.addWidget(self.button1)
        stacked_layout.addWidget(self.button2)

        # Bottom layout with the last button
        bottom_layout.addWidget(self.exit_button, alignment=Qt.AlignRight)

        # Add the stacked layout and bottom layout to the main layout
        main_layout.addLayout(stacked_layout)
        main_layout.addLayout(bottom_layout)

        # Set the layout of the window
        self.setLayout(main_layout)

    # Opens the Climate Summary window
    def open_climate_summary(self):
        if self.climate_window is None:
            self.climate_window = climateSummary()  
        self.climate_window.show()

    # Opens the Pressure Forecast window
    def open_pressure_forecast(self):
        if self.forecast_window is None:
            self.forecast_window = pressureForecast() 
        self.forecast_window.show()       

# Run the PyQt application
if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = mainMenu()
    window.show()
    sys.exit(app.exec_())
