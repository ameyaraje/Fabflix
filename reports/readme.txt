----------------------------
Set up files for website
----------------------------
 
We assume all files are in current working directory and the database has 
already been populated with createtable.sql and data.sql. If they are not, 
please refer to the previous Project 1 README and follow steps so that they are. 

------------------
Location of Files
------------------

After downloading the fabflix.war and moving it to the correct server 
location (ex: /var/lib/tomcat7/webapps/) and deploying the information, 
there are 7 folders titled: css, images, js, META-INF, WEB-INF, reports, and source_XML_parsing.
1.css: contains *.css files
2.images: contains images used throughout site
3.js: empty
4.META-INF: contains manifest file (MANIFEST.MF)which has list of attributes that describe package
5.WEB-INF: contains web.xml, *.class, *.java files, and *.jar files
6. reports: contains background on program and compilation process
7. source_XML_parsing: files relating to XML parsing

--------------------------------------------------------------------------------------------------
Compile your Java Servlets (and other Java resources if any) and install the generated class files
--------------------------------------------------------------------------------------------------

*.java files are located at fabflix/WEB-INF/sources/edu/fabflix/.
You have to compile all *.java files together as they are a package. If you don’t, you will get dependency errors. 

Go to fabflix/WEB-INF/ and compile the java files with the command below. This runs compile.sh which contains the specific commands to compile the java files.

sudo sh compile.sh

Then go to fabflix/source_XML_parsing and execute XML parser code end to end with the command below. This runs compile_run.sh which contains the specific commands to execute XML parser code.

sudo sh compile_run.sh

For the XML parsing only, the database is set as moviedb_project3_grading (not moviedb). Ensure that the database moviedb_project3_grading has been created and populated before running command. The 3 data files with the name: casts124.xml, actors63.xml, and mains243.xml are in the source_XML_parsing root directory (fabflix/source_XML_parsing).