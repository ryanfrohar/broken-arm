import sqlite3
import os
from sqlite3 import Error 

def create_connection(db_file):
    # Running a defensive try/except for DB connection
    # Argument is DB file and Return is conn object

    conn = None
    try:
        conn = sqlite3.connect(db_file)
        conn.row_factory = sqlite3.Row;
        return conn
    except Error as e:
        print(e)
 
    return conn

def add_testresult_row(conn, testresult_row_sql):
    """ Add a row in the testcase database
    :param conn: Connection object
    :param testcase_row_sql: Adding row to database
    :return:
    """
    #running defensive try/except block
    try:
        c = conn.cursor()
        #This is where I use the os python library to implement parameter passing
        #I used (?, ?, ?) in the variable declaration since sqlite3 python library allows the version of execution below
        c.execute(testresult_row_sql, (os.environ['NOW'], os.environ['RESULTS'], os.environ['TCID']))
        conn.commit() 

    except Error as e:
        print(e)

def main():
    database = "testing.db"
    #A way to add values of each in another function
    sql_add_testresult_row = '''insert into testresults(timeStamp, result, testcase_id) values(?,?,?)'''

    # create a database connection
    conn = create_connection(database)
    
    if conn is not None:
        add_testresult_row(conn, sql_add_testresult_row)
 
if __name__ == '__main__':
    main()
