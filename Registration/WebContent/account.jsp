<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Konto</title>
</head>
<body>

<div align="center">
<form action="logout" method="post">
Zalogowano jako: <input type="text" name="user" required="required" value=${user} readonly>
<input type="submit" value="WYLOGUJ">
</form>
<p>Ostatnie logowanie: ${loginTime} </p>
<p>Ostatnie wylogowanie: ${lastLogout} </p>
<p>Liczba blednych logowan: ${num_of_bad_login} </p>
<form action="changepassword" method="post">
<input type="text" name="user" required="required" value=${user} readonly>
Nowe Haslo: <input type="password" name="password" required="required">
<input type="submit" value="ZMIEN HASLO">
</form>

<c:choose>

  <c:when test="${empty newPassword}">
   <div></div>
  </c:when>
  <c:otherwise>
    <div>Haslo zostalo zmienione na ${newPassword}</div>
  </c:otherwise>
</c:choose>


</body>
</html>