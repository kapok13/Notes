package com.vb.notes.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.vb.notes.R;
import com.vb.notes.data.Note;
import com.vb.notes.data.NoteRepository;
import com.vb.notes.ui.activities.MainActivity;
import com.vb.notes.ui.adapters.NoteRecyclerAdapter;
import com.vb.notes.ui.presenters.NoteListPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class NotesListFragment extends Fragment implements INoteListFragment{

    @BindView(R.id.notes_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.progress_circle)
    ProgressBar mCircleBar;

    @BindView(R.id.empty_list_text)
    TextView mEmptyListText;

    @BindView(R.id.horizontal_progress)
    ProgressBar mHorizontalBar;

    private NoteListPresenter mNoteListPresenter;

    private NoteRecyclerAdapter adapter;

    private NavController mNavController;

    private List<Note> mList;

    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_note_list, container, false);
        ButterKnife.bind(this, view);

        // Возвращаем видимость FAB
        FloatingActionButton mainFab = getActivity().findViewById(R.id.add_notes_fab);
        if (!mainFab.isShown()){
            mainFab.setVisibility(View.VISIBLE);
        }
        mNavController = Navigation.findNavController(getActivity(), R.id.host_fragment);
        mNoteListPresenter = new NoteListPresenter(this,
                new NoteRepository());
        adapter = new NoteRecyclerAdapter(getContext(), mNavController);
        mNoteListPresenter.setList();
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setSwipe(mRecyclerView);
        return view;
    }

    /*
        если в списке нету элементов запускаем progress bar
        если у юзера есть интернет то после 5 секунд выводим сообщение о том что список пуст
        если интернета нету или он пропадает то выводим сообщение о проблемах с коннектом

        если в списке есть элементы запускаем горизонтальный progress bar
        загружаем progress bar 5 секунд после чего убираем
        в случае отсутствия интернета показываем snackbar

        получаем список элементов бд и перетаем в местный список а также в список адаптера
     */

    @Override
    public void setNotesList(Flowable<List<Note>> notesList) {
        notesList.observeOn(AndroidSchedulers.mainThread())
                .subscribe((List<Note> notes) -> {
                    if (notes.isEmpty()){
                        mCircleBar.setVisibility(View.VISIBLE);
                        if(((MainActivity)getActivity()).isNetworkAvailable(getContext()) == true){
                            initData();
                        } else {
                            mCircleBar.setVisibility(View.GONE);
                            mEmptyListText.setVisibility(View.VISIBLE);
                            mEmptyListText.setText(getResources().getString(R.string.connect_problem));
                        }

                    } else {
                        mEmptyListText.setVisibility(View.GONE);
                        mHorizontalBar.setVisibility(View.VISIBLE);
                        if(((MainActivity)getActivity()).isNetworkAvailable(getContext()) == true){

                            Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        for (int i = 0; i <= 100; i++){
                                            Thread.sleep(60);
                                            mHorizontalBar.setProgress(i);
                                            if(mHorizontalBar.getProgress() == 100){
                                                getActivity().runOnUiThread(() -> {
                                                    mHorizontalBar.setVisibility(View.GONE);
                                                });
                                            }
                                        }
                                    } catch (InterruptedException e){
                                        e.printStackTrace();
                                    }
                                }
                            });
                            t.start();
                        } else {
                            mHorizontalBar.setVisibility(View.GONE);
                            Snackbar.make(view, getResources().getString(R.string.connect_problem),
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    mList = notes;
                    adapter.setList(notes);
                });
    }



    @Override
    public void deleteBySwipe(Note note) {
        mNoteListPresenter.deleteItem(note);
    }

    /*
        устанавливаем свайп для элементов списка
     */
    private void setSwipe(RecyclerView recyclerView){
        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Note currentNote = mList.get(viewHolder.getAdapterPosition());
                deleteBySwipe(currentNote);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    /*
        Имитируем обращение к серверу
     */
    private void initData(){
        new Handler().postDelayed(() -> {

            if (!getDataFromServer()){
                mCircleBar.setVisibility(View.GONE);
                mEmptyListText.setText(getResources().getString(R.string.empty_list));
                mEmptyListText.setVisibility(View.VISIBLE);
            } else {
                //обрабатываем полученный с сервера список и вставляем в бд
            }

        }, 5000);
    }

    private boolean getDataFromServer(){
        return false;
    }

    /*
        возвращаем стандартный title
     */
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).setActionBarTitle(getResources().getString(R.string.app_name));
    }


}
