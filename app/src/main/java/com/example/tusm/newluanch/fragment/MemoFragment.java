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
import android.widget.ListView;
import android.widget.TextView;

import com.example.tusm.newluanch.LinePathView;
import com.iboard.tusm.newluanch.R;
import com.example.tusm.newluanch.adapter.MyAdapter;
import com.example.tusm.newluanch.dp.DBManager;
import com.example.tusm.newluanch.entity.Note;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tusm on 17/7/4.
 */

public class MemoFragment extends Fragment implements View.OnClickListener  {
    private View v;
    private ImageButton actionButton;
    private DBManager dm;
    private List<Note> noteDataList = new ArrayList<>();
    private MyAdapter adapter;
    private ListView listView;
    private TextView emptyListTextView;
    private  String noteId;
    private EditText note_title,note_content;
    private ImageButton button;
    private LinePathView pathView;
    private File f;

    private ScaleAnimation sato0 = new ScaleAnimation(1,0,1,1,
            Animation.RELATIVE_TO_PARENT,0.5f,Animation.RELATIVE_TO_PARENT,0.5f);
    private ScaleAnimation sato1 = new ScaleAnimation(0,1,1,1,
            Animation.RELATIVE_TO_PARENT,0.5f,Animation.RELATIVE_TO_PARENT,0.5f);
    private String path = Environment.getExternalStorageDirectory() + File.separator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v == null) {
           v = inflater.inflate(R.layout.fragment_memor,null);
            init();
            }

            return v;
    }
    private void init() {
        dm = new DBManager(getContext());
        dm.readFromDB(noteDataList);
        listView = (ListView) v.findViewById(R.id.list);
        actionButton = (ImageButton) v.findViewById(R.id.add);
        emptyListTextView = (TextView) v.findViewById(R.id.empty);
        actionButton.setOnClickListener(this);
        adapter = new MyAdapter(getContext(), noteDataList,v);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new NoteClickListener());
        listView.setOnItemLongClickListener(new NoteLongClickListener());
        updateView();
    }

    //空数据更新
    private void updateView() {
        if (noteDataList.isEmpty()) {
            listView.setVisibility(View.GONE);
            emptyListTextView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            emptyListTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
    //   MemoDialog memoDialog =new MemoDialog(getContext());
   //     memoDialog.show();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View   dialog = inflater.inflate(R.layout.layout_editext,(ViewGroup) v.findViewById(R.id.dialog));
         note_title = (EditText) dialog.findViewById(R.id.note_title);
         note_content = (EditText) dialog.findViewById(R.id.note_content);
        button=(ImageButton)dialog.findViewById(R.id.save);
        pathView=(LinePathView) dialog.findViewById(R.id.pathview);

        showImage1();
        sato0.setDuration(500);
        sato1.setDuration(500);
        sato0.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (note_content.getVisibility() == View.VISIBLE){
                    note_content.setAnimation(null);
                    showImage2();
                    pathView.startAnimation(sato1);
                }else{
                    pathView.setAnimation(null);
                    showImage1();
                    note_content.startAnimation(sato1);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
     //   builder.setTitle("添加备忘录");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = note_title.getText().toString();
                String content = note_content.getText().toString();
                String time = getTime();
                dm.addToDB( title,"game", content, time);
                noteDataList.clear();
                dm.readFromDB(noteDataList);
                adapter = new MyAdapter(getContext(), noteDataList,v);
                listView.setAdapter(adapter);
                updateView();
                if(pathView.getTouched()) {
                    try {
                        pathView.save(path + title + ".png");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (note_content.getVisibility() == View.VISIBLE){
                    note_content.startAnimation(sato0);
                }else{
                    pathView.startAnimation(sato0);
                }
            }
        });
        builder.setView(dialog);
        AlertDialog ad = builder.create();
             ad.show();
        ad.getWindow().setLayout(846,550);


    }
    //listView单击事件
    private class NoteClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(final AdapterView<?> adapterView, View view, int i, long l) {

            MyAdapter.ViewHolder viewHolder = (MyAdapter.ViewHolder) view.getTag();
              noteId = viewHolder.tvId.getText().toString().trim();
            Log.i("noteId",noteId);

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View   dialog = inflater.inflate(R.layout.layout_editext,(ViewGroup) v.findViewById(R.id.dialog));
              note_title = (EditText) dialog.findViewById(R.id.note_title);
               note_content = (EditText) dialog.findViewById(R.id.note_content);
            button=(ImageButton)dialog.findViewById(R.id.save);
            pathView=(LinePathView)dialog.findViewById(R.id.pathview);
            showImage1();
            sato0.setDuration(500);
            sato1.setDuration(500);
            sato0.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (note_content.getVisibility() == View.VISIBLE){
                        note_content.setAnimation(null);
                        showImage2();
                        pathView.startAnimation(sato1);
                    }else{
                        pathView.setAnimation(null);
                        showImage1();
                        note_content.startAnimation(sato1);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            final Note note = dm.readData(Integer.parseInt(noteId));
            note_title.setText(note.getTitle());
            note_content.setText(note.getContent());
             f=new File(path+note.getTitle()+".png");
          final   boolean isexists = f.exists();
            if(isexists){
                final Bitmap imageBitmap = BitmapFactory.decodeFile(path+note.getTitle()+".png");
                pathView.drawback(imageBitmap,imageBitmap.getWidth(),imageBitmap.getHeight());

            }

            //控制光标
            Spannable spannable = note_title.getText();
            Selection.setSelection(spannable, note_title.getText().length());
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            //   builder.setTitle("添加备忘录");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String title = note_title.getText().toString();
                    String content = note_content.getText().toString();
                    String time = getTime();
                    dm.updateNote( Integer.parseInt(noteId),"game",title, content, time);
                    noteDataList.clear();
                    dm.readFromDB(noteDataList);
                    adapter.notifyDataSetChanged();
                    updateView();
                    if(pathView.getTouched()||isexists) {
                        try {
                            if(title.equals(note.getTitle())) {
                                pathView.save(path + title + ".png");
                            }else{
                                boolean aa =f.delete();
                                pathView.save(path + title + ".png");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            builder.setView(dialog);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (note_content.getVisibility() == View.VISIBLE){
                        note_content.startAnimation(sato0);
                    }else{
                        pathView.startAnimation(sato0);

                    }
                }
            });
            AlertDialog ad = builder.create();
            ad.show();
            ad.getWindow().setLayout(846,550);



        }

    }
    private void showImage1(){

        note_content.setVisibility(View.VISIBLE);
        pathView.setVisibility(View.GONE);
    }
    private void showImage2(){

        note_content.setVisibility(View.GONE);
        pathView.setVisibility(View.VISIBLE);
//        Drawable  drawable = getResources().getDrawable(R.drawable.back_nomarl);
//        BitmapDrawable bd = (BitmapDrawable)drawable;
//        pathView.drawback(bd.getBitmap());
    }
    //得到时间
    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm E");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }
    private class NoteLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
            final Note note = ((MyAdapter) adapterView.getAdapter()).getItem(i);
            if (note == null) {
                return true;
            }
            final int id = note.getId();
            AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
            builder.setTitle("提示");
            builder.setMessage("是否删除该记录?");
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int a) {

                    DBManager.getInstance(getContext()).deleteNote(id);
                    adapter.removeItem(i);
                    updateView();
                    f=new File(path+note.getTitle()+".png");
                    if(f.exists()){
                        f.delete();
                    }
                }
            });
            builder.setPositiveButton("取消",null);
            AlertDialog ad = builder.create();
            ad.show();
            ad.getWindow().setLayout(846,200);

            return true;
        }
    }

}
