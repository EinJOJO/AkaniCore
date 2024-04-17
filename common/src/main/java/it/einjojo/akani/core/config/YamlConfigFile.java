package it.einjojo.akani.core.config;

import dev.dejvokep.boostedyaml.YamlDocument;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class YamlConfigFile {


    private final MariaDbConfig mariaDBConfig;
    private final RedisCredentials redisCredentials;
    private final YamlDocument yamlDocument;

    public YamlConfigFile(Path filePath) throws IOException {
        yamlDocument = YamlDocument.create(filePath.toFile(), defaultFileContents());
        mariaDBConfig = new MariaDbConfig.Impl(yamlDocument);
        redisCredentials = new RedisCredentials.Impl(yamlDocument);
    }

    public boolean save() throws IOException {
        return yamlDocument.save();
    }

    public boolean update() throws IOException {
        return yamlDocument.update();
    }

    public boolean reload() throws IOException {
        return yamlDocument.reload();
    }

    public InputStream defaultFileContents() {
        return getClass().getResourceAsStream("/config.yml");
    }

    public MariaDbConfig mariaDBConfig() {
        return mariaDBConfig;
    }

    public RedisCredentials redisCredentials() {
        return redisCredentials;
    }


}
