package id.kardihaekal.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import id.kardihaekal.todolist.data.TaskContract;


public class MainActivity extends AppCompatActivity implements
    LoaderManager.LoaderCallbacks<Cursor> {

  private static final String TAG = MainActivity.class.getSimpleName();
  private static final int TASK_LOADER_ID = 0;
  private CustomCursorAdapter mAdapter;

  RecyclerView mRecyclerView;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewTasks);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    mAdapter = new CustomCursorAdapter(this);
    mRecyclerView.setAdapter(mAdapter);


    new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
      @Override
      public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
      }

      @Override
      public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

        int id = (int) viewHolder.itemView.getTag();

        String stringId = Integer.toString(id);
        Uri uri = TaskContract.TaskEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        getContentResolver().delete(uri, null, null);

        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, MainActivity.this);

      }
    }).attachToRecyclerView(mRecyclerView);


    FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);

    fabButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent addTaskIntent = new Intent(MainActivity.this, AddTaskActivity.class);
        startActivity(addTaskIntent);
      }
    });

    getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
  }


  @Override
  protected void onResume() {
    super.onResume();

    // re-queries for all tasks
    getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
  }


  @Override
  public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

    return new AsyncTaskLoader<Cursor>(this) {

      Cursor mTaskData = null;

      @Override
      protected void onStartLoading() {
        if (mTaskData != null) {

          deliverResult(mTaskData);
        } else {

          forceLoad();
        }
      }

      @Override
      public Cursor loadInBackground() {

        try {
          return getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
              null,
              null,
              null,
              TaskContract.TaskEntry.COLUMN_PRIORITY);

        } catch (Exception e) {
          Log.e(TAG, "Failed to asynchronously load data.");
          e.printStackTrace();
          return null;
        }
      }

      public void deliverResult(Cursor data) {
        mTaskData = data;
        super.deliverResult(data);
      }
    };

  }


  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    mAdapter.swapCursor(data);

  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    mAdapter.swapCursor(null);
  }

}