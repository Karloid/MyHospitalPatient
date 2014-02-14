package com.krld.patient;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import java.util.*;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        final List<Button> buttons = new ArrayList<Button>();
        final Game gameView = new Game(this);
        final MainActivity thisActivity = this;
        final LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
//	layout.setLayoutMode(H
        final Button upButton = new Button(this);
//	layout.addView(upButton);
        layout.addView(gameView);
    }
}

   /*  	final	Button addButton = new Button(this);
addButton.setText("push me!");
final LinearLayout layout = (LinearLayout)findViewById(R.id.layout);	
final Button resetButton = new Button(this);
layout.addView(resetButton);
resetButton.setText("Try clear app!");
resetButton.setOnClickListener(new
View.OnClickListener() {
@Override
public void onClick(View	v)
{
if (Math.random() > 0.5)
{addButton.setText("nice one!");}
else
{addButton.setText("im think, u good human!");}
for (Button button:buttons)
{
layout.removeView(button);

}

}
});
layout.addView(addButton);
addButton.setOnClickListener(new
View.OnClickListener() {
@Override
public void onClick(View	v)
{
if (Math.random() > 0.5)
{addButton.setText("OH NOES! PLZ STOOOOP!");}
else
{addButton.setText("WHUT ARE U DOING???:77!");}
Button button = new Button(thisActivity);
button.setEnabled(false);
if (Math.random() > 0.5)
{
button.setText("BAD YELLOW BUTTON");
}
else*/
