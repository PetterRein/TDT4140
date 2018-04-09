import sqlite3
#import bcrypt

databaseName = "testData.db"
def databaseSetup():
    conn = sqlite3.connect(databaseName)
    c = conn.cursor()
    c.execute('DROP TABLE IF EXISTS users')
    c.execute('DROP TABLE IF EXISTS patientData')
    c.execute('DROP TABLE IF EXISTS feedback')
    c.execute('''CREATE TABLE IF NOT EXISTS users
            (id INTEGER PRIMARY KEY,
            role varchar(64),
            name varchar(64),
            email varchar(64),
            passwordHash varchar(2000),
            salt varchar(256),
            cookie varchar(256),
            doctorID int,
            FOREIGN KEY (doctorID) REFERENCES users(id))''')
    c.execute('''CREATE TABLE IF NOT EXISTS patientData
            (id INTEGER PRIMARY KEY,
            patientID int not null,
            rating int,
            extrainfo text,
            times TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
            FOREIGN KEY (patientID) REFERENCES users(id))''')
    c.execute('CREATE TABLE IF NOT EXISTS feedback(id INTEGER PRIMARY KEY, message VARCHAR (20000000))')

    conn.commit()
    conn.close()

def getIdFromEmail(email):
    conn = sqlite3.connect(databaseName)
    c = conn.cursor()
    c.execute("SELECT id FROM USERS WHERE email = ?", [email])
    data = c.fetchone()
    conn.close()
    return data[0]

def createUser(role, username, email, password, salt, userDoctor):
    #salt = bcrypt.gensalt()
    #passwordHash = bcrypt.hashpw(password.encode('utf-8'), salt)
    passwordHash = password
    conn = sqlite3.connect(databaseName)
    c = conn.cursor()
    if userDoctor is None:
        doctorId = -1
    else:
        doctorId = getIdFromEmail(userDoctor)
    c.execute('INSERT INTO users(role, name, email, passwordHash, salt, doctorID) values(?, ?, ?, ?, ?, ?)', (role, username, email, passwordHash, salt, doctorId))
    conn.commit()
    conn.close()

def insertUserData(email, rating, extrainfo, times):
    conn = sqlite3.connect(databaseName)
    c = conn.cursor()
    patientId = getIdFromEmail(email)
    c.execute('INSERT INTO patientData(patientID, rating, extrainfo, times) values(?, ?, ?, ?)', (patientId, rating, extrainfo, times))
    conn.commit()
    conn.close()

databaseSetup()
#ALL PASSWORDS ARE PASSWORD
createUser("Admin", "Tom", "tom@admin.com", "$2a$10$prMsoZN9yy6rgy2KuyE5mOKHAAuJ6wbGF4cr82ouNFYgcjNtTFMPK", "$2a$10$prMsoZN9yy6rgy2KuyE5mO", None)
createUser("Doctor", "Jake", "jake@doctor.com", "$2a$10$Vrg6sD8DupdURM5E6HfnY.Z7e0..qsHLPx4wvSxExXJKQ7EH5HHH6", "$2a$10$Vrg6sD8DupdURM5E6HfnY.", None)
createUser("Patient", "Anna", "anna@patient.com", "$2a$10$1N6zxQnYuULXzW1onF45mOvmD9AGHTbwpGMH1FxeS0PxcQcT.s1LG", "$2a$10$1N6zxQnYuULXzW1onF45mO", "jake@doctor.com")
insertUserData("anna@patient.com", 1, "Worst day ever", "2018-04-01 09:54:01")
insertUserData("anna@patient.com", 5, "Decent day", "2018-04-10 09:54:01")
insertUserData("anna@patient.com", 9, "Went and got ice cream", "2018-04-15 09:54:01")
insertUserData("anna@patient.com", 2, "Horrible day", "2018-04-20 09:54:01")
insertUserData("anna@patient.com", 4, "Better", "2018-04-23 09:54:01")
insertUserData("anna@patient.com", 6, "Pretty good", "2018-04-26 09:54:01")
insertUserData("anna@patient.com", 2, "BAD!!!", "2018-05-01 09:54:01")
insertUserData("anna@patient.com", 10, "AMAZING!!!", "2018-05-08 09:54:01")
insertUserData("anna@patient.com", 7, "Pretty good", "2018-05-14 09:54:01")
insertUserData("anna@patient.com", 9, "Really goo day", "2018-05-20 09:54:01")