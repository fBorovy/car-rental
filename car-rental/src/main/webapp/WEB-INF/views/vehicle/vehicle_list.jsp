<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%-- fmt:setLocale konfiguruje ustawienia lokalne dla tej strony na polskie; używane przez fmt:formatNumber przy formatowaniu liczby--%>
<fmt:setLocale value = "pl_PL"/>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Wypożyczalnia pojazdów</title>
    </head>
    <body>
        <table>
            <thead>
                <tr>
                    <th>Marka</th>
                    <th>Model</th>
                    <th>Cena</th>
                    <th>Operacje</th>
                </tr>
            </thead>
            <tbody>
                <%-- c:forEach renderuje zawartość dla każdego elementu na liscie, wewnatrz znacznika kolejne elementy będą dostępne pod nazwą wskazaną przez atrybut var --%>
                <c:forEach items='${vehicleList}' var='vehicle'>
                    <tr>
                        <%-- fn:escapeXml(value) dodaje kody ucieczki jeśli tekst zwiera znaczniki - zabezpiecza przez atakiem XSS --%>
                        <td>${fn:escapeXml(vehicle.brand)}</td>
                        <td>${fn:escapeXml(vehicle.model)}</td>
                        <td>
                            <%-- c:url dodaje do url nazwę aplikacji (context root) oraz identifykator sesji jsessionid jeśli sesja jest włączona i brak obsługi ciasteczek --%>
                            <%--<a href="<c:url value='/vehicle/edit/${vehicle.id}'/>">Edytuj</a>,
                            <a href="<c:url value='/vehicle/remove/${vehicle.id}'/>">Usuń</a>--%>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <a href="<c:url value='/vehicle/edit'/>">Dodaj pojazd</a>
   </body>
</html>
