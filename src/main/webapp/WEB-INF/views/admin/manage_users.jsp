<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Manage Users</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Manage Users</h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/manage-elections">Manage Elections</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/manage-users">Manage Users</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/logout">Logout</a></li>
                </ul>
            </nav>
        </header>

        <section class="manage-users">
            <c:if test="${not empty errorMessage}">
                <div class="alert error">${errorMessage}</div>
            </c:if>

            <c:if test="${not empty successMessage}">
                <div class="alert success">${successMessage}</div>
            </c:if>

            <table>
                <thead>
                    <tr>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Full Name</th>
                        <th>Date of Birth</th>
                        <th>Verified</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${users}" var="user">
                        <tr>
                            <td>${user.username}</td>
                            <td>${user.email}</td>
                            <td>${user.fullName}</td>
                            <td>${user.dateOfBirth}</td>
                            <td>${user.verified ? 'Yes' : 'No'}</td>
                            <td>
                                <c:if test="${not user.verified}">
                                    <a href="${pageContext.request.contextPath}/admin/verify-user?id=${user.id}" class="btn">Verify</a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </section>
    </div>
</body>
</html>