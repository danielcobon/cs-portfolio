# Import relevant libraries
library(tidyverse)
library(dslabs)

library(caret)

# Read "exams.csv" from Rstudio directory
exams <- read_csv(file = "exams.csv")
str(exams)

# Remove columns 2 to 4 from the data set
exams <-  exams[, -2:-4]
str(exams)

# Convert the class variable to a factor (male/female) 
exams$gender <- as.factor(exams$gender)
str(exams)

# Convert the 'Test preparation course' values to numerical
exams$`test preparation course`<- as.numeric(as.factor(exams$`test preparation course`))
str(exams)

# Removes rows with NA values and then checks for any null values
exams <- na.omit(exams)
any(is.na(exams))

# View final processed data set
View(exams)

#split to training and testing 
set.seed(1) # For reproducibility

# Old data partition code
# train_indices <- sample(1:nrow(exams), 0.7 * nrow(exams))

# Splits data distribution (gender) evenly for training and testing
train_indices <- exams$gender %>% 
  createDataPartition(p = 0.7, list = FALSE)
train_data <- exams[train_indices, ]
test_data <- exams[-train_indices, ]

print(train_data)
print(test_data)