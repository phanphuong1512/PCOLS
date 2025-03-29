// add hovered class to selected list item
let list = document.querySelectorAll(".navigation li");

function activeLink() {
    list.forEach((item) => {
        item.classList.remove("hovered");
    });
    this.classList.add("hovered");
}

list.forEach((item) => item.addEventListener("mouseover", activeLink));

// Menu Toggle
let toggle = document.querySelector(".toggle");
let navigation = document.querySelector(".navigation");
let main = document.querySelector(".main");

toggle.onclick = function () {
    navigation.classList.toggle("active");
    main.classList.toggle("active");
};

// Product Deletion
document.addEventListener("DOMContentLoaded", function () {
    const deleteButtons = document.querySelectorAll(".delete-btn");

    deleteButtons.forEach(button => {
        button.addEventListener("click", function (event) {
            event.preventDefault();
            const productId = this.getAttribute("data-id");

            if (confirm("Are you sure you want to delete this product?")) {
                fetch(`/admin/product/delete/${productId}`, {
                    method: "GET"
                })
                    .then(response => {
                        if (response.ok) {
                            alert("Delete Successfully!");
                            window.location.reload();
                        } else {
                            alert("Failed to delete product.");
                        }
                    })
                    .catch(error => {
                        console.error("Error:", error);
                        alert("An error occurred.");
                    });
            }
        });
    });
});

