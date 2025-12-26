import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error, mean_absolute_error
from xgboost import XGBRegressor
# Model evaluation
from sklearn.metrics import r2_score
import seaborn as sns
import matplotlib.pyplot as plt
import os

# Different Machine Learning models for testing
from sklearn.linear_model import LinearRegression
from sklearn.ensemble import RandomForestRegressor

# Get the directory of the current script
current_directory = os.path.dirname(__file__)

# Build the full file path to the CSV file
file_path = os.path.join(current_directory, 'DailyDelhiClimateTrain.csv')

# Importing data using the correct file path
df = pd.read_csv(file_path)

# Select relevant columns
X = df[['meantemp', 'humidity']]  # feature variables
y = df['meanpressure']  # target variable

# Train-test split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=101)

# Initialize XGBoost Regressor
model = XGBRegressor()

# Fit the model
model.fit(X_train, y_train)

# Make predictions
predictions = model.predict(X_test)

# Model evaluation
print('Mean Squared Error: ', mean_squared_error(y_test, predictions))
print('Mean Absolute Error: ', mean_absolute_error(y_test, predictions))

# R-squared (R²)
r_squared = model.score(X_test, y_test)
print('R-squared: ', r_squared)

# Create a DataFrame with the original data and predictions
df_predictions = pd.DataFrame({
    'Mean Temperature': X_test['meantemp'],
    'Humidity': X_test['humidity'],
    'Actual Pressure': y_test,
    'Predicted Pressure': predictions
})

# Define the file path relative to the script's location
file_path = os.path.join(os.path.dirname(__file__), 'predictions_output.csv')

# Overwrite the predictions CSV file
df_predictions.to_csv(file_path, index=False)

# Print the file location
print(f"File saved as '{file_path}'")
print(f"File location: {os.path.abspath(file_path)}")