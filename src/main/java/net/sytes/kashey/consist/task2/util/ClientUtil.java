package net.sytes.kashey.consist.task2.util;

import org.springframework.core.env.Environment;

public class ClientUtil {

    public static String getActualUrl(Environment environment) {
        return environment.getProperty("gitlab.base.url") +
                environment.getProperty("gitlab.project.id") +
                "/issues/" +
                environment.getProperty("gitlab.issue.id") +
                "/notes";
    }
}



