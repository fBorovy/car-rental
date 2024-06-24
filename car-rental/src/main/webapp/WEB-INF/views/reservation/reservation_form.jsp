<%--
  Created by IntelliJ IDEA.
  User: Konrad
  Date: 24.06.2024
  Time: 12:10
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Formularz rezerwacji pojazdu</title>
    <style>.error { color: red; }</style>
    <script>
        function calculateTotalPrice() {
            const pricePerDay = parseFloat(document.getElementById("price_per_day").value);
            const startDate = new Date(document.getElementById("start_date").value);
            const endDate = new Date(document.getElementById("end_date").value);
            if (pricePerDay && startDate && endDate && endDate >= startDate) {
                const diffTime = Math.abs(endDate - startDate);
                const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
                document.getElementById("total_price").value = (pricePerDay * diffDays).toFixed(2) + " PLN";
            } else {
                document.getElementById("total_price").value = "";
            }
        }
    </script>
</head>
<body>
<h3>Formularz rezerwacji pojazdu</h3>
<form method="post">
    <table>
        <tr>
            <td>Rezerwowany samochód:</td>
            <td>
                <c:out value="${brand}"/> <c:out value="${model}"/>
            </td>
            <td><span class="error">${errors.vehicle_id}</span></td>
        </tr>
        <tr>
            <td>Początek rezerwacji:</td>
            <td><input type="date" name="start_date" id="start_date" onchange="calculateTotalPrice()"></td>
            <td><span class="error">${errors.start_date}</span></td>
        </tr>
        <tr>
            <td>Koniec rezerwacji:</td>
            <td><input type="date" name="end_date" id="end_date" onchange="calculateTotalPrice()"></td>
            <td><span class="error">${errors.end_date}</span></td>
        </tr>
        <tr>
            <td>Cena za dzień:</td>
            <td><input type="text" id="price_per_day" value="${fn:escapeXml(price)}" readonly></td>
        </tr>
        <tr>
            <td>Całkowita cena:</td>
            <td><input type="text" id="total_price" name="total_price" readonly></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="hidden" name="vehicle_id" value="$${fn:escapeXml(param.vehicleId)}"></td>
        </tr>
    </table>
    <input type="submit" value="Rezerwuj">
</form>
<script>
    document.addEventListener("DOMContentLoaded", function() {
        const startDateInput = document.getElementById("start_date");
        const endDateInput = document.getElementById("end_date");

        const invalidDates = ['2024-06-24'];

        // Set the minimum date to today
        let today = new Date().toISOString().split("T")[0];
        startDateInput.setAttribute("min", today);
        endDateInput.setAttribute("min", today);

        // Event listener for start date
        startDateInput.addEventListener("change", function() {
            let startDate = startDateInput.value;
            endDateInput.setAttribute("min", startDate);
        });

        // Event listener for end date
        endDateInput.addEventListener("change", function() {
            let endDate = endDateInput.value;
            startDateInput.setAttribute("max", endDate);
        });
    });
</script>
</body>
</html>

