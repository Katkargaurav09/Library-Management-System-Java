import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;
import java.util.Date;

public class ReturnBookServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String bookId = request.getParameter("bookid");
        String studentId = request.getParameter("studentid");

        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_db", "root", "root");

            // Get the student's details
            String studentQuery = "SELECT name, phone FROM students WHERE student_id = ?";
            PreparedStatement studentStmt = con.prepareStatement(studentQuery);
            studentStmt.setInt(1, Integer.parseInt(studentId));
            ResultSet studentRs = studentStmt.executeQuery();

            if (!studentRs.next()) {
                out.println("<h3>Student not found!</h3>");
                return;
            }

            String studentName = studentRs.getString("name");
            String studentMobile = studentRs.getString("phone");

            // Get the issued book details
            String bookQuery = "SELECT issue_date FROM issued_book WHERE book_id = ? AND student_id = ?";
            PreparedStatement bookStmt = con.prepareStatement(bookQuery);
            bookStmt.setInt(1, Integer.parseInt(bookId));
            bookStmt.setInt(2, Integer.parseInt(studentId));
            ResultSet bookRs = bookStmt.executeQuery();

            if (!bookRs.next()) {
                out.println("<h3>Book not issued to the student!</h3>");
                return;
            }

            Date issuedDate = bookRs.getDate("issue_date");
            Date returnDate = new Date(); // current date

            // Calculate the number of days the book was issued
            long diffInMillies = returnDate.getTime() - issuedDate.getTime();
            long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);

            // Fine calculation: Rs 10 per day after 7 days
            int fine = 0;
            if (diffInDays > 7) {
                fine = (int) ((diffInDays - 7) * 10); // Rs 10 per day after 7 days
            }

            // Insert return book record
            String insertReturnQuery = "INSERT INTO return_book (book_id, student_id, student_name, student_mobile, issued_date, return_date, days, fine) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement returnStmt = con.prepareStatement(insertReturnQuery);
            returnStmt.setInt(1, Integer.parseInt(bookId));
            returnStmt.setInt(2, Integer.parseInt(studentId));
            returnStmt.setString(3, studentName);
            returnStmt.setString(4, studentMobile);
            returnStmt.setDate(5, new java.sql.Date(issuedDate.getTime()));
            returnStmt.setDate(6, new java.sql.Date(returnDate.getTime()));
            returnStmt.setInt(7, (int) diffInDays);
            returnStmt.setInt(8, fine);

            int result = returnStmt.executeUpdate();

            if (result > 0) {
                // Update the issued_books table to mark the book as returned
                String updateIssuedQuery = "DELETE FROM issued_book WHERE book_id = ? AND student_id = ?";
                PreparedStatement updateIssuedStmt = con.prepareStatement(updateIssuedQuery);
                updateIssuedStmt.setInt(1, Integer.parseInt(bookId));
                updateIssuedStmt.setInt(2, Integer.parseInt(studentId));
                updateIssuedStmt.executeUpdate();

                // Update the book's quantity in the book table
                String updateBookQuery = "UPDATE book SET quantity = quantity + 1 WHERE book_id = ?";
                PreparedStatement updateBookStmt = con.prepareStatement(updateBookQuery);
                updateBookStmt.setInt(1, Integer.parseInt(bookId));
                updateBookStmt.executeUpdate();

                out.println("<h3>Book returned successfully! Fine: " + fine + " Rs.</h3>");
            } else {
                out.println("<h3>Failed to return the book.</h3>");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
