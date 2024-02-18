package com.example.applicationlist;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    public int number_text=0;



    public String[] listArr;

    public String[] NamesArr;

    private ListView listView;



    boolean fast_klick= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println("открыто первое активити============================================");
        listView=findViewById(R.id.listView);


        number_text=read_config(1);


        if(number_text!=0){
            ArrayList<File> b=listFilesWithSubFolders(new File("/data/data/com.example.applicationlist/files/docs"));

            //System.out.println(b);


            System.out.println("загрузка имеющихся документов");
            set_list(b);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("пауза");


        finish();

    }

    public void set_date(){
        // Текущее время
        Date currentDate = new Date();

        // Форматирование времени как "день.месяц.год"
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);

        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);


        String date_time=dateText+" "+timeText;

        System.out.println(date_time);
        //System.out.println(timeText);


    }
    public String get_time(){
        // Текущее время
        Date currentDate = new Date();

        // Форматирование времени как "день.месяц.год"
        //DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        //String dateText = dateFormat.format(currentDate);

        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String timeText = timeFormat.format(currentDate);


        //String date_time=dateText+" "+timeText;

        //System.out.println(timeText);
        //System.out.println(timeText);

        return timeText;

    }
    public ArrayList<File> listFilesWithSubFolders(File dir) {
        ArrayList<File> files = new ArrayList<File>();
        for (File file : dir.listFiles()) {
            if (file.isDirectory())
                files.addAll(listFilesWithSubFolders(file));
            else
                files.add(file);
        }
        return files;
    }
    public void setMainActivity2(View v){



        Intent intent= new Intent(this,MainActivity2.class);

        intent.putExtra("number_text",number_text);
        startActivity(intent);
        //finish();

    }

    public void set_doc(View v,String full_name_doc){


        //MainActivity2.load_doc(v);
        Intent intent= new Intent(this,MainActivity2.class);
        intent.putExtra("number_text",number_text);
        intent.putExtra("full_name_doc",full_name_doc);
        startActivity(intent);


    }

//    public void save_config(int num_string, int result){
//
//        String scoretxt= String.valueOf(result);
//
//        try {
//            File f = new File(getFilesDir(), "config.txt");
//            RandomAccessFile randomAccessFile=new RandomAccessFile(f, "rw");
//
//
//
//            randomAccessFile.seek(0);
//            randomAccessFile.writeBytes(scoretxt);
//
//
//
//            randomAccessFile.close();
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//
//    }

    public void save_config(int num_string, int result){

        String scoretxt= String.valueOf(result);



        try {

            FileOutputStream fileOutput =openFileOutput("config.txt",0);
            fileOutput.write((scoretxt+"\n").getBytes());//запись очков с отступом строки в память

            fileOutput.close();


            Toast.makeText(this,"сохранен документ "+scoretxt, Toast.LENGTH_SHORT).show();//временный вывод текста


            //Toast.makeText(this,"сохранено "+number_text, Toast.LENGTH_SHORT).show();//временный вывод текста

        } catch (FileNotFoundException e) {

            Toast.makeText(this,"ошибка записи", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {

            Toast.makeText(this,"ошибка записи", Toast.LENGTH_SHORT).show();
        }


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

    public String get_string_txt(int num_string,String fileadress){
        String result = "";

        //System.out.println("начало чтения ");

        try {
            File quizfile = new File(fileadress);
            FileInputStream fileInput = new FileInputStream(quizfile);

            InputStreamReader reader =new InputStreamReader(fileInput);
            BufferedReader bR=new BufferedReader(reader);

            int nuberstrings=1;
            String lines = "";
            //System.out.println("документ открыт");
            while(((lines=bR.readLine())!=null)){
                //System.out.println("номер строки="+nuberstrings+"lines="+lines);
                if(nuberstrings==num_string){
                    //System.out.println("нужная строка"+lines);

                    result= lines;
                    //System.out.println("выход из поиска");
                    return result;
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
        //System.out.println("выход из поиска");
        return result;
    }

    public void add_new_text_list(View v){


        setMainActivity2(v);


    }

    @SuppressLint("ClickableViewAccessibility")
    public void set_list(ArrayList<File> b){

        //System.out.println(b);

        listArr= new String[b.size()];

        NamesArr= new String[b.size()];


        for(int i=0;i<b.size();i++){


            listArr[i]= String.valueOf(b.get(i));


            NamesArr[i]= (get_string_txt(1,listArr[i])+"\n"+get_string_txt(2,listArr[i])+"\n"+get_string_txt(3,listArr[i]));
            //NamesArr[i]= (NamesArr[i]+get_string_txt(2,listArr[i]));
            //System.out.println("название документа="+NamesArr[i]);
            //NamesArr[i]= String.valueOf(b.get(i));
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, R.layout.style1,R.id.score_name,NamesArr);
        //ArrayAdapter<String> adapter2=new ArrayAdapter<>(this, R.layout.style1,R.id.textView,NamesArr);
        listView.setAdapter(adapter);
        //listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {



            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                System.out.println(fast_klick);
                if(fast_klick){
                    System.out.println(id);

                    System.out.println(position);

                    System.out.println("открытие документа="+listArr[position]);


                    set_doc(listView,listArr[position]);
                }else {
                    System.out.println("было зажатие");
                }





            }

        });



        listView.setOnTouchListener(new AdapterView.OnTouchListener() {


            public boolean onTouch(View v, MotionEvent event) {


                System.out.println("нажал");





                fast_klick=false;

                fast_klick=listView.onTouchEvent(event);

                //System.out.println(long_klick);
                return listView.onTouchEvent(event);
            }

        });

    }



}