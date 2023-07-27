package Service;

import java.util.*;
import DAO.*;
import Model.*;

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
     * @return message object that was inserted (if successful), null otherwise.
     */
    public Message insertNewMessage(Message newMessage) {
        return messageDao.insertNewMessage(newMessage);
    } 
    
}
