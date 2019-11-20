package id.kardihaekal.todolist.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import id.kardihaekal.todolist.data.TaskContract.TaskEntry;

public class TaskDbHelper extends SQLiteOpenHelper {

  // nama databasenya
  private static final String DATABASE_NAME = "tasksDb.db";

  // jika kita mengubah skema database maka kita juga harus mengubah versinya
  private static final int VERSION = 1;


  // Constructor
  TaskDbHelper(Context context) {
    super(context, DATABASE_NAME, null, VERSION);
  }



  @Override
  public void onCreate(SQLiteDatabase db) {

    // disini kita akan membuat tabel task, gunkan sesuai aturan format SQL
    final String CREATE_TABLE = "CREATE TABLE "  + TaskEntry.TABLE_NAME + " (" +
        TaskEntry._ID                + " INTEGER PRIMARY KEY, " +
        TaskEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
        TaskEntry.COLUMN_PRIORITY    + " INTEGER NOT NULL);";

    db.execSQL(CREATE_TABLE);
  }


  //fungsi ini untuk menghapus tabel lama dan menggantinya dengan tabel baru dengan memanggil onCretae
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME);
    onCreate(db);
  }
}
