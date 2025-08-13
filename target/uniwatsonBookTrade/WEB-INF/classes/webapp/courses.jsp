<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
  <meta charset="UTF-8">
  <title>課程用書 - UniWatson 二手書</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>.page-hero{background:linear-gradient(135deg,#f8fbff,#eef3ff);border-bottom:1px solid #e9eef7}</style>
</head>
<body>
  <jsp:include page="navbar.jsp"/>

  <section class="page-hero py-4">
    <div class="container">
      <h3 class="mb-0">課程用書</h3>
      <div class="text-muted">查看每門課指定教材並快速前往書籍頁面</div>
    </div>
  </section>

  <div class="container my-4">
    <c:if test="${empty courses}">
      <div class="alert alert-info">目前沒有課程資料</div>
    </c:if>

    <c:if test="${not empty courses}">
      <table class="table table-hover">
        <thead class="table-light">
          <tr><th>課號</th><th>課名</th><th>授課教師</th><th>用書</th><th>ISBN</th><th></th></tr>
        </thead>
        <tbody>
        <c:forEach var="c" items="${courses}">
          <tr>
            <td>${c.courseId}</td>
            <td>${c.name}</td>
            <td>${c.professor}</td>
            <td>${c.bookName}</td>
            <td>${c.isbn}</td>
            <td>
              <a class="btn btn-sm btn-outline-primary"
                 href="${pageContext.request.contextPath}/books?action=view&isbn=${c.isbn}">
                查看書籍
              </a>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:if>
  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
