# 📚 Library Management System (Java, Servlets, JDBC)

A web-based Library Management System developed using Java Servlets, JDBC, HTML, and CSS. It allows students and librarians to manage library operations like book issue, return, and fine calculation efficiently.

---

## 🔧 Tech Stack

- **Frontend:** HTML, CSS  
- **Backend:** Java Servlets  
- **Database:** MySQL  
- **Server:** Apache Tomcat  
- **Tools Used:** Eclipse/VS Code, Git, GitHub  

---

## ✨ Features

### 👤 Student
- Register and Login  
- View available books  
- Issue a book  
- Return a book  
- View list of issued books  
- Fine calculation for late returns  

### 🧑‍🏫 Librarian
- Register and Login  
- Add new books  
- Update/delete book details  
- View issued books  
- Manage returns and fines  

---

## 🧪 How to Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/Library-Management-System-Java.git
   
2. Import the project into Eclipse or Visual Studio Code as a Dynamic Web Project.
  Setup MySQL Database:
  Create a database named library_db

3. Import library.sql to create necessary tables
  Configure JDBC Connection:
  Open DAO files and update DB URL, username, and password

4. Deploy on Apache Tomcat:
  Right-click the project → Run on Server

5. Access the application:
  http://localhost:8080/LibraryManagementSystem/

---

## 📌 Database Tables
- students – stores student login and profile information
- librarians – stores librarian login details
- books – stores book records (ID, name, author, quantity)
- issued_books – tracks issued books, return status, and fine

---

## Screenshots

### Login Page
![Login Page](images/Login%20Page.png)

### Student Dashboard
![Student Dashboard](images/Student%20Dashboard.png)

### Librarian Dashboard
![Librarian Dashboard](images/Librarian%20Dashboard.png)

---

## 🙌 Contributing
Contributions are welcome! Fork the repository, create a new branch, make your changes, and submit a pull request.

---

Let me know if you’d like a sample `library.sql` file or a custom license file to go along with it.

