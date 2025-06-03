import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.sql.*;

public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String sName = request.getParameter("sname");
        String sPhone = request.getParameter("sphone");
        String sUsername = request.getParameter("susername");
        String sPassword = request.getParameter("spassword");

        String lName = request.getParameter("lname");
        String lPhone = request.getParameter("lphone");
        String lUsername = request.getParameter("lusername");
        String lPassword = request.getParameter("lpassword");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_db", "root", "root");

            PreparedStatement ps;

            if (sUsername != null && sPassword != null) {
                ps = con.prepareStatement("INSERT INTO students (name, username, password, phone) VALUES (?, ?, ?, ?)");
                ps.setString(1, sName);
                ps.setString(2, sUsername);
                ps.setString(3, sPassword);
                ps.setString(4, sPhone);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    response.sendRedirect("login.html");
                } else {
                    out.println("<h2>Student Registration Failed!</h2>");
                }
            } else if (lUsername != null && lPassword != null) {
                ps = con.prepareStatement(
                        "INSERT INTO librarians (name, username, password, phone) VALUES (?, ?, ?, ?)");
                ps.setString(1, lName);
                ps.setString(2, lUsername);
                ps.setString(3, lPassword);
                ps.setString(4, lPhone);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    response.sendRedirect("login.html");
                } else {
                    out.println("<h2>Librarian Registration Failed!</h2>");
                }
            } else {
                out.println("<h2>Invalid Form Submission</h2>");
            }

            con.close();
        } catch (SQLIntegrityConstraintViolationException e) {
            out.println("<h2>User Already Exists!</h2>");
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }
}
