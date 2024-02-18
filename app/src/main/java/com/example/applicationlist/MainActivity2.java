package com.example.applicationlist;

import static android.app.PendingIntent.getActivity;
import static android.view.KeyEvent.KEYCODE_HOME;
import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity2 extends AppCompatActivity {

    private int number_text;

    private String name_doc;

    private String full_name_doc="";

    private String text_doc;

    List<String> list;

    boolean new_doc;

    boolean fast_text;

    boolean need_save=true;
    private EditText editTextTextMultiLine;

    private EditText editTextText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        System.out.println("открыто второе активити============================================");

        editTextTextMultiLine= findViewById(R.id.main_text_doc);
        editTextText= findViewById(R.id.header_text);
        System.out.println((getIntent().getIntExtra("number_text",number_text)));
        //fast_text=(getIntent().getIntExtra("number_text",number_text));
        if((getIntent().getIntExtra("number_text",number_text))>0){
            fast_text=false;
        }else{
            fast_text=true;
        }

        number_text=read_config(1);
        full_name_doc=(getIntent().getStringExtra("full_name_doc"));


        if(full_name_doc==null){
            new_doc=true;
            System.out.println("создание нового документа");

            new_doc();
        }
        else
        {
            new_doc=false;
            System.out.println("загрузка документа по адресу");

            load_doc(full_name_doc);


        }

        call_keyboard();



    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("пауза");
        //Toast.makeText(this,"пауза",Toast.LENGTH_SHORT).show();

        if(need_save){
            Toast.makeText(this,"сохранено",Toast.LENGTH_SHORT).show();
            save_doc(editTextText);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //System.out.println("произошло закрытие");





    }
    public int read_config(int num_string){
        int result = 0;

        //System.out.println("начало чтения ");

        try {
            FileInputStream fileInput = openFileInput("config.txt");
            InputStreamReader reader =new InputStreamReader(fileInput);
            BufferedReader bR=new BufferedReader(reader);

            int nuberstrings=1;
            String lines = "";
            //System.out.println("документ открыт");
            while(((lines=bR.readLine())!=null)){
                //System.out.println("номер строки="+nuberstrings+"lines="+lines);
                if(nuberstrings==num_string){
                    //System.out.println("нужная строка"+lines);
                    result= Integer.parseInt(lines);
                    //Toast.makeText(this,lines,Toast.LENGTH_SHORT).show();
                }
                nuberstrings=nuberstrings+1;
            }
            if(nuberstrings>0){
                //System.out.println("количество строк в документе"+nuberstrings);
                //Toast.makeText(this,nuberstrings,Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Сохраниний не обнуружено",Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this,"Нет сохранений",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this,"Иная ошибка",Toast.LENGTH_SHORT).show();
        }
        return result;
    }
    public String get_date_time(){
        // Текущее время
        Date currentDate = new Date();

        // Форматирование времени как "день.месяц.год"
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);

        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);

        String date_time=dateText+" "+timeText;


        return date_time;

    }

    public void call_keyboard(){


        EditText editText = findViewById(R.id.main_text_doc);
        editText.requestFocus();



        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


        if(new_doc){
            editText.selectAll();
        }


    }

    public void setMainActivity(View v){


        //need_save=false;
        Intent intent= new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void exit_button(View v){
        need_save=false;

        setMainActivity(v);
    }
    public void save_button(View v){
        need_save=true;

        if(fast_text){
            //need_save=false;
            finish();
        }else{
            setMainActivity(v);
        }

    }
    public void save_doc(View v){
        if(full_name_doc==null){
            System.out.println("новый документ");

            //System.out.println(full_name_doc);

            save_new_text(v);
        }
        else
        {
            System.out.println("изменение старого документа");
            //System.out.println(full_name_doc);
            edit_doc(full_name_doc);
        }

    }
    public void edit_doc(String full_name_doc){

        String scoretxt=editTextText.getText().toString();
        scoretxt=scoretxt+"\n"+ editTextTextMultiLine.getText().toString();
        try {
            File quizesDir = new File(full_name_doc);
            try (FileOutputStream fos = new FileOutputStream(quizesDir)) {
                fos.write((scoretxt+"\n").getBytes());
                fos.close();
                //Toast.makeText(this,"сохранено", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this,"ошибка записи", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this,"ошибка записи", Toast.LENGTH_SHORT).show();
        }


        //setMainActivity(editTextText);
    }
    public void save_new_text(View v){

        String scoretxt=editTextText.getText().toString();
        scoretxt=scoretxt+"\n"+ editTextTextMultiLine.getText().toString();
        try {
            File quizesDir = new File(getFilesDir(), "docs");
            if (!quizesDir.exists()) {
                quizesDir.mkdirs();
            }

            File quizfile = new File(quizesDir, number_text+".txt");
            try (FileOutputStream fos = new FileOutputStream(quizfile)) {

                fos.write((scoretxt+"\n").getBytes());
                fos.close();
                //Toast.makeText(this,"сохранено", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this,"ошибка записи", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {

            Toast.makeText(this,"ошибка записи", Toast.LENGTH_SHORT).show();
        }
        save_config(1, number_text+1);

        full_name_doc="/data/data/com.example.applicationlist/files/docs/"+number_text+".txt";

        //need_save=false;



    }
    public void save_config(int num_string, int result){

        String scoretxt= String.valueOf(result);
        try {

            FileOutputStream fileOutput =openFileOutput("config.txt",0);
            fileOutput.write((scoretxt+"\n").getBytes());//запись очков с отступом строки в память
            fileOutput.close();

        } catch (FileNotFoundException e) {
            Toast.makeText(this,"ошибка записи", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this,"ошибка записи", Toast.LENGTH_SHORT).show();
        }


    }
    public void new_doc(){

        editTextText.setText(get_date_time());
        editTextTextMultiLine.setText("Новая заметка");

    }

    public void load_doc(String dir){
        try {
            File quizesDir = new File(dir);
            FileInputStream fileInput = new FileInputStream(quizesDir);
            InputStreamReader reader =new InputStreamReader(fileInput);
            BufferedReader bR=new BufferedReader(reader);

            int nuberstrings=0;
            String lines = "";
            text_doc="";

            list = new ArrayList<String>();
            while(((lines=bR.readLine())!=null)){

                //System.out.println("номер строки="+nuberstrings+"lines="+lines);

                list.add(lines);

                //System.out.println(list.get(nuberstrings));

                if(nuberstrings==0){
                    name_doc=lines;
                }else{
                    text_doc=(text_doc+lines).toString();
                }

                nuberstrings=nuberstrings+1;

            }
            System.out.println("шапка="+name_doc);
            System.out.println("текст документа="+text_doc);



            editTextText.setText(name_doc);


            String str="";
            for(int i = 1; i < list.size(); i++) {


                str =str+ list.get(i);
                editTextTextMultiLine.setText(str);
                str =str+ "\n";
            }

            if(nuberstrings>0){
                //System.out.println("количество строк в документе"+nuberstrings);
            }else{
                Toast.makeText(this,"Сохраниний не обнуружено",Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this,"Нет сохранений",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this,"Иная ошибка",Toast.LENGTH_SHORT).show();
        }

    }
}