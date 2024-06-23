<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Rejestracja</title>
    </head>
    <body>
        <h2>Rejestracja</h2>
        <form action="${pageContext.request.contextPath}/security/register" method="post">
            <div>
                <label for="login">E-mail:</label>
                <input type="text" id="login" name="login" required>
            </div>
            <div>
                <label for="password">Hasło:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div>
                <label for="password">Powtórz hasło:</label>
                <input type="password" id="re_password" name="re_password" required>
            </div>
            <div>
                <button type="submit">Zarejestruj</button>
            </div>
        </form>
        <c:if test="${not empty error}">
            <p style="color: red;">${error}</p>
        </c:if>
    </body>
</html>