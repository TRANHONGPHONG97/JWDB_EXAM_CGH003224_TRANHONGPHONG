<!DOCTYPE html>
<html lang="en">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <title>PRODUCT MANAGER</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Main CSS-->
    <link rel="stylesheet" type="text/css" href="../../assets/css/main.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/boxicons@latest/css/boxicons.min.css">
    <!-- or -->
    <link rel="stylesheet" href="https://unpkg.com/boxicons@latest/css/boxicons.min.css">

    <!-- Font-icon css-->
    <link rel="stylesheet" type="text/css"
          href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/2.1.2/sweetalert.min.js"></script>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-confirm/3.3.2/jquery-confirm.min.css">

</head>

<body onload="time()" class="app sidebar-mini rtl">
<!-- Navbar-->
<header class="app-header">
    <!-- Sidebar toggle button--><a class="app-sidebar__toggle" href="#" data-toggle="sidebar"
                                    aria-label="Hide Sidebar"></a>
    <!-- Navbar Right Menu-->
    <ul class="app-nav">


        <!-- User Menu-->
        <li><a class="app-nav__item" href="/index.html"><i class='bx bx-log-out bx-rotate-180'></i> </a>

        </li>
    </ul>
</header>
<!-- Sidebar menu-->
<div class="app-sidebar__overlay" data-toggle="sidebar"></div>
<aside class="app-sidebar">
    <div class="app-sidebar__user"><img class="app-sidebar__user-avatar" src="/images/anhbo.jpg" width="50px"
                                        alt="User Image">
    </div>
    <hr>
    <ul class="app-menu">

        <li><a class="app-menu__item" href="/user_manager"><i class='app-menu__icon bx bx-id-card'></i>
            <span class="app-menu__label">User Management</span></a></li>

        <li><a class="app-menu__item" href="/product"><i class='app-menu__icon bx bx-purchase-tag-alt'></i><span
                class="app-menu__label">Product Management</span></a>
        </li>
        <li><a class="app-menu__item" href="#"><i class='app-menu__icon bx bx-task'></i><span
                class="app-menu__label">Order management</span></a></li>
        <li><a><img src="/images/anh3.jpg" alt="" style="height: 380px; width: 230px"></a></li>
    </ul>
</aside>
<main class="app-content">
    <div class="col-sm-12" style="background-color: chocolate">
        <ul class="app-breadcrumb breadcrumb side" >
            <li class="breadcrumb-item " ><h3><b>THẾ GIỚI DI ĐỘNG</b></h3></li>
        </ul>
        <div id="clock"></div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <div class="tile">
                <div class="tile-body">

                    <div class="row element-button">
                        <div class="col-sm-2">

                            <a style="font-size: larger" class="btn btn-add btn-sm" href="/product?action=create" title="Thêm"><i
                                    class="fas fa-plus"></i>
                                Add new product</a>
                        </div>
                        <div class="col-sm-4"></div>
                        <div class="formm col-sm-5" style="
                                           border: 1px solid black; border-radius: 3px; background-color: limegreen;
                                            font-weight: bold">
                            <form action="product" style="padding: 5px;">
                                Search: <input placeholder="search" type="text" hint="search" value="${requestScope.q}" name="q">
<%--                                <select name="category_id" id="">--%>

<%--                                    <option value="-1">All</option>--%>

<%--                                    <c:forEach items="${applicationScope.listCategory}" var="category">--%>

<%--                                        <c:choose>--%>
<%--                                            <c:when test="${category.getId() == requestScope.category_id}">--%>
<%--                                                <option selected value="${category.getId()}">${category.getName()}</option>--%>
<%--                                            </c:when>--%>
<%--                                            <c:otherwise>--%>
<%--                                                <option value="${category.getId()}">${category.getName()}</option>--%>
<%--                                            </c:otherwise>--%>
<%--                                        </c:choose>--%>
<%--                                    </c:forEach>--%>

<%--                                </select>--%>
                                <button type="get" class="btn btn-primary"><span class="bi-submit"></span> Submit</button>
                            </form>
                        </div>
                    </div>
                    <table class="table table-hover table-bordered js-copytextarea" cellpadding="0" cellspacing="0"
                           border="0"
                           id="sampleTable">
                        <thead>
                        <tr>
                            <th>ID PRODUCT</th>
                            <th>NAME PRODUCT</th>
                            <th>PRICE</th>
                            <th>QUANTITY</th>
                            <th>COLOR</th>
                            <th>DESCRIPTION</th>
                            <th>CATEGORY</th>
                            <th>ACTION</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="product" items="${requestScope.list}">
                            <tr>
                                <td><c:out value="${product.getId()}"/></td>
                                <td><c:out value="${product.getName()}"/></td>
                                <td><fmt:formatNumber value="${product.getPrice()}" type="currency" pattern="#,### ₫"/></td>
                                <td><c:out value="${product.getQuantity()}"/></td>
                                <td><c:out value="${product.getColor()}"/></td>
                                <td><c:out value="${product.getDescription()}"/></td>
                                <td>
                                    <c:if test="${product.getCategory_id() == 1}">
                                        <c:out value="Apple"/>
                                    </c:if>
                                    <c:if test="${product.getCategory_id() == 2}">
                                        <c:out value="Sam sung"/>
                                    </c:if>
                                </td>
                                <td>
                                    <a href="/product?action=edit&id=${product.getId()}"
                                       class="fas fa-edit"> Edit </a>
                                    <a onclick="showMessage(${product.getId()})"
                                       class="fas fa-trash-alt"> Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>

                    </table>
                    <nav class= col-sm-12 style="display: flex; justify-content: center" aria-label="Page navigation example" style="position: relative; left: 500px;">
                        <ul class="pagination" >
                            <c:if test="${requestScope.currentPage != 1}">
                                <li class="page-item ">
                                    <a class="page-link " href="product?page=${requestScope.currentPage - 1}" style="background-color: aquamarine">Previous</a>
                                </li>
                            </c:if>

                            <%--Để hiển thị số Trang.
                            Điều kiện khi không hiển thị liên kết cho trang hiện tại--%>

                            <c:forEach begin="1" end="${noOfPages}" var="i">
                                <c:choose>
                                    <c:when test="${requestScope.currentPage eq i}">
                                        <li  class="page-item "><a class="page-link" href="product?page=${i}">${i}</a></li>
                                    </c:when>
                                    <c:otherwise>
                                        <li  class="page-item ">
                                            <a class="page-link"
                                               href="product?page=${i}">${i}</a> </li>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>

                            <%--Để hiển thị liên kết Tiếp theo--%>

                            <c:if test="${requestScope.currentPage lt requestScope.noOfPages}">
                                <li class="page-item ">
                                    <a class="page-link" href="product?page=${requestScope.currentPage + 1}" style="background-color: aquamarine">Next</a>
                                </li>
                            </c:if>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>
    </div>
</main>

<!-- Essential javascripts for application to work-->
<script src="../../assets/js/jquery-3.2.1.min.js"></script>
<script src="../../assets/js/popper.min.js"></script>
<script src="../../assets/js/bootstrap.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="src/jquery.table2excel.js"></script>
<script src="../../assets/js/main.js"></script>
<!-- The javascript plugin to display page loading on top-->
<script src="../../assets/js/plugins/pace.min.js"></script>
<!-- Page specific javascripts-->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-confirm/3.3.2/jquery-confirm.min.js"></script>
<!-- Data table plugin-->
<script type="text/javascript" src="../../assets/js/plugins/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="../../assets/js/plugins/dataTables.bootstrap.min.js"></script>
<script>
    function showMessage(id) {
        if (confirm("Bạn có muốn xóa sản phẩm này không?") === true){
            window.location.href ="/product?action=delete&id=" +id;
        }
    }
</script>

</body>

</html>