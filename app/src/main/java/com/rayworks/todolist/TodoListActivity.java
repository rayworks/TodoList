package com.rayworks.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rayworks.todolist.model.Todo;
import com.rayworks.todolist.network.TodoResp;
import com.rayworks.todolist.network.Webservices;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class TodoListActivity extends AppCompatActivity {

    @BindView(R.id.empty_tips)
    TextView tips;

    @BindView(R.id.list)
    ListView listView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private Webservices.TodoService todoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        ButterKnife.bind(this);

        listView.setVisibility(View.GONE);

        todoService = Webservices.getInstance().getTodoService();
        fetchTodoList();
    }

    private void fetchTodoList() {
        todoService.getList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<TodoResp, Observable<List<String>>>() {
                    @Override
                    public Observable<List<String>> call(TodoResp resp) {
                        List<String> texts = new LinkedList<String>();
                        for (Todo todo : resp.getItems()) {
                            texts.add(todo.getTitle());
                        }

                        return Observable.just(texts);
                    }
                })
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> resp) {
                        Timber.i("On next ");
                        //updateView(resp.getItems());
                        updateView(resp);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        String message = throwable.getMessage();
                        Timber.i("On error : %s ", message);
                        Toast.makeText(TodoListActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        Timber.i("On complete ");
                    }
                })
        ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_sync){
            progressBar.bringToFront();
            progressBar.setVisibility(View.VISIBLE);

            fetchTodoList();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateView(List<String> texts) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TodoListActivity.this,
                android.R.layout.simple_list_item_1, texts);
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        progressBar.setVisibility(View.GONE);
        tips.setVisibility(View.GONE);
    }
}
