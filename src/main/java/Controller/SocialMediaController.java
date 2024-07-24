package Controller;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Service.AccountService;
import Service.MessageService;



/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    

    public SocialMediaController(){
        this.accountService=new AccountService();
        this.messageService=new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public  Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register",this::postAccountHandler);
        app.post("/login",this::postLoginHandler);
        app.post("/messages",this::postMessagesHandler);
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{message_id}",this::getMessageByIdHandler);
        app.delete("/messages/{message_id}",this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}",this::patchMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByparticularUserHandler);


        return app;
    }
    private void postAccountHandler(Context ctx) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

         Account existingAccount = accountService.getAccountByUsername(account.getUsername());
         if (existingAccount != null) {
           ctx.status(400);
        
        }
         else if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            ctx.status(400);
        
        }
        else{
            Account addedAccount = accountService.insertAccount(account);
             ctx.json(addedAccount).status(200);

        }
       
        

        

       

    }

    private void postLoginHandler(Context ctx) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedInAccount = accountService.login(account.getUsername(), account.getPassword());
        if (loggedInAccount != null) {
            ctx.json(loggedInAccount);
        } else {
            ctx.status(401);
        }
    }

    private void postMessagesHandler(Context ctx) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);

        if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 255 || accountService.getAccountById(message.getPosted_by())!=true) {
            ctx.status(400);
            return;
        }else{
              Message addedMessage = messageService.insertMessage(message);
              ctx.json(addedMessage).status(200);
        }

      
    }

    private void getMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);

    }

    private void getMessageByIdHandler(Context ctx) {
   
            int messageId = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(messageId);
            
            if (message != null) { 
                ctx.json(message).status(200);
            } 
             else 
                ctx.status(200);
       
    }

    private void deleteMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(messageId);
    
        if (deletedMessage != null) {
            ctx.json(deletedMessage);
        } else {
            ctx.status(200).json("");
        }
    }
    private void patchMessageByIdHandler(Context ctx) throws Exception {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        String newMessageText = mapper.readValue(ctx.body(), Message.class).getMessage_text();

        if (newMessageText.isBlank() || newMessageText.length() > 255) {
            ctx.status(400);
            return;
        }

        Message messageToUpdate = new Message();
        messageToUpdate.setMessage_id(messageId);
        messageToUpdate.setMessage_text(newMessageText);

        boolean isUpdated = messageService.updateMessage(messageToUpdate);
        if (isUpdated) {
            Message updatedMessage = messageService.getMessageById(messageId);
            ctx.json(updatedMessage);
        } else {
            ctx.status(400);
        }
    }

    private void getMessagesByparticularUserHandler(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByUser(accountId);
        ctx.json(messages);
    }



}