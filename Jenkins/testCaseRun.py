import sqlite3
import subprocess
import shlex

def readSqliteTable():
    try:
        sqliteConnection = sqlite3.connect('testing.db')
        cursor = sqliteConnection.cursor()
        print("Connected to Testing DB")

        sqlite_select_query = """SELECT * from testcases"""
        cursor.execute(sqlite_select_query)
        testcases = cursor.fetchall()
        print("Total number of testcases:  ", len(testcases))
        for row in testcases:
	    subprocess.call(shlex.split('./runTest.sh %s, %s' % (row[0], row[2])))
        cursor.close()

    except sqlite3.Error as error:
        print("Failed to read data from Testcases table", error)
    finally:
        if (sqliteConnection):
            sqliteConnection.close()
            print("The SQLite connection is closed")

def main():
    readSqliteTable()


if __name__ == '__main__':
    main()
