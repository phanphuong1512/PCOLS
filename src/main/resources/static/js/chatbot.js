const chatbotToggler = document.querySelector(".chatbot-toggler");
const closeBtn = document.querySelector(".close-btn");
const chatbox = document.querySelector(".chatbox");
const chatInput = document.querySelector(".chat-input textarea");
const sendChatBtn = document.querySelector(".chat-input span");

let userMessage = null;
let isProcessing = false;
const inputInitHeight = chatInput.scrollHeight;

const createChatLi = (message, className) => {
    const chatLi = document.createElement("li");
    chatLi.classList.add("chat", `${className}`);
    chatLi.innerHTML = className === "outgoing" ? `<p></p>` : `<span class="material-symbols-outlined">smart_toy</span><p></p>`;
    chatLi.querySelector("p").textContent = message;
    return chatLi;
};

const generateResponse = async (chatElement) => {
    const messageElement = chatElement.querySelector("p");

    try {
        const response = await fetch(`http://localhost:8080/ai/generate?message=${encodeURIComponent(userMessage)}`, {
            method: 'GET',
        });
        const data = await response.json();
        messageElement.textContent = data.generation;
    } catch (error) {
        console.error('Lỗi khi gọi API:', error);
        messageElement.textContent = "Không thể kết nối với server AI. Vui lòng kiểm tra kết nối hoặc thử lại.";
        const retryButton = document.createElement("button");
        retryButton.textContent = "Thử lại";
        retryButton.style.marginTop = "5px";
        retryButton.style.padding = "5px 10px";
        retryButton.style.backgroundColor = "#007bff";
        retryButton.style.color = "white";
        retryButton.style.border = "none";
        retryButton.style.borderRadius = "5px";
        retryButton.style.cursor = "pointer";
        retryButton.onclick = () => {
            messageElement.textContent = "Thinking...";
            generateResponse(chatElement);
        };
        chatElement.appendChild(retryButton);
    }

    chatbox.scrollTo(0, chatbox.scrollHeight);
    isProcessing = false;
};

const handleChat = () => {
    if (isProcessing) return;

    userMessage = chatInput.value.trim();
    if (!userMessage) return;

    isProcessing = true;

    chatInput.value = "";
    chatInput.style.height = `${inputInitHeight}px`;

    chatbox.appendChild(createChatLi(userMessage, "outgoing"));
    chatbox.scrollTo(0, chatbox.scrollHeight);

    setTimeout(() => {
        const incomingChatLi = createChatLi("Thinking...", "incoming");
        chatbox.appendChild(incomingChatLi);
        chatbox.scrollTo(0, chatbox.scrollHeight);
        generateResponse(incomingChatLi);
    }, 600);
};

chatInput.addEventListener("input", () => {
    chatInput.style.height = `${inputInitHeight}px`;
    chatInput.style.height = `${chatInput.scrollHeight}px`;
});

chatInput.addEventListener("keydown", (e) => {
    if (e.key === "Enter" && !e.shiftKey && window.innerWidth > 800) {
        e.preventDefault();
        handleChat();
    }
});

sendChatBtn.addEventListener("click", (e) => {
    e.preventDefault();
    handleChat();
});

closeBtn.addEventListener("click", () => document.body.classList.remove("show-chatbot"));
chatbotToggler.addEventListener("click", () => document.body.classList.toggle("show-chatbot"));