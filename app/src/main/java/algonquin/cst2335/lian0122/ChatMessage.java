package algonquin.cst2335.lian0122;

import androidx.annotation.NonNull;

public class ChatMessage {
    public static final int TYPE_SENT = 1;
    public static final int TYPE_RECEIVED = 2;

    private String message;
    private String timeSent;
    private int messageType; // Use this to store message type (sent or received)

    public ChatMessage(String message, String timeSent, int messageType) {
        this.message = message;
        this.timeSent = timeSent;
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public int getMessageType() {
        return messageType;
    }
    @NonNull
    @Override
    public String toString(){return message;}
}