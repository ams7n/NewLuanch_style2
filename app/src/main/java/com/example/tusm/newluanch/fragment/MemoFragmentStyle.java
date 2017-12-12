package com.example.tusm.newluanch.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.tusm.newluanch.LinePathView;
import com.iboard.tusm.newluanch.R;
import com.example.tusm.newluanch.adapter.MyAdapter;
import com.example.tusm.newluanch.dp.DBManager;
import com.example.tusm.newluanch.entity.Note;
import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.internal.PLA_AdapterView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tusm on 17/7/4.
 */

public class MemoFragmentStyle extends Fragment implements View.OnClickListener  {
    private View v;
    private ImageView add,back;
    private DBManager dm;
    private List<Note> noteDataList = new ArrayList<>();
    private MyAdapter adapter;
    private MultiColumnListView listView;
    private TableLayout TypeLayout;
    private EditText note_title,note_content;
    private ImageButton button;
    public static String type;
    private LinearLayout idea,work,bill,car,game,read;
    private TextView tvidea,tvwork,tvbill,tvcar,tvgame,tvread,title;
    public static TextView note;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v == null) {
           v = inflater.inflate(R.layout.fragment_memor_style2,null);
            init();
            readNotes();
            }

            return v;
    }
    private void init() {
        dm = new DBManager(getContext());
        listView = (MultiColumnListView) v.findViewById(R.id.list);
        TypeLayout=(TableLayout) v.findViewById(R.id.typelayout);
        idea = (LinearLayout)v.findViewById(R.id.idea) ;
        work= (LinearLayout)v.findViewById(R.id.work) ;
        read= (LinearLayout)v.findViewById(R.id.read) ;
        bill= (LinearLayout)v.findViewById(R.id.bill) ;
        car = (LinearLayout)v.findViewById(R.id.cards) ;
        game= (LinearLayout)v.findViewById(R.id.game) ;
        note= (TextView)v.findViewById(R.id.textView_note);
        tvidea = (TextView)v.findViewById(R.id.textViewidea) ;
        tvwork= (TextView)v.findViewById(R.id.textViewwork) ;
        tvread= (TextView)v.findViewById(R.id.textViewread) ;
        tvbill= (TextView)v.findViewById(R.id.textViewbill) ;
        tvcar = (TextView)v.findViewById(R.id.textViewcards) ;
        tvgame= (TextView)v.findViewById(R.id.textViewgame) ;
        title=(TextView)v.findViewById(R.id.textView_title) ;
        add = (ImageView) v.findViewById(R.id.add);
        work.setOnClickListener(this);
        idea.setOnClickListener(this);
        bill.setOnClickListener(this);
        game.setOnClickListener(this);
        car.setOnClickListener(this);
        read.setOnClickListener(this);

        add.setOnClickListener(this);
        back = (ImageView) v.findViewById(R.id.back);
        back.setOnClickListener(this);

       // listView.setOnItemLongClickListener(new NoteLongClickListener());

    }

    //空数据更新
    public static void updateView() {
        note.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.add) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View dialog = inflater.inflate(R.layout.layout_editext, (ViewGroup) v.findViewById(R.id.dialog));
            note_title = (EditText) dialog.findViewById(R.id.note_title);
            note_content = (EditText) dialog.findViewById(R.id.note_content);
            button = (ImageButton) dialog.findViewById(R.id.save);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            //   builder.setTitle("添加备忘录");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String title = note_title.getText().toString();
                    String content = note_content.getText().toString();
                    String time = getTime();
                    dm.addToDB(title, type, content, time);
                    noteDataList.clear();
                    noteDataList =  dm.readTypeData(type);
                    adapter = new MyAdapter(getContext(), noteDataList,v);
                    listView.setAdapter(adapter);
                    note.setVisibility(View.GONE);

                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            builder.setView(dialog);
            AlertDialog ad = builder.create();
            ad.show();
            ad.getWindow().setLayout(846, 550);

        }
        if(view.getId()==R.id.idea){
            title.setText(getResources().getString(R.string.idea));
            type = "idea";
            TypeLayout.setVisibility(View.GONE);
            readType(type);
            listView.setVisibility(View.VISIBLE);

        }
        if(view.getId()==R.id.work){
            title.setText(getResources().getString(R.string.work));
            type = "work";
            TypeLayout.setVisibility(View.GONE);
            readType(type);
            listView.setVisibility(View.VISIBLE);

        }
        if(view.getId()==R.id.bill){
            title.setText(getResources().getString(R.string.bill));
            type = "bill";
            TypeLayout.setVisibility(View.GONE);
            readType(type);
            listView.setVisibility(View.VISIBLE);

        }
        if(view.getId()==R.id.game){
            title.setText(getResources().getString(R.string.games));
            type = "game";
            TypeLayout.setVisibility(View.GONE);
            readType(type);
            listView.setVisibility(View.VISIBLE);

        }
        if(view.getId()==R.id.cards){
            title.setText(getResources().getString(R.string.cards));
            type = "cards";
            TypeLayout.setVisibility(View.GONE);
            readType(type);
            listView.setVisibility(View.VISIBLE);

        }
        if(view.getId()==R.id.read){
            title.setText(getResources().getString(R.string.read));
            type = "read";
            TypeLayout.setVisibility(View.GONE);
            readType(type);
            listView.setVisibility(View.VISIBLE);

        }
        if(view.getId()==R.id.back){
            title.setText(getResources().getString(R.string.notebook));
            noteDataList.clear();
            listView.setVisibility(View.GONE);
            TypeLayout.setVisibility(View.VISIBLE);
            add.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
            note.setVisibility(View.GONE);
            readNotes();
        }

    }



    //得到时间
    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm E");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }


    private void readType(String type){
        back.setVisibility(View.VISIBLE);
        add.setVisibility(View.VISIBLE);
        noteDataList.clear();
        noteDataList =   dm.readTypeData(type);
        if(noteDataList.isEmpty()){
            note.setVisibility(View.VISIBLE);
        }
        adapter = new MyAdapter(getContext(), noteDataList,v);
        listView.setAdapter(adapter);
    }
    private void readNotes(){
        List<Note> worksize = dm.readTypeData("work");
        tvwork.setText(worksize.size()+getResources().getString(R.string.notes));
        List<Note> readsize = dm.readTypeData("read");
        tvread.setText(readsize.size()+getResources().getString(R.string.notes));
        List<Note> cardssize = dm.readTypeData("cards");
        tvcar.setText(cardssize.size()+getResources().getString(R.string.notes));
        List<Note> billsize = dm.readTypeData("bill");
        tvbill.setText(billsize.size()+getResources().getString(R.string.notes));
        List<Note> gamesize = dm.readTypeData("game");
        tvgame.setText(gamesize.size()+getResources().getString(R.string.notes));
        List<Note> ideasize = dm.readTypeData("idea");
        tvidea.setText(ideasize.size()+getResources().getString(R.string.notes));
    }
}
