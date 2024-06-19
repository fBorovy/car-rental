<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%-- fmt:setLocale konfiguruje ustawienia lokalne dla tej strony na polskie; używane przez fmt:formatNumber przy formatowaniu liczby --%>
<fmt:setLocale value="pl_PL" />
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Wypożyczalnia pojazdów</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        function reserveVehicle(vehicleId, statusCell) {
            $.ajax({
                url: '<c:url value="/vehicle/reserve" />',
                type: 'POST',
                data: { id: vehicleId },
                success: function (response) {
                    if (response.success) {
                    } else {
                        alert('Nie udało się zaktualizować statusu');
                    }
                },
                error: function () {
                    alert('Wystąpił błąd podczas aktualizacji statusu');
                }
            });
        }
    </script>
</head>

<body>
<table>
    <thead>
    <tr>
        <th>Marka</th>
        <th>Model</th>
        <th>Cena/dzień</th>
        <th>Status</th>
        <th>Operacje</th>
        <th>Rezerwacja</th>
    </tr>
    </thead>
    <tbody>
    <%-- c:forEach renderuje zawartość dla każdego elementu na liście, wewnątrz znacznika kolejne elementy będą dostępne pod nazwą wskazaną przez atrybut var --%>
    <c:forEach items='${vehicleList}' var='vehicle'>
        <tr>
                <%-- fn:escapeXml(value) dodaje kody ucieczki jeśli tekst zawiera znaczniki - zabezpiecza przed atakiem XSS --%>
            <td>${fn:escapeXml(vehicle.brand)}</td>
            <td>${fn:escapeXml(vehicle.model)}</td>
            <td><fmt:formatNumber value="${vehicle.price_per_day}" type="number" />PLN</td>
                    <td id="status-${vehicle.id}">
                        <c:choose>
                            <c:when test="${vehicle.reserved}">
                                ZAREZERWOWANO
                            </c:when>
                            <c:otherwise>
                                DOSTĘPNY
                            </c:otherwise>
                        </c:choose>
                    </td>
            <td>
                    <%-- c:url dodaje do url nazwę aplikacji (context root) oraz identyfikator sesji jsessionid jeśli sesja jest włączona i brak obsługi ciasteczek --%>
                <a href="<c:url value='/vehicle/edit/${vehicle.id}' />">Edytuj</a>,
                <a href="<c:url value='/vehicle/remove/${vehicle.id}' />">Usuń</a>
            </td>
            <td>
                <c:choose>
                    <c:when test="${reserved}">
                        X
                    </c:when>
                    <c:otherwise>
                        <button type="button" onclick="reserveVehicle(${vehicle.id}, '#status-${vehicle.id}')">Rezerwuj</button>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<a href="<c:url value='/vehicle/edit' />">Dodaj pojazd</a>
</body>

</html>
