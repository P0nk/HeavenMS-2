If you have new SQL to update the database,
please put it in this folder and start from version V1.1.0 or V1.0.68

The v1 directory is only convenient for classifying SQL to avoid too many files in one directory. 
You can also create directories for other versions (flyway will automatic find all files in community). 
It should be noted that flyway will be executed in order according to the version. 
Therefore, unless you have decided to stop the development of v1, 
And start the development of v2, 
otherwise it is not recommended to create a v2 directory