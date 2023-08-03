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

    /**
     * Method to create a new message in the message table.
     * @param newMessage message to be inserted.
     * @return Message object.
     */
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

    /**
     * Method to get all messages from the message table.
     * @return List<Message> a list of messages.
     */
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

    /**
     * Method to retrieve a message by its id.
     * @param messageId id of message to be retrieved.
     * @return Message the message that was retrieved.
     */
    @Override
    public Message getMessageByID(int messageId) {

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?"; // SQL statement to return a message by id.
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, messageId);

            ResultSet rs = ps.executeQuery(); // Now 'rs' has only one row from 'message' table (if query was successful).

            // Retrieve data from 'rs' and return the message.
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

    /**
     * Method to delete a message given its id.
     * @param messageId id of message to be deleted.
     * @return boolean returns true if successful, false otherwise.
     */
    @Override
    public boolean deleteMessageByID(int messageId) {

        try {
            String sql = "DELETE FROM message WHERE message.id = ?"; // SQL statement to delete a message by id.
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, messageId);

            ps.executeUpdate();

            return true;

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return false; // No message was found by the specified 'id'.
    }

    /**
     * Method to update a message given its id.
     * @param messageId id of message to be updated.
     * @return Message the message that was updated.
     */
    @Override
    public Message updateMessageByID(int message_Id, String message_text) {

        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, message_text);
            ps.setInt(2, message_Id);

            ResultSet rs = ps.executeQuery();
            Message message = new Message();
            message.setMessage_id(rs.getInt("message_id"));
            message.setPosted_by(rs.getInt("posted_by"));
            message.setMessage_text(rs.getString("message_text"));
            message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));

            return message;

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Method to get all messages by a user given the user id.
     * @param userId id of user whose messages need to be retrieved.
     * @return List<Message> a list of messages that were retrieved.
     */
    @Override
    public List<Message> getAllMessagesByUserID(int userId) {
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message WHERE message.posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Message newMessage = new Message();
                newMessage.setMessage_id(rs.getInt("message_id"));
                newMessage.setPosted_by(rs.getInt("posted_by"));
                newMessage.setMessage_text(rs.getString("message_text"));
                newMessage.setTime_posted_epoch(rs.getLong("time_posted_epoch"));

                messages.add(newMessage);
            }
            return messages;

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    
}
