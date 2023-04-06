package notebook.util;

import java.io.File;

public class DBConnector {
    public String dbPath;

    public DBConnector(String dbPath) {
        this.dbPath = dbPath;
    }
    public DBConnector() { this("db.txt"); }

    public void createDB() {
        try {
            File db = new File(dbPath);
            if (db.createNewFile()) {
                System.out.println("DB created");
            }
            else {
                System.out.println("DB already exists");
            }
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }
}
