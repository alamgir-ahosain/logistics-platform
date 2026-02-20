package csembstu.alamgir.server.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvConfig {
    private static final Logger log = LoggerFactory.getLogger(EnvConfig.class);
    public static void loadEnv() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        for (DotenvEntry entry : dotenv.entries()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value != null && !value.isEmpty()) {
                System.setProperty(key, value);
                log.info("Loaded property: {}", key);
            }
        }
    }
}