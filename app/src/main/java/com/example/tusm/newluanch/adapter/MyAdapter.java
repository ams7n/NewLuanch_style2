package com.example.tusm.newluanch.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.iboard.tusm.newluanch.R;
import com.example.tusm.newluanch.dp.DBManager;
import com.example.tusm.newluanch.entity.Note;
import com.example.tusm.newluanch.fragment.MemoFragmentStyle;

import java.util.List;
import java.util.Random;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by XP on 2015/2/15.
 */
public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<Note> notes;
    private View vs;
    private String[] hex;
    private DBManager dm;
    private EditText note_title,note_content;
    private String last = "";
    private  int x;
    public MyAdapter(Context context, List<Note> notes,View v) {
        this.context = context;
        this.notes = notes;
        hex = new String[]{"#5ACD3B","#3A93F7","#F63271","#F7BB40","#896DF7"};
        dm = new DBManager(context);
        vs =v;
    }

    public void removeAllItem() {
        notes.clear();
        notifyDataSetChanged();
    }

    //从List移除对象
    public void removeItem(int position) {
        notes.remove(position);
        notifyDataSetChanged();
        if(position==0){
            MemoFragmentStyle.updateView();
        }
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Note getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;

         x=0+(int)(Math.random()*4);
        while(hex[x].equals(last)){
         x=0+(int)(Math.random()*5);
        }
        last = hex[x];
        if (convertView == null) {
            convertView
                    = LayoutInflater.from(context).inflate(R.layout.notes_row, null);
           viewHolder = new ViewHolder();
            viewHolder.tvId
                    = (TextView) convertView.findViewById(R.id.note_id);
            viewHolder.tvTitle
                    = (TextView) convertView.findViewById(R.id.note_title);
            viewHolder.tvContent
                    = (TextView) convertView.findViewById(R.id.note_content);
            viewHolder.tvTime
                    = (TextView) convertView.findViewById(R.id.note_time);
            convertView.setTag(viewHolder);

            viewHolder.relativeLayout=(RelativeLayout)convertView.findViewById(R.id.colorlay);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvId.setText(notes.get(position).getId() + "");
        viewHolder.tvTitle.setText(notes.get(position).getTitle());
        viewHolder.tvContent.setText(notes.get(position).getContent());
        viewHolder.tvTime.setText(notes.get(position).getTime());

        viewHolder.relativeLayout.setBackgroundColor(Color.parseColor(hex[x]));
        viewHolder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder =new AlertDialog.Builder(context);
                builder.setTitle("提示");
                builder.setMessage("是否删除该记录?");
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int a) {

                        DBManager.getInstance(context).deleteNote(notes.get(position).getId());
                       removeItem(position);

                    }
                });
                builder.setPositiveButton("取消",null);
                AlertDialog ad = builder.create();
                ad.show();
                ad.getWindow().setLayout(846,200);
                return true;
            }
        });
        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater=(LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View   dialog = inflater.inflate(R.layout.layout_editext,(ViewGroup)  vs.findViewById(R.id.dialog));
                note_title = (EditText) dialog.findViewById(R.id.note_title);
                note_content = (EditText) dialog.findViewById(R.id.note_content);

                final Note note = dm.readData(notes.get(position).getId());
                note_title.setText(note.getTitle());
                note_content.setText(note.getContent());


                //控制光标
                Spannable spannable = note_title.getText();
                Selection.setSelection(spannable, note_title.getText().length());
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //   builder.setTitle("添加备忘录");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = note_title.getText().toString();
                        String content = note_content.getText().toString();
                        String time = notes.get(position).getTime();
                        dm.updateNote( notes.get(position).getId(), MemoFragmentStyle.type,title, content, time);
                        notes = dm.readTypeData(MemoFragmentStyle.type);
                          notifyDataSetChanged();


                    }
                });
                builder.setView(dialog);

                AlertDialog ad = builder.create();
                ad.show();
                ad.getWindow().setLayout(846,550);

            }
        });
        return convertView;
    }

    //ViewHolder内部类
    public static class ViewHolder {
        public TextView tvId;
        public TextView tvTitle;
        public TextView tvContent;
        public TextView tvTime;
        private RelativeLayout relativeLayout;
    }
}