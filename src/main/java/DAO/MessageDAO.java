package DAO;

import Model.Message;
import java.util.*;

// Interface to set the foundation of how one will interract with the message table.
public interface MessageDAO {

    public Message insertNewMessage(Message newMessage);

    public List<Message> getAllMessages();
    
    public Message getMessageByID(int messageId);

    public boolean deleteMessageByID(int messageId);

    public Message updateMessageByID(int messageId, Message message);

    public List<Message> getAllMessagesByUserID(int userId);

}
