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
                    quantity: 1
                })
            })
                .then(response => response.text())
                .then(data => {
                    console.log("Response:", data);
                    // Cập nhật giao diện hoặc thông báo thành công
                })
                .catch(error => {
                    console.error("Error adding product to cart:", error);
                });
        });
    });
});