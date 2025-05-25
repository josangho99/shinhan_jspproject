<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
<link
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
    rel="stylesheet">
<link href = "css/shop_style.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Coding Shop - 상품 목록</title>
</head>
<body class="bg-light">
    <div class="container py-5">
        <h1 class="text-center mb-4">Coding Shop - 상품 목록</h1>

                <div class="d-flex justify-content-end mb-3">
            <div class="dropdown">
                <button class="btn btn-secondary dropdown-toggle" type="button" id="sortDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                    정렬 기준
                </button>
                <ul class="dropdown-menu" aria-labelledby="sortDropdown">
                    <%-- 각 정렬 옵션을 POST 요청을 보내는 폼으로 변경 --%>
                    <li>
                        <form action="shop" method="post" style="margin: 0; padding: 0;">
                            <input type="hidden" name="order" value="created_at">
                            <button type="submit" class="dropdown-item">최신순</button>
                        </form>
                    </li>
                    <li>
                        <form action="shop" method="post" style="margin: 0; padding: 0;">
                            <input type="hidden" name="order" value="total_sales">
                            <button type="submit" class="dropdown-item">매출순</button>
                        </form>
                    </li>
                    <li>
                        <form action="shop" method="post" style="margin: 0; padding: 0;">
                            <input type="hidden" name="order" value="avg_rating">
                            <button type="submit" class="dropdown-item">평점순</button>
                        </form>
                    </li>
                </ul>
            </div>
        </div>


        <table class="table table-hover table-striped table-bordered shop-table">
            <thead class="table-dark">
                <tr>
                    <th>상품번호</th>
                    <th>상품명</th>
                    <th>가격</th>
                    <th>브랜드</th>
                    <th>총 판매량</th>
                    <th>카테고리</th>
                    <th>평점</th>
                    <th>등록일</th>
                    <th class="th_inventory">재고</th>
                    <th class="th_isActive">판매여부</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${products}" var="product">
                    <tr>
                        <td>${product.productNum}</td>
                        <td>${product.productName}</td>
                        <td>${product.price}</td>
                        <td>${product.brandName}</td>
                        <td>${product.totalSales}</td>
                        <td>${product.cate}</td>
                        <td>
                            <div class="star-rating" data-rating="${product.avgRating}">
                                <%-- 별점 아이콘이 여기에 JavaScript로 삽입됩니다 --%>
                            </div>
                            <span class="rating-text">(${product.avgRating})</span>
                        </td>
                        <td>${product.createdAt}</td>
                        <td class="td_inventory">
                            <c:choose>
                                <c:when test="${product.inventory le 0}">
                                    <span class="text-danger">품절</span>
                                </c:when>
                                <c:otherwise>
                                    ${product.inventory}개
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="td_isActive">
                            <c:choose>
                                <c:when test="${product.isActive eq 1}">
                                    <span class="text-success">판매중</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-danger">판매중지</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty products}">
                    <tr>
                        <td colspan="10" class="text-center text-muted py-4">상품이 없습니다.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>

    <script>
        $(document).ready(function() {
            $('.star-rating').each(function() {
                let rating = parseFloat($(this).data('rating'));
                let starsHtml = '';
                for (let i = 1; i <= 5; i++) {
                    if (rating >= i) {
                        starsHtml += '<i class="fas fa-star full-star"></i>';
                    } else if (rating > i - 1 && rating < i) {
                        starsHtml += '<i class="fas fa-star-half-alt half-star"></i>';
                    } else {
                        starsHtml += '<i class="far fa-star empty-star"></i>';
                    }
                }
                $(this).html(starsHtml);
            });
        });
    </script>
</body>
</html>