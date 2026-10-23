<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Register</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
  <div class="card">
    <h2>Register</h2>
    <c:if test="${not empty error}"><div class="msg error"><c:out value="${error}"/></div></c:if>
    <form method="post" action="${pageContext.request.contextPath}/register">
      <input type="text" name="username" placeholder="Username" value="<c:out value='${param.username}'/>" required>
      <input type="email" name="email" placeholder="Email" value="<c:out value='${param.email}'/>" required>
      <input type="password" name="password" placeholder="Password" required>
      <input type="password" name="confirmPassword" placeholder="Confirm Password" required>
      <button type="submit">Sign Up</button>
    </form>
    <div class="foot">Already have an account? <a href="${pageContext.request.contextPath}/login">Log In</a></div>
  </div>
</body>
</html>
