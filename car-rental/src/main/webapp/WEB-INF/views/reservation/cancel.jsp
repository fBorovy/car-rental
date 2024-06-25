<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
        <!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Anulowanie rezerwacji</title>
</head>
<body>
    <h2>Anulowanie rezerwacji</h2>
    <p>Uwaga- tej operacji nie da się cofnąć</p>
    <form action="${pageContext.request.contextPath}/reservation/cancel?id=${reservation.id}" method="post">
        <div>
            <label for="password">Wprowadź swoje hasło:</label>
            <input type="password" id="password" name="password">
        </div>
        <div>
            <button type="submit">Anuluj rezerwację</button>
        </div>
    </form>
    <c:if test="${not empty error}">
        <p style="color: red;">${error}</p>
    </c:if>
</body>
</html>