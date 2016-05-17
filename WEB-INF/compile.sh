# Add employees table.
echo "Please enter database password when prompted."
echo "Adding employees table and employee TA CS122B..."
mysql -u root -p moviedb < create_employees_table.sql

# Add stored procedure.
echo "Adding stored procedure 'add_movie'..."
mysql -u root -p moviedb < create_stored_procedure_add_movie.sql

# Compile all servlets and java files.
echo "Compiling java files..."
javac -cp ./lib/servlet-api.jar:./lib/mysql-connector-java-5.0.8-bin.jar:./lib/javax.json-1.0.4.jar ./sources/edu/fabflix/*.java ./sources/edu/fabflix/*/*.java -d ./classes

# Restart Tomcat.
echo "Restarting Tomcat..."
service tomcat7 restart