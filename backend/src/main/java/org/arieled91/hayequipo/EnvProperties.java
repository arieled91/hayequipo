package org.arieled91.hayequipo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "env")
public class EnvProperties {

    private String frontendUrl = "http://localhost:4200";


    public String getFrontendUrl() {
        return frontendUrl;
    }

    public EnvProperties setFrontendUrl(String frontendUrl) {
        this.frontendUrl = frontendUrl;
        return this;
    }
}
