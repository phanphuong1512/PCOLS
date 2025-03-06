const inputs = [...document.querySelectorAll(".otp-box input[type='text']")];
const submitBtn = document.querySelector(".otp-box button");

inputs.forEach((input, index) => {
    input.addEventListener("keyup", (e) => {
        // Nếu nhấn Backspace và ô hiện tại trống, chuyển focus về ô trước đó (nếu có)
        if (e.key === "Backspace" && input.value === "" && index > 0) {
            inputs[index - 1].focus();
            return;
        }
        // Kiểm tra nếu ký tự nhập vào là một số (0-9)
        if (!/^\d$/.test(e.key)) {
            e.target.value = "";
            return;
        }

        // Nếu input hợp lệ, giữ nguyên giá trị
        // Nếu chưa phải ô cuối, tự động focus ô kế tiếp
        if (index < inputs.length - 1) {
            inputs[index + 1].focus();
        }

        // Kiểm tra nếu tất cả các input đều có 1 ký tự
        setTimeout(() => {
            const isDisabledBtn = inputs.some(inp => inp.value.length !== 1);
            console.log("Input values:", inputs.map(inp => inp.value)); // Debug: log giá trị các input
            submitBtn.disabled = isDisabledBtn;
        }, 0);
    });
});
document.addEventListener("DOMContentLoaded", function () {
    const otpForm = document.getElementById('otpForm');
    otpForm.addEventListener('submit', function (e) {
        // Lấy tất cả các ô input chứa chữ số
        const digitInputs = document.querySelectorAll("input[name^='digit']");
        let otpValue = "";
        digitInputs.forEach(input => {
            otpValue += input.value;
        });
        // Gán giá trị kết hợp vào trường ẩn 'otp'
        document.getElementById('otp').value = otpValue;
    });
});