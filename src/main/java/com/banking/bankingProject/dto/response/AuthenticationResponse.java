package com.banking.bankingProject.dto.response;

import com.banking.bankingProject.enums.ModuleEnum;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class AuthenticationResponse {
    private final String token;
    private final Long expiresIn;
    private final Map<ModuleEnum, List<String>> permissions;

    private AuthenticationResponse(String token, Long expiresIn, Map<ModuleEnum, List<String>> permissions) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.permissions = permissions;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String token;
        private Long expiresIn;
        private Map<ModuleEnum, List<String>> permissions;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder expiresIn(Long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public Builder permissions(Map<ModuleEnum, List<String>> permissions) {
            this.permissions = permissions;
            return this;
        }

        public AuthenticationResponse build() {
            return new AuthenticationResponse(token, expiresIn, permissions);
        }

    }
}
