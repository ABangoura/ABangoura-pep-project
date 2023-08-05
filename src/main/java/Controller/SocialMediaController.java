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
        app.post("/register", this::registerAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::insertNewMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("/messages/{message_id}", this::updateMessageByIDHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserIDHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getallMessages();
        context.json(messages);
    }

    private void getMessageByIDHandler(Context context) throws JsonProcessingException {
        int id = Integer.parseInt(context.pathParam("message_id"));

        ObjectMapper mapper = new ObjectMapper();
        //Message message = mapper.readValue(context.body(), Message.class);
        Message messageToReturn = messageService.getMessageById(id);
        if(messageToReturn != null) {
            context.json(mapper.writeValueAsString(messageToReturn));
        } 
    }

    private void insertNewMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);

        Message addedMessage = messageService.insertNewMessage(message);
        if(addedMessage != null) {
            context.json(mapper.writeValueAsString(addedMessage));
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
    private void deleteMessageByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(context.pathParam("message_id"));

        Message message = messageService.getMessageById(id);
        if(message != null) {
            messageService.deleteMessageByID(id);
            context.json(mapper.writeValueAsString(message));
        }
    }

    private void updateMessageByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        int message_id = Integer.parseInt(context.pathParam("message_id"));

        messageService.updateMessageByID(message_id, message);
        Message updatedMessage = messageService.getMessageById(message_id);

        if((updatedMessage != null) && (!updatedMessage.getMessage_text().isBlank()) && (updatedMessage.getMessage_text().length() < 255)) {
            context.json(mapper.writeValueAsString(updatedMessage));
        }else {
            context.status(400);
        }
    }

    private void getAllMessagesByUserIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int user_id = Integer.parseInt(context.pathParam("account_id"));
        List<Message> list = messageService.getAllMessagesByUserID(user_id);

        if(list != null) {
            context.json(mapper.writeValueAsString(list));
            //context.status(200);
        }
    }

    private void registerAccountHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);

        Account addedAccount = accountService.register(account);
        if(addedAccount != null) {
            context.json(mapper.writeValueAsString(addedAccount));
        } else {
            context.status(400);
        }
    }

    private void loginHandler(Context context) throws JsonProcessingException {

        // ObjectMapper mapper = new ObjectMapper();
        // Account account = mapper.readValue(context.body(), Account.class);
        // String username = context.pathParam(account.getUsername());
        // String password = context.pathParam(account.getPassword());
        
        // Account existingAccount = accountService.getAccountByUsername(username);

        // if((existingAccount.username == username) && (existingAccount.getPassword() == password)) {
        //     context.json(mapper.writeValueAsString(existingAccount));
        // }else {
        //     context.status(400);
        // }

        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account accountToLogin = accountService.login(account);
        if(accountToLogin != null) {
            //Account accountToLogin = accountService.login(account);
            context.json(mapper.writeValueAsString(accountToLogin));
        } else {
            context.status(401);
        }
        

    }


}