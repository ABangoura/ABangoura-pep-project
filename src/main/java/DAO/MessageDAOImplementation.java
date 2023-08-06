package DAO;

import Model.Message;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Class to provide the functionalities to access and modify message related data.
 * Implements the Message Data Access Object interface.
 */
public class MessageDAOImplementation implements MessageDAO {

    private Connection connection;

    // Constructor that creates a new connection to the database every time
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
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?)"; // SQL query to create a new message.
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); // Message ids will be automatically generated.
            ps.setInt(1, newMessage.getPosted_by());
            ps.setString(2, newMessage.getMessage_text());
            ps.setLong(3, newMessage.getTime_posted_epoch());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys(); // Will return the message id.
            if(rs.next()) { // If the message id exists...
                int messageId = (int) rs.getInt(1);
                // ...then assign it to the newly created message, and return the message.
                return new Message(messageId, newMessage.getPosted_by(), newMessage.getMessage_text(), newMessage.getTime_posted_epoch());
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null; // No message was created.
    }

    /**
     * Method to get all messages from the message table.
     * @return List<Message> a list of messages.
     */
    @Override
    public List<Message> getAllMessages() {

        List<Message> messages = new ArrayList<>(); // List to hold all messages.

        try {
            String sql = "SELECT * FROM message"; // Query to select all messages from message table.
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) { // Process result of query.
                Message newMessage = new Message();
                newMessage.setMessage_id(rs.getInt("message_id"));
                newMessage.setPosted_by(rs.getInt("posted_by"));
                newMessage.setMessage_text(rs.getString("message_text"));
                newMessage.setTime_posted_epoch(rs.getLong("time_posted_epoch"));

                messages.add(newMessage); // add each row (message) to the 'messages' list.
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return messages; // Return the list.
    }

    /**
     * Method to retrieve a message by its id.
     * @param messageId id of message to be retrieved.
     * @return Message the message that was retrieved.
     */
    @Override
    public Message getMessageByID(int messageId) {

        try {
            String sql = "SELECT * FROM message WHERE message.message_id = ?"; // SQL statement to return a message by id.
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, messageId);

            ResultSet rs = ps.executeQuery(); // Now 'rs' has only one row from 'message' table (if query was successful).

            // Retrieve data from 'rs' and return the message through a new Message object.
            while(rs.next()) {
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

            return true; // Message deleted.

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
    public Message updateMessageByID(int message_Id, Message message) {
        try {
            String sql = "UPDATE message SET message.message_text = ? WHERE message.message_id = ?"; // SQL query to update the message from the given id.
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, message.getMessage_text());
            ps.setInt(2, message_Id);
            int numberOfRows = ps.executeUpdate(); // The update will return the number of rows affected.

            if(numberOfRows > 0) { // If update was successful...
                String mySQL = "SELECT * FROM message WHERE message.message_id = ?"; // ...get the message from database...
                PreparedStatement ps2 = connection.prepareStatement(mySQL);
                ps2.setInt(1, message_Id);

                ResultSet rs = ps2.executeQuery(mySQL);
                // ...place it a new message object...
                Message newMessage = new Message();
                newMessage.setMessage_id(rs.getInt("message_id"));
                newMessage.setPosted_by(rs.getInt("posted_by"));
                newMessage.setMessage_text(rs.getString("message_text"));
                newMessage.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                
                return newMessage; // ...and return the updated message.
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null; // No message was updated.
    }

    /**
     * Method to get all messages by a user given the user id.
     * @param userId id of user whose messages need to be retrieved.
     * @return List<Message> a list of messages that were retrieved.
     */
    @Override
    public List<Message> getAllMessagesByUserID(int userId) {
        List<Message> messages = new ArrayList<>(); // List to hold all messages retrieved.

        try {
            String sql = "SELECT * FROM message WHERE message.posted_by = ?"; // Query to get all messages from a given user id.
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery(); // Execute query. Will return all messages from a given user.

            while(rs.next()) { // If query was successful, pass the data to a new message object...
                Message newMessage = new Message();
                newMessage.setMessage_id(rs.getInt("message_id"));
                newMessage.setPosted_by(rs.getInt("posted_by"));
                newMessage.setMessage_text(rs.getString("message_text"));
                newMessage.setTime_posted_epoch(rs.getLong("time_posted_epoch"));

                messages.add(newMessage); // ...and add message to list.
            }
            return messages; // Return the list.

        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null; // No messages were found.
    }
    
    
}
