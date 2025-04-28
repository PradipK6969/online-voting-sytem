<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Manage Elections</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Manage Elections</h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/manage-elections">Manage Elections</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/manage-users">Manage Users</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/logout">Logout</a></li>
                </ul>
            </nav>
        </header>

        <section class="manage-elections">
            <c:if test="${not empty errorMessage}">
                <div class="alert error">${errorMessage}</div>
            </c:if>

            <c:if test="${not empty successMessage}">
                <div class="alert success">${successMessage}</div>
            </c:if>

            <a href="${pageContext.request.contextPath}/admin/add-election" class="btn">Add New Election</a>

            <table>
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Description</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${elections}" var="election">
                        <tr>
                            <td>${election.title}</td>
                            <td>${election.description}</td>
                            <td>${election.startDate}</td>
                            <td>${election.endDate}</td>
                            <td>${election.status}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/manage-candidates?electionId=${election.id}" class="btn">Manage Candidates</a>
                                <a href="${pageContext.request.contextPath}/admin/view-results?electionId=${election.id}" class="btn">View Results</a>
                                <a href="${pageContext.request.contextPath}/admin/delete-election?id=${election.id}" class="btn danger">Delete</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </section>
    </div>
</body>
</html>