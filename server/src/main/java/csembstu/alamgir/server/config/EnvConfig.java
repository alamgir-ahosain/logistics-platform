package csembstu.alamgir.server.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    public static void loadEnv() {

        Dotenv dotenv = Dotenv.configure().load();

        String dbhost = dotenv.get("DB_HOST");
        String dbport = dotenv.get("DB_PORT");
        String dbname = dotenv.get("DB_NAME");
        String dbuser = dotenv.get("DB_USER");
        String dbpassword = dotenv.get("DB_PASSWORD");

        System.setProperty("DB_HOST", dbhost);
        System.setProperty("DB_PORT", dbport);
        System.setProperty("DB_NAME", dbname);
        System.setProperty("DB_USER", dbuser);
        System.setProperty("DB_PASSWORD", dbpassword);

    }
}
