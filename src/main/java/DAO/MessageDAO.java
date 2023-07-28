package DAO;

import Model.Message;
import java.util.*;

// Interface to abstract table 'message' data access implementation.
public interface MessageDAO {

    public Message insertNewMessage(Message newMessage);

    public List<Message> getAllMessages();
    
    public Message getMessageByID(int messageId);

    public boolean deleteMessageByID(int messageId);

    public Message updateMessageByID(int messageId);

    public List<Message> getAllMessagesByUserID(int userId);

}
