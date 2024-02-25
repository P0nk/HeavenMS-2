package database.note;

import model.Note;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NoteRowMapper implements RowMapper<Note> {

    @Override
    public Note map(ResultSet rs, StatementContext ctx) throws SQLException {
        int id = rs.getInt("id");
        String message = rs.getString("message");
        String from = rs.getString("from");
        String to = rs.getString("to");
        long timestamp = rs.getLong("timestamp");
        int fame = rs.getInt("fame");
        return new Note(id, message, from, to, timestamp, fame);
    }
}
