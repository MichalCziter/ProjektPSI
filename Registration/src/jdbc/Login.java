package jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Login() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String name = request.getParameter("user");
			String password = request.getParameter("password");
			System.out.println(name + " " + password);
			String dbName = null;
			String dbPassword = null;
//			String sql = "select * from registration where name=? and password=?";
			String sql = "select * from registration where name=?";
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza_danych", "root",
					"12345678");
			java.sql.PreparedStatement ps = conn.prepareStatement(sql);

			ps.setString(1, name);
//			ps.setString(2,  password);
			ResultSet rs = ps.executeQuery();
			PrintWriter out = response.getWriter();
			while (rs.next()) {
				dbName = rs.getString(2);
				dbPassword = rs.getString("password");
				System.out.println(dbName + " " + dbPassword);
			}

			if (name.equals(dbName) && password.equals(dbPassword)) {
				// zalogowanie i wyslanie informacji o dacie logowania
				Date date = Calendar.getInstance().getTime();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
				String strDate = dateFormat.format(date);

				sql = "update registration set last_login = ? where name=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, strDate);
				ps.setString(2, name);
				ps.executeUpdate();

				out.println("Zalogowano. Czas ostatniego logowania: " + strDate);

				sql = "select * from registration where name=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, name);
				rs = ps.executeQuery();
				String num_of_bad_login = "";
				String lastLogin = "";
				String lastLogout = "";
				while (rs.next()) {
					name = rs.getString(2);
					num_of_bad_login = rs.getString(5);
					lastLogin = rs.getString(4);
					lastLogout = rs.getString(6);
				}

				request.setAttribute("user", name);
				request.setAttribute("loginTime", strDate);
				request.setAttribute("num_of_bad_login", num_of_bad_login);
				request.setAttribute("lastLogout", lastLogout);
				request.getRequestDispatcher("account.jsp").forward(request, response);

			} else if (name.equals(dbName)) {
				int amountOfBadLogin = 0;
				String amountOfBadLoginString = "";
				// dodaj bledne wpisanie hasla do bazy danych

				sql = "select * from registration where name=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, name);
				rs = ps.executeQuery();

				while (rs.next()) {
					amountOfBadLoginString = rs.getString(5);
				}

				if (amountOfBadLoginString == null) {
					amountOfBadLogin = 1;
				} else {
					amountOfBadLogin = Integer.parseInt(amountOfBadLoginString);
					amountOfBadLogin++;
				}

				sql = "update registration set num_of_bad_login = ? where name=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, Integer.toString(amountOfBadLogin));
				ps.setString(2, name);
				ps.executeUpdate();
				// wypisz ile razy uzykownik wpisal zle haslo

				sql = "select * from registration where name=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, name);
				rs = ps.executeQuery();
				while (rs.next()) {
					out.println("UZYTKOWNIK " + rs.getString(2) + " wpisal zle haslo: " + rs.getString(5) + " razy");
				}
			} else {
//				response.sendRedirect("login.jsp");
				RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
				rd.include(request, response);
			}

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

}
