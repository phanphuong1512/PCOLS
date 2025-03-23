// chat-fragment.js
(function() {
    // Tạo và chèn HTML
    const chatHTML = `
        <div id="chat-icon">💬</div>
        <div id="chat-window">
            <div id="chat-header">
                <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTD0YYenG7K-zHncYQ53_wOU4Tg5kd1XXyLUw&s" alt="Chatbot Logo" class="chatbot-logo">
                <span>Chat</span>
            </div>
            <div id="chat-messages"></div>
            <input id="chat-input" type="text" placeholder="Nhập tin nhắn...">
        </div>
    `;
    document.body.insertAdjacentHTML('beforeend', chatHTML);

    // Chèn CSS
    const style = document.createElement('style');
    style.textContent = `
        #chat-icon {
            position: fixed;
            bottom: 20px;
            right: 20px;
            width: 60px;
            height: 60px;
            background-color: #007bff;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 30px;
            cursor: move;
            z-index: 1000;
        }

        #chat-window {
            display: none;
            position: fixed;
            bottom: 90px;
            right: 20px;
            width: 300px;
            height: 400px;
            background-color: white;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            z-index: 1000;
        }

        #chat-header {
            background-color: #007bff;
            color: white;
            padding: 10px;
            border-radius: 5px 5px 0 0;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
        }

        #chat-header .chatbot-logo {
            width: 30px;
            height: 30px;
            object-fit: contain;
        }

        #chat-messages {
            height: 300px;
            overflow-y: auto;
            padding: 10px;
        }

        #chat-input {
            width: 100%;
            padding: 10px;
            border: none;
            border-top: 1px solid #ccc;
            box-sizing: border-box;
        }
    `;
    document.head.appendChild(style);

    // Xử lý logic JavaScript
    const chatIcon = document.getElementById('chat-icon');
    const chatWindow = document.getElementById('chat-window');
    const chatInput = document.getElementById('chat-input');
    const chatMessages = document.getElementById('chat-messages');

    let isDragging = false;
    let currentX = window.innerWidth - chatIcon.offsetWidth - 20;
    let currentY = window.innerHeight - chatIcon.offsetHeight - 20;
    let initialX, initialY;

    // Khởi tạo vị trí ban đầu
    chatIcon.style.left = currentX + 'px';
    chatIcon.style.top = currentY + 'px';

    // Sự kiện kéo thả
    chatIcon.addEventListener('mousedown', startDragging);
    document.addEventListener('mousemove', drag);
    document.addEventListener('mouseup', stopDragging);

    function startDragging(e) {
        initialX = e.clientX - currentX;
        initialY = e.clientY - currentY;
        if (e.target === chatIcon) {
            isDragging = true;
        }
    }

    function drag(e) {
        if (isDragging) {
            e.preventDefault();
            currentX = e.clientX - initialX;
            currentY = e.clientY - initialY;
            chatIcon.style.left = currentX + 'px';
            chatIcon.style.top = currentY + 'px';
            if (chatWindow.style.display === 'block') {
                chatWindow.style.right = '20px';
                chatWindow.style.bottom = (90 + chatIcon.offsetHeight) + 'px';
            }
        }
    }

    function stopDragging() {
        isDragging = false;
    }

    // Mở/đóng chat
    chatIcon.addEventListener('click', toggleChat);

    function toggleChat() {
        if (chatWindow.style.display === 'none' || chatWindow.style.display === '') {
            chatWindow.style.display = 'block';
        } else {
            chatWindow.style.display = 'none';
        }
    }

    // Gửi tin nhắn
    chatInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter' && chatInput.value.trim() !== '') {
            const message = document.createElement('div');
            message.textContent = chatInput.value;
            chatMessages.appendChild(message);
            chatMessages.scrollTop = chatMessages.scrollHeight;
            chatInput.value = '';
        }
    });
})();