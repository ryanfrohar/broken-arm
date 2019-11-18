import sqlite3
import os

testcase_file = 'test_case.db' # name of the database file 

#import environment variables from calling shell script
# Connecting to the database file
conn = sqlite3.connect(testcase_file)
conn.row_factory = sqlite3.Row

#Create a cursor to work with db
c = dbconnect.cursor()

#Creating an SQLite table with columns
cursor.execute('''create table if not exits testcases (TCID integer primary key autoincrement, suite text, PFPerc real);''')
cursor.execute('''insert into testcases values (os.environ['ID'], os.environ['SUITE'], 0);''')
conn.commit();

for row in cursor:
    print(row['TCID'],row['suite'],row['PFPerc'])
conn.close()
