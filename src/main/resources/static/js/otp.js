const inputs = document.querySelectorAll(".otp-box input");
const submitBtn = document.querySelector(".otp-box button");

inputs.forEach((input, index) => {
    input.addEventListener("keyup", (e) => {
        // 1. Kiểm tra xem phím có phải là 0-9 không
        if (e.keyCode < 48 || e.keyCode > 57) {
            e.target.value = "";
            return;
        }

        // 2. Gán ký tự số vào ô input
        e.target.value = String.fromCharCode(e.keyCode);

        // 3. Nếu chưa phải ô cuối, tự động focus ô tiếp theo
        if (index < inputs.length - 1) {
            inputs[index + 1].focus();
        }

        // 4. Kiểm tra tất cả ô input xem đã đủ 1 ký tự chưa
        //    Nếu có ô nào trống, nút submit bị disable
        setTimeout(() => {
            const isDisabledBtn = [...inputs].some((inp) => inp.value.length !== 1);
            submitBtn.disabled = isDisabledBtn;
        }, 0);
    });
});
