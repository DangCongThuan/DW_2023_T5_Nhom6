<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="en">

<head>
    <meta charset="UTF-8" pageEncoding="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Exchange Rate</title>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css"
          integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA=="
          crossorigin="anonymous" referrerpolicy="no-referrer"/>
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
                    <a class="vcb" href="#" onclick="updateBankInfo('vcb')">Tỷ giá Vietcombank</a>
                </div>
                <div class="tcb bank">
                    <a href="#" onclick="updateBankInfo('acb')">Tỷ giá ACB</a>
                </div>
            </div>
        </div>
        <div class="date">
            <input type="date" id="selectedDate" onchange="updateDateInfo(this.value)">
        </div>
    </header>
    <div class="container max_w_website">
        <p class="title" id="exchangeRateTitle">Tỷ giá ngân hàng</p>
        <div class="table">
                <span class="size_20px" id="updateDateInfo">Tỷ giá được cập nhật vào ngày<span class="size_20px"> 23/10/2023 </span>lúc
                    00h00 và chỉ mang tính chất tham khảo
                </span>
            <div class="wrapper_table">
                <div class="row">
                    <div class="item1 header_table border_1">Tên ngoại tệ</div>
                    <div class="item2 header_table border_1">Mã ngoại tệ</div>
                    <div class="item3 header_table border_1">Mua tiền mặt</div>
                    <div class="item4 header_table border_1">Mua chuyển khoản</div>
                    <div class="item5 header_table border_1">Bán tiền mặt</div>
                    <div class="item5 header_table border_1">Bán chuyển khoản</div>
                </div>
                <div id="data">
                </div>
                <%--                <c:forEach items="${arrayListTienTe}" var="li">--%>
                <%--                    <div class="row">--%>
                <%--                        <div class="item1 border_1">${li.currencyName}</div>--%>
                <%--                        <div class="item2 border_1">${li.currencySymbol}</div>--%>
                <%--                        <div class="item3 border_1">${li.buyCash}</div>--%>
                <%--                        <div class="item4 border_1">${li.buyTransfer}</div>--%>
                <%--                        <div class="item5 border_1">${li.saleCash}</div>--%>
                <%--                        <div class="item5 border_1">${li.saleTransfer}</div>--%>
                <%--                    </div>--%>
                <%--                </c:forEach>--%>
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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>

    function updateBankInfo(selectedBank) {
        sessionStorage.setItem("bank", selectedBank);
        var selectedDate = sessionStorage.getItem("date");
        // Cập nhật thông tin ngân hàng trên trang web
        var titleElement = document.getElementById('exchangeRateTitle');
        if (selectedBank === 'vcb') {
            titleElement.innerHTML = 'Tỷ giá Vietcombank';
        } else if (selectedBank === 'acb') {
            titleElement.innerHTML = 'Tỷ giá ACB ';
        }
        var xhr = new XMLHttpRequest();
        var url = "/DataWarehouse_war/HomeEx?bank=" + selectedBank + "&date=" + selectedDate;
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var data = xhr.responseText;
                var element = document.getElementById("data");
                while (element.firstChild) {
                    element.removeChild(element.firstChild);
                }
                element.innerHTML += data;
            }
        }
        xhr.open("GET", url, true);
        xhr.send();
        // Thêm các điều kiện khác nếu có nhiều ngân hàng
    }

    // function updateBankInfo(selectedBank) {
    //     sessionStorage.setItem("bank", selectedBank);
    //     var selectedDate=sessionStorage.getItem("date");
    //     $.ajax({
    //         url: "/HomeEx",
    //         type: "get", //send it through get method
    //         data: {
    //             bank: selectedBank,
    //             date:selectedDate
    //         },
    //         success: function (data) {
    //             var row = document.getElementById("data");
    //             row.innerHTML += data;
    //         },
    //         error: function (xhr) {
    //             //Do Something to handle error
    //         }
    //     });
    // }
    // function updateDateInfo(selectedDate) {
    //     sessionStorage.setItem("date", selectedDate);
    //     var selectedDate=sessionStorage.getItem("bank");
    //     $.ajax({
    //         url: "/HomeEx",
    //         type: "get", //send it through get method
    //         data: {
    //             bank: selectedBank,
    //             date:selectedDate
    //         },
    //         success: function (data) {
    //             var row = document.getElementById("data");
    //             row.innerHTML += data;
    //         },
    //         error: function (xhr) {
    //             //Do Something to handle error
    //         }
    //     });
    // }

    function updateDateInfo(selectedDate) {
        sessionStorage.setItem("date", selectedDate);
        var selectedBank = sessionStorage.getItem("bank");
        var dateInfoElement = document.getElementById('updateDateInfo');
        dateInfoElement.innerHTML = 'Tỷ giá được cập nhật vào ngày <span class="size_20px">' + selectedDate + '</span> lúc 00h00 và chỉ mang tính chất tham khảo';
        // Gọi Ajax hoặc chuyển giá trị ngày vào một biểu mẫu để gửi lên server
        // Ví dụ sử dụng Ajax:
        var xhr = new XMLHttpRequest();
        var url = "/DataWarehouse_war/HomeEx?date=" + selectedDate + "&bank=" + selectedBank;
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var data = xhr.responseText;
                var element = document.getElementById("data");
                while (element.firstChild) {
                    element.removeChild(element.firstChild);
                }
                element.innerHTML += data;
            }
        }
        xhr.open("GET", url, true);
        xhr.send();

    }
    $(document).ready(function () {
        // Kiểm tra xem có dữ liệu trong sessionStorage không
        var storedBank = sessionStorage.getItem("bank");
        var storedDate = sessionStorage.getItem("date");

        // Nếu có dữ liệu, khôi phục trạng thái trước đó
        if (storedBank && storedDate) {
            updateBankInfo(storedBank);
            updateDateInfo(storedDate);
        }
    });
</script>
</body>

</html>