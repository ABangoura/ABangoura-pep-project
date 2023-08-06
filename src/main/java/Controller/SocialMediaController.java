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
        // Endpoints.
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
     * Handler to register new account.
     * @param context Javalin Context object for managing HTTP request/response.
     * @throws JsonProcessingException exception thrown when Jackson cannot create a class instance.
     */
    private void registerAccountHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class); // Use the ObjectMapper to create an instance of the account in the 'context' body.

        Account addedAccount = accountService.register(account); // Register the account (add to account table).
        if(addedAccount != null) { // If registration was successful...
            context.json(mapper.writeValueAsString(addedAccount)); //...then return a JSON representation of the registered account.
        } else {
            context.status(400); // Else, return a 400 client error.
        }
    }

    /**
     * Handler to log a user in.
     * @param context Javalin Context object for managing HTTP request/response.
     * @throws JsonProcessingException exception thrown when Jackson cannot create a class instance.
     */
    private void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class); // Use the ObjectMapper to create an instance of the account in the 'context' body.
        Account accountToLogin = accountService.login(account); // Log account in.
        if(accountToLogin != null) { // If login was successful...
            context.json(mapper.writeValueAsString(accountToLogin)); //...then return a JSON representation of the account that was logged in.
        } else {
            context.status(401); // Else, return a 401 unthorized error.
        }
    }

    /**
     * Handler to create a new message.
     * @param context Javalin Context object for managing HTTP request/response.
     * @throws JsonProcessingException exception thrown when Jackson cannot create a class instance.
     */
    private void insertNewMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class); // Use the ObjectMapper to create an instance of a message from data in the 'context' body.

        Message addedMessage = messageService.insertNewMessage(message); // Insert the newly created message.
        if(addedMessage != null) { // If successful...
            context.json(mapper.writeValueAsString(addedMessage)); // ...return a JSON representation of the message.
        } else {
            context.status(400); // Else return a 400 client error.
        }
    }

    /**
     * Handler to retrieve all messages from database.
     * @param Context Javalin Context object for managing HTTP request/response.
     */
    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getallMessages(); // Retrieve all messages...
        context.json(messages); // ...and return them as a list through the Context object.
    }

    /**
     * Handler to retrieve a message by the message id.
     * @param context Javalin Context object for managing HTTP request/response.
     * @throws JsonProcessingException exception thrown when Jackson cannot create a class instance.
     */
    private void getMessageByIDHandler(Context context) throws JsonProcessingException {
        int id = Integer.parseInt(context.pathParam("message_id")); // Get message id from endpoint path parameter.

        ObjectMapper mapper = new ObjectMapper();
        Message messageToReturn = messageService.getMessageById(id); // Retrieve the message by given id.
        if(messageToReturn != null) { // If successful...
            context.json(mapper.writeValueAsString(messageToReturn)); // ...return a JSON representation of the message.
        } 
    }

    /**
     * Handler to delete a message given the message id.
     * This handler should return a message object if deletion was successful,
     * with code 200.
     * @param context Javalin Context object for managing HTTP request/response.
     * @throws JsonProcessingException exception thrown when Jackson cannot create a class instance.
     */
    private void deleteMessageByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(context.pathParam("message_id")); // From the path parameter, get id of message to be deleted.
        // Check if the message exists.
        Message message = messageService.getMessageById(id);
        if(message != null) { // If message exists...
            messageService.deleteMessageByID(id); // ...delete it...
            context.json(mapper.writeValueAsString(message)); // ...then return a JSON representation of the deleted message.
        }
    }

    /**
     * Handler to update a message, given a message id.
     * @param context Javalin Context object for managing HTTP request/response.
     * @throws JsonProcessingException exception thrown when Jackson cannot create a class instance.
     */
    private void updateMessageByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class); // Use the ObjectMapper to create an instance of the message in the 'context' body.
        int message_id = Integer.parseInt(context.pathParam("message_id")); // Retrieve message id from path parameter since it was not included in the 'context' body.

        messageService.updateMessageByID(message_id, message); // Perform update given the data retrieved from the request.
        Message updatedMessage = messageService.getMessageById(message_id); // Retrieve updated message.

        // Before returning updated message through the response, check that it exists,
        // it's not blank and its length is less than 255.
        if((updatedMessage != null) && (!updatedMessage.getMessage_text().isBlank()) && (updatedMessage.getMessage_text().length() < 255)) {
            context.json(mapper.writeValueAsString(updatedMessage)); // If those constraints are met, then return a JSON representation of the updated message.
        }else {
            context.status(400); // If constraints are not met, return a 400 client error.
        }
    }

    /**
     * Handler to get all messages by a user, given the user id.
     * @param context Javalin Context object for managing HTTP request/response.
     * @throws JsonProcessingException exception thrown when Jackson cannot create a class instance.
     */
    private void getAllMessagesByUserIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int user_id = Integer.parseInt(context.pathParam("account_id")); // Retrieve account id (user id) from path parameter.
        // Since 'account_id' is a foreing key referenced by 'posted_by' in the message table,
        // we can use it to retrieve all messages associated to it.
        List<Message> list = messageService.getAllMessagesByUserID(user_id);

        if(list != null) { // If the list is not empty (no messages from given account)...
            context.json(mapper.writeValueAsString(list)); //...then return a JSON representation of the list of messages.
        }
    }
}