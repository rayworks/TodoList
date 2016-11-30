package com.rayworks.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.rayworks.todolist.viewmodel.TodoViewModel;

import org.robobinding.ViewBinder;
import org.robobinding.binder.BinderFactory;

public class TodoListActivity extends AppCompatActivity {

    TodoViewModel viewModel;

    public void initializeContentView(int layoutId, Object presentationModel) {
        ViewBinder viewBinder = createViewBinder(true);
        View rootView = viewBinder.inflateAndBind(layoutId, presentationModel);
        setContentView(rootView);
    }

    protected ViewBinder createViewBinder(boolean withPreInitializingViews) {
        BinderFactory binderFactory = DroidApp.get().getReusableBinderFactory();
        return binderFactory.createViewBinder(this, withPreInitializingViews);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new TodoViewModel();
        initializeContentView(R.layout.activity_todo_list, viewModel);

        viewModel.fetchData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sync) {
            viewModel.fetchData();
        }
        return super.onOptionsItemSelected(item);
    }
}
