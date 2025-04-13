$(document).ready(function () {
    $('.add-to-cart').click(function (e) {
        e.preventDefault();
        var productId = $(this).data('product-id');
        console.log("Adding product with ID: " + productId);

        // Tìm input số lượng trong cùng khối với nút được click
        var quantityInput = $(this).closest('.product-btns').find('.quantity-field');
        var quantity = "1"; // giá trị mặc định
        if (quantityInput.length > 0) {
            quantity = quantityInput.val();
        }
        console.log("Using quantity: " + quantity);

        $.ajax({
            type: 'POST',
            url: '/cart/add',
            data: $.param({
                productId: productId,
                quantity: quantity
            }),
            contentType: "application/x-www-form-urlencoded",
            success: function (data) {
                console.log("Response: " + data);
            },
            error: function (xhr) {
                console.error("Error adding product to cart: " + xhr.responseText);
            }
        });
    });
});

// Phần review dùng jQuery
$(document).ready(function () {
    // Submit review
    $('#submitReviewBtn').click(function () {
        const formData = $('#reviewForm').serialize();
        const productId = $('[name="productId"]').val();

        $('#reviewForm .alert').remove();

        $.ajax({
            type: 'POST',
            url: '/product-detail/review',
            data: formData,
            success: function (response) {
                $('#reviewForm').prepend('<div class="alert alert-success">' + response + '</div>');
                $('#reviewForm')[0].reset();
                setTimeout(() => {
                    loadReviews(productId, 0); // HIGHLIGHT: Trở về trang đầu tiên sau khi submit
                }, 500);
            },
            error: function (xhr) {
                if (xhr.status === 401) {
                    window.location.href = '/auth/login';
                } else {
                    const errorMessage = xhr.responseText || 'Error submitting review';
                    $('#reviewForm').prepend('<div class="alert alert-danger">' + errorMessage + '</div>');
                }
            }
        });
    });

    // HIGHLIGHT: Xử lý click trên các nút phân trang
    $(document).on('click', '.reviews-pages .page-link', function (e) {
        e.preventDefault();
        console.log('Page link clicked'); // HIGHLIGHT: Kiểm tra sự kiện
        const page = $(this).data('page');
        const productId = $('[name="productId"]').val();
        console.log('productId:', productId, 'page:', page); // HIGHLIGHT: Kiểm tra giá trị
        if (!$(this).parent().hasClass('disabled')) {
            loadReviews(productId, page);
        }
    });
});

// HIGHLIGHT: Sửa hàm loadReviews để hỗ trợ phân trang
function loadReviews(productId, page = 0) {
    $.ajax({
        type: 'GET',
        url: `/product-detail/review?productId=${productId}&page=${page}`, // HIGHLIGHT: Thêm tham số page
        success: function (data) {
            $('#reviewsContainer').html(data);
        },
        error: function () {
            console.error('Error loading reviews');
        }
    });
}