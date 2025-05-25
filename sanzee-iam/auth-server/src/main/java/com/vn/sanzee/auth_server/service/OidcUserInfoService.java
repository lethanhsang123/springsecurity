package com.vn.sanzee.auth_server.service;

import com.nimbusds.jose.shaded.gson.JsonObject;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
public class OidcUserInfoService {

    public OidcUserInfo loadUser(String name, Set<String> scopes) {
        OidcUserInfo.Builder builder = OidcUserInfo.builder().subject(name);
        if (!CollectionUtils.isEmpty(scopes)) {
            if (scopes.contains(OidcScopes.PROFILE)) {
                builder.name("Thanh Sang")
                        .givenName("Sang")
                        .familyName("Le")
                        .middleName("Thanh")
                        .nickname("Sanzee")
                        .preferredUsername(name)
                        .profile("http://127.0.0.1:8080/" + name)
                        .picture("http://127.0.0.1:8080/" + name + ".jpg")
                        .website("http://127.0.0.1:8080/")
                        .gender("male")
                        .birthdate("2001-08-14")
                        .zoneinfo("Asia/Ho_Chi_Minh") // or simply use system default if running in Vietnam
                        .locale("vi-VN")              // Vietnamese (Vietnam)
                        .updatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
            if (scopes.contains(OidcScopes.EMAIL)) {
                builder.email(name + "@gmail.com").emailVerified(true);
            }
            if (scopes.contains(OidcScopes.ADDRESS)) {
                JsonObject formatted = new JsonObject();
                formatted.addProperty("formatted", "Thuy Trieu\nThuy Nguyen\nHai Phong\nVietnam");
                JsonObject address = new JsonObject();
                address.add("address", formatted);
                builder.address(address.toString());
            }
            if (scopes.contains(OidcScopes.PHONE)) {
                builder.phoneNumber("999999999999").phoneNumberVerified(false);
            }
        }
        return builder.build();
    }
}
