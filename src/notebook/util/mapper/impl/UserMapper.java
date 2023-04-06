package notebook.util.mapper.impl;

import notebook.util.mapper.Mapper;
import notebook.model.User;

public class UserMapper implements Mapper<User, String> {
    private final String separator;

    public UserMapper(String sep) {
        this.separator = sep;
    }
    public UserMapper() {
        this(",");
    }
    @Override
    public String toInput(User user) {
        return String.join(separator, user.getId().toString(), user.getFirstName(), user.getLastName(), user.getPhone());
    }
    @Override
    public User toOutput(String str) {
        String[] lines = str.split(separator);
        long id;
        if (isDigit(lines[0])) {
            id = Long.parseLong(lines[0]);
            return new User(id, lines[1], lines[2], lines[3]);
        }
        throw new NumberFormatException("Id must be a large number");
    }
    private boolean isDigit(String str) throws NumberFormatException {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
