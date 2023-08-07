package com.example.todo.ui.adapter;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.todo.R;
import com.example.todo.data.model.Category;

import java.util.Collections;
import java.util.List;


public class CategorySpinnerAdapter implements SpinnerAdapter {
    private final Context context;
    private final DataSetObservable observable;
    private List<Category> data;

    public CategorySpinnerAdapter(Context context) {
        this.context = context;
        this.observable = new DataSetObservable();
        this.data = Collections.emptyList();
    }

    public void setData(List<Category> data) {
        this.data = data;
        observable.notifyChanged();
    }

    private boolean isLastEntry(int i) {
        return data.isEmpty() || data.size() == i;
    }

    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        return getView(i, view, viewGroup);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        observable.registerObserver(dataSetObserver);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        observable.unregisterObserver(dataSetObserver);
    }

    @Override
    public int getCount() {
        return data.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        if (isLastEntry(i)) return null;
        return data.get(i);
    }


    @Override
    public long getItemId(int i) {
        if (isLastEntry(i)) return 0;
        return data.get(i).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.item_spinner_category, viewGroup, false);

            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        onBindViewHolder(i, viewHolder);

        return view;
    }

    private void onBindViewHolder(int i, ViewHolder viewHolder) {
        TextView textView = viewHolder.getTxtCategoryName();
        ImageView imageView = viewHolder.getImgCategoryAccent();

        if (isLastEntry(i)) {
            textView.setText(R.string.new_category_message);
            textView.setTextColor(context.getColor(R.color.gray));
            imageView.setImageDrawable(getAddDrawable());
        } else {
            textView.setText(data.get(i).getName());
            imageView.setImageDrawable(getCategoryAccent(R.color.accentColor700));
        }
    }

    private Drawable getCategoryAccent(int color) {
        Drawable accent = context.getDrawable(R.drawable.category_accent);
        accent.setTint(context.getColor(color));
        return  accent;
    }

    private Drawable getAddDrawable() {
        Drawable drawable = context.getDrawable(R.drawable.baseline_add);
        drawable.setTint(context.getColor(R.color.gray));
        return drawable;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public static class ViewHolder {
        private final TextView txtCategoryName;
        private final ImageView imgCategoryAccent;

        public ViewHolder(View view) {
            txtCategoryName = view.findViewById(R.id.txtCategoryName);
            imgCategoryAccent = view.findViewById(R.id.imgCategoryAccent);
        }

        public TextView getTxtCategoryName() {
            return txtCategoryName;
        }

        public ImageView getImgCategoryAccent() {
            return imgCategoryAccent;
        }
    }
}
