package dev.jbcl.Service.Backup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.jbcl.Local.LocalDateAdapter;
import dev.jbcl.Local.LocalDateTimeAdapter;
import dev.jbcl.Model.Funkos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BackupImpl implements Backup<Funkos>{
    private static BackupImpl instance;
    private final String APP_PATH = System.getProperty("user.dir");
    private final String DATA_DIR = APP_PATH + File.separator + "data";
    private final String BACKUP_FILE = DATA_DIR + File.separator + "funkos.json";
    private static Logger logger = LoggerFactory.getLogger(BackupImpl.class);
    private BackupImpl() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
    }
    public static BackupImpl getInstance() {
        if (instance == null) {
            instance = new BackupImpl();
        }
        return instance;
    }

    @Override
    public void backup(List<Funkos> funkos) throws IOException {
        logger.debug("Creando backup de funkos");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
        String json = gson.toJson(funkos);
        Files.writeString(new File(BACKUP_FILE).toPath(), json);
    }

    @Override
    public List<Funkos> restore() throws IOException {
        logger.debug("Borrando backup de funkos");
        Gson gson = new GsonBuilder().create();
        String json = "";
        json = Files.readString(new File(BACKUP_FILE).toPath());
        return gson.fromJson(json, new TypeToken<List<Funkos>>() {
        }.getType());
    }

}
