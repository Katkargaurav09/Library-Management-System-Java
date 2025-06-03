import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String sUsername = request.getParameter("susername");
        String sPassword = request.getParameter("spassword");
        String lUsername = request.getParameter("lusername");
        String lPassword = request.getParameter("lpassword");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_db", "root", "root");

            PreparedStatement ps;
            ResultSet rs;

            // Student login
            if (sUsername != null && sPassword != null) {
                ps = con.prepareStatement("SELECT * FROM students WHERE username=? AND password=?");
                ps.setString(1, sUsername);
                ps.setString(2, sPassword);
                rs = ps.executeQuery();

                if (rs.next()) {
                    response.sendRedirect("./studentDashboard.html");
                } else {
                    out.println("<h2>Invalid Student Credentials</h2>");
                }
            }
            // Librarian login
            else if (lUsername != null && lPassword != null) {
                ps = con.prepareStatement("SELECT * FROM librarians WHERE username=? AND password=?");
                ps.setString(1, lUsername);
                ps.setString(2, lPassword);
                rs = ps.executeQuery();

                if (rs.next()) {
                    response.sendRedirect("./librarianDashboard.html");
                } else {
                    out.println("<h2>Invalid Librarian Credentials</h2>");
                }
            } else {
                out.println("<h2>Invalid form submission</h2>");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
