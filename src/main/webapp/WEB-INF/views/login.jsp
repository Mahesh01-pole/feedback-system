<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Login</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
  <div class="card">
    <h2>Login</h2>
    <c:if test="${not empty error}"><div class="msg error"><c:out value="${error}"/></div></c:if>
    <c:if test="${not empty success}"><div class="msg success"><c:out value="${success}"/></div></c:if>
    <form method="post" action="${pageContext.request.contextPath}/login">
      <input type="text" name="username" placeholder="Username" value="<c:out value='${param.username}'/>" required>
      <input type="password" name="password" placeholder="Password" required>
      <button type="submit">Log In</button>
    </form>
    <div class="foot">Don't have an account? <a href="${pageContext.request.contextPath}/register">Sign Up</a></div>
  </div>
</body>
</html>
