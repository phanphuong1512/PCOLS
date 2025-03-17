$(document).ready(function () {
    console.log("checkout.js loaded");

    // Xử lý thay đổi số lượng
    $('.quantity-field').on('input', function () {
        var $input = $(this);
        var detailId = $input.data('detail-id');
        var newQuantity = parseInt($input.val());
        var price = parseFloat($input.data('price'));
        console.log("Quantity input changed for detailId:", detailId, "newQuantity:", newQuantity, "price:", price);

        // Cập nhật UI ngay lập tức
        var immediateTotal = (price * newQuantity).toFixed(2);
        var $totalElement = $("#total-" + detailId);
        if ($totalElement.length) {
            $totalElement.text(immediateTotal);
            console.log("Updated immediateTotal to:", immediateTotal);
        } else {
            console.warn("Không tìm thấy element với id: total-" + detailId);
        }

        // Gửi AJAX POST để cập nhật vào DB
        $.ajax({
            url: '/cart/update',
            type: 'POST',
            data: { detailId: detailId, quantity: newQuantity },
            dataType: 'json',
            success: function (data) {
                console.log("Server returned:", data);
                if ($totalElement.length && data.lineTotal) {
                    $totalElement.text(data.lineTotal);
                    console.log("Updated UI with server lineTotal:", data.lineTotal);
                }
            },
            error: function (xhr, status, error) {
                console.error("Error updating quantity:", error);
            }
        });
    });

    // Xử lý nút xóa sản phẩm khỏi giỏ hàng
    $('.delete-btn').on('click', function (e) {
        e.preventDefault();
        var $btn = $(this);
        var detailId = $btn.data('detail-id');
        console.log("Delete button clicked for detailId:", detailId);

        $.ajax({
            url: '/cart/remove',
            type: 'POST',
            data: { detailId: detailId },
            dataType: 'json',
            success: function (data) {
                console.log("Server returned for delete:", data);
                if (data.status === 'ok') {
                    $btn.closest('tr').remove();
                    console.log("Removed row for detailId:", detailId);
                }
            },
            error: function (xhr, status, error) {
                console.error("Error removing order detail:", error);
            }
        });
    });
});