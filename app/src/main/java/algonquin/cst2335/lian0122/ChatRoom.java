package algonquin.cst2335.lian0122;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import java.util.concurrent.ExecutorService;
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
    private ChatMessageDAO mDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);

        // Initialize the database and DAO asynchronously to avoid blocking the UI thread.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
            mDAO = db.cmDAO();

            // Load messages from the database only if they haven't already been loaded.
            if (chatMessages.isEmpty()) {
                chatMessages.addAll(mDAO.getAllMessages());
                runOnUiThread(() -> binding.recycleView.getAdapter().notifyDataSetChanged());
            }
        });

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        setupRecyclerView();
        setupMessageSendReceiveListeners();
    }

    private void setupRecyclerView() {
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

    private void setupMessageSendReceiveListeners() {
        binding.sendButton.setOnClickListener(v -> {
            sendMessage(ChatMessage.TYPE_SENT);
        });

        binding.receiveButton.setOnClickListener(v -> {
            sendMessage(ChatMessage.TYPE_RECEIVED);
        });
    }

    private void sendMessage(int messageType) {
        String messageText = binding.textInput.getText().toString();
        String currentDateAndTime = sdf.format(new Date());
        ChatMessage newMessage = new ChatMessage(messageText, currentDateAndTime, messageType);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            mDAO.insertMessage(newMessage); // Insert into database
            chatMessages.add(newMessage); // Add to local list

            runOnUiThread(() -> {
                myAdapter.notifyItemInserted(chatMessages.size() - 1);
                binding.textInput.setText(""); // Clear the previous text
            });
        });
    }

    private void promptForDelete(int position, TextView messageView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
        builder.setMessage("Do you want to delete the message: " + messageView.getText())
                .setTitle("Question: ")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", (dialog, cl) -> {
                    ChatMessage removedMessage = chatMessages.remove(position);
                    new Thread(() -> {
                        mDAO.deleteMessage(removedMessage); // Delete from database
                        runOnUiThread(() -> {
                            myAdapter.notifyItemRemoved(position);
                            Snackbar.make(messageView, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk -> {
                                        new Thread(() -> {
                                            mDAO.insertMessage(removedMessage); // Re-insert into database on Undo
                                            chatMessages.add(position, removedMessage); // Re-add to local list on Undo
                                            runOnUiThread(() -> myAdapter.notifyItemInserted(position));
                                        }).start();
                                    })
                                    .show();
                        });
                    }).start();
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
