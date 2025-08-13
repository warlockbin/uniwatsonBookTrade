<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-Hant">
<head>
  <meta charset="UTF-8">
  <title>我的商品 - UniWatson 二手書</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <!-- 你的專案若已全域引入 Bootstrap，可省略這行 -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
  <jsp:include page="navbar.jsp">
    <jsp:param name="active" value="sell"/>
  </jsp:include>

  <div class="container my-4">

    <!-- 上架表單 -->
    <div class="card shadow-sm mb-4">
      <div class="card-header bg-primary text-white fw-bold">上架新商品</div>
      <div class="card-body">
        <form class="row g-3" method="post"
              action="${pageContext.request.contextPath}/sell"
              accept-charset="UTF-8">
          <input type="hidden" name="op" value="add">
          <div class="col-12 col-md-3">
            <label class="form-label">ISBN</label>
            <input class="form-control" name="isbn" required>
          </div>
          <div class="col-12 col-md-3">
            <label class="form-label">書名（可選）</label>
            <input class="form-control" name="name" placeholder="若新 ISBN 建議填寫">
          </div>
          <div class="col-12 col-md-3">
            <label class="form-label">作者（可選）</label>
            <input class="form-control" name="author">
          </div>
          <div class="col-12 col-md-2">
            <label class="form-label">價格</label>
            <input class="form-control" name="price" type="number" min="0" required>
          </div>
          <div class="col-12 col-md-1 d-flex align-items-end">
            <button class="btn btn-primary w-100" type="submit">上架</button>
          </div>
          <div class="col-12 text-muted">
            若書目不存在，系統會自動建立一筆基本資料（書名/作者若有填就一併存檔）。
          </div>
        </form>
      </div>
    </div>

    <!-- 我上架中的商品 -->
    <div class="card shadow-sm">
      <div class="card-header bg-dark text-white fw-bold">我上架中的商品</div>
      <div class="card-body p-0">
        <c:choose>
          <c:when test="${empty mySell}">
            <div class="p-4 text-muted">尚無上架商品</div>
          </c:when>
          <c:otherwise>
            <table class="table table-hover align-middle mb-0">
              <thead class="table-light">
                <tr>
                  <th style="width:140px">ISBN</th>
                  <th>書名</th>
                  <th style="width:180px">作者</th>
                  <th style="width:120px" class="text-end">價格</th>
                  <th style="width:160px" class="text-end">操作</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach items="${mySell}" var="s">
                  <tr>
                    <td>${s.isbn}</td>
                    <td>${s.bookName}</td>
                    <td>${s.author}</td>
                    <td class="text-end">$${s.price}</td>
                    <td class="text-end">
                      <!-- 售出 -->
                      <form class="d-inline" method="post"
                            action="${pageContext.request.contextPath}/sell"
                            accept-charset="UTF-8">
                        <input type="hidden" name="op" value="sold">
                        <input type="hidden" name="isbn" value="${s.isbn}">
                        <button class="btn btn-success btn-sm" type="submit">售出</button>
                      </form>
                      <!-- 下架 -->
                      <form class="d-inline ms-1" method="post"
                            action="${pageContext.request.contextPath}/sell"
                            accept-charset="UTF-8">
                        <input type="hidden" name="op" value="remove">
                        <input type="hidden" name="isbn" value="${s.isbn}">
                        <button class="btn btn-outline-danger btn-sm" type="submit">下架</button>
                      </form>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </c:otherwise>
        </c:choose>
      </div>
    </div>

  </div>

  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
