<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Login</title>
    </head>
    <body>
        <h2>Login</h2>
        <form action="${pageContext.request.contextPath}/security/login" method="post">
            <div>
                <label for="login">E-mail:</label>
                <input type="text" id="login" name="login" required>
            </div>
            <div>
                <label for="password">Hasło:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div>
                <button type="submit">Zaloguj</button>
            </div>
        </form>
        <c:if test="${not empty error}">
            <p style="color: red;">${error}</p>
        </c:if>
    </body>
</html>