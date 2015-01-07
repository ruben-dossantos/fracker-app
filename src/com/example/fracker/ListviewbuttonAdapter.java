package com.example.fracker;

import java.util.ArrayList;
import java.util.List;

import com.example.fracker.model.Group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ListviewbuttonAdapter extends BaseAdapter implements ListAdapter {

	private List<Group> list = new ArrayList<Group>();
	private Context context;
	private int imgId;

	public ListviewbuttonAdapter(List<Group> list, Context context,
			int imgId) {
		this.list = list;
		this.context = context;
		this.imgId = imgId;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int pos) {
		return list.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return list.get(pos).getId();
		// just return 0 if your list items do not have an Id variable.
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listview_button, null);
		}

		// Handle TextView and display string from your list
		TextView listItemText = (TextView) view
				.findViewById(R.id.list_item_string);
		listItemText.setText(list.get(position).toString());

		// Handle buttons and add onClickListeners
		ImageButton actionBtn = (ImageButton) view
				.findViewById(R.id.action_btn);
		actionBtn.setImageResource(imgId);

		actionBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// do something
				list.remove(position); // or some other task
				notifyDataSetChanged();
			}
		});

		return view;
	}

}
