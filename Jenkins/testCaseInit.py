import sqlite3
import os
from sqlite3 import Error 
 
def create_connection(db_file):
    # Running a defensive try/except for DB connection
    conn = None
    try:
        conn = sqlite3.connect(db_file)
        conn.row_factory = sqlite3.Row;
        return conn
    except Error as e:
        print(e)
 
    return conn
 
 
def create_table(conn, create_table_sql):
    """ create a table from the create_table_sql statement
    :param conn: Connection object
    :param create_table_sql: a CREATE TABLE statement
    :return: void
    """
    try:
        c = conn.cursor()
        c.execute(create_table_sql)
    except Error as e:
        print(e) 
 
def add_testcase_row(conn, testcase_row_sql):
    """ Add a row in the testcase database
    :param conn: Connection object
    :param testcase_row_sql: Adding row to database
    :return:
    """
    try:
        c = conn.cursor()
        c.execute(testcase_row_sql, (os.environ['SUITE'], os.environ['LOCATION']))
        conn.commit() 
    except Error as e:
        print(e)

def main():
    # initialize variable for the database to eleminate "magic number"
    database = "testing.db" 
 
    # initialize variable for the command to create a table to eleminate "magic number"
    sql_create_testcase_table = """ CREATE TABLE IF NOT EXISTS testcases (
                                        tcid integer primary key,
                                        suite text,
					location text
                                    ); """
    
    # initialize variable for the testresults table to eleminate "magic number" 
    #One can see that testcase_id references TCID in the testcases table

    sql_create_testresult_table = """CREATE TABLE IF NOT EXISTS testresults (
                                    testNumber integer primary key,
                                    timeStamp text,
			            result blob,
                                    testcase_id integer,
                                    FOREIGN KEY (testcase_id) REFERENCES testcases(tcid)
                                );"""
    sql_add_testcase_row = '''insert into testcases(suite, location) values(?,?)'''

    # create a database connection
    conn = create_connection(database)
 
    # create tables
    if conn is not None:
        # create projects table
        create_table(conn, sql_create_testcase_table)
 
        # create tasks table
        create_table(conn, sql_create_testresult_table)

	# Add testcase
	add_testcase_row(conn, sql_add_testcase_row)
	
    else:
        print("Error! cannot create the database connection.")
 
 
if __name__ == '__main__':
    main()
