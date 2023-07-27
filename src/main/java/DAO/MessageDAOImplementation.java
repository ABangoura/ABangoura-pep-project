package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MessageDAOImplementation implements MessageDAO {

    private Connection connection;

    // Constructor that creates a new connection every time
    // a new MessageDAOImplementation object is created.
    public MessageDAOImplementation() {
        connection = ConnectionUtil.getConnection();
    }

    // Message to create a new message in the 'message' table.
    @Override
    public Message insertNewMessage(Message newMessage) {
        try {
            String sql = "INSERT INTO message VALUES(default, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, newMessage.getPosted_by());
            ps.setString(2, newMessage.getMessage_text());
            ps.setLong(3, newMessage.getTime_posted_epoch());
            
            ResultSet rows = ps.executeQuery();
            if(rows.next()) {
                newMessage.setMessage_id(rows.getInt("message_id"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return newMessage;
    }

    @Override
    public List<Message> getAllMessages() {

        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Message newMessage = new Message();
                newMessage.setMessage_id(rs.getInt("message_id"));
                newMessage.setPosted_by(rs.getInt("posted_by"));
                newMessage.setMessage_text(rs.getString("message_text"));
                newMessage.setTime_posted_epoch(rs.getLong("time_posted_epoch"));

                messages.add(newMessage);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    @Override
    public Message getMessageByID(int messageId) {
        try {
            String sql = "SELECT * FROM message WHERE id = ?"; // SQL statement to return a message by id.
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, messageId);

            ResultSet rs = ps.executeQuery(); // Now 'rs' has only one row from 'message' table (if query was successful).

            // Retrive data from 'rs' and return the message.
            if(rs.next()) {
                // Create a new Message object  and set its fields from 'rs'.
                Message newMessage = new Message();
                newMessage.setMessage_id(rs.getInt("message_id"));
                newMessage.setPosted_by(rs.getInt("posted_by"));
                newMessage.setMessage_text(rs.getString("message_text"));
                newMessage.setTime_posted_epoch(rs.getLong("time_posted_epoch"));

                return newMessage;
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null; // No message was found by the specified 'id'.
    }

    @Override
    public boolean deleteMessageByID(int messageId) {
        try {
            String sql = "DELETE FROM message WHERE id = ?"; // SQL statement to delete a message by id.
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, messageId);

            ps.executeUpdate();

            return true;

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false; // No message was found by the specified 'id'.
    }

    @Override
    public Message updateMessageByID(int messageId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateMessageByID'");
    }

    @Override
    public Message getAllMessagesByUserId(int userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllMessagesByUserId'");
    }
    
}
