----- Authorization Override -----
Use school

ALTER AUTHORIZATION ON DATABASE:: school TO sa;
-----------------------------------

----- Testing -----
SELECT *
FROM dbo.Course;

SELECT *
FROM dbo.CourseInstructor;

SELECT *
FROM dbo.Department;

SELECT *
FROM dbo.OfficeAssignment;

SELECT *
FROM dbo.OnlineCourse;

SELECT *
FROM dbo.OnsiteCourse;

SELECT *
FROM dbo.Person;

SELECT *
FROM dbo.StudentGrade;
-----------------------------------

---------- SQL TSQL03 Task - Querying with Select ----------

----- Writing Simple SELECT Statement -----

-- Select All --
-- Selects all columns in the Course table of the database
SELECT *
FROM dbo.Course;

-- Select specific columns --
-- Selects only the PersonID, LastName and FirstName columns from the database
SELECT PersonID, LastName, FirstName
FROM dbo.Person;

----- Eliminating Duplicates with DISTINCT -----
-- Selects all data in the StudentID column with only showing repeated data once
SELECT DISTINCT StudentID
FROM dbo.StudentGrade;

----- Using Column and Table Aliases -----
-- Changes column name of LastName, FirstName and Discriminator into Surname, Given Name and Role respectively
-- Also changes the dbo.Person table as PersonalRole
SELECT PersonID, LastName AS [Surname], FirstName AS [Given Name], Discriminator AS Role
FROM dbo.Person AS PersonalRole;

----- Writing Simple CASE Expressions -----
-- Adds a column called Status where when the data in HireDate is 'Null', it displays Staff as its data.
-- If not, it sets the data displayed as Student.
SELECT PersonID, P.LastName, P.FirstName,
	CASE
		WHEN P.HireDate IS NULL THEN 'Student'
		ELSE 'Staff'
	END AS Status
FROM dbo.Person AS P;

------------------------------------------------------------

---------- SQL TSQL04 Task - Querying with Multiple Tables ----------

----- Relating 2 or more tables - Joins -----

-- INNER --
-- Joins the columns CourseID from dbo.CourseIntructor
-- to show the namesof the instructor for each course.
-- as well as LastName and FirstName column from dbo.Person
-- using PersonID from both tables as long there is matching data between the two tables.
SELECT c.CourseID, p.LastName, p.FirstName
FROM dbo.CourseInstructor AS c
INNER JOIN dbo.Person AS p
ON c.PersonID = p.PersonID;

-- OUTER --
-- Join the listed columns in 'SELECT' from their respective tables
-- to show the grades receieved by students.
-- where PersonID from Person table is equal to StudentID from StudentGrade table.
-- It will return all data from both tables which satisfy the join condition as well as rows that do not.
SELECT p.PersonID, p.LastName, p.FirstName,  s.CourseID, s.Grade
FROM dbo.Person AS p, dbo.StudentGrade AS s
WHERE p.PersonID = s.StudentID;

-- LEFT --
-- Joins the listed columns in 'SELECT' from their respectctive tables
-- to show the hiredate of staff, as well as the enrollmentdate and enrollmentID for students.
-- where PersonID from Person table is equal to StudentID from StudentGrade table.
-- It will return matching data that satisfy the join condition plus all rows in the first table that do not.
SELECT p.PersonID, p.LastName, p.FirstName, p.Discriminator, p.HireDate, p.EnrollmentDate, s.EnrollmentID
FROM dbo.Person AS p
LEFT OUTER JOIN dbo.StudentGrade AS s
ON p.PersonID = s.StudentID;

-- RIGHT --
-- Joins the listed columns in 'SELECT' from their respectctive tables
-- to show the location of onsite courses.
-- where CourseID from both tables are equal.
-- It will return matching data that satisfy the join condition plus all rows in the second table that do not.
SELECT c.CourseID, o.Location, o.Days, o.Time, c.Title, c.Credits, c.DepartmentID
FROM dbo.OnsiteCourse AS o
RIGHT OUTER JOIN dbo.Course AS c
ON o.CourseID = c.CourseID;

----- Joining multiple tables - inner, outer and cross -----
-- Joins the listed columns in 'SELECT' from their respectctive tables
-- to show the which courses are taken by which student.
SELECT p.PersonID, p.LastName, p.FirstName, s.EnrollmentID, c.Title
FROM dbo.Person AS p
LEFT OUTER JOIN dbo.StudentGrade AS s ON p.PersonID = s.StudentID
RIGHT OUTER JOIN dbo.Course as c ON s.CourseID = c.CourseID;

------------------------------------------------------------

---------- SQL TSQL05 Task - Sort and Filtering Data ----------

----- Sort with Order By -----
-- Sorts the last name in ascending order (A to Z) of the people within the dbo.Person table
SELECT PersonID, LastName, FirstName, Discriminator
FROM dbo.Person
Order By LastName;

----- Filter with Where By -----
-- Sorts the StudentGrade table for students who took courseID 1050 (SQL Server Programming)
-- and displays their grade.
SELECT *
FROM dbo.StudentGrade
WHERE CourseID = 1050;

----- Filter with top offset fetch -----
-- Selects the top 10 rows only of data from StudentGrade table.
-- CourseID and Grade are sorted in ascending order.
-- Offset is used to select the rows of index 0 to 9 (10 not inclusive)
-- Reminder: Both tables will yeild the same result
SELECT TOP(10) *
FROM dbo.StudentGrade
ORDER BY CourseID ASC, Grade ASC;

SELECT *
FROM dbo.StudentGrade
ORDER BY CourseID ASC, Grade ASC
OFFSET 0 ROWS FETCH FIRST 10 ROWS ONLY;

----- Handling Nulls -----
-- Helps in searching for students who have no grade for their courses.
-- Only selects the rows where Grade is 'NULL' from the dbo.StudentGrade table.
SELECT *
FROM dbo.StudentGrade
WHERE Grade IS NULL;

------------------------------------------------------------

---------- SQL TSQL06 Task - Working with SQL Server Data ----------

----- Conversion in a Query -----
-- Changes the data type of the variable 'Grade' in the table into VARCHAR, 
-- which then can be added together with the string 'Grade: ' before it
SELECT EnrollmentID, CourseID, StudentID, 'Grade: ' + CAST(Grade AS VARCHAR(10)) AS Points
FROM dbo.StudentGrade;

----- Collation in a Query -----
-- Selects all rows with 'Instructor' as Discriminator.
-- This collation is case sensity as the 'Instructor' used is not the same as 'instructor'
SELECT *
FROM dbo.Person
WHERE Discriminator COLLATE Latin1_General_CS_AS = 'Instructor';

----- Date and Time Function -----
-- Returns the date at the end of the month after 6 months of the date in 'StartDate' variable
SELECT *, EOMONTH(StartDate, 6) AS [End of day after 6 months]
FROM dbo.Department;

------------------------------------------------------------

---------- SQL TSQL07 Task - Using DML to Modify Data ----------

----- Adding Data to Tables -----
-- Inserts a new student 'Jake Webber' into the dbo.Person table.
-- The list of variables needed is listed and inserted into the existing table.
-- To confirm that it works, a simple Select ALL command is used on dbo.Person table later.
-- The latest data added can be found at the end of the dbo.Person table.
INSERT INTO dbo.Person(LastName, FirstName, EnrollmentDate, Discriminator)
VALUES('Webber', 'Jake', '2022/01/11', 'Student');

SELECT *
FROM dbo.Person

----- Modifying and Removing Data -----
-- Modifies the EnrollmentDate of the recently added student 'Jake Webber'
-- from '2022/01/11' into '2022/01/23'.
-- To confirm that it works, a simple Select ALL command is used on dbo.Person table later.
-- The latest data added can be found at the end of the dbo.Person table.
UPDATE dbo.Person
SET EnrollmentDate = '2022/01/23'
WHERE LastName='Webber' and FirstName = 'Jake';

SELECT *
FROM dbo.Person

----- Generate Automatic Column Values -----


------------------------------------------------------------
