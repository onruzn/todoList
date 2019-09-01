package com.oyisoftware.onuruzun.todolist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.oyisoftware.onuruzun.todolist.model.ToDo;
import com.oyisoftware.onuruzun.todolist.model.ToDoItem;
import com.oyisoftware.onuruzun.todolist.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TodoDB";

    // Table Names
    private static final String TABLE_USER = "users";
    private static final String TABLE_TODO = "todos";
    private static final String TABLE_TODO_ITEM = "todo_items";



    // USER Table - column nmaes
    private static final String USER_ID = "user_id";
    private static final String USER_NAME = "username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String USER_CREATED_AT = "user_created_at";

    // TODO Table - column names
    private static final String TODO_ID = "todo_id";
    private static final String TODO_NAME = "todo_name";
    private static final String TODO_userID = "todo_userid";
    private static final String TODO_CREATED_AT = "todo_created_at";

    // TODO_ITEM Table - column names
    private static final String ITEM_ID = "item_id";
    private static final String IT_TODO_ID = "it_todo_id";
    private static final String IT_USER_ID = "it_user_id";
    private static final String ITEM_NAME = "item_name";
    private static final String ITEM_DESC = "item_desc";
    private static final String ITEM_DEADLINE = "item_deadline";
    private static final String ITEM_STATUS = "item_status";
    private static final String ITEM_CREATED_AT = "item_created_at";

    public static final String INTENT_TODO_ID = "TodoId";
    public static final String INTENT_TODO_NAME = "TodoName";
    public static final String INTENT_USER_ID = "userId";
    public static final String INTENT_USER_NAME = "userName";

    // USER table create statement
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER
            + "(" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_NAME + " TEXT,"+ PASSWORD + " TEXT,"+ EMAIL + " TEXT,"
            + USER_CREATED_AT + " DATETIME" + ")";


    // Todo table create statement
    private static final String CREATE_TABLE_TODO = "CREATE TABLE "
            + TABLE_TODO + "(" + TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TODO_NAME
            + " TEXT,"+ TODO_userID + " INTEGER," + TODO_CREATED_AT + " DATETIME" + ")";


    // todo_tag table create statement
    private static final String CREATE_TABLE_TODO_TAG = "CREATE TABLE "
            + TABLE_TODO_ITEM + "(" + ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + IT_TODO_ID + " INTEGER," + IT_USER_ID + " INTEGER,"+ ITEM_NAME + " TEXT,"+ ITEM_DESC + " TEXT,"+ ITEM_DEADLINE + " DATETIME,"
            + ITEM_STATUS + " INTEGER,"
            + ITEM_CREATED_AT + " DATETIME" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_TODO);
        db.execSQL(CREATE_TABLE_TODO_TAG);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_ITEM);

        // create new tables
        onCreate(db);

    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void create_user(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_NAME, user.getUsername());
        values.put(PASSWORD, user.getPassword());
        values.put(EMAIL, user.getEmail());
        values.put(USER_CREATED_AT, getDateTime());

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    public boolean check_User(String email) {


        String[] columns = { USER_ID };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = EMAIL + " = ?";

        String[] selectionArgs = {email};

        Cursor cursor = db.query(TABLE_USER, columns,selection,selectionArgs,null,null,null);

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
    public boolean check_User(String email, String password) {


        String[] columns = { USER_ID };
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = EMAIL + " = ?" + " AND " + PASSWORD + " = ?";

        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(TABLE_USER, columns,selection,selectionArgs,null,null,null);

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

       return false;
    }

    public List<User> getUser(String email) {

        String[] columns = {
                USER_ID,
                USER_NAME,
                PASSWORD,
                EMAIL,
                USER_CREATED_AT
        };

        String sortOrder =
                USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        String selection = EMAIL + " = ?";

        String[] selectionArgs = {email};

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER,columns, selection,  selectionArgs, null,null, sortOrder);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(USER_ID))));
                user.setUsername(cursor.getString(cursor.getColumnIndex(USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(PASSWORD)));

                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();


        return userList;
    }


    public boolean addToDo(ToDo todo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TODO_NAME, todo.getName());
        cv.put(TODO_userID, todo.getUserid());
        long result = db.insert(TABLE_TODO, null, cv);
        return result != -1;
    }


    public void updateToDo(ToDo todo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TODO_NAME, todo.getName());
        db.update(TABLE_TODO, cv, TODO_ID + "=?", new String[]{String.valueOf(todo.getId())});
    }

    public void deleteToDo(int todoId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TODO_ITEM, IT_TODO_ID + "=?", new String[]{String.valueOf(todoId)});
        db.delete(TABLE_TODO, TODO_ID + "=?", new String[]{String.valueOf(todoId)});
    }

    public void updateToDoItemCompletedStatus(int todoId, Boolean isCompleted) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor queryResult = db.rawQuery("SELECT * FROM " + TABLE_TODO_ITEM + " WHERE " + IT_TODO_ID + "=" + todoId, null);
        if (queryResult.moveToFirst()) {
            do {
                ToDoItem item = new ToDoItem();
                item.setId(queryResult.getInt(queryResult.getColumnIndex(ITEM_ID)));
                item.setToDoId(queryResult.getInt(queryResult.getColumnIndex(IT_TODO_ID)));
                item.setItemName(queryResult.getString(queryResult.getColumnIndex(ITEM_NAME)));
                item.setCompleted(isCompleted);
                updateToDoItem(item);
            } while (queryResult.moveToNext());
        }
        queryResult.close();
    }

    public void updateToDoItem(ToDoItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEM_NAME, item.getItemName());
        cv.put(ITEM_DESC, item.getDescrp());
        cv.put(ITEM_DEADLINE, item.getDeadline());
        cv.put(ITEM_STATUS, item.isCompleted());
        cv.put(IT_TODO_ID, item.getToDoId());
        db.update(TABLE_TODO_ITEM, cv, ITEM_ID + "=?", new String[]{String.valueOf(item.getId())});
    }

    public ArrayList<ToDo> getToDos() {
        ArrayList<ToDo> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor queryResult = db.rawQuery("SELECT * from " + TABLE_TODO, null);
        if (queryResult.moveToFirst()) {
            do {
                ToDo todo = new ToDo();
                todo.setId(queryResult.getInt(queryResult.getColumnIndex(TODO_ID)));
                todo.setName(queryResult.getString(queryResult.getColumnIndex(TODO_NAME)));
                result.add(todo);
            } while (queryResult.moveToNext());
        }
        queryResult.close();
        return result;
    }

    public boolean addToDoItem(ToDoItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEM_NAME, item.getItemName());
        cv.put(ITEM_DESC, item.getDescrp());
        cv.put(ITEM_DEADLINE, item.getDeadline());
        cv.put(ITEM_STATUS, item.isCompleted());
        cv.put(IT_TODO_ID, item.getToDoId());

        long result = db.insert(TABLE_TODO_ITEM, null, cv);
        return result != -1;
    }

    public void deleteToDoItem(long itemId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TODO_ITEM, ITEM_ID + "=?", new String[]{String.valueOf(itemId)});

    }

    public ArrayList<ToDoItem> getToDoItems(int todoId) {
        ArrayList<ToDoItem> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor queryResult = db.rawQuery("SELECT * FROM " + TABLE_TODO_ITEM + " WHERE " + IT_TODO_ID + "=" + todoId, null);
        if (queryResult.moveToFirst()) {
            do {
                ToDoItem item = new ToDoItem();
                item.setId(queryResult.getInt(queryResult.getColumnIndex(ITEM_ID)));
                item.setToDoId(queryResult.getInt(queryResult.getColumnIndex(IT_TODO_ID)));
                item.setItemName(queryResult.getString(queryResult.getColumnIndex(ITEM_NAME)));
                item.setDescrp(queryResult.getString(queryResult.getColumnIndex(ITEM_DESC)));
                item.setDeadline(queryResult.getString(queryResult.getColumnIndex(ITEM_DEADLINE)));
                item.setCompleted(queryResult.getInt(queryResult.getColumnIndex(ITEM_STATUS)) == 1);
                result.add(item);
            } while (queryResult.moveToNext());
        }

        queryResult.close();
        return result;
    }
    public ArrayList<ToDo> getToDosByUser(int userId) {
        ArrayList<ToDo> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor queryResult = db.rawQuery("SELECT * from " + TABLE_TODO+ " WHERE " + TODO_userID + "=" + userId, null);
        if (queryResult.moveToFirst()) {
            do {
                ToDo todo = new ToDo();
                todo.setId(queryResult.getInt(queryResult.getColumnIndex(TODO_ID)));
                todo.setName(queryResult.getString(queryResult.getColumnIndex(TODO_NAME)));
                result.add(todo);
            } while (queryResult.moveToNext());
        }
        queryResult.close();
        return result;
    }


}
