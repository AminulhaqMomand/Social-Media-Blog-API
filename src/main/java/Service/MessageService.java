package Service;

import static org.mockito.ArgumentMatchers.nullable;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    public MessageService(){
        this.messageDAO=new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO){
        this.messageDAO=messageDAO;
    }
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }
    public Message insertMessage(Message message){
        return messageDAO.insertMessage(message);
    }
    public List<Message> getMessagesByUser(int posted_by){
        return messageDAO.getMessagesByUser(posted_by);
    }
    public Message getMessageById(int message_id) {
        return messageDAO.getMessageById(message_id);
    }
    public boolean updateMessage(Message message) 
    {
      return messageDAO.updateMessage(message);
    }
    public Message deleteMessage(int message_id)
    {
       Message message= messageDAO.getMessageById(message_id);
       if( messageDAO.deleteMessage(message_id)==true)
        return message;
        else
        return null;
    }

}
