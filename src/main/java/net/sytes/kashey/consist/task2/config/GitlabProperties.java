package net.sytes.kashey.consist.task2.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gitlab")
public record GitlabProperties(
        String token,
        String project,
        String issue,
        String url) {
}
