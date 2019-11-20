package id.kardihaekal.todolist;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import id.kardihaekal.todolist.data.TaskContract;

public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.TaskViewHolder> {

  private Cursor mCursor;
  private Context mContext;

  public CustomCursorAdapter(Context mContext) {
    this.mContext = mContext;
  }

  @Override
  public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View view = LayoutInflater.from(mContext)
        .inflate(R.layout.task_layout, parent, false);

    return new TaskViewHolder(view);
  }


  @Override
  public void onBindViewHolder(TaskViewHolder holder, int position) {

    int idIndex = mCursor.getColumnIndex(TaskContract.TaskEntry._ID);
    int descriptionIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_DESCRIPTION);
    int priorityIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_PRIORITY);

    mCursor.moveToPosition(position);

    final int id = mCursor.getInt(idIndex);
    String description = mCursor.getString(descriptionIndex);
    int priority = mCursor.getInt(priorityIndex);

    holder.itemView.setTag(id);
    holder.taskDescriptionView.setText(description);

    String priorityString = "" + priority;
    holder.priorityView.setText(priorityString);

    GradientDrawable priorityCircle = (GradientDrawable) holder.priorityView.getBackground();

    int priorityColor = getPriorityColor(priority);
    priorityCircle.setColor(priorityColor);

  }


  private int getPriorityColor(int priority) {
    int priorityColor = 0;

    switch(priority) {
      case 1: priorityColor = ContextCompat.getColor(mContext, R.color.materialRed);
        break;
      case 2: priorityColor = ContextCompat.getColor(mContext, R.color.materialOrange);
        break;
      case 3: priorityColor = ContextCompat.getColor(mContext, R.color.materialYellow);
        break;
      default: break;
    }
    return priorityColor;
  }

  @Override
  public int getItemCount() {
    if (mCursor == null) {
      return 0;
    }
    return mCursor.getCount();
  }



  public Cursor swapCursor(Cursor c) {
    if (mCursor == c) {
      return null;
    }
    Cursor temp = mCursor;
    this.mCursor = c;

    if (c != null) {
      this.notifyDataSetChanged();
    }
    return temp;
  }

  class TaskViewHolder extends RecyclerView.ViewHolder {

    TextView taskDescriptionView;
    TextView priorityView;

    public TaskViewHolder(View itemView) {
      super(itemView);

      taskDescriptionView = (TextView) itemView.findViewById(R.id.taskDescription);
      priorityView = (TextView) itemView.findViewById(R.id.priorityTextView);
    }
  }
}
