<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<jsp:include page="navbar.jsp">
  <jsp:param name="active" value="orders"/>
</jsp:include>

<div class="container mt-4">
  <h2 class="mb-3">我的訂單</h2>

  <div class="row g-4">
    <!-- 我購買的 -->
    <div class="col-lg-6">
      <div class="card shadow-sm">
        <div class="card-header bg-primary text-white fw-bold">我購買的</div>
        <div class="card-body p-0">
          <table class="table table-hover mb-0">
            <thead><tr>
              <th>#</th><th>ISBN</th><th>賣家</th><th>狀態</th><th>操作</th>
            </tr></thead>
            <tbody>
            <c:choose>
              <c:when test="${empty buyOrders}">
                <tr><td class="text-muted" colspan="5">目前沒有資料</td></tr>
              </c:when>
              <c:otherwise>
                <c:forEach var="o" items="${buyOrders}">
                  <tr>
                    <td>${o.id}</td>
                    <td>${o.book}</td>
                    <td>${o.sellerId}</td>
                    <td>
                      <span class="badge ${o.status==1?'bg-success':'bg-secondary'}">
                        ${o.status==1?'已完成':'處理中'}
                      </span>
                    </td>
                    <td>
                      <a class="btn btn-outline-primary btn-sm"
                         href="${pageContext.request.contextPath}/messages?orderId=${o.id}">訊息</a>
                      <form class="d-inline" method="post"
                            action="${pageContext.request.contextPath}/orders"
                            onsubmit="return confirm('確定要刪除這筆訂單？');">
                        <input type="hidden" name="op" value="delete"/>
                        <input type="hidden" name="id" value="${o.id}"/>
                        <button type="submit" class="btn btn-outline-danger btn-sm">刪除</button>
                      </form>
                    </td>
                  </tr>
                </c:forEach>
              </c:otherwise>
            </c:choose>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- 我賣出的 -->
    <div class="col-lg-6">
      <div class="card shadow-sm">
        <div class="card-header bg-success text-white fw-bold">我賣出的</div>
        <div class="card-body p-0">
          <table class="table table-hover mb-0">
            <thead><tr>
              <th>#</th><th>ISBN</th><th>買家</th><th>金額</th><th>狀態</th><th>操作</th>
            </tr></thead>
            <tbody>
            <c:choose>
              <c:when test="${empty sellOrders}">
                <tr><td class="text-muted" colspan="6">目前沒有資料</td></tr>
              </c:when>
              <c:otherwise>
                <c:forEach var="o" items="${sellOrders}">
                  <tr>
                    <td>${o.id}</td>
                    <td>${o.book}</td>
                    <td>${o.buyerId}</td>
                    <td>$${o.price}</td>
                    <td>
                      <span class="badge ${o.status==1?'bg-success':'bg-secondary'}">
                        ${o.status==1?'已完成':'處理中'}
                      </span>
                    </td>
                    <td>
                      <a class="btn btn-outline-primary btn-sm"
                         href="${pageContext.request.contextPath}/messages?orderId=${o.id}">訊息</a>
                      <form class="d-inline" method="post"
                            action="${pageContext.request.contextPath}/orders"
                            onsubmit="return confirm('確定要刪除這筆訂單？');">
                        <input type="hidden" name="op" value="delete"/>
                        <input type="hidden" name="id" value="${o.id}"/>
                        <button type="submit" class="btn btn-outline-danger btn-sm">刪除</button>
                      </form>
                    </td>
                  </tr>
                </c:forEach>
              </c:otherwise>
            </c:choose>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>
