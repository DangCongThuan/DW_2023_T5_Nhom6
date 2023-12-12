<%--
  Created by IntelliJ IDEA.
  User: trand
  Date: 11/12/2023
  Time: 6:46 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="en">

<head>
  <meta charset="UTF-8" pageEncoding="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Exchange Rate</title>
  <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css"
        integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA=="
        crossorigin="anonymous" referrerpolicy="no-referrer" />
  <link rel="stylesheet" href="style.css">
</head>

<body>
<div class="page">
  <div class="logo_currency max_w_website">
    <div class="logo">
      <img src="Vector.png">
    </div>
    <div class="name">Exchange Rate</div>
  </div>
  <header>
    <div class="menu">
                <span class="name_menu">Tỷ giá ngân hàng <i class="fa-solid fa-angle-down"
                                                            style="height: 18px;"></i></span>
      <div class="list_items">
        <div class="vcb bank">
          <a class="vcb" href="HomeEx?">Tỷ giá Vietcombank</a>
        </div>
        <div class="tcb bank">
          <a href="#" onclick="getData('acb')">Tỷ giá ACB</a>
        </div>
      </div>
    </div>
    <div class="date">
      <input type="date" id="selectedDate" onchange="getData()">
    </div>
  </header>
  <div class="container max_w_website" >
    <p class="title" id="exchangeRateTitle">Tỷ giá ngân hàng Vietcombank</p>

    <div class="table">
                <span class="size_20px" id="updateDateInfo">Tỷ giá được cập nhật vào ngày<span class="size_20px"> 23/10/2023 </span>lúc
                    00h00 và chỉ mang tính chất tham khảo
                </span>
      <div class="wrapper_table">
        <div class="row">
          <div class="item1 header_table border_1">Tên ngoại tệ</div>
          <div class="item2 header_table border_1">Mã ngoại tệ</div>
          <div class="item3 header_table border_1">Mua tiền mặt</div>
          <div class="item4 header_table border_1">Mua chuyển khoản </div>
          <div class="item5 header_table border_1">Bán tiền mặt</div>
          <div class="item5 header_table border_1">Bán chuyển khoản</div>
        </div>
        <c:forEach items="${arrayListTienTe}" var="li">
          <div class="row">
            <div class="item1 border_1">${li.currencyName}</div>
            <div class="item2 border_1">${li.currencySymbol}</div>
            <div class="item3 border_1">${li.buyCash}</div>
            <div class="item4 border_1">${li.buyTransfer}</div>
            <div class="item5 border_1">${li.saleCash}</div>
            <div class="item5 border_1">${li.saleTransfer}</div>
          </div>
        </c:forEach>
      </div>
    </div>
  </div>
</div>
<!-- Js Plugins -->
<script src="js/jquery-3.3.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.min.js"></script>
<script src="js/jquery.slicknav.js"></script>
<script src="js/mixitup.min.js"></script>
<script src="js/owl.carousel.min.js"></script>
<script src="js/main.js"></script>
<script src="admin/assets/js/vendor-all.min.js"></script>
<script src="admin/assets/js/plugins/bootstrap.min.js"></script>
<script src="js/axios.min.js"></script>
<script>
  function updateBankInfo(selectedBank,updatedDate) {
    // Cập nhật thông tin ngân hàng trên trang web
    var titleElement = document.getElementById('exchangeRateTitle');
    var dateInfoElement = document.getElementById('updateDateInfo');
    if (selectedBank === 'vcb') {
      titleElement.innerHTML = 'Tỷ giá Vietcombank';
    } else if (selectedBank === 'acb') {
      titleElement.innerHTML = 'Tỷ giá ACB ';
    }
    dateInfoElement.innerHTML = 'Tỷ giá được cập nhật vào ngày <span class="size_20px">' + updatedDate + '</span> lúc 00h00 và chỉ mang tính chất tham khảo';
    // Thêm các điều kiện khác nếu có nhiều ngân hàng
  }
</script>
</body>

</html>