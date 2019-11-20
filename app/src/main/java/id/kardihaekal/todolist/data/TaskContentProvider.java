package id.kardihaekal.todolist.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import androidx.annotation.NonNull;

// Verifikasi bahwa TaskContentProvider extends dari ContentProvider dan mengimplementasikan metode yang diperlukan
public class TaskContentProvider extends ContentProvider {

  // Tetapkan konstanta integer akhir untuk direktori task dan satu item.
  // Konvensi untuk menggunakan 100, 200, 300, dll untuk direktori,
  // dan ints terkait (101, 102, ..) untuk item dalam direktori itu.
  public static final int TASKS = 100;
  public static final int TASK_WITH_ID = 101;

  // CDklik variabel statis untuk pencocokan Uri yang telah kita buat
  private static final UriMatcher sUriMatcher = buildUriMatcher();

  // Tetapkan metode buildUriMatcher statis yang mengaitkan URI dengan kecocokan int mereka
  public static UriMatcher buildUriMatcher() {

    // Inisialisasi UriMatcher tanpa kecocokan dengan mengirimkan NO_MATCH ke konstruktor
    UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /*
    Semua jalur yang ditambahkan ke UriMatcher memiliki int yang sesuai.
        Untuk setiap jenis uri yang mungkin ingin Anda akses, tambahkan kecocokan yang sesuai dengan addURI.
        Dua panggilan di bawah ini menambahkan kecocokan untuk direktori tugas dan satu item dengan ID.
         */
    uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_TASKS, TASKS);
    uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_TASKS + "/#", TASK_WITH_ID);

    return uriMatcher;
  }

  // Variabel anggota untuk TaskDbHelper yang diinisialisasi dalam metode onCreate ()
  private TaskDbHelper mTaskDbHelper;

  /*onCreate() adalah tempat dimana kita harus menginisialisasi apa pun yang perlu kita siapkan
  sumber data dasar kita.
  Dalam hal ini, kita bekerja dengan basis data SQLite, jadi kita harus
  menginisialisasi DbHelper untuk mendapatkan akses ke sana. */
  @Override
  public boolean onCreate() {

    // Selesaikan onCreate() dan inisialisasi TaskDbhelper saat memulai
    // [Petunjuk] Nyatakan DbHelper sebagai variabel global
    Context context = getContext();
    mTaskDbHelper = new TaskDbHelper(context);
    return true;
  }


  // Terapkan insert untuk menangani permintaan memasukkan satu baris data baru
  @Override
  public Uri insert(@NonNull Uri uri, ContentValues values) {

    // Dapatkan akses ke database task (untuk menulis data baru)
    final SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();

    // Tulis kode pencocokan URI untuk mengidentifikasi kecocokan  direktori task
    int match = sUriMatcher.match(uri);
    Uri returnUri; // untuk mengembalikan nilai Uri

    switch (match) {
      case TASKS:
        // Masukkan nilai baru ke dalam database
        // Memasukkan nilai ke dalam tabel task
        long id = db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
        if ( id > 0 ) {
          returnUri = ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI, id);
        } else {
          throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        break;
      // Tetapkan nilai untuk ReturnUri dan tuliskan case default untuk URI yang tidak diketahui
      // case default melempar UnsupportedOperationException
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    // Beri tahu resolver jika uri telah diubah, dan return URI yang baru dimasukkan
    getContext().getContentResolver().notifyChange(uri, null);

    return returnUri;
  }


  // Terapkan permintaan untuk menangani permintaan data oleh URI
  @Override
  public Cursor query(@NonNull Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {

    // Dapatkan akses ke database yang mendasarinya (hanya baca untuk kueri)
    final SQLiteDatabase db = mTaskDbHelper.getReadableDatabase();

    // Tulis kode kecocokan URI dan tetapkan variabel untuk mengembalikan kursor
    int match = sUriMatcher.match(uri);
    Cursor retCursor;

    // Permintaan untuk direktori task dan tulis case default
    switch (match) {
      // kuery untuk task direktori
      case TASKS:
        retCursor =  db.query(TaskContract.TaskEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder);
        break;
      // Default exception
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    // Atur URI notifikasi pada kursor dan return kursor tersebut
    retCursor.setNotificationUri(getContext().getContentResolver(), uri);

    return retCursor;
  }


  // Terapkan hapus untuk menghapus satu baris data
  @Override
  public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

    // Dapatkan akses ke database dan tulis kode pencocokan URI untuk mengenali satu item
    final SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();

    int match = sUriMatcher.match(uri);
    // Melacak jumlah task yang dihapus
    int tasksDeleted; // memulai dengan 0

    // Tulis kode untuk menghapus satu baris data
    // [Petunjuk] Gunakan pilihan untuk menghapus item dengan ID barisnya
    switch (match) {
      // Tangani case single item, yang dikenali oleh ID yang termasuk dalam jalur URI
      case TASK_WITH_ID:
        // dapatkan task ID dari Uri
        String id = uri.getPathSegments().get(1);
        // gunakan selction untuk memfilter ID
        tasksDeleted = db.delete(TaskContract.TaskEntry.TABLE_NAME, "_id=?", new String[]{id});
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    if (tasksDeleted != 0) {
      // apabila sebuah task telah dihapus maka akan ada notify
      getContext().getContentResolver().notifyChange(uri, null);
    }

    // return nomor yang telah dihapus
    return tasksDeleted;
  }


  @Override
  public int update(@NonNull Uri uri, ContentValues values, String selection,
      String[] selectionArgs) {

    throw new UnsupportedOperationException("Not yet implemented");
  }


  @Override
  public String getType(@NonNull Uri uri) {

    throw new UnsupportedOperationException("Not yet implemented");
  }

}