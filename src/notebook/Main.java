package notebook;

import notebook.controller.UserController;
import notebook.repository.GBRepository;
import notebook.repository.impl.UserRepository;
import notebook.util.DBConnector;
import notebook.view.UserView;

public class Main {
    public static void main(String[] args) {
        // сохраняем данные в новом формате
        DBConnector dbConnector = new DBConnector("db.txt");
        dbConnector.createDB();
        GBRepository repository = new UserRepository(dbConnector);
        UserController controller = new UserController(repository);
        UserView view = new UserView(controller);
        view.run();
    }
}
