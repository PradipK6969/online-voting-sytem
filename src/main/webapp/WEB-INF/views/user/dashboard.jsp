<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Welcome, ${user.fullName}</h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/user/dashboard">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/user/elections">Elections</a></li>
                    <li><a href="${pageContext.request.contextPath}/user/voting-history">Voting History</a></li>
                    <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
                </ul>
            </nav>
        </header>

        <section class="dashboard">
            <div class="card">
                <h2>Active Elections</h2>
                <c:forEach items="${elections}" var="election">
                    <c:if test="${election.status eq 'ACTIVE'}">
                        <div class="election-item">
                            <h3>${election.title}</h3>
                            <p>${election.description}</p>
                            <p>${election.startDate} to ${election.endDate}</p>
                            <a href="${pageContext.request.contextPath}/user/vote?electionId=${election.id}" class="btn">Vote Now</a>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <div class="card">
                <h2>Upcoming Elections</h2>
                <c:forEach items="${elections}" var="election">
                    <c:if test="${election.status eq 'UPCOMING'}">
                        <div class="election-item">
                            <h3>${election.title}</h3>
                            <p>${election.description}</p>
                            <p>Starts on: ${election.startDate}</p>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </section>
    </div>
</body>
</html>