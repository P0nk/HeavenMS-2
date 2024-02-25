package database;

import org.jdbi.v3.core.JdbiException;

public class DaoException extends JdbiException {

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
