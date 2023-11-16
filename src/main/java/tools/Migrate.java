package tools;

import config.YamlConfig;
import org.flywaydb.core.Flyway;

import java.util.ArrayList;
import java.util.List;

public class Migrate {
    public static void run() {
        List<String> locations = new ArrayList<>();
        locations.add("filesystem:./flyway/community");
        locations.add("filesystem:./flyway/customize");

        if (YamlConfig.config.server.INIT_DATABASE_AT_FIRST_START) {
            locations.add("filesystem:./flyway/initial");
        }

        String[] locationsArray = locations.toArray(new String[0]);

        Flyway flyway = Flyway.configure()
                .dataSource(
                        String.format(YamlConfig.config.server.DB_URL_FORMAT, YamlConfig.config.server.DB_HOST),
                        YamlConfig.config.server.DB_USER,
                        YamlConfig.config.server.DB_PASS
                )
                .locations(locationsArray) // the sql files in the path
                .outOfOrder(true)
                .load();

        flyway.migrate();
    }
}