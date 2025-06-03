import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class AddBookServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String bookId = request.getParameter("bookid");
        String bookName = request.getParameter("bookname");
        String author = request.getParameter("author");
        String quantity = request.getParameter("quantity");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_db", "root", "root");

            String query = "INSERT INTO book (book_id, name, author, quantity, issued) VALUES (?, ?, ?, ?, 0)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, Integer.parseInt(bookId));
            ps.setString(2, bookName);
            ps.setString(3, author);
            ps.setInt(4, Integer.parseInt(quantity));

            int i = ps.executeUpdate();
            if (i > 0) {
                response.sendRedirect("./librarianDashboard.html");
            } else {
                out.println("<h3>Failed to add the book.</h3>");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
