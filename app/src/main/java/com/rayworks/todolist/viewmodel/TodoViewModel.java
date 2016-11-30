package com.rayworks.todolist.viewmodel;

import com.rayworks.todolist.model.Todo;
import com.rayworks.todolist.network.TodoResp;
import com.rayworks.todolist.network.Webservices;

import org.robobinding.annotation.ItemPresentationModel;
import org.robobinding.annotation.PresentationModel;
import org.robobinding.presentationmodel.HasPresentationModelChangeSupport;
import org.robobinding.presentationmodel.PresentationModelChangeSupport;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;


/**
 * Created by Sean on 11/30/16.
 */

@PresentationModel
public class TodoViewModel implements HasPresentationModelChangeSupport {

    private final PresentationModelChangeSupport changeSupport;
    private final Webservices.TodoService todoService;

    public TodoViewModel() {
        changeSupport = new PresentationModelChangeSupport(this);
        todoService = Webservices.getInstance().getTodoService();
    }

    @Override
    public PresentationModelChangeSupport getPresentationModelChangeSupport() {
        return changeSupport;
    }

    private List<String> datasrc = Collections.EMPTY_LIST;

    public void fetchData() {

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
                        //updateView(resp);

                        datasrc = resp;
                        refresh();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        String message = throwable.getMessage();
                        Timber.i("On error : %s ", message);

                        datasrc = Collections.EMPTY_LIST;
                        refresh();

                        //progressBar.setVisibility(View.GONE);
                        //Toast.makeText(TodoListActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        Timber.i("On complete ");
                    }
                })
        ;
    }

    private void refresh() {
        Timber.i(">>> fire Property Change event");
        changeSupport.firePropertyChange("items");
    }

    @ItemPresentationModel(ItemViewModel.class)
    public List<String> getItems() {
        Timber.i(">>> getItems invoked ");

        return datasrc; //Arrays.asList("test1", "t2", "t3");
    }


}
