import sys
import os
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error, mean_absolute_error
from sklearn.linear_model import LinearRegression
from xgboost import XGBRegressor
from PyQt5.QtWidgets import QApplication, QWidget, QVBoxLayout, QLabel, QFrame, QPushButton, QHBoxLayout
from PyQt5.QtCore import Qt

class pressureForecast(QWidget):
    def __init__(self):
        super().__init__()

        # Get screen's resolution (width and height)
        screen_geometry = QApplication.desktop().availableGeometry()

        # Calculate center position
        x = (screen_geometry.width() - self.width()) // 2
        y = (screen_geometry.height() - self.height()) // 2        

        # Set up the window properties
        self.setWindowTitle("Pressure Forecast")
        # Set the window's geometry to be centered
        self.setGeometry(x, y, 600, 500)
        self.setFixedSize(self.size())

        # Create labels to display the statistics
        self.header_label = QLabel("Accuracy Values According to Training Data", self)
        self.header_label.setAlignment(Qt.AlignCenter)
        self.r2_label = QLabel("R² Score: Calculating...", self)
        self.mse_label = QLabel("Mean Squared Error: Calculating...", self)
        self.mae_label = QLabel("Mean Absolute Error: Calculating...", self)

        # Create a separator line
        separator = QFrame(self)
        separator.setFrameShape(QFrame.HLine)
        separator.setFrameShadow(QFrame.Sunken)

        # Labels for predicted pressure summary
        self.pressure_summary_header = QLabel("Pressure Prediction Summary According to DATA.csv", self)
        self.pressure_summary_header.setAlignment(Qt.AlignCenter)
        self.avg_pressure_label = QLabel("Average Predicted Pressure: --", self)
        self.lowest_pressure_label = QLabel("Lowest Predicted Pressure: --", self)
        self.highest_pressure_label = QLabel("Highest Predicted Pressure: --", self)

        # Exit button
        self.exit_button = QPushButton("Exit", self)
        self.exit_button.setFixedSize(100, 30)
        self.exit_button.clicked.connect(self.close)

        # Buttons to generate graphs
        self.graph_button_temp = QPushButton("Temp-Pressure Graph", self)
        self.graph_button_temp.setFixedSize(200, 30)
        self.graph_button_temp.clicked.connect(self.plot_graph_temp)

        self.graph_button_humidity = QPushButton("Humidity-Pressure Graph", self)
        self.graph_button_humidity.setFixedSize(200, 30)
        self.graph_button_humidity.clicked.connect(self.plot_graph_humidity)

        # Layout setup
        main_layout = QVBoxLayout()
        stats_layout = QVBoxLayout()
        pressure_layout = QVBoxLayout()
        bottom_layout = QHBoxLayout()

        # Add elements to layouts
        stats_layout.addWidget(self.header_label)
        stats_layout.addWidget(self.r2_label)
        stats_layout.addWidget(self.mse_label)
        stats_layout.addWidget(self.mae_label)
        stats_layout.addWidget(separator)

        pressure_layout.addWidget(self.pressure_summary_header)
        pressure_layout.addWidget(self.avg_pressure_label)
        pressure_layout.addWidget(self.lowest_pressure_label)
        pressure_layout.addWidget(self.highest_pressure_label)

        bottom_layout.addWidget(self.graph_button_temp, alignment=Qt.AlignLeft)
        bottom_layout.addWidget(self.graph_button_humidity, alignment=Qt.AlignCenter)
        bottom_layout.addWidget(self.exit_button, alignment=Qt.AlignRight)

        main_layout.addLayout(stats_layout)
        main_layout.addLayout(pressure_layout)
        main_layout.addLayout(bottom_layout)

        self.setLayout(main_layout)
        self.runPrediction()

    # Loads training and test data, trains a machine learning model (XGBoost),
    # makes predictions, and calculates statistical accuracy metrics.
    def runPrediction(self):
        train_file_path = os.path.join(os.path.dirname(__file__), "..", "Data", "DailyDelhiClimateTrain.csv")
        test_file_path = os.path.join(os.path.dirname(__file__), "..", "Data", "DATA.csv")

        try:
            # Load training data
            df_train = pd.read_csv(train_file_path)
            if 'meantemp' not in df_train.columns or 'humidity' not in df_train.columns or 'meanpressure' not in df_train.columns:
                raise ValueError("Training data is missing required columns.")

            X_train = df_train[['meantemp', 'humidity']]
            y_train = df_train['meanpressure']

            # Load test data
            df_test = pd.read_csv(test_file_path)
            if 'meantemp' not in df_test.columns or 'humidity' not in df_test.columns:
                raise ValueError("Test data is missing required columns.")
            
            X_test = df_test[['meantemp', 'humidity']]

            # Train the XGBoost model
            model = XGBRegressor()
            model.fit(X_train, y_train)

            # Make predictions
            self.predictions = model.predict(X_test)
            self.temperatures = X_test['meantemp'].values
            self.humidities = X_test['humidity'].values

            # Split data for evaluation
            X_train_split, X_test_split, y_train_split, y_test_split = train_test_split(X_train, y_train, test_size=0.3, random_state=101)
            model.fit(X_train_split, y_train_split)
            predictions_split = model.predict(X_test_split)

            # Calculate evaluation metrics
            mse = mean_squared_error(y_test_split, predictions_split)
            mae = mean_absolute_error(y_test_split, predictions_split)
            r_squared = model.score(X_test_split, y_test_split)

            # Display metrics in the UI
            self.r2_label.setText(f"R² Score: {r_squared:.4f}")
            self.mse_label.setText(f"Mean Squared Error: {mse:.4f}")
            self.mae_label.setText(f"Mean Absolute Error: {mae:.4f}")

            self.avg_pressure_label.setText(f"Average Predicted Pressure: {np.mean(self.predictions):.2f}")
            self.lowest_pressure_label.setText(f"Lowest Predicted Pressure: {np.min(self.predictions):.2f}")
            self.highest_pressure_label.setText(f"Highest Predicted Pressure: {np.max(self.predictions):.2f}")

        except Exception as e:
            print(f"Error: {e}")

    #Generates a scatter plot of temperature vs. predicted pressure with a best-fit line
    def plot_graph_temp(self):
        plt.scatter(self.temperatures, self.predictions, color='blue', label='Predicted Pressure')
        model = LinearRegression()
        model.fit(self.temperatures.reshape(-1, 1), self.predictions)
        plt.plot(self.temperatures, model.predict(self.temperatures.reshape(-1, 1)), color='red', label='Best Fit Line')
        plt.xlabel("Temperature")
        plt.ylabel("Predicted Pressure")
        plt.title("Temperature vs. Predicted Pressure")
        plt.legend()
        plt.show()

    # Generates a scatter plot of humidity vs. predicted pressure with a best-fit line
    def plot_graph_humidity(self):
        plt.scatter(self.humidities, self.predictions, color='green', label='Predicted Pressure')
        model = LinearRegression()
        model.fit(self.humidities.reshape(-1, 1), self.predictions)
        plt.plot(self.humidities, model.predict(self.humidities.reshape(-1, 1)), color='orange', label='Best Fit Line')
        plt.xlabel("Humidity")
        plt.ylabel("Predicted Pressure")
        plt.title("Humidity vs. Predicted Pressure")
        plt.legend()
        plt.show()