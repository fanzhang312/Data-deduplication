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
	
If export the source code into a runable jar file then could run as (Example): 

    java -jar deduplication.jar 1 /Users/fan/Documents
    
    The above command will choose fixed size chunking for directory /Users/fan/Documents. All the file inside that directory will be chunked into 1MB sized chunks. If duplicated chunks found, it will print out the file which contains the duplicated chunks.
    
    If want to use file based chunking, 2 need to be specified. Then if there is duplicated files in the given directory, the file name will be returned.
    
    3 is specified, then it will perform content defined chunking for given directory. For each file, it will return the number of chunks be generated and number of duplicated chunks.