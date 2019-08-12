package com.example.gsf98.notebook.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gsf98.notebook.R;
import com.example.gsf98.notebook.bean.IconTag;
import com.example.gsf98.notebook.entity.Attach;
import com.example.gsf98.notebook.entity.Diary;
import com.example.gsf98.notebook.util.AttachHelper;
import com.example.gsf98.notebook.view.IconColorUtil;
import com.example.gsf98.notebook.view.IconTextView;

import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiaryListAdapter extends BaseAdapter {
	private Context context;
	private final LayoutInflater inflater;
	private List<Diary> diaries;
	private SimpleDateFormat format;
	private int width;

	public DiaryListAdapter(Context context, List<Diary> diaries) {
		this.context = context;
		this.diaries = diaries;
		inflater = LayoutInflater.from(context);
		format = new SimpleDateFormat("MM/dd", Locale.ENGLISH);
		width = context.getResources().getDisplayMetrics().widthPixels;
	}

	@Override
	public int getCount() {
		return diaries.size();
	}

	@Override
	public Object getItem(int arg0) {
		return diaries.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.diary_item, null);

			holder.header = (LinearLayout) convertView.findViewById(R.id.item_head);
			holder.imgLay = (LinearLayout) convertView.findViewById(R.id.item_img);
			holder.iconLay = (LinearLayout) convertView.findViewById(R.id.item_moods);
			holder.imgMood = (IconTextView) convertView.findViewById(R.id.item_img_mood);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.item_title);
			holder.txtDate = (TextView) convertView.findViewById(R.id.item_date);
			holder.txtContent = (TextView) convertView.findViewById(R.id.item_content);
			holder.txtTag = (TextView) convertView.findViewById(R.id.item_tag);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Diary diary = diaries.get(position);

		holder.txtTitle.setText(diary.getTitle());
		holder.txtDate.setText(format.format(diary.getDate()));
		holder.txtContent.setText(diary.getContent());

		showTag(diary, holder.txtTag);
		showIconTag(diary, holder.header, holder.imgMood, holder.iconLay);
		showImg(diary, holder.imgLay);

		return convertView;
	}

	public final class ViewHolder {
		public LinearLayout header;
		public LinearLayout imgLay;
		public LinearLayout iconLay;
		public IconTextView imgMood;
		public TextView txtTitle;
		public TextView txtDate;
		public TextView txtContent;
		public TextView txtTag;
	}

	private void showImg(Diary diary, LinearLayout container) {
		container.removeAllViews();
		if (diary.getAttaches() != null && diary.getAttaches().size() > 0) {
			for (Attach attach : diary.getAttaches()) {
				if (attach.getType() == 0) {
					View view = inflater.inflate(R.layout.diary_item_image, null);
					ImageView img = (ImageView) view.findViewById(R.id.diary_item_image);
					LayoutParams params = img.getLayoutParams();
					params.width = width / 5;
					params.height = width / 5;
					view.setLayoutParams(params);

					x.image().bind(img, "file://" + AttachHelper.parseSmallThumb(attach.getName()));
					container.addView(view);
				}
			}
		}
	}

	private void showIconTag(Diary diary, LinearLayout header, IconTextView imgMood, LinearLayout container) {
		IconTag mood = diary.getMood();
		IconTag weather = diary.getWeather();
		List<IconTag> iconTags = diary.getIconTags();

		List<IconTag> all = new ArrayList<IconTag>();
		if (mood != null) {
			all.add(mood);
		}

		if (weather != null) {
			all.add(weather);
		}

		all.addAll(iconTags);

		if (diary.getAttaches() != null && diary.getAttaches().size() > 0) {
			for (Attach attach : diary.getAttaches()) {
				IconTag icon = null;
				if (attach.getType() == 0) {
					icon = new IconTag("fb-image");
				} else if (attach.getType() == 1) {
					icon = new IconTag("fb-bullhorn");
				} else if (attach.getType() == 2) {
					icon = new IconTag("fb-location");
				}

				if (icon != null && !all.contains(icon)) {
					all.add(icon);
				}
			}
		}

		imgMood.setVisibility(View.VISIBLE);
		if (all.size() > 0) {
			header.setBackgroundColor(context.getResources().getColor(IconColorUtil.getFaMap().get(all.get(0).getText())[0]));
			imgMood.setIcon(all.get(0).getText());
		} else {
			header.setBackgroundColor(context.getResources().getColor(R.color.clr_tag0));
			imgMood.setVisibility(View.GONE);
		}

		container.removeAllViews();
		if (all.size() > 1) {
			for (int i = 1; i < all.size(); i++) {
				IconTag iconTag = all.get(i);
				if (iconTag != null) {
					View v = inflater.inflate(R.layout.diary_item_icon, null);
					IconTextView iconTextView = (IconTextView) v.findViewById(R.id.diary_item_icon);
					iconTextView.setIcon(all.get(i).getText());
					iconTextView.setBackgroundColor(context.getResources().getColor(IconColorUtil.getFaMap().get(iconTag.getText())[0]));
					container.addView(iconTextView);
				}
			}
		}
	}

	private void showTag(final Diary diary, TextView txtTag) {
		String tags = diary.retriveTags();
		if (TextUtils.isEmpty(tags)) {
			txtTag.setVisibility(View.GONE);
		} else {
			txtTag.setVisibility(View.VISIBLE);
			txtTag.setText(tags);
		}
	}
}
