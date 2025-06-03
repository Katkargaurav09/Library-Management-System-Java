import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class FineSectionServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Fine Section</title>");
        out.println("<link rel='stylesheet' type='text/css' href='style.css'>");
        out.println("</head><body>");

        out.println(
                "<p><b>Note : </b> If the book is not returned within 15 days then fine will be 10 Rs. per day.</p>");
        out.println("<h2>Return Book Fine Section</h2>");
        out.println("<table border='1' cellpadding='10'>");
        out.println("<tr>");
        out.println("<th>Book ID</th>");
        out.println("<th>Student ID</th>");
        out.println("<th>Student Name</th>");
        out.println("<th>Mobile</th>");
        out.println("<th>Issued Date</th>");
        out.println("<th>Return Date</th>");
        out.println("<th>Days Late</th>");
        out.println("<th>Status</th>");
        out.println("<th>Fine (₹)</th>");
        out.println("</tr>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_db", "root", "root");

            String query = "SELECT * FROM return_book";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                out.println("<tr>");
                out.println("<td>" + rs.getInt("book_id") + "</td>");
                out.println("<td>" + rs.getInt("student_id") + "</td>");
                out.println("<td>" + rs.getString("student_name") + "</td>");
                out.println("<td>" + rs.getString("student_mobile") + "</td>");
                out.println("<td>" + rs.getDate("issued_date") + "</td>");
                out.println("<td>" + rs.getDate("return_date") + "</td>");
                out.println("<td>" + rs.getInt("days") + "</td>");
                out.println("<td>" + rs.getString("status") + "</td>");
                out.println("<td>" + rs.getInt("fine") + "</td>");
                out.println("</tr>");
            }

            if (!hasData) {
                out.println("<tr><td colspan='9'>No records found.</td></tr>");
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            out.println("<tr><td colspan='9'>Error: " + e.getMessage() + "</td></tr>");
            e.printStackTrace(out);
        }

        out.println("</table>");
    }
}
