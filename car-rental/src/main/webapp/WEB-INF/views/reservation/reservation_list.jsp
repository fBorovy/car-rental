<%--
  Created by IntelliJ IDEA.
  User: Konrad
  Date: 24.06.2024
  Time: 12:10
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Lista rezerwacji</title>
    <style>
        .centered {
            margin-left: auto;
            margin-right: auto;
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
</head>
<body>
<div class="centered">
    <h1>Lista rezerwacji</h1>
    <table>
        <thead>
        <tr>
            <th>Data rozpoczęcia</th>
            <th>Data zakończenia</th>
            <th>Data rezerwacji</th>
            <th>Pojazd</th>
            <th>Cena</th>
            <th>Status płatności</th>
            <th>Status rezerwacji</th>
            <th>Właścicel rezerwacji</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${reservationList}" var="reservation">
            <tr>
                <td><fmt:formatDate value="${reservation.reservation_start}" pattern="yyyy-MM-dd" /></td>
                <td><fmt:formatDate value="${reservation.reservation_end}" pattern="yyyy-MM-dd" /></td>
                <td><fmt:formatDate value="${reservation.reservation_date}" pattern="yyyy-MM-dd" /></td>
                <td>
                    <c:choose>
                        <c:when test="${not empty reservation.vehicle}">
                            ${reservation.vehicle.brand} ${reservation.vehicle.model}
                        </c:when>
                        <c:otherwise>
                            ID: ${reservation.reserved_vehicle_id}
                        </c:otherwise>
                    </c:choose>
                </td>
                <td><fmt:formatNumber value="${reservation.price}" type="currency" currencySymbol="PLN" /></td>
                <td>
                    <c:choose>
                        <c:when test="${reservation.payment_status==true}">
                            Opłacona
                        </c:when>
                        <c:otherwise>
                            Nieopłacona
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${reservation.reservation_status==true}">
                            Aktywna
                        </c:when>
                        <c:otherwise>
                            Anulowana
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>${usernames[reservation.id]}</td> <!-- Dodana komórka -->
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
