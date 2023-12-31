package dev.jbcl.Service.Backup;

import java.io.IOException;
import java.util.List;

public interface Backup<T> {
    void backup(List<T> data) throws IOException;

    List<T> restore() throws IOException;
}
