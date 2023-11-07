# Flyway

## why use it

Flyway can automatically update the database when the program starts, 
which makes it easier for us to deploy services.

## how to use

Create a SQL file with the name format "V1.0.0__XX.sql". 
The file name should begin with "V" followed by the version number. 
Ensure that version numbers are sequential and not repeated. 
The file name structure should include two underscores ("__") after the version number, 
followed by a description, and ending with ".sql".

The contents of the SQL file should contain the SQL statements you want to execute.

Please do not modify any files in the initial folder, including adding, modifying or deleting.

The files in the initial folder are made based on the files in the original database folder.
They are used to initialize the database.

If you have new SQL to update the database,
please put it in the community directory and start from version V1.1.0 or V1.0.68

If there are changes you do not wish to share with the community, 
store those SQL files in the "customize" folder.
To avoid version number duplication between the "customize" and "community" directories, 
ensure that version numbers in the "customize" directory start from 1000.0.0.

## developer notes

Please do not modify any submitted SQL files.

Please ensure that the SQL statements are correct. 
In the event of incorrect statements being executed by Flyway, 
you will need to manually delete the corresponding records from the database (table: flyway_schema_history).