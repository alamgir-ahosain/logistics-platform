package csembstu.alamgir.server.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

public class EnvConfig {
    public static void loadEnv() {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        for (DotenvEntry entry : dotenv.entries()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value != null && !value.isEmpty()) {
                System.setProperty(key, value);
            }
        }
    }
}