package algonquin.cst2335.lian0122.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
public class MainViewModel extends ViewModel{

//    public String editString; // 2
    public MutableLiveData<String> editString = new MutableLiveData<>(); // use MutableLiveData class to simplify the holding value
}
