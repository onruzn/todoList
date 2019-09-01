package com.oyisoftware.onuruzun.todolist.activities;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oyisoftware.onuruzun.todolist.R;
import com.oyisoftware.onuruzun.todolist.database.DatabaseHelper;
import com.oyisoftware.onuruzun.todolist.model.ToDoItem;

import java.util.ArrayList;

import static com.oyisoftware.onuruzun.todolist.database.DatabaseHelper.INTENT_TODO_ID;
import static com.oyisoftware.onuruzun.todolist.database.DatabaseHelper.INTENT_TODO_NAME;

public class ItemActivity extends AppCompatActivity {

    Toolbar item_toolbar;
    RecyclerView rv_item;
    FloatingActionButton fab_item;

    int todoId;
    ItemActivity activity;
    DatabaseHelper dbHandler;
    ItemAdapter adapter;
    ArrayList<ToDoItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        item_toolbar = findViewById(R.id.item_toolbar);
        rv_item = findViewById(R.id.rv_item);
        fab_item = findViewById(R.id.fab_item);
        setSupportActionBar(item_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(INTENT_TODO_NAME));
        todoId = getIntent().getIntExtra(INTENT_TODO_ID, -1);
        activity = this;
        dbHandler = new DatabaseHelper(activity);
        rv_item.setLayoutManager(new LinearLayoutManager(activity));

        rv_item.setLayoutManager(new LinearLayoutManager(this));
        rv_item.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        Drawable mDivider = ContextCompat.getDrawable(this, R.drawable.divider);
        DividerItemDecoration hItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL);
        hItemDecoration.setDrawable(mDivider);
        DividerItemDecoration vItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        vItemDecoration.setDrawable(mDivider);


        fab_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setTitle("Add ToDo Item");
                View view = getLayoutInflater().inflate(R.layout.dialog_todo_item, null);
                final EditText toDoName = view.findViewById(R.id.dg_todoName);
                final EditText toDoDesc = view.findViewById(R.id.dg_todoDesc);
                final String deadline="16/09/1991";
                dialog.setView(view);
                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (toDoName.getText().toString().length() > 0) {
                            ToDoItem item = new ToDoItem();
                            item.setItemName(toDoName.getText().toString());
                            item.setToDoId(todoId);
                            item.setCompleted(false);
                            item.setDeadline(deadline);
                            item.setDescrp(toDoDesc.getText().toString());
                            dbHandler.addToDoItem(item);
                            refreshList();
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();



            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        refreshList();
        super.onResume();
    }

    void refreshList() {
        list = dbHandler.getToDoItems(todoId);
        adapter = new ItemAdapter(activity);
        rv_item.setAdapter(adapter);
    }

    void updateItem(final ToDoItem item) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("Update ToDo Item");
        View view = getLayoutInflater().inflate(R.layout.dialog_todo_item, null);
        final EditText toDoName = view.findViewById(R.id.dg_todoName);
        toDoName.setText(item.getItemName());
        dialog.setView(view);
        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (toDoName.getText().toString().length() > 0) {
                    item.setItemName(toDoName.getText().toString());
                    item.setToDoId(todoId);
                    item.setCompleted(false);
                    dbHandler.updateToDoItem(item);
                    refreshList();
                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
        ItemActivity activity;

        ItemAdapter(ItemActivity activity) {
            this.activity = activity;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.icon_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
            holder.itemName.setText(list.get(i).getItemName());
            holder.itemName.setChecked(list.get(i).isCompleted());
            holder.deadline.setText("Deadline:"+list.get(i).getDeadline());
            holder.desc.setText("Description:"+list.get(i).getDescrp());
            holder.itemName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.get(i).setCompleted(!list.get(i).isCompleted());
                    activity.dbHandler.updateToDoItem(list.get(i));
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                    dialog.setTitle("Are you sure");
                    dialog.setMessage("Do you want to delete this item ?");
                    dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int pos) {
                            activity.dbHandler.deleteToDoItem(list.get(i).getId());
                            activity.refreshList();
                        }
                    });
                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialog.show();
                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.updateItem(list.get(i));
                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox itemName;
            ImageView edit;
            ImageView delete;
            TextView deadline;
            TextView desc;

            ViewHolder(View v) {
                super(v);
                itemName = v.findViewById(R.id.cb_item);
                edit = v.findViewById(R.id.iv_edit);
                delete = v.findViewById(R.id.iv_delete);
                deadline=v.findViewById(R.id.deadline);
                desc=v.findViewById(R.id.desc);
            }
        }
    }

}
