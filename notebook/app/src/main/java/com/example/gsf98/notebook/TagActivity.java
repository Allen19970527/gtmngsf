package com.example.gsf98.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.example.gsf98.notebook.adapter.TagAdapter;
import com.example.gsf98.notebook.entity.Tag;
import com.example.gsf98.notebook.logic.Logic;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class TagActivity extends BaseActivity {
	@ViewInject(value = R.id.diary_tag_list)
	private ListView lstTag;

	@ViewInject(value = R.id.diary_tag_input)
	private EditText edtInput;

	private List<Tag> tags;
	private TagAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_tag_list);

		x.view().inject(this);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		tags = new ArrayList<Tag>();
		adapter = new TagAdapter(getApplicationContext(), tags);
		lstTag.setAdapter(adapter);

		ArrayList<Parcelable> tmp = getIntent().getParcelableArrayListExtra("tagSelect");
		List<Tag> tagSelect = new ArrayList<Tag>();
		if (tmp != null) {
			for (Parcelable parcelable : tmp) {
				if (parcelable instanceof Tag) {
					tagSelect.add((Tag) parcelable);
				}
			}
		}

		try {
			tags.clear();
			tags.addAll(Logic.getInstance(getApplicationContext()).searchTags());
			for (Tag tag : tags) {
				if (tagSelect.contains(tag)) {
					tag.setSelect(true);
				}
			}
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_edit, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.menu_edit_save:
			save();
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void save() {
		ArrayList<Tag> tagSelect = new ArrayList<Tag>();
		for (Tag tag : tags) {
			if (tag.isSelect()) {
				tagSelect.add(tag);
			}
		}

		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList("tagSelect", tagSelect);
		intent.putExtras(bundle);

		setResult(RESULT_OK, intent);
	}

	@Event(value = R.id.diary_tag_add)
	private void addEvent(View v) {
		if (TextUtils.isEmpty(edtInput.getText().toString().trim()))
			return;

		Tag tag = new Tag();
		tag.setName(edtInput.getText().toString());
		try {
			Logic.getInstance(getApplicationContext()).saveTag(tag);
		} catch (Exception e) {
		}

		edtInput.setText("");
		tag.setSelect(true);
		tags.add(0, tag);
		adapter.notifyDataSetChanged();
	}

	@Event(value = R.id.diary_tag_list, type = OnItemClickListener.class)
	private void tagClickEvent(AdapterView<?> parent, View view, int position, long id) {
		tags.get(position).setSelect(!tags.get(position).isSelect());
		adapter.notifyDataSetChanged();
	}
}
