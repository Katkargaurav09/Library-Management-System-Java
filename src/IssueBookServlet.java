import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class IssueBookServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String bookId = request.getParameter("bookid");
        String studentId = request.getParameter("studentid");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_db", "root", "root");

            // Check if student exists
            String checkStudent = "SELECT name, phone FROM students WHERE student_id = ?";
            PreparedStatement psStudent = con.prepareStatement(checkStudent);
            psStudent.setInt(1, Integer.parseInt(studentId));
            ResultSet rsStudent = psStudent.executeQuery();

            if (!rsStudent.next()) {
                out.println("<h3 style='color:red;'>Student not found. Cannot issue book.</h3>");
                return;
            }

            String studentName = rsStudent.getString("name");
            String studentMobile = rsStudent.getString("phone");

            // Check if book exists and is available
            String checkBook = "SELECT quantity, issued FROM book WHERE book_id = ?";
            PreparedStatement psBook = con.prepareStatement(checkBook);
            psBook.setInt(1, Integer.parseInt(bookId));
            ResultSet rsBook = psBook.executeQuery();

            if (!rsBook.next()) {
                out.println("<h3 style='color:red;'>Book not found.</h3>");
                return;
            }

            int quantity = rsBook.getInt("quantity");
            int issued = rsBook.getInt("issued");

            if (issued >= quantity) {
                out.println("<h3 style='color:red;'>No copies available to issue.</h3>");
                return;
            }

            // Insert into issued_book table
            String insertIssue = "INSERT INTO issued_book (book_id, student_id, student_name, student_mobile, issue_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement psIssue = con.prepareStatement(insertIssue);
            psIssue.setInt(1, Integer.parseInt(bookId));
            psIssue.setInt(2, Integer.parseInt(studentId));
            psIssue.setString(3, studentName);
            psIssue.setString(4, studentMobile);
            psIssue.setDate(5, java.sql.Date.valueOf(LocalDate.now()));

            int insertStatus = psIssue.executeUpdate();

            // Update issued count in book table
            if (insertStatus > 0) {
                String updateBook = "UPDATE book SET issued = issued + 1 WHERE book_id = ?";
                PreparedStatement psUpdate = con.prepareStatement(updateBook);
                psUpdate.setInt(1, Integer.parseInt(bookId));
                psUpdate.executeUpdate();

                out.println("<h3 style='color:green;'>Book issued successfully!</h3>");
            } else {
                out.println("<h3 style='color:red;'>Failed to issue the book.</h3>");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
