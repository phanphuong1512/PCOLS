console.log("✅ JS Loaded!"); // Debugging

document.addEventListener("DOMContentLoaded", function () {
    let priceSlider = document.getElementById("price-slider");
    let priceRangeText = document.getElementById("priceRangeText");

    // ✅ Fix: Ensure event listeners do not get duplicated
    document.querySelectorAll(".brand-filter").forEach(link => {
        link.addEventListener("click", function (event) {
            event.preventDefault();
            let brand = this.getAttribute("data-brand");
            console.log("✅ Brand filter clicked:", brand);
            updateFilter({ brand: brand });
        });
    });

    document.querySelectorAll(".category-filter").forEach(link => {
        link.addEventListener("click", function (event) {
            event.preventDefault();
            let category = this.getAttribute("data-category");
            console.log("✅ Category filter clicked:", category);
            updateFilter({ category: category });
        });
    });

    // ✅ Ensure noUiSlider is initialized only once
    noUiSlider.create(priceSlider, {
        start: [0, 5000],
        connect: true,
        range: { 'min': 0, 'max': 5000 },
        step: 50,
        tooltips: true,
        format: {
            to: value => Math.round(value),
            from: value => Math.round(value)
        }
    });

        // ✅ Fix: Ensure price filter updates the URL
    priceSlider.noUiSlider.on("change", function (values) {
        let minPrice = values[0];
        let maxPrice = values[1];
        priceRangeText.innerText = `${minPrice} - ${maxPrice}`;
        console.log("✅ Price filter updated:", minPrice, "-", maxPrice);
        updateFilter({ minPrice: minPrice, maxPrice: maxPrice });
    });

    // ✅ Clear Filters Button (Resets Everything)
    document.getElementById("clearFilters").addEventListener("click", function () {
        window.location.href = "/products"; // Reset to base URL (no filters)
    });

    // ✅ Function to Update Filters Dynamically (Fix for Category & Price)
    function updateFilter(params) {
        let currentUrl = new URL(window.location.href);
        for (let key in params) {
            currentUrl.searchParams.set(key, params[key]);
        }
        console.log("✅ Updated URL:", currentUrl.toString());
        fetch(currentUrl)
            .then(response => response.text())
            .then(html => {
                let parser = new DOMParser();
                let doc = parser.parseFromString(html, "text/html");
                let newContent = doc.querySelector("#product-list");
                if (newContent) {
                    document.getElementById("product-list").innerHTML = newContent.innerHTML;
                } else {
                    console.error("❌ #product-list not found in the response");
                    document.getElementById("product-list").innerHTML = "<p>Không có sản phẩm nào.</p>";
                }
                window.history.pushState({}, "", currentUrl.toString());
            })
            .catch(error => console.error("❌ Error updating filters:", error));
    }

    // ✅ Infinite Scrolling Implementation
    let loading = false;
    let observer = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting && !loading) {
            loading = true;
            loadMoreProducts();
        }
    });

    observer.observe(document.getElementById("loading"));

    function loadMoreProducts() {
        let currentUrl = new URL(window.location.href);
        let nextPage = (parseInt(currentUrl.searchParams.get("page")) || 1) + 1;
        currentUrl.searchParams.set("page", nextPage);

        console.log("✅ Loading more products for page:", nextPage);

        fetch(currentUrl)
            .then(response => response.text())
            .then(html => {
                document.getElementById("product-list").insertAdjacentHTML("beforeend",
                    new DOMParser().parseFromString(html, "text/html").querySelector("#product-list").innerHTML);
                window.history.pushState({}, "", currentUrl);
                loading = false;
            });
    }
});
