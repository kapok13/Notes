package com.vb.notes.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vb.notes.R;
import com.vb.notes.data.Note;
import com.vb.notes.data.NoteRepository;
import com.vb.notes.ui.activities.MainActivity;
import com.vb.notes.ui.presenters.NoteEditPresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteEditFragment extends Fragment implements INoteEditFragment{

    @BindView(R.id.title_edition)
    EditText mTitleEdit;

    @BindView(R.id.desc_edition)
    EditText mDesctiprionEdit;

    private NoteEditPresenter mNoteEditPresenter;

    private NavController mNavController;

    private Note currentNote;


    @Override
    public void onCreate(Bundle savedInstanceState){
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        mNoteEditPresenter = new NoteEditPresenter(this,
                new NoteRepository());
        mNavController = Navigation.findNavController(getActivity(), R.id.host_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_edit, container, false);
        ButterKnife.bind(this, view);

        // Прячем FAB
        FloatingActionButton mainFab = getActivity().findViewById(R.id.add_notes_fab);
        mainFab.setVisibility(View.GONE);

        // Получаем итем
        if (getArguments().getSerializable("note") != null){
            currentNote =  (Note) getArguments().getSerializable("note");
            mTitleEdit.setText(currentNote.getTitle());
            mDesctiprionEdit.setText(currentNote.getNote());
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_note_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_note){
            updateItem();
            mNavController.navigate(R.id.notesListFragment);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    // обновляем итем если он был изменен

    @Override
    public void updateItem() {
        if (!(mTitleEdit.getText().toString().equals(currentNote.getTitle()))
                || !(mDesctiprionEdit.getText().toString().equals(currentNote.getNote()))){

            Calendar calendar = Calendar.getInstance();
            String time = new SimpleDateFormat("HH:mm").format(calendar.getTime());
            String date = new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime());
            currentNote.setDate(date);
            currentNote.setTime(time);
            currentNote.setTitle(mTitleEdit.getText().toString());
            currentNote.setNote(mDesctiprionEdit.getText().toString());
            mNoteEditPresenter.updNote(currentNote);

        }

    }

    /*
        Изменяем title на action bar
     */
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setActionBarTitle(getResources().getString(R.string.new_note));
    }
}
