package algonquin.cst2335.lian0122.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import algonquin.cst2335.lian0122.data.MainViewModel;
import algonquin.cst2335.lian0122.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding variableBinding; // Use the new method of using widgets
    private MainViewModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);
        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

//        setContentView(R.layout.activity_main);
//        TextView mytext = findViewById(R.id.mytext); //0 replace findViewById(R.id.myTextView) by variableBinding.myTextView
//        Button mybutton = findViewById(R.id.mybutton); //0
//        EditText myedit = findViewById(R.id.myedittext);//0

//        variableBinding.mytext.setText(model.editString); // 2
        variableBinding.mybutton.setOnClickListener(click ->{

//                String editString = variableBinding.myedittext.getText().toString(); // 1
//                model.editString = variableBinding.myedittext.getText().toString(); // 2
//                variableBinding.mytext.setText("Your edit text has:" + model.editString); // 2
                model.editString.postValue(variableBinding.myedittext.getText().toString());
        });
        model.editString.observe(this,s -> {
            variableBinding.mytext.setText("Your edit text has " + s);
        });

        // Page 5
        variableBinding.checkBox.setOnCheckedChangeListener((btn,isChecked) ->{
            model.isSelected.postValue(variableBinding.checkBox.isChecked());
        });

        variableBinding.radioButton.setOnCheckedChangeListener((btn,isChecked) ->{
            model.isSelected.postValue(variableBinding.radioButton.isChecked());
        });

        variableBinding.switch1.setOnCheckedChangeListener((btn, isChecked) ->{
            model.isSelected.postValue(variableBinding.switch1.isChecked());
        });

        model.isSelected.observe(this,selected ->{
            variableBinding.checkBox.setChecked(selected);
            variableBinding.radioButton.setChecked(selected);
            variableBinding.switch1.setChecked(selected);

            CharSequence text = "The value is now: " + selected ;
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(this,text ,duration).show();
        });

        // Page 6

    }
}