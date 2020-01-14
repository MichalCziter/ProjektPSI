package jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ChangePassword
 */
@WebServlet("/changepassword")
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangePassword() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("user");
		String password = request.getParameter("password");
		System.out.println(name + " " + password);
		String dbName = null;
		String dbPassword = null;
		String sql = "update registration set password = ? where name=?";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/baza_danych", "root",
					"12345678");
			//pobierz hasla usera z bazy danych
			sql = "select id, password, date from passwords where name=?";
			java.sql.PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			boolean bylo_takie_haslo = false;
			int liczba_hasel = 0;
			int id_najstarszego_hasla = 0;
			Timestamp data_najstarszego_hasla = new Timestamp(System.currentTimeMillis());
			while (rs.next()) {
				if(liczba_hasel==0) {
					id_najstarszego_hasla = rs.getInt(1);
					data_najstarszego_hasla = rs.getTimestamp(3);
				}
				liczba_hasel++;
				System.out.println("POROWNYWANIE HASEL: " + liczba_hasel);
				if(rs.getString(2).equals(password)) {
					bylo_takie_haslo = true;
				}
				
				if(!data_najstarszego_hasla.before(rs.getTimestamp(3))) {
					id_najstarszego_hasla = rs.getInt(1);
					data_najstarszego_hasla = rs.getTimestamp(3);
				}
				
				
			}
			//porownaj czy ktores haslo pasuje do nowego hasla
			//jezeli tak, zwroc informacje ze takie haslo juz bylo
			if(bylo_takie_haslo) {
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
				request.setAttribute("loginTime", lastLogin);
				request.setAttribute("num_of_bad_login", num_of_bad_login);
				request.setAttribute("lastLogout", lastLogout);
				request.setAttribute("newPassword", "BYLO");
				request.getRequestDispatcher("account.jsp").forward(request, response);
			} else {
				//jezeli nie
				//sprawdz czy ilosc hasel jest rowna 5
				//jezeli tak, usun najstarsze haslo i wstaw nowe haslo z data do tabeli
				if(liczba_hasel==5) {
					sql = "delete from passwords where id=?";
					ps = conn.prepareStatement(sql);
					ps.setInt(1, id_najstarszego_hasla);
					ps.executeUpdate();
					
					sql = "update registration set password = ? where name=?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, password);
					ps.setString(2, name);
					ps.executeUpdate();

					Calendar calendar = Calendar.getInstance();
					java.util.Date currentTime = calendar.getTime();
					long time = currentTime.getTime();
					
					sql = "insert into passwords (name, password, date) values (?, ?, ?)";
					ps = conn.prepareStatement(sql);
					ps.setString(1, name);
					ps.setString(2, password);
					ps.setTimestamp(3, new Timestamp(time));
					ps.executeUpdate();
					
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
					request.setAttribute("loginTime", lastLogin);
					request.setAttribute("num_of_bad_login", num_of_bad_login);
					request.setAttribute("lastLogout", lastLogout);
					request.setAttribute("newPassword", password);
					request.getRequestDispatcher("account.jsp").forward(request, response);
					
				} else {
					//jezeli nie, wstaw nowe haslo z data do tabeli
					sql = "update registration set password = ? where name=?";
					ps = conn.prepareStatement(sql);
					ps.setString(1, password);
					ps.setString(2, name);
					ps.executeUpdate();

					Calendar calendar = Calendar.getInstance();
					java.util.Date currentTime = calendar.getTime();
					long time = currentTime.getTime();
					
					sql = "insert into passwords (name, password, date) values (?, ?, ?)";
					ps = conn.prepareStatement(sql);
					ps.setString(1, name);
					ps.setString(2, password);
					ps.setTimestamp(3, new Timestamp(time));
					ps.executeUpdate();
					
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
					request.setAttribute("loginTime", lastLogin);
					request.setAttribute("num_of_bad_login", num_of_bad_login);
					request.setAttribute("lastLogout", lastLogout);
					request.setAttribute("newPassword", password);
					request.getRequestDispatcher("account.jsp").forward(request, response);
				}
			}
			

			

			
			


			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
