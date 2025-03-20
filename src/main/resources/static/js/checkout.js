$(document).ready(function () {
    console.log("checkout.js loaded");

    // Hàm tính Subtotal, Shipping, Total
    function recalcTotals() {
        let subtotal = 0;
        // Mỗi dòng sản phẩm cần có class .order-detail-row
        // và phần tử hiển thị line total có class .line-total
        $('.order-detail-row').each(function () {
            // Lấy text line total, loại bỏ ký tự không phải số
            let rawText = $(this).find('.line-total').text() || "0";
            let lineTotal = parseFloat(rawText.replace(/[^0-9.]/g, '')) || 0;
            subtotal += lineTotal;
        });

        // Lấy phí ship từ radio shipping (ví dụ: FREE=0, STANDARD=4)
        let shippingCost = 0;
        let shippingValue = $('input[name="shipping"]:checked').val();
        if (shippingValue === 'STANDARD') {
            shippingCost = 4.00;
        }

        // Tính total
        let total = subtotal + shippingCost;

        // Cập nhật hiển thị
        $('#subtotal-value').text(subtotal.toFixed(2));
        $('#shipping-value').text(shippingCost.toFixed(2));
        $('#total-value').text(total.toFixed(2));
    }

    // ===================== Xử lý thay đổi số lượng =====================
    $('.quantity-field').on('input', function () {
        let $input = $(this);
        let detailId = $input.data('detail-id');
        let newQuantity = parseInt($input.val());
        let price = parseFloat($input.data('price'));

        console.log("Quantity changed:", {detailId, newQuantity, price});

        // Cập nhật line total tại client ngay lập tức
        let immediateTotal = (price * newQuantity).toFixed(2);
        let $totalElement = $("#total-" + detailId);
        if ($totalElement.length) {
            $totalElement.text(immediateTotal);
            console.log("Updated immediateTotal to:", immediateTotal);
        } else {
            console.warn("Không tìm thấy element với id: total-" + detailId);
        }

        // Gửi AJAX POST cập nhật DB
        $.ajax({
            url: '/cart/update',
            type: 'POST',
            data: {detailId, quantity: newQuantity},
            dataType: 'json',
            success: function (data) {
                console.log("Server returned:", data);
                // Nếu server trả về lineTotal chính xác, cập nhật lại
                if ($totalElement.length && data.lineTotal) {
                    $totalElement.text(data.lineTotal);
                    console.log("Updated UI with server lineTotal:", data.lineTotal);
                }
                // Sau khi server cập nhật xong, tính lại Subtotal
                recalcTotals();
            },
            error: function (xhr, status, error) {
                console.error("Error updating quantity:", error);
            }
        });
    });

    // ===================== Xử lý nút xóa sản phẩm =====================
    $('.delete-btn').on('click', function (e) {
        e.preventDefault();
        let $btn = $(this);
        let detailId = $btn.data('detail-id');

        console.log("Delete button clicked for detailId:", detailId);

        $.ajax({
            url: '/cart/remove',
            type: 'POST',
            data: {detailId},
            dataType: 'json',
            success: function (data) {
                console.log("Server returned for delete:", data);
                if (data.status === 'ok') {
                    // Xóa dòng tương ứng
                    $btn.closest('tr').remove();
                    console.log("Removed row for detailId:", detailId);
                    // Tính lại Subtotal, Total
                    recalcTotals();
                }
            },
            error: function (xhr, status, error) {
                console.error("Error removing order detail:", error);
            }
        });
    });

    // ===================== Xử lý thay đổi shipping method =====================
    $('input[name="shipping"]').on('change', function () {
        recalcTotals();
    });

    // ===================== Gọi recalcTotals() ban đầu =====================
    recalcTotals();
});