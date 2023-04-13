package notebook.view;

import notebook.controller.UserController;
import notebook.model.User;
import notebook.util.Commands;
import notebook.util.logger.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserView {
    private final static Logger log = Log.log(UserView.class.getName());
    private final UserController userController;

    public UserView(UserController userController) {
        this.userController = userController;
    }

    public void run() {
        Map<String, Command> commands = new HashMap<>();
        commands.put("CREATE", this::createUser);
        commands.put("READ", this::readUser);
        commands.put("LIST", this::listUsers);
        commands.put("UPDATE", this::updateUser);
        commands.put("DELETE", this::deleteUser);
        commands.put("EXIT", () -> System.exit(0));

        while (true) {
            String command = prompt("Введите команду: ");
            Command com = commands.get(command.toUpperCase());
            if (com == null) {
                System.out.println("Недопустимая команда: " + command);
                continue;
            }
            com.execute();
        }
    }

    private interface Command {
        void execute();
    }

    private User createUser() {
        User user = creation();
        userController.saveUser(user);
        log.log(Level.INFO, "New note is created: " + user);
        return user;
    }

    private void readUser() {
        String id = prompt("Enter user id: ");
        try {
            User user = userController.findUserById(Long.parseLong(id));
            System.out.println(user);
            System.out.println();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void listUsers() {
        List<User> users = userController.getAllUsers();
        System.out.println();
        for (User u : users) {
            System.out.println(u);
            System.out.println("----------------");
        }
    }

    private void updateUser() {
        String userId = prompt("Enter user id: ");
        userController.update(Long.parseLong(userId), creation());
        log.log(Level.INFO, "Note is updated, id: " + userId);
    }

    private void deleteUser() {
        String userId = prompt("Enter user id: ");
        userController.deleteUser(Long.parseLong(userId));
        log.log(Level.INFO, "Note is deleted, id: " + userId);
    }

    private String prompt(String message) {
        Scanner in = new Scanner(System.in);
        System.out.print(message);
        return in.nextLine();
    }

    private User creation() {
        return User.builder()
            .firstName(prompt("Имя: "))
            .lastName(prompt("Фамилия: "))
            .phone(prompt("Номер: ")).build();
    }
}
