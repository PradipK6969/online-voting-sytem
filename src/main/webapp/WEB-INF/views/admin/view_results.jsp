<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Election Results</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Results for ${election.title}</h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/manage-elections">Manage Elections</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/manage-users">Manage Users</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/logout">Logout</a></li>
                </ul>
            </nav>
        </header>

        <section class="election-results">
            <a href="${pageContext.request.contextPath}/admin/manage-elections" class="btn">Back to Elections</a>

            <h2>Vote Count</h2>
            <table>
                <thead>
                    <tr>
                        <th>Candidate</th>
                        <th>Party</th>
                        <th>Votes</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${results}" var="result">
                        <tr>
                            <td>${result[1]}</td>
                            <td>${result[2]}</td>
                            <td>${result[3]}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <div class="chart-container">
                <canvas id="resultsChart"></canvas>
            </div>
        </section>
    </div>

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
    <script>
        const resultsData = [
            <c:forEach items="${results}" var="result">
                {
                    candidate: '${result[1]}',
                    party: '${result[2]}',
                    votes: ${result[3]}
                },
            </c:forEach>
        ];

        renderResultsChart(resultsData);
    </script>
</body>
</html>