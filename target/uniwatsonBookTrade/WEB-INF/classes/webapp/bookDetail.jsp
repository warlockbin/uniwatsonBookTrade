<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="navbar.jsp">
  <jsp:param name="active" value="books"/>
</jsp:include>

<div class="container mt-4">
  <h2 class="mb-3">
    <c:out value="${book.name != null ? book.name : '（尚無書名資料）'}"/>
  </h2>

  <div class="card shadow-sm mb-4">
    <div class="card-body">
      <div class="row g-3">
        <div class="col-md-8">
          <p class="mb-1"><b>ISBN：</b><c:out value="${book.isbn}"/></p>
          <p class="mb-1"><b>作者：</b><c:out value="${book.author}"/></p>
          <p class="mb-1"><b>分類：</b><c:out value="${book.category}"/></p>
          <p class="mb-0"><b>簡介：</b><c:out value="${book.description}"/></p>
        </div>
        <div class="col-md-4">
          <c:if test="${not empty book.imageUrl}">
            <img class="img-fluid rounded border" alt="封面"
                 src="${pageContext.request.contextPath}/images/${book.imageUrl}"/>
          </c:if>
          <c:if test="${empty book.imageUrl && not empty book.coverImg}">
            <img class="img-fluid rounded border" alt="封面"
                 src="${pageContext.request.contextPath}/images/${book.coverImg}"/>
          </c:if>
        </div>
      </div>
    </div>
  </div>

  <h4 class="mb-3">可購買的賣家</h4>
  <c:choose>
    <c:when test="${empty sellers}">
      <div class="alert alert-secondary">目前沒有這本書的上架。</div>
    </c:when>
    <c:otherwise>
      <table class="table table-hover align-middle">
        <thead>
          <tr><th>賣家</th><th>價格</th><th style="width:220px">操作</th></tr>
        </thead>
        <tbody>
        <c:forEach var="s" items="${sellers}">
          <tr>
            <td><c:out value="${s.sellerId}"/></td>
            <td>$<c:out value="${s.price}"/></td>
            <td>
              <!-- 購買此書（若你已有 /orders 負責下單，可以保留） -->
              <form method="post" class="d-inline"
                    action="${pageContext.request.contextPath}/orders">
                <input type="hidden" name="op" value="buy"/>
                <input type="hidden" name="isbn" value="${book.isbn}"/>
                <input type="hidden" name="sellerId" value="${s.sellerId}"/>
                <input type="hidden" name="price" value="${s.price}"/>
                <button type="submit" class="btn btn-primary btn-sm">購買此書</button>
              </form>

              <!-- 聯絡賣家：先確保/建立訂單，再導到 /messages?orderId=... -->
              <form method="get" class="d-inline"
                    action="${pageContext.request.contextPath}/messages">
                <input type="hidden" name="action" value="start"/>
                <input type="hidden" name="isbn" value="${book.isbn}"/>
                <input type="hidden" name="sellerId" value="${s.sellerId}"/>
                <button type="submit" class="btn btn-outline-secondary btn-sm">聯絡賣家</button>
              </form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:otherwise>
  </c:choose>

  <a class="btn btn-link" href="${pageContext.request.contextPath}/books">返回列表</a>
</div>
