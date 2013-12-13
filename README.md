Data-deduplication
==================

Multiple ways of chunking for data deduplication: Fixed size chunking, Content defined chunking, and File based chunking.

## How to run the program
Two arguments are needed: deduplication type and directory

1. Deduplication type
	* Fixed size chunking : 1
	* File based chunking : 2
	* Content defined chunking : 3
	
	Need to enter a number (1, 2, or 3) to choose deduplication type.
	
2. Directory
	* The path to a directory need to perform deduplication.
	
	Right now, only chunking and identify the duplicated data chunks are implemented. The deduplication part is not implemented.  
	
If export the source code into a runable jar file then could run as: 

`java -jar jarname.jar 1 /Users/fan/Documents`