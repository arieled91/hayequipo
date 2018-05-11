package org.arieled91.hayequipo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "env")
public class EnvProperties {

    private String frontendUrl = "http://localhost:4200";
    private String authSecret = "secret";


    public String getFrontendUrl() {
        return frontendUrl;
    }

    public EnvProperties setFrontendUrl(String frontendUrl) {
        this.frontendUrl = frontendUrl;
        return this;
    }

    public String getAuthSecret() {
        return authSecret;
    }

    public void setAuthSecret(String authSecret) {
        this.authSecret = authSecret;
    }
}
