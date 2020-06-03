package com.vb.notes.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.vb.notes.R;
import com.vb.notes.data.Note;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.NoteHolder>{


    private Context mContext;
    private List<Note> mNotes;
    private NavController mNavController;

    public NoteRecyclerAdapter(Context mContext, NavController mNavController) {
        this.mContext = mContext;
        this.mNavController = mNavController;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_notes, parent, false);
        return new NoteHolder(v);
    }

    /*
        бинд итемов со списка значений бд
        проверка текущей даты для изменения отображения вчерашней даты
        установка слушателя на итемы со сменой фрагмента и передачи Bundle с итемом
     */
    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
            if (mNotes != null){

                Calendar calendar = Calendar.getInstance();
                String date = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime());
                Note currentNote = mNotes.get(position);
                if (currentNote.getTitle() != null){
                    holder.titleText.setText(currentNote.getTitle());
                } else {
                    holder.titleText.setText(R.string.title_hint);
                }
                if (currentNote.getNote() != null){
                    holder.noteText.setText(currentNote.getNote());
                } else {
                    holder.noteText.setText(R.string.note_hint);
                }
                if (currentNote.getDate().equals(date)){
                    holder.dateText.setText(currentNote.getTime());
                } else {
                    holder.dateText.setText(currentNote.getDate());
                }
                holder.currentView.setOnClickListener((view) -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("note", currentNote);
                    mNavController.navigate(R.id.noteEditFragment, bundle);
                });
            }
    }

    public void setList(List<Note> notes){
        this.mNotes = notes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mNotes != null){
            return mNotes.size();
        }
        else {
            return 0;
        }
    }

    class NoteHolder extends RecyclerView.ViewHolder{

        View currentView;
        TextView titleText;
        TextView noteText;
        TextView dateText;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            currentView = itemView;
            titleText = itemView.findViewById(R.id.title_edit);
            noteText = itemView.findViewById(R.id.note_edit);
            dateText = itemView.findViewById(R.id.edit_date);
        }
    }
}
