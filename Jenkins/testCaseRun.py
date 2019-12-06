import sqlite3
import subprocess
import shlex

def readSqliteTable():
    # Running a defensive try/except for DB connection
    try:
        sqliteConnection = sqlite3.connect('testing.db')
        cursor = sqliteConnection.cursor()
        print("Connected to Testing DB")
        
        # initialize variable for select query to eliminate "magic number" type of statement
        sqlite_select_query = """SELECT * from testcases"""
        cursor.execute(sqlite_select_query)
        testcases = cursor.fetchall() #use sqliteConnection cursor to fetch all rows as type n x m array
        print("Total number of testcases:  ", len(testcases)) 
        for row in testcases:
            #Run a subprocess "runTest.sh" passed with TCID and Path index of testcase (row) that we're on
            subprocess.call(shlex.split('./runTest.sh %s, %s' % (row[0], row[2])))

        # initialize variable for select query to eliminate "magic number" type of statement
        sqlite_select_results = """SELECT * from testresults"""
        cursor.execute(sqlite_select_results)
        testresults = cursor.fetchall()
        #Once all testcases have ran and testresults are published to DB, output testresults row by row
        for row in testresults:
            print(row)

        cursor.close()
        
    except sqlite3.Error as error:
        print("Failed to read data from Testcases table", error)
    finally:
        if (sqliteConnection):
            sqliteConnection.close()
            print("The SQLite connection is closed")

def main():
    # This python script has one purpose and its to run the function 
    readSqliteTable()

if __name__ == '__main__':
    main()
