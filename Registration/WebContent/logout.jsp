<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<p>Uzytkownik: ${user} </p>
<p>Zostal wylogowany o godzinie: ${lastLogout} </p>

<div>
<form action="login.jsp">
    <input type="submit" value="ZALOGUJ">
</form>
</div>
<div>
<form action="registration.jsp">
    <input type="submit" value="ZAREJESTRUJ">
</form>
</div>

</body>
</html>