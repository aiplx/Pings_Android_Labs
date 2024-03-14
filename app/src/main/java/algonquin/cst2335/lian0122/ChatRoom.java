package algonquin.cst2335.lian0122;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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

        if (chatMessages == null) {
            chatMessages = new ArrayList<>();
            chatModel.getChatMessages().postValue(chatMessages);
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

    // Define ViewHolder for sent messages
    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageText;
        private final TextView timeText;

        SentMessageViewHolder(SentMessageBinding binding) {
            super(binding.getRoot());
            messageText = binding.messageText;
            timeText = binding.timeText;
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getMessage());
            timeText.setText(message.getTimeSent());
        }
    }

    // Define ViewHolder for received messages
    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageText;
        private final TextView timeText;

        ReceivedMessageViewHolder(ReceiveMessageBinding binding) {
            super(binding.getRoot());
            messageText = binding.messageText2;
            timeText = binding.timeText2;
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getMessage());
            timeText.setText(message.getTimeSent());
        }
    }
}
