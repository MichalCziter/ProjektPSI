<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Logowanie</title>
</head>
<body>
<div align="center">
<form action="login" method="post">
User name: <input type="text" name="user" required="required">
Password: <input type="password" name="password" required="required">
<input type="submit" value="ZALOGUJ">
</form>
</div>
<div align="center">
<form action="registration.jsp">
    <input type="submit" value="ZAREJESTRUJ">
</form>
</div>
</body>
</html>