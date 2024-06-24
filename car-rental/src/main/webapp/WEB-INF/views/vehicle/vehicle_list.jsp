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
        <style>
            centered {
                margin-left: 0 auto;
                margin-right: 0 auto;
            }
            body {
                width: 800px;
            }
            .content {
                margin-left: auto;
                margin-right: auto;
            }
            button {
                margin: 10px;
            }
        </style>
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
    <div class="centered">
        <h1>Wypożyczalnia pojazdów</h1>
        <c:if test="${not empty sessionScope.user}">
            <c:choose>
                <c:when test="${fn:contains(sessionScope.userGroups, 'ROLE_ADMIN')}">
                    <h2>Witaj, ${sessionScope.username}!</h2>
                </c:when>
                <c:otherwise>
                    <h2>Witaj, ${sessionScope.username}, oto nasza oferta!</h2>
                </c:otherwise>
            </c:choose>
        </c:if>
        <table>
            <thead>
            <tr>
                <th>Marka</th>
                <th>Model</th>
                <th>Cena/dzień</th>
                <th>Status</th>
                <th>Rezerwacja</th>
                <c:if test="${fn:contains(sessionScope.userGroups, 'ROLE_ADMIN')}">
                    <th>Operacje</th>
                </c:if>
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
                                <c:choose>
                                    <c:when test="${not empty sessionScope.user}">
                                        <button type="button" onclick="window.location.href = '<c:url value='/reservation/form?vehicleId=${vehicle.id}' />';">Rezerwuj</button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button" onclick="window.location.href = '<c:url value='/security/login' />';">Rezerwuj</button>
                                    </c:otherwise>
                                </c:choose>
                            </td>
<%--                    <td>--%>
<%--                        <c:choose>--%>
<%--                            <c:when test="${reserved}">--%>
<%--                                X--%>
<%--                            </c:when>--%>
<%--                            <c:otherwise>--%>
<%--                                <c:choose>--%>
<%--                                    <c:when test="${not empty sessionScope.user}">--%>
<%--                                        <button type="button" onclick="reserveVehicle(${vehicle.id}, '#status-${vehicle.id}')">Rezerwuj</button>--%>
<%--                                    </c:when>--%>
<%--                                    <c:otherwise>--%>
<%--                                        <button type="button" onclick="window.location.href = '<c:url value='/security/login' />';">Rezerwuj</button>--%>
<%--                                    </c:otherwise>--%>
<%--                                </c:choose>--%>
<%--                            </c:otherwise>--%>
<%--                        </c:choose>--%>
<%--                    </td>--%>
                    <td>
                        <!-- Sprawdzenie, czy użytkownik jest w grupie USER_ADMIN -->
                        <c:if test="${fn:contains(sessionScope.userGroups, 'ROLE_ADMIN')}">
                            <button type="button" onclick="window.location.href = '<c:url value='/vehicle/edit/${vehicle.id}' />';">Edytuj</button>
                            <button type="button" onclick="window.location.href = '<c:url value='/vehicle/remove/${vehicle.id}' />';">Usuń</button>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:if test="${fn:contains(sessionScope.userGroups, 'ROLE_ADMIN')}">
            <div><button type="button" onclick="window.location.href = '<c:url value='/vehicle/edit' />';">Dodaj pojazd</button></div>
        </c:if>
        <c:if test="${empty sessionScope.user}">
            <div><button type="button" onclick="window.location.href = '<c:url value='/security/login' />';">Zaloguj</button></div>
            <div><button type="button" onclick="window.location.href = '<c:url value='/security/register' />';">Zarejestruj</button></div>
        </c:if>
        <div>
            <c:if test="${not empty sessionScope.user}">
                <form action="${pageContext.request.contextPath}/security/logout" method="post">
                    <button type="submit">Wyloguj</button>
                </form>
            </c:if>
        </div>
    </div>
    </body>
</html>
