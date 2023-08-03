package Controller;

import Model.*;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    /**
     * No-arg constructor. Sets up service classes.
     * @return nothing.
     */
    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::insertNewAccount);
        app.post("/login", this::logAccountIn);
        app.post("/messages", this::insertNewMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageByID);
        app.delete("/messages/{message_id}", this::deleteMessageByID);
        app.patch("/messages/{message_id}", this::updateMessageByID);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserID);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessages(Context context) {
        List<Message> messages = messageService.getallMessages();
        context.json(messages);
    }

    private void getMessageByID(Context context) throws JsonProcessingException {
        int id = Integer.parseInt(context.pathParam("message_id"));

        ObjectMapper mapper = new ObjectMapper();
        //Message message = mapper.readValue(context.body(), Message.class);
        Message messageToReturn = messageService.getMessageById(id);
        if(messageToReturn != null) {
            context.json(mapper.writeValueAsString(messageToReturn));
        } 
    }

    private void insertNewMessage(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message addedMessage = messageService.insertNewMessage(message);
        if(addedMessage != null) {
            context.json(mapper.writeValueAsString(addedMessage));
            //context.status(200);
        } else {
            context.status(400);
        }
    }

    /**
     * Handler to delete a message given the message id.
     * This handler should return a message object if deletion was successful,
     * with code 200.
     * @param context
     * @throws JsonProcessingException
     */
    private void deleteMessageByID(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(context.pathParam("message_id"));

        Message message = messageService.getMessageById(id);
        if(message != null) {
            messageService.deleteMessageByID(id);
            context.json(mapper.writeValueAsString(message));
            //context.status(200);
        }
    }

    private void updateMessageByID(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        String message_text = context.pathParam("message_text");

        Message message = messageService.getMessageById(message_id);
        if(message != null) {
            Message updatedMessage = messageService.updateMessageByID(message_id, message_text);
            context.json(mapper.writeValueAsString(updatedMessage));
            //context.status(200);
        }
    }

    private void getAllMessagesByUserID(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int user_id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> list = messageService.getAllMessagesByUserID(user_id);

        if(list != null) {
            context.json(mapper.writeValueAsString(list));
            //context.status(200);
        }
    }

    private void insertNewAccount(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account addedAccount = accountService.insertNewAccount(account);
        if(addedAccount != null) {
            context.json(mapper.writeValueAsString(addedAccount));
            //context.status(200);
        } else {
            context.status(400);
        }
    }

    private void logAccountIn(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        if(accountService.logAccountIn(account) != null) {
            context.json(mapper.writeValueAsString(account));
        } else {
            context.status(401);
        }
        

    }


}