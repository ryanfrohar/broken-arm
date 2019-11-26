import sqlite3
import subprocess

def readSqliteTable():
    try:
    	import subprocess
	cmd = ['./runTest.sh', '--arg', 'value']
	p = subprocess.Popen(cmd, stdout=subprocess.PIPE)
	
        sqliteConnection = sqlite3.connect('testing.db')
        cursor = sqliteConnection.cursor()
        print("Connected to Testing DB")

        sqlite_select_query = """SELECT * from testcases"""
        cursor.execute(sqlite_select_query)
        testcases = cursor.fetchall()
        print("Total number of testcases:  ", len(testcases))
        for row in testcases:
	    cmd = ['./runTest.sh', '--arg', 'value']
	    tcid = row[0]
	    location = row[3]
            print("tcid ", row[0])
	    print("location: ", row[3]) 
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
