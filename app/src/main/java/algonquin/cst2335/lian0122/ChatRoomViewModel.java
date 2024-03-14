package algonquin.cst2335.lian0122;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ChatRoomViewModel extends ViewModel {
    public MutableLiveData<ArrayList<ChatMessage>> chatMessages = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ChatMessage>> getChatMessages(){return chatMessages;}
}
