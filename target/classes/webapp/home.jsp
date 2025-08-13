<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
  <meta charset="UTF-8">
  <title>首頁 - UniWatson 二手書</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light">

  <!-- 統一導覽列（帶 active=home 高亮） -->
  <jsp:include page="navbar.jsp">
    <jsp:param name="active" value="home"/>
  </jsp:include>

  <div class="container py-4" style="max-width: 980px;">
    <!-- 書籍搜尋 -->
    <div class="card shadow-sm mb-4">
      <div class="card-header bg-primary text-white fs-4">書籍搜尋</div>
      <div class="card-body">
        <form class="row g-2" action="${pageContext.request.contextPath}/books" method="get">
          <div class="col-12">
            <input type="text" class="form-control form-control-lg"
                   name="q" placeholder="輸入書名、ISBN或作者">
          </div>
          <div class="col-12 text-end">
            <button class="btn btn-primary btn-lg">搜尋</button>
          </div>
        </form>
      </div>
    </div>

    <!-- 熱門書籍（預留，之後可填資料） -->
    <div class="card shadow-sm">
      <div class="card-header bg-info text-white fs-4">熱門書籍</div>
      <div class="card-body">
        <div class="text-muted">尚無資料</div>
      </div>
    </div>
  </div>
</body>
</html>
