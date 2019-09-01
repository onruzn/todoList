package com.oyisoftware.onuruzun.todolist.activities;


import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.oyisoftware.onuruzun.todolist.R;
import com.oyisoftware.onuruzun.todolist.database.DatabaseHelper;
import com.oyisoftware.onuruzun.todolist.model.ToDo;
import com.oyisoftware.onuruzun.todolist.model.ToDoItem;
import com.oyisoftware.onuruzun.todolist.helpers.SendMail;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


import static com.oyisoftware.onuruzun.todolist.database.DatabaseHelper.INTENT_TODO_ID;
import static com.oyisoftware.onuruzun.todolist.database.DatabaseHelper.INTENT_TODO_NAME;
import static com.oyisoftware.onuruzun.todolist.database.DatabaseHelper.INTENT_USER_ID;

public class ToDoActivity extends AppCompatActivity {

    DatabaseHelper dbHandler;
    ToDoActivity activity;
    Toolbar dashboard_toolbar;
    RecyclerView rv_dashboard;
    FloatingActionButton fab_dashboard;
    int intentUserId;
    ArrayList<ToDoItem> items;
    private File pdfFile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        dashboard_toolbar = findViewById(R.id.dashboard_toolbar);
        rv_dashboard = findViewById(R.id.rv_dashboard);
        fab_dashboard = findViewById(R.id.fab_dashboard);
        setSupportActionBar(dashboard_toolbar);
        setTitle("ToDos");
        activity = this;
        dbHandler = new DatabaseHelper(activity);
        intentUserId = getIntent().getIntExtra(INTENT_USER_ID, -1);
        rv_dashboard.setLayoutManager(new LinearLayoutManager(activity));

        rv_dashboard.setLayoutManager(new LinearLayoutManager(this));
        rv_dashboard.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        Drawable mDivider = ContextCompat.getDrawable(this, R.drawable.divider);
        DividerItemDecoration hItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL);
        hItemDecoration.setDrawable(mDivider);
        DividerItemDecoration vItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        vItemDecoration.setDrawable(mDivider);



        fab_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setTitle("Add ToDo");
                View view = getLayoutInflater().inflate(R.layout.dialog_todo, null);
                final EditText toDoName = view.findViewById(R.id.ev_todo);
                dialog.setView(view);

                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (toDoName.getText().toString().length() > 0) {
                            ToDo toDo = new ToDo();
                            toDo.setName(toDoName.getText().toString());
                            toDo.setUserid(intentUserId);
                            dbHandler.addToDo(toDo);
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
    protected void onResume() {
        refreshList();
        super.onResume();
    }

    public void updateToDo(final ToDo toDo) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("Update ToDo");
        View view = getLayoutInflater().inflate(R.layout.dialog_todo, null);
        final EditText toDoName = view.findViewById(R.id.ev_todo);
        toDoName.setText(toDo.getName());
        dialog.setView(view);
        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (toDoName.getText().toString().length() > 0) {
                    toDo.setName(toDoName.getText().toString());
                    dbHandler.updateToDo(toDo);
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

    public void refreshList() {
        rv_dashboard.setAdapter(new TodoAdapter(activity, dbHandler.getToDosByUser(intentUserId)));
    }

    private void createPdf() throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();

        }

        pdfFile = new File(docsFolder.getAbsolutePath(),"HelloWorld.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();
        //document.add(new Paragraph(mContentEditText.getText().toString()));

        document.close();
        previewPdf();

    }

    private void previewPdf() {

        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");

            startActivity(intent);
        }else{
            Toast.makeText(this,"Download a PDF Viewer to see the generated PDF",Toast.LENGTH_SHORT).show();
        }
    }


    class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
        ArrayList<ToDo> list;
        ToDoActivity activity;

        TodoAdapter(ToDoActivity activity, ArrayList<ToDo> list) {
            this.list = list;
            this.activity = activity;
            Log.d("TodoAdapter" , "TodoAdapter");
            Log.d("TodoAdapter" , "list : " + list.size());
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.icon_todo, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
            holder.toDoName.setText(list.get(i).getName());

            holder.toDoName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, ItemActivity.class);
                    intent.putExtra(INTENT_TODO_ID, list.get(i).getId());
                    intent.putExtra(INTENT_TODO_NAME, list.get(i).getName());
                    activity.startActivity(intent);
                }
            });

            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(activity, holder.menu);
                    popup.inflate(R.menu.todo_menu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.menu_edit: {
                                    activity.updateToDo(list.get(i));
                                    break;
                                }
                                case R.id.menu_delete: {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                                    dialog.setTitle("Are you sure");
                                    dialog.setMessage("Do you want to delete this task ?");
                                    dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int pos) {
                                            activity.dbHandler.deleteToDo(list.get(i).getId());
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
                                case R.id.menu_mark_as_completed: {
                                    activity.dbHandler.updateToDoItemCompletedStatus(list.get(i).getId(), true);
                                    break;
                                }
                                case R.id.menu_reset: {
                                    activity.dbHandler.updateToDoItemCompletedStatus(list.get(i).getId(), false);
                                    break;
                                }
                                case R.id.export: {

                                    break;
                                }
                                case R.id.sentemail: {


                                    AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                                    dialog.setTitle("Sent an email");
                                    View view = getLayoutInflater().inflate(R.layout.dialog_mail, null);
                                    final EditText mailAdress = view.findViewById(R.id.dg_mailAdress);
                                    dialog.setView(view);

                                    dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int j) {
                                            if (mailAdress.getText().toString().length() > 0) {

                                                items =dbHandler.getToDoItems(list.get(i).getId());

                                                String email = mailAdress.getText().toString().trim();
                                                String subject = "";
                                                String message = items.get(0).getItemName() + " " + items.get(0).getDescrp()+ " " +items.get(0).getDeadline();
                                                //String message = "Todo List";

                                                //Creating SendMail object
                                                SendMail sm = new SendMail(activity, email, subject, message);

                                                //Executing sendmail to send email
                                                sm.execute();

                                            }
                                        }
                                    });
                                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int j) {

                                        }
                                    });
                                    dialog.show();


                                    break;
                                }
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView toDoName;
            ImageView menu;

            ViewHolder(View v) {
                super(v);
                toDoName = v.findViewById(R.id.tv_todo_name);
                menu = v.findViewById(R.id.iv_menu);
            }
        }
    }
}
