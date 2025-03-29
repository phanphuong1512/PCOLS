    // Phần order dùng vanilla JS
    document.addEventListener("DOMContentLoaded", function () {
        const buttons = document.querySelectorAll('.add-to-cart');
        buttons.forEach(button => {
            button.addEventListener('click', function (e) {
                e.preventDefault();
                const productId = this.getAttribute('data-product-id');
                console.log("Adding product with ID:", productId);
                fetch('/cart/add', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: new URLSearchParams({
                        productId: productId,
                        quantity: String(1)
                    })
                })
                    .then(res => res.text())
                    .then(data => {
                        console.log("Response:", data);
                    })
                    .catch(error => {
                        console.error("Error adding product to cart:", error);
                    });
            });
        });

        const commentTextarea = document.getElementById('comment');
        if (commentTextarea) {
            commentTextarea.addEventListener('focus', function () {
                this.selectionStart = 0;
                this.selectionEnd = 0;
            });
        }
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
                    $('#reviewForm').prepend('<div class="alert alert-success">Review submitted successfully</div>');
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