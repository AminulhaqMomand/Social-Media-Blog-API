package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    public Message tMessage;
 // Retrieve all messages
 public List<Message> getAllMessages() {
    Connection connection = ConnectionUtil.getConnection();
    List<Message> messages = new ArrayList<>();
    try {
        String sql = "SELECT * FROM message;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Message message = new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
            );
            messages.add(message);
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    
    return messages;
}

// Insert a new message
public Message insertMessage(Message message) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, message.getPosted_by());
        preparedStatement.setString(2, message.getMessage_text());
        preparedStatement.setLong(3, message.getTime_posted_epoch());
        preparedStatement.executeUpdate();
        ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
        if (pkeyResultSet.next()) {
            int generated_message_id = (int) pkeyResultSet.getLong(1);
            return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return null;
}

// Retrieve messages by a specific user
public List<Message> getMessagesByUser(int posted_by) {
    Connection connection = ConnectionUtil.getConnection();
    List<Message> messages = new ArrayList<>();
    try {
        String sql = "SELECT * FROM message WHERE posted_by = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, posted_by);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            Message message = new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
            );
            messages.add(message);
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return messages;
}

// Retrieve a message by its ID
public Message getMessageById(int message_id) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "SELECT * FROM message WHERE message_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, message_id);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            tMessage= new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
            );
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return tMessage;
}

// Update a message
public boolean updateMessage(Message message) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "UPDATE message set message_text = ? WHERE message_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
      
        preparedStatement.setString(1, message.getMessage_text());
       
        preparedStatement.setInt(2, message.getMessage_id());
        int rowsUpdated = preparedStatement.executeUpdate();
        return rowsUpdated > 0;
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return false;
}

// Delete a message
public boolean deleteMessage(int message_id) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "DELETE FROM message WHERE message_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, message_id);
        int rowsDeleted = preparedStatement.executeUpdate();
        return rowsDeleted >0;
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return false;
}

}
