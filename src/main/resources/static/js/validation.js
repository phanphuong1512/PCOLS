document.addEventListener('DOMContentLoaded', function () {
    // Validate cho form đăng ký (Sign Up)
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', function (event) {
            // Kiểm tra điều kiện của form, nếu không hợp lệ thì ngăn submit
            if (!registerForm.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
                // Bạn có thể hiển thị thông báo lỗi bằng cách alert hoặc cập nhật DOM
                alert('Vui lòng điền đầy đủ thông tin theo đúng yêu cầu!');
            }
            // Thêm class này nếu bạn muốn áp dụng style báo lỗi (có thể custom trong CSS)
            registerForm.classList.add('was-validated');
        });
    }

    // Validate cho form đăng nhập (Sign In)
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', function (event) {
            if (!loginForm.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
                alert('Vui lòng điền đầy đủ Username và Password hợp lệ!');
            }
            loginForm.classList.add('was-validated');
        });
    }
});
