package algonquin.cst2335.lian0122;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.lian0122.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.lian0122.databinding.SentMessageBinding;
import algonquin.cst2335.lian0122.databinding.ReceiveMessageBinding;

public class ChatRoom extends AppCompatActivity {
    private ActivityChatRoomBinding binding;
    private ArrayList<ChatMessage> chatMessages = new ArrayList<>();
    private ChatRoomViewModel chatModel;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> myAdapter;
    private SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        chatMessages = chatModel.getChatMessages().getValue();

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(),MessageDatabase.class,"database-name").build();
        ChatMessageDAO mDAO = db.cmDAO();

        if (chatMessages == null) {
            chatModel.chatMessages.setValue(chatMessages = new ArrayList<>());
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                chatMessages.addAll( mDAO.getAllMessages() ); //Get the data from database
                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //Load the RecyclerView
            });
        }

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.sendButton.setOnClickListener(clk -> {
            String inputMessage = binding.textInput.getText().toString();
            String currentDateAndTime = sdf.format(new Date());
            chatMessages.add(new ChatMessage(inputMessage, currentDateAndTime, ChatMessage.TYPE_SENT));
            myAdapter.notifyItemInserted(chatMessages.size() - 1);
            binding.textInput.setText(""); // Clear the previous text
        });

        binding.receiveButton.setOnClickListener(clk -> {
            // Simulate receiving a message
            String receivedMessage = binding.textInput.getText().toString();
            String currentDateAndTime = sdf.format(new Date());
            chatMessages.add(new ChatMessage(receivedMessage, currentDateAndTime, ChatMessage.TYPE_RECEIVED));
            myAdapter.notifyItemInserted(chatMessages.size() - 1);
            binding.textInput.setText(""); // Clear the previous text
        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == ChatMessage.TYPE_SENT) {
                    SentMessageBinding sentBinding = SentMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new SentMessageViewHolder(sentBinding);
                } else {
                    ReceiveMessageBinding receiveBinding = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new ReceivedMessageViewHolder(receiveBinding);
                }
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ChatMessage message = chatMessages.get(position);
                if (holder instanceof SentMessageViewHolder) {
                    ((SentMessageViewHolder) holder).bind(message);
                } else if (holder instanceof ReceivedMessageViewHolder) {
                    ((ReceivedMessageViewHolder) holder).bind(message);
                }
            }

            @Override
            public int getItemCount() {
                return chatMessages.size();
            }

            @Override
            public int getItemViewType(int position) {
                return chatMessages.get(position).getMessageType();
            }
        };
        binding.recycleView.setAdapter(myAdapter);
    }

    private void promptForDelete(int position, TextView messageView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
        builder.setMessage("Do you want to delete the message: " + messageView.getText())
                .setTitle("Question: ")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", (dialog, cl) -> {
                    ChatMessage removedMessage = chatMessages.remove(position);
                    myAdapter.notifyItemRemoved(position);
                    Snackbar.make(messageView, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                            .setAction("Undo", clk -> {
                                chatMessages.add(position, removedMessage);
                                myAdapter.notifyItemInserted(position);
                            })
                            .show();
                })
                .show();
    }

    // Define ViewHolder for sent messages
    class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public SentMessageViewHolder(SentMessageBinding binding) {
            super(binding.getRoot());
            messageText = binding.messageText;
            timeText = binding.timeText;
            itemView.setOnClickListener(v -> promptForDelete(getAbsoluteAdapterPosition(),messageText));
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getMessage());
            timeText.setText(message.getTimeSent());
        }
    }

    // Define ViewHolder for received messages
     class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        ReceivedMessageViewHolder(ReceiveMessageBinding binding) {
            super(binding.getRoot());
            messageText = binding.messageText2;
            timeText = binding.timeText2;
            itemView.setOnClickListener(v -> promptForDelete(getAbsoluteAdapterPosition(),messageText));
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getMessage());
            timeText.setText(message.getTimeSent());
        }
    }
}
