package id.kardihaekal.todolist.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class TaskContract {


  // untuk mengetahui Penyedia Konten mana yang dapat diakses
  public static final String AUTHORITY = "id.kardihaekal.todolist";

  // konten dasar URI = "content://" + <authority>
  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);


// Tetapkan jalur yang memungkinkan untuk mengakses data dalam kontrak ini
  // Ini adalah jalur untuk direktori "tasks"
  public static final String PATH_TASKS = "tasks";

/* TaskEntry adalah kelas dalam yang mendefinisikan isi dari tabel Tasks*/
  public static final class TaskEntry implements BaseColumns {

    // TaskEntry content URI = base content URI + path
    public static final Uri CONTENT_URI =
        BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();


    // tabel task dan nama kolom
    public static final String TABLE_NAME = "tasks";

  // Karena TaskEntry mengimplementasikan antarmuka "BaseColumns", itu telah diproduksi secara otomatis
  // "_ID" selain dua di bawah
  public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRIORITY = "priority";


    /*
  Struktur tabel di atas terlihat seperti contoh tabel di bawah ini.
  Dengan nama tabel dan kolom di atas, dan konten potensial dalam baris
  Catatan: Karena ini mengimplementasikan BaseColumns, kolom _id dihasilkan secara otomatis
  tugas


         - - - - - - - - - - - - - - - - - - - - - -
        | _id  |    description     |    priority   |
         - - - - - - - - - - - - - - - - - - - - - -
        |  1   |  Description 1     |       1       |
         - - - - - - - - - - - - - - - - - - - - - -
        |  2   |  Description 2     |       3       |
         - - - - - - - - - - - - - - - - - - - - - -
        .
        .
        .
         - - - - - - - - - - - - - - - - - - - - - -
        | 43   |   Description 43     |       2     |
         - - - - - - - - - - - - - - - - - - - - - -
         */

  }
}
