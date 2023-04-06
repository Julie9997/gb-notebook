package notebook.repository.impl;

import notebook.util.DBConnector;
import notebook.util.mapper.impl.UserMapper;
import notebook.model.User;
import notebook.repository.GBRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserRepository implements GBRepository {
    private final UserMapper mapper;
    private final DBConnector connector;

    public UserRepository(DBConnector connector, String sep) {
        this.mapper = new UserMapper(sep);
        this.connector = connector;
    }

    public UserRepository(DBConnector connector) {
        this(connector, ",");
    }

    public List<String> readAll() {
        List<String> lines = new ArrayList<>();
        try {
            File file = new File(connector.dbPath);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            if (line != null) {
                lines.add(line);
            }
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    lines.add(line);
                }
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    @Override
    public List<User> findAll() {
        List<String> lines = readAll();
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            users.add(mapper.toOutput(line));
        }
        return users;
    }

    @Override
    public User create(User user) {
        List<User> users = findAll();
        long max = 0L;
        for (User u : users) {
            long id = u.getId();
            if (max < id){
                max = id;
            }
        }
        long next = max + 1;
        user.setId(next);
        users.add(user);
        write(users);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        List<User> users = findAll();
        for (User user : users) {
            if (Objects.equals(user.getId(), id)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> update(Long id, User update) {
        try {
            List<User> users = findAll();
            User updateUser = users.stream().filter(u -> u.getId().equals(id)).findFirst().get();
            updateUser.setFirstName(update.getFirstName());
            updateUser.setLastName(update.getLastName());
            updateUser.setPhone(update.getPhone());
            List<String> list = new ArrayList<>();
            for(User user: users) {
                list.add(mapper.toInput(user));
            }
            write(users);
            return Optional.of(updateUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Long id) {
        List<User> users = findAll();
        for (User user: users) {
            if (user.getId().equals(id)) {
                users.remove(user);
                write(users);
                return true;
            }
        }
        return false;
    }


    private void write(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User u: users) {
            lines.add(mapper.toInput(u));
        }
        try (FileWriter writer = new FileWriter(connector.dbPath, false)) {
            for (String line : lines) {
                writer.write(line);
                writer.append('\n');
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public UserMapper getMapper() {
        return mapper;
    }
}
