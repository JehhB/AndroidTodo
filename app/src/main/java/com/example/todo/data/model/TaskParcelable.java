package com.example.todo.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

public class TaskParcelable extends Task implements Parcelable {
    public TaskParcelable(Task task) {
        super(
                task.getId(),
                task.getCategoryId(),
                task.getTask(),
                task.getDescription(),
                task.getCreatedAt(),
                task.getDueAt(),
                task.getCompletedAt()
        );
    }

    public TaskParcelable(Parcel in) {
        super(
                (Long) in.readValue(Long.class.getClassLoader()),
                (Long) in.readValue(Long.class.getClassLoader()),
                in.readString(),
                in.readString(),
                (Long) in.readValue(Long.class.getClassLoader()),
                (Long) in.readValue(Long.class.getClassLoader()),
                (Long) in.readValue(Long.class.getClassLoader())
        );
    }

    public static final Creator<TaskParcelable> CREATOR = new Creator<TaskParcelable>() {
        @Override
        public TaskParcelable createFromParcel(Parcel in) {
            return new TaskParcelable(in);
        }

        @Override
        public TaskParcelable[] newArray(int size) {
            return new TaskParcelable[size];
        }
    };

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeValue(getId());
        dest.writeValue(getCategoryId());
        dest.writeString(getTask());
        dest.writeString(getDescription());
        dest.writeValue(getCreatedAt());
        dest.writeValue(getDueAt());
        dest.writeValue(getCompletedAt());
    }
}
