USE Movies

ALTER AUTHORIZATION ON DATABASE:: Movies TO sa;

-------------------- SQL Server - TSQL Essentials Portfolio --------------------

----- A: TSQL03-ICA Demo - Querying with Select -----
-- Selects all the columns from the dbo.movie database

SELECT *
FROM dbo.movie;

----- B: TSQL04-ICA Demo – Querying with Multiple Tables -----
-- Joins three tables using inner join on movie_id and genre_id to determine the genre(s) 
-- from dbo.genres for each movie title in dbo.movie

SELECT m.title, g.genre_name
FROM dbo.movie as m
INNER JOIN dbo.movie_genres AS mg
ON m.movie_id = mg.movie_id
INNER JOIN dbo.genre AS g
ON mg.genre_id = g.genre_id;

----- C: TSQL05-ICA Demo – Sort and Filtering Data -----
-- Selects all 'Male' actors from the dbo.person database, without duplicates using 'DISTINCT',
-- and orders the names in alphabetical order with 'ORDER BY'

SELECT DISTINCT p.person_name, g.gender
FROM dbo.person AS p
INNER JOIN dbo.movie_cast as mc
ON p.person_id = mc.person_id
INNER JOIN dbo.gender as g
ON mc.gender_id = g.gender_id
WHERE g.gender = 'Male'
ORDER BY p.person_name;

----- D: TSQL06 ICA Demo – Working with SQL Server Data -----
-- List of movies which were released are after the 2000s,
-- where the year which was a string previously, is converted into an int for comparison.
-- When converting date

SELECT m.movie_id, m.title, m.release_date
FROM dbo.movie AS m
WHERE TRY_CONVERT(INT, SUBSTRING(release_date, 1, 4)) > 1999
AND TRY_CONVERT(DATE, release_date) IS NOT NULL
ORDER BY m.release_date;

----- E: TSQL07 ICA Demo – Using DML to Modify Data -----
-- Updates the movie 'Four Rooms' in dbo.movie with a homepage as there was none previously.
-- Inserts a new person 'Daniel Obon' into the dbo.person database.
-- Deletes the person with person_id = 1893240 in dbo.person as they have been an error or inactivity.
-- A transaction and rollback is used in this example to revert the changes made by this query.

UPDATE dbo.movie
SET homepage = 'https://www.miramax.com/movie/four-rooms/'
WHERE title = 'Four Rooms';

INSERT INTO dbo.person(person_id, person_name)
VALUES (1893240, 'Daniel Obon')

DELETE FROM dbo.person
WHERE person_id = 1893240;

SELECT *
FROM dbo.movie;

SELECT *
FROM dbo.person;

----- F: TSQL08 ICA Demo – Using Built-In Functions -----
-- The built-in LEN function is used to count the number of characters on the overview column of dbo.movie.
-- A subquery with the MAX aggregate function is used to find the overview with the most characters.
-- Then the movie with the longest overview is displayed with its title and overview.

SELECT title, overview
FROM dbo.movie
WHERE LEN(overview) = 
(
	SELECT MAX(LEN(overview))
	FROM dbo.movie
);

-------------------- SQL Server - TSQL Basics Portfolio --------------------
---------- TSQL09-Grouping and Aggregating Data ----------

----- TSQL09-ICA Demo A-Using Aggregate Functions (like MAX, MIN, COUNT, SUM and AVERAGE function) -----
-- SUM function is used to check the total budget spent on Star Wars movies.
-- AVG function is used to check the average spent on Star Wars movies.
-- MAX function is used to check the highest spent on a Star Wars movies.
-- MIN function is used to check the lowest spent on a Star Wars movie.
-- COUNT function (* means including NULL) is used to check the total number of Star Wars movies.
-- LIKE '%Star Wars%' ensures that the title has to include Star Wars to count towards these functions used.

SELECT SUM(budget) AS total_budget_star_wars_movies,
	AVG(budget) AS average_budget_star_wars_movies,
	MAX(budget) AS maximum_budget_star_wars_movies,
	MIN(budget) AS minimum_budget_star_wars_movies,
	COUNT(*) AS total_star_wars_movies
FROM dbo.movie
WHERE title LIKE '%Star Wars%';

----- TSQL09- ICA Demo B-Using the GROUP BY Clause -----
----- TSQL 09-ICA Demo C-Filtering Groups with HAVING -----
-- Displays the actor's name, gender and the number of movies they have been a part of in dbo.movie_cast.
-- It joins with dbo.person with person_id and joins again with dbo.gender with gender_id.
-- Then it groups by gender and person_name and displays the actors
-- with more than or equal to 30 appearences in movies in dbo.movie_cast.

SELECT p.person_name AS actor_name,
	g.gender AS gender,
	COUNT(DISTINCT mc.movie_id) AS movies_appeared
FROM dbo.movie_cast mc
JOIN dbo.person p ON mc.person_id = p.person_id
JOIN dbo.gender g ON mc.gender_id = g.gender_id
GROUP BY g.gender, p.person_name
HAVING COUNT(DISTINCT mc.movie_id) >= 30;

---------- TSQL10-Using Subqueries ----------

----- TSQL10- ICA Demo A-Writing Self-Contained Subqueries -----
-- Selects the movies with an above average budget in dbo.movie
-- The subquery calculates the average budget across all movies in dbo.movie
-- which is used in WHERE for checking/comparison in 'budget >'

SELECT title, budget
FROM dbo.movie
WHERE budget > 
(
	SELECT AVG(budget)
	FROM dbo.movie
);

----- TSQL10- ICA Demo B-Writing Correlated Subqueries -----
-- Selects the titles with an above average title length from dbo.movie as m1.
-- The correlated subquery is used to calculaye the average length of movie titles in dbo.movie as m2.
-- The main query then checks each movie's title length against the average title length calculated.

SELECT title, LEN(title) AS title_length
FROM dbo.movie m1
WHERE LEN(m1.title) > 
(
	SELECT AVG(LEN(m2.title))
	FROM dbo.movie m2
);

----- TSQL10- ICA Demo C-Using the EXISTS Predicate with Subqueries -----
-- Selects the movie title from dbo.movies and uses EXIST with the subquery to check.
-- The subquery checks dbo.movie_cast if there is at least one character 
-- in the movie with 'Tom' as their first name with LIKE 'Tom%'.

SELECT title
FROM dbo.movie m
WHERE EXISTS 
(
	SELECT *
	FROM dbo.movie_cast mc
	WHERE m.movie_id = mc.movie_id
	AND mc.character_name LIKE 'Tom%'
);

-------------------- SQL Server - TSQL Intermediate Portfolio --------------------
---------- TSQL11-Using Table Expressions ----------

----- TSQL11-ICA Demo A-Using Views -----
-- Creates a view that selects a movie title and movie runtime from dbo.movie
-- This view is called 'MovieTitlesWitheRuntime'.

DROP VIEW IF EXISTS MovieTitlesWitheRuntime;
GO

CREATE VIEW MovieTitlesWithRuntime AS
SELECT title, runtime
FROM dbo.movie;

SELECT * 
FROM MovieTitlesWithRuntime;

----- TSQL11- ICA Demo B-Using Inline TVFs -----
-- Creates an Inline TVF that returns movies that have a release_year after a specified year.
-- This specified year is entered by ther user when calling this function,  
-- which is 2000 in this example.

DROP FUNCTION IF EXISTS GetMoviesReleasedAfterYear;
GO

CREATE FUNCTION GetMoviesReleasedAfterYear
(
	@year INT
)
RETURNS TABLE
AS
RETURN
(
    SELECT *
    FROM dbo.movie
    WHERE CAST(SUBSTRING(release_date, 1, 4) AS INT) > @year
);

SELECT title, release_date
FROM GetMoviesReleasedAfterYear(2000);

----- TSQL11-ICA Demo C-Using Derived Tables -----
-- Displays the title, runtime and release_date for movies which are released after 2000.
-- the title and runtime are from the MovieTitlesWithRuntime view,
-- while the GetMoviesReleasedAfterYear(2000) function 
-- is used to check if a movie's released date is after the year 2000 

SELECT mr.title, mr.runtime, my.release_date
FROM MovieTitlesWithRuntime AS mr
INNER JOIN GetMoviesReleasedAfterYear(2000) AS my 
ON mr.title = my.title;

----- TSQL11- ICA Demo D-Using CTEs -----
-- Creates a CTE which returns movies runtime that were created after 2010
-- using the MovieTitleWithRuntime view.
-- It then selects the title and runtime for these movies,
-- using 'GetMoviesReleasedAfterYear(2010)' to check if a movie is after 2010.

WITH MoviesWithTitleAndRuntime AS 
(
	SELECT title, runtime
	FROM MovieTitlesWithRuntime
)
SELECT title, runtime
FROM MoviesWithTitleAndRuntime
WHERE title IN 
(
	SELECT title FROM GetMoviesReleasedAfterYear(2010)
);

---------- TSQL12-Using Set Operators ----------

----- TSQL12- ICA Demo A-Writing Queries with the UNION Operator -----
-- Selects movie_id and person_id from both dbo.movie_cast and dbo.movie_crew 
-- and merges them together, not eliminating duplicate values due to UNION ALL.
-- Then displays the movie_id with all its asscociated person_id. 
-- Thus showing all person_id that are credited in each movie.

SELECT movie_id, person_id
FROM dbo.movie_cast
UNION ALL
SELECT movie_id, person_id
FROM dbo.movie_crew;

----- TSQL12-ICA Demo B-Using EXCEPT and INTERSECT -----
-- Selects the person_id from dbo.person, not including (EXCEPT)
-- people who are actors (dbo.movie_cast is the list of actors).
-- Thus showing only the person_id of the movie crew members such as directors.

SELECT person_id
FROM dbo.person
EXCEPT
SELECT person_id
FROM dbo.movie_cast;

-- Selects the person_id from dbo.person, but only include (INTERSECT)
-- people who are actors (dbo.movie_cast is the list of actors).
-- Thus showing only the person_id of the movie actors.

SELECT person_id
FROM dbo.person
INTERSECT
SELECT person_id
FROM dbo.movie_cast;

----- TSQL12-ICA Demo C-Using APPLY -----
-- Selects the list of all movies in dbo.movie and the genres in dbo.genre.
-- It then cross joins the movie list and associates them with their appropriate genre.

SELECT m.*, g.genre_name
FROM dbo.movie AS m
CROSS APPLY 
(
	SELECT mg.genre_id
	FROM dbo.movie_genres mg
	WHERE m.movie_id = mg.movie_id
) AS mg
JOIN dbo.genre AS g 
ON mg.genre_id = g.genre_id;

---------- TSQL13-Using Window Ranking, Offset, and Aggregate Functions ----------

----- TSQL13-ICA Demo A-Using Creating Windows with OVER -----
----- TSQL13- ICA Demo B-Using Exploring Window Functions -----
-- Selects all movies in dbo.movie and ranks them by revenue in descending order 
-- (top selling first) and shows the difference its revenue has compared to the average.
-- ROW_NUMBER() is used to squentially number each row according to its revenue_difference_from_avg.
-- Revenue difference is calculated by taking the revenue of a movie and 
-- subtracting the total average revenue in dbo.movie, which is calculated with AVG(revenue).

SELECT movie_id, title, revenue,
	ROW_NUMBER() OVER(ORDER BY revenue DESC) AS revenue_rank,
	revenue - AVG(revenue) OVER() AS revenue_difference_from_avg
FROM dbo.movie;

---------- TSQL14-Pivoting and Grouping Sets ----------

----- TSQL14-ICA Demo A-Writing Queries with PIVOT and UNPIVOT -----
-- Selects movie_id and gender_id in dbo.movie_cast to be used as the source for pivotting.
-- Pivot is used to aggregate (COUNT) the number of time a particular gender_id is in a movie_id.
-- [1] is male, [2] is female and [3] is unspecified which will be made into seprate columns.
-- Thus representing the counts of occurences of those 'gender_id' for each 'movie_id'.

SELECT *
FROM 
(
	SELECT movie_id, gender_id
	FROM dbo.movie_cast
) AS SourceTable
PIVOT 
(
	COUNT(gender_id)
	FOR gender_id IN ([1], [2], [0])
) AS PivotTable

-- The title, vote and subject will be the displayed columns.
-- by unpivoting the vote_average and vote_count (as float) from dbo.movie
-- Displaying the movie title and two rows, one being vote_average and another vote_count.

SELECT title, vote, subject
FROM 
(
	SELECT title, CAST(vote_average AS FLOAT) AS vote_average, CAST(vote_count AS FLOAT) AS vote_count
	FROM dbo.movie
) AS SourceTable
UNPIVOT 
(
	vote
	FOR subject IN (vote_average, vote_count)
) AS UnpivotTable;

----- TSQL14-ICA Demo B-Working with Grouping Sets -----
-- Selects the release year, total budget and total movie count of a year.
-- Grouping sets is used to group the first set by release year, 
-- and the next set is total budget and total movie count in dbo.movie.

SELECT
	YEAR(TRY_CONVERT(DATE, release_date)) AS ReleaseYear,
	SUM(budget) AS TotalBudget,
	COUNT(*) AS MovieCount
FROM dbo.movie
GROUP BY
	GROUPING SETS 
	(
		(YEAR(TRY_CONVERT(DATE, release_date))),
		()
    );

-------------------- SQL Server - TSQL Advanced Portfolio --------------------
---------- TSQL15-Executing Stored Procedures ----------

----- TSQL15-ICA Demo A-Querying Data with Stored Procedures -----
-- Creates and executes a stored procedure that returns the top 10 highest rated movies 
-- using SELECT TOP 10 title and its appropraite vote_average (rating) by arranging the list in descending order.

DROP PROCEDURE IF EXISTS dbo.GetTopRated;
GO

CREATE PROCEDURE dbo.GetTopRated
AS
BEGIN
	SELECT TOP 10 title, vote_average
	FROM dbo.movie
	ORDER BY vote_average DESC
END

EXECUTE dbo.GetTopRated;

----- TSQL15-ICA Demo B-Passing Parameters to Stored Procedures -----
----- TSQL15-ICA Demo C-Creating Simple Stored Procedures -----
-- Creates execute a stored procedure which passes the release_year as an argument.
-- Returns all movie titles and release_date with the specified release_year passed as argument by the user.

DROP PROCEDURE IF EXISTS dbo.GetMovieByYear;
GO

CREATE PROCEDURE dbo.GetMovieByYear(@release_year INT = NULL)
AS
BEGIN
	SELECT title, release_date
	FROM dbo.movie
	WHERE CAST(SUBSTRING(release_date, 1, 4) AS INT) = @release_year
	ORDER BY release_date ASC
END;

EXECUTE dbo.GetMovieByYear @release_year = 2000;

----- TSQL15-ICA Demo D-Working with Dynamic SQL -----
-- Creates a stored procedure which updates the homepage of a movie in dbo.movie
-- movie_id and the string for the homepage are passed as arguments in the stored procedure (user inputs).
-- Thus allowing for the update of homepage for any title in dbo.movie

DROP PROCEDURE IF EXISTS dbo.UpdateMovieHomepage;
GO

CREATE PROCEDURE dbo.UpdateMovieHomepage(@movie_id INT, @homepage NVARCHAR(MAX))
AS
BEGIN
	UPDATE dbo.movie
	SET homepage = @homepage
	WHERE movie_id = @movie_id;
END;

EXECUTE dbo.UpdateMovieHomepage @movie_id = 13, @homepage = 'https://www.paramountpictures.com/movies/forrest-gump';

SELECT *
FROM dbo.movie;

-- Used to revert homepage change back to empty string
EXECUTE dbo.UpdateMovieHomepage @movie_id = 13, @homepage = '';

---------- TSQL16-Programming with T-SQL ----------

----- TSQL16-ICA Demo A-T-SQL Programming Elements -----
----- TSQL16-ICA Demo B-Controlling Program Flow -----
-- Creates a stored procedure with returns the title, vote_average and an appropriate rating.
-- movie_id is passed as an argument in the stored procedure (user input).
-- The rating is given by a controlled program flow with a switch case, according to the vote_average.
-- The rating given covers all situations as it rates appropriately for 1-10, 
-- and also covers NULLs with the ELSE statement.

DROP PROCEDURE IF EXISTS GetMovieRating;
GO

CREATE PROCEDURE GetMovieRating(@movie_id INT)
AS
BEGIN
	DECLARE 
		@title NVARCHAR(MAX),
		@vote_average FLOAT,
		@rating NVARCHAR(20);

	-- Retrieve vote_average based on the input movie_id
	SELECT @title = title, @vote_average = vote_average
	FROM dbo.movie
	WHERE movie_id = @movie_id;

	-- Determine the rating based on vote_average
	SET @rating = 
		CASE 
		WHEN @vote_average <= 1 THEN
			N'Bad'
		WHEN @vote_average > 1 AND @vote_average <= 4 THEN
			N'Below Average'
		WHEN @vote_average > 4 AND @vote_average <= 6 THEN
			N'Average'
		WHEN @vote_average > 6 AND @vote_average <= 9 THEN
			N'Above Average'
		WHEN @vote_average > 9 THEN
			N'Excellent'
		ELSE
			N'Not Rated'
		END;

	-- Display the result
	SELECT 
		@movie_id AS movie_id,
		@title AS title,
		@vote_average AS vote_average,
		@rating AS rating;
END;

-- Execute the stored procedure with a specific movie_id (user input)
EXEC GetMovieRating @movie_id = 5;

---------- TSQL17-Implementing Error Handling  ----------

----- TSQL17-ICA Demo A-Implementing T-SQL Error Handling -----
----- TSQL17-ICA Demo B-Implementing Structured Exception Handling -----
-- An error catch is implemented where the user enters an input for @stringValue argument.
-- It then tries to convert the string into an INT where if it is successful, it will display a success message.
-- If it is not able to convert, an error message will be printed and it will THROW an exception.
-- This example will display an error as the input in @stringValue is not an INT, but a string.

DECLARE @stringValue NVARCHAR(MAX) = 'Strings cannot be converted';

BEGIN TRY
    DECLARE @intValue INT;
    SET @intValue = CONVERT(INT, @stringValue);
    PRINT 'Conversion successful. Result: ' + CAST(@intValue AS NVARCHAR);
END TRY
BEGIN CATCH
    PRINT 'Error: Unable to convert the string to an integer.';
	THROW;
END CATCH;

---------- TSQL18-Implementing Transactions in SQL Server ----------

----- TSQL18-ICA Demo A-Transactions and the Database Engine -----
----- TSQL18-ICA Demo B-Controlling Transactions -----
-- A transaction is implemented where person_id is an argument that should be an INT.
-- If the person_id already exist in dbo.person, then the transaction is rollbacked.
-- If it does not exist, then the person_id and person_name will be added into dbo.person.
-- If an invalid input is entered such as DECLARE @person_id INT = 'this is a string', 
-- error handling will occur and that error will be caught and an approriate error message will display.

BEGIN TRY
	DECLARE @person_id INT = 1;

	IF NOT EXISTS (SELECT * FROM dbo.person WHERE person_id = @person_id)
	BEGIN
		BEGIN TRANSACTION;
		INSERT INTO dbo.person (person_id, person_name) VALUES (@person_id, 'John Doe');
		PRINT 'Transaction committed successfully.';
		COMMIT TRANSACTION;
	END
	ELSE
	BEGIN
		PRINT 'Person_id already exists. Rolling back transaction.';
	END
END TRY
BEGIN CATCH
	PRINT 'An error occurred: ' + ERROR_MESSAGE();
	ROLLBACK TRANSACTION;
END CATCH;
