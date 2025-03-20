package fpt.swp.pcols.service.impl;

import fpt.swp.pcols.entity.Authority;
import fpt.swp.pcols.entity.User;
import fpt.swp.pcols.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserService userService;

    public OAuth2UserServiceImpl(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Sử dụng default OAuth2UserService để load thông tin user từ Google
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        // Lấy các thuộc tính trả về từ Google
        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = (String) attributes.get("email");
        String googleName = (String) attributes.get("name");  // Lấy tên tài khoản Google

        // Tìm kiếm user theo email; nếu không có, tự đăng ký mới
        userService.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername(googleName);  // Sử dụng tên tài khoản Google làm username
            newUser.setEmail(email);
            newUser.setEnabled(true);
            // Gán role mặc định ROLE_USER; truyền null cho id vì nó được auto-generate
            newUser.setAuthorities(List.of(new Authority(null, "ROLE_USER")));
            return userService.save(newUser);
        });

        // Lấy authorities từ oauth2User
        Collection<? extends GrantedAuthority> authorities = oauth2User.getAuthorities();

        // Trả về OAuth2User, sử dụng "name" làm identifier để hiển thị tên tài khoản Google
        return new DefaultOAuth2User(authorities, attributes, "name");
    }
}