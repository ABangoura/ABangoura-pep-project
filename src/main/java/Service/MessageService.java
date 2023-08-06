package Service;

import java.util.*;
import DAO.MessageDAOImplementation;
import Model.Message;

/**
 * Message service class. This class provides the functionalities that will allow us
 * to implement the business logic of our messaging service.
 */

public class MessageService {
    private MessageDAOImplementation messageDao;

    /**
     * No-arg constructor.
     */
    public MessageService() {
        messageDao = new MessageDAOImplementation();
    }

    /**
     * Constructor with 'messageDao' parameter.
     * @param messageDao
     */
    public MessageService(MessageDAOImplementation messageDao) {
        this.messageDao = messageDao;
    }

    /**
     * Method to insert a new message into the database.
     * @param newMessage message to be inserted.
     * @return Message object message that was inserted (if successful), null otherwise.
     */
    public Message insertNewMessage(Message newMessage) {
        // Before creating a new message, check that it exists, it's not blank and its length is less than 255.
        if((messageDao.getMessageByID(newMessage.getMessage_id()) == null) &&
            (!newMessage.getMessage_text().isBlank()) && (newMessage.getMessage_text().length() < 255))
            return messageDao.insertNewMessage(newMessage);

        return null;
    } 

    /**
     * Method to return all messages from the database.
     * @return List<Message> a list of messages.
     */
    public List<Message> getallMessages() {
        return messageDao.getAllMessages();
    }

    /**
     * Method to return a message with a specified id.
     * @param id id of message to look for.
     * @return Message message to return.
     */
    public Message getMessageById(int id) {
        return messageDao.getMessageByID(id);
    }

    /**
     * Method to delete a message by id.
     * @param id id of message to be deleted.
     * @return boolean true if successful, false otherwise.
     */
    public boolean deleteMessageByID(int id) {
        return messageDao.deleteMessageByID(id);
    }

    /**
     * Method to update a message by its id.
     * @param id id of message to be updated.
     * @return Message the message that was updated.
     */
    public Message updateMessageByID(int id, Message message) {
        
        if(messageDao.getMessageByID(id) != null) { // If a message with given id exists...
            return messageDao.updateMessageByID(id, message); // ...return the message.
        } else {
            return null; // No message was found.
        }  
    }

    /**
     * Method to retrieve all messages from a user, using the user's id.
     * @param id id of the user.
     * @return List<Message> a list of all messages by a given user.
     */
    public List<Message> getAllMessagesByUserID(int id) {
        return messageDao.getAllMessagesByUserID(id);
    } 
}
