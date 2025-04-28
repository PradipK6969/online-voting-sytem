<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Election</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>Add New Election</h1>
            <nav>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/manage-elections">Manage Elections</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/manage-users">Manage Users</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/logout">Logout</a></li>
                </ul>
            </nav>
        </header>

        <section class="add-election">
            <c:if test="${not empty errorMessage}">
                <div class="alert error">${errorMessage}</div>
            </c:if>

            <c:if test="${not empty successMessage}">
                <div class="alert success">${successMessage}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/admin/save-election" method="post">
                <div class="form-group">
                    <label for="title">Title:</label>
                    <input type="text" id="title" name="title" required>
                </div>
                <div class="form-group">
                    <label for="description">Description:</label>
                    <textarea id="description" name="description" required></textarea>
                </div>
                <div class="form-group">
                    <label for="startDate">Start Date:</label>
                    <input type="datetime-local" id="startDate" name="startDate" required>
                </div>
                <div class="form-group">
                    <label for="endDate">End Date:</label>
                    <input type="datetime-local" id="endDate" name="endDate" required>
                </div>
                <button type="submit" class="btn">Create Election</button>
            </form>
        </section>
    </div>
</body>
</html>