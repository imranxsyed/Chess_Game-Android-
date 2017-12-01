package com.example.ptafo.chessgame42;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;



public class SavedGames extends Activity {
    TableRow selected_Row;
    GameActivity game;
    Button delete,openButton;
    RecordMoves holdRM;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_game);

        try{
            GameActivity reg = this.game = GameRead.readGames(this.getApplicationContext());
            if(reg==null){
                this.game = new GameActivity();
                GameRead.saveGames(this.game,this.getApplicationContext());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holdRM==null){
                    showError();
                    return;
                }
                game.removeGame(holdRM.getFileName());
                holdRM = null;
                selected_Row =null;
                clearTable();
                upDateSavedGames();
                fillTable();
            }
        });
        openButton = (Button) findViewById(R.id.open);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGame();
            }
        });
        fillTable();

    }

    private void showError(){
        Toast toast = Toast.makeText(this,"Error: No game was selected, Please Select One",Toast.LENGTH_LONG);
        toast.show();
    }

    private void fillTable(){
        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/Fontastique.ttf");
        TextView fnHeader = (TextView) findViewById(R.id.filename);
        TextView dHeader = (TextView) findViewById(R.id.date);
        fnHeader.setTypeface(font);
        dHeader.setTypeface(font);

        TableLayout table = (TableLayout) findViewById(R.id.files);


        for(RecordMoves file:game.recordedGames){
            TableRow row = new TableRow(this);
            TextView filename = new TextView(this);
            TextView date = new TextView(this);
            filename.setWidth(200);
            filename.setText(file.getFileName());
            filename.setTypeface(font);
            date.setText(file.getStrnDate());
            date.setTypeface(font);
            row.addView(filename);
            row.addView(date);
            row.setTag(file.getFileName());
            row.setPadding(0, 4, 0, 2);
            row.setBackgroundColor(Color.TRANSPARENT);
            addlistener(row, file);
            table.addView(row);
            if(holdRM==file){
                row.setBackgroundColor(Color.LTGRAY);
            }
            //TableRow row2 = new TableRow(this);
            //row2.addView(divisorLine);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu ,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.sort_date:
                Collections.sort(game.recordedGames, RecordMoves.compareByDate);
                clearTable();
                fillTable();
                return true;
            case R.id.sort_filename:
                Collections.sort(game.recordedGames, RecordMoves.compareByName);
                clearTable();
                fillTable();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void addlistener(final TableRow row, final RecordMoves rm){

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holdRM = rm;
                selected_Row = row;
                clearTable();
                fillTable();
                /*Intent intent = new Intent(SavedGames.this, Replay.class);
                String filename = (String) row.getTag();
                intent.putExtra("filename", filename);

                startActivity(intent);*/
            }
        });

    }

    private void openGame(){
        if(selected_Row==null){
            showError();
            return;
        }
        Intent intent = new Intent(SavedGames.this, Replay.class);
        String filename = (String) selected_Row.getTag();
        intent.putExtra("filename", filename);

        startActivity(intent);
    }


    private void clearTable(){
        TableLayout table = (TableLayout)findViewById(R.id.files);
        table.removeAllViews();
    }

    private void upDateSavedGames(){
        ;
        try {
            GameRead.saveGames(game,SavedGames.this.getApplicationContext());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
