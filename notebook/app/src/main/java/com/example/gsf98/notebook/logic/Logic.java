package com.example.gsf98.notebook.logic;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.gsf98.notebook.bean.IconTag;
import com.example.gsf98.notebook.bean.MonthBean;
import com.example.gsf98.notebook.entity.Attach;
import com.example.gsf98.notebook.entity.DIR;
import com.example.gsf98.notebook.entity.DTR;
import com.example.gsf98.notebook.entity.Diary;
import com.example.gsf98.notebook.entity.Tag;
import com.example.gsf98.notebook.util.Constant;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Logic {
	private static Logic instance;

	DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbUpgradeListener(new DbManager.DbUpgradeListener() {
		@Override
		public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

		}
	});

	public static Logic getInstance(Context context) {
		if (instance == null) {
			instance = new Logic(context);
		}

		return new Logic(context);
	}

	private final DbManager dbUtils;

	private Logic(Context context) {
		dbUtils = x.getDb(daoConfig);
	}

	public void saveDiary(Diary diary) throws DbException {
		// 保存diary
		if (diary.getId() == 0) {
			dbUtils.saveBindingId(diary);
		} else {
			dbUtils.update(diary, "title", "content", "version", "date");
		}

		// 删除diary-tag-relation
		dbUtils.delete(DTR.class, WhereBuilder.b("did", "=", diary.getId()));
		if (diary.getTags() != null && diary.getTags().size() > 0) {
			for (Tag tag : diary.getTags()) {
				// 保存tag
				tag = saveTag(tag);

				// 保存diary-tag-relation
				DTR dtr = new DTR();
				dtr.setDid(diary.getId());
				dtr.setTid(tag.getId());
				dbUtils.saveBindingId(dtr);
			}
		}

		dbUtils.delete(Attach.class, WhereBuilder.b("did", "=", diary.getId()));
		if (diary.getAttaches() != null && diary.getAttaches().size() > 0) {
			for (Attach attach : diary.getAttaches()) {
				attach.setDid(diary.getId());
				dbUtils.saveBindingId(attach);
			}
		}

		dbUtils.delete(DIR.class, WhereBuilder.b("did", "=", diary.getId()));
		if (diary.getMood() != null) {
			DIR dir = new DIR();
			dir.setDid(diary.getId());
			dir.setContent(diary.getMood().getText());
			dir.setType(0);
			dbUtils.saveBindingId(dir);
		}

		if (diary.getWeather() != null) {
			DIR dir = new DIR();
			dir.setDid(diary.getId());
			dir.setContent(diary.getWeather().getText());
			dir.setType(1);
			dbUtils.saveBindingId(dir);
		}

		if (diary.getIconTags() != null && diary.getIconTags().size() > 0) {
			for (IconTag iconTag : diary.getIconTags()) {
				DIR dir = new DIR();
				dir.setDid(diary.getId());
				dir.setContent(iconTag.getText());
				dir.setType(2);
				dbUtils.saveBindingId(dir);
			}
		}
	}

	public Tag saveTag(Tag tag) throws DbException {
		Tag existTag = dbUtils.selector(Tag.class).where("name", "=", tag.getName()).findFirst();
		if (existTag == null) {
			dbUtils.saveBindingId(tag);
		} else {
			tag.setId(existTag.getId());
		}

		return tag;
	}

	public void deleteDiary(Diary diary) {
		if (diary != null) {
			deleteDiaryByID(diary.getId());
		}
	}

	public void deleteTag(Tag tag) throws DbException {
		dbUtils.deleteById(Tag.class, tag.getId());
	}

	public List<Tag> searchTags() throws DbException {
		return dbUtils.selector(Tag.class).orderBy("id", true).findAll();
	}

	public Diary searchDiaryByID(int id) {
		Diary diary = null;
		try {
			diary = dbUtils.findById(Diary.class, id);
			diary = findDiaryDetail(diary);
		} catch (DbException e) {
		}
		return diary;
	}

	public void deleteDiaryByID(int id) {
		try {
			dbUtils.deleteById(Diary.class, id);
			dbUtils.delete(Attach.class, WhereBuilder.b("did", "=", id));
			dbUtils.delete(DIR.class, WhereBuilder.b("did", "=", id));
			dbUtils.delete(DTR.class, WhereBuilder.b("did", "=", id));
		} catch (Exception e) {
		}
	}

	public List<Diary> searchDiary(int page) {
		List<Diary> results = new ArrayList<Diary>();
		try {
			List<Diary> diaries = dbUtils.selector(Diary.class).orderBy("id", true).offset(page * Constant.PAGE_NUM).limit(Constant.PAGE_NUM).findAll();
			if (diaries != null && diaries.size() > 0) {
				for (Diary diary : diaries) {
					diary = findDiaryDetail(diary);
					results.add(diary);
				}
			}
		} catch (Exception e) {
		}
		return results;
	}

	public List<Diary> searchDiary(MonthBean month, int page) {
		if (month == null)
			return searchDiary(page);
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		List<Diary> results = new ArrayList<Diary>();
		String yyyy = month.getContent().split("/")[0];
		String mm = month.getContent().split("/")[1];
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, Integer.valueOf(yyyy));
		c.set(Calendar.MONTH, Integer.valueOf(mm) - 1);
		c.set(Calendar.DATE, 1);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		Log.v("aaaaaaa", format.format(c.getTime()));

		long from = c.getTimeInMillis();

		c.add(Calendar.MONTH, 1);
		c.add(Calendar.DATE, -1);
		c.set(Calendar.HOUR, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		Log.v("aaaaaaa", format.format(c.getTime()));

		long to = c.getTimeInMillis();

		try {
			List<Diary> diaries = dbUtils.selector(Diary.class).where("date", ">=", from).and("date", "<=", to).orderBy("id", true).offset(page * Constant.PAGE_NUM).limit(Constant.PAGE_NUM).findAll();
			if (diaries != null && diaries.size() > 0) {
				for (Diary diary : diaries) {
					diary = findDiaryDetail(diary);
					results.add(diary);
				}
			}
		} catch (Exception e) {
		}

		return results;
	}

	public List<Diary> searchDiary(Tag tag, int page) {
		if (tag == null)
			return searchDiary(page);

		List<Diary> results = new ArrayList<Diary>();
		List<Integer> ids = new ArrayList<Integer>();
		String sql = "select distinct did from com_liuzb_moodiary_entity_DTR where tid = " + tag.getId() + " order by did desc limit " + Constant.PAGE_NUM + " offset " + page * Constant.PAGE_NUM;

		try {
			Cursor cur = dbUtils.execQuery(sql);
			for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
				ids.add(cur.getInt(0));
			}

			if (ids.size() > 0) {
				for (Integer id : ids) {
					Diary diary = dbUtils.findById(Diary.class, id);
					results.add(findDiaryDetail(diary));
				}
			}
		} catch (Exception e) {
		}

		return results;
	}

	public List<Diary> searchDiary(List<IconTag> types, List<IconTag> tags, int page) {
		if (types == null || types.size() == 0)
			return searchDiary(tags, page);
		if (types.size() > 0 && types.get(0).getText().equals("fb-file"))
			return searchDiary(tags, page);

		List<Diary> results = new ArrayList<Diary>();
		List<Integer> ids = new ArrayList<Integer>();

		StringBuffer sb = new StringBuffer();
		for (IconTag tag : types) {
			if (tag.getText().equals("fb-image")) {
				sb.append("0");
			}
			if (tag.getText().equals("fb-bullhorn")) {
				sb.append("1");
			}
			if (tag.getText().equals("fb-location")) {
				sb.append("2");
			}
			sb.append(",");
		}

		sb.deleteCharAt(sb.length() - 1);

		String s1 = sb.toString();
		String s2 = retriveConditionIn(tags);

		try {
			if (s2 == null) {
				String sql = "select distinct did from com_liuzb_moodiary_entity_ATTACH where type in (" + s1 + ") order by did desc limit " + Constant.PAGE_NUM + " offset "
						+ page * Constant.PAGE_NUM;
				Cursor cur = dbUtils.execQuery(sql);
				for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
					ids.add(cur.getInt(0));
				}
			} else {
				String sql = "select distinct a.did from com_liuzb_moodiary_entity_ATTACH a, com_liuzb_moodiary_entity_DIR b where a.type in (" + s1 + ") and b.content in (" + s2
						+ ") and a.did = b.did order by a.did desc limit " + Constant.PAGE_NUM + " offset " + page * Constant.PAGE_NUM;
				Cursor cur = dbUtils.execQuery(sql);
				for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
					ids.add(cur.getInt(0));
				}
			}

			if (ids.size() > 0) {
				for (Integer id : ids) {
					Diary diary = dbUtils.findById(Diary.class, id);
					results.add(findDiaryDetail(diary));
				}
			}
		} catch (Exception e) {
		}

		return results;
	}

	public List<Diary> searchDiary(List<IconTag> tags, int page) {
		if (tags == null || tags.size() == 0)
			return searchDiary(page);

		List<Diary> results = new ArrayList<Diary>();
		List<Integer> ids = new ArrayList<Integer>();
		String sb = retriveConditionIn(tags);

		try {
			String sql = "select distinct did from com_liuzb_moodiary_entity_DIR where content in (" + sb + ") order by did desc limit " + Constant.PAGE_NUM + " offset " + page * Constant.PAGE_NUM;
			Cursor cur = dbUtils.execQuery(sql);
			for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
				ids.add(cur.getInt(0));
			}

			if (ids.size() > 0) {
				for (Integer id : ids) {
					Diary diary = dbUtils.findById(Diary.class, id);
					results.add(findDiaryDetail(diary));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return results;
	}

	private String retriveConditionIn(List<IconTag> tags) {
		if (tags == null || tags.size() == 0)
			return null;

		StringBuffer sb = new StringBuffer();

		for (IconTag tag : tags) {
			sb.append("'" + tag.getText() + "'");
			sb.append(",");
		}

		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	private Diary findDiaryDetail(Diary diary) {
		try {
			List<DTR> ddtrs = dbUtils.selector(DTR.class).where("did", "=", diary.getId()).findAll();
			ArrayList<Tag> tags = new ArrayList<Tag>();

			if (ddtrs != null && ddtrs.size() > 0) {
				for (DTR ddtr : ddtrs) {
					Tag dtag = dbUtils.findById(Tag.class, ddtr.getTid());
					tags.add(dtag);
				}
			}
			diary.setTags(tags);

			List<DIR> dirs = dbUtils.selector(DIR.class).where("did", "=", diary.getId()).findAll();
			ArrayList<IconTag> iconTags = new ArrayList<IconTag>();
			if (dirs != null && dirs.size() > 0) {
				for (DIR dir : dirs) {
					if (dir.getType() == 0) {
						IconTag iconTag = new IconTag();
						iconTag.setText(dir.getContent());
						diary.setMood(iconTag);
					}

					if (dir.getType() == 1) {
						IconTag iconTag = new IconTag();
						iconTag.setText(dir.getContent());
						diary.setWeather(iconTag);
					}

					if (dir.getType() == 2) {
						IconTag iconTag = new IconTag();
						iconTag.setText(dir.getContent());
						iconTags.add(iconTag);
					}
				}
				diary.setIconTags(iconTags);
			}

			List<Attach> attachs = dbUtils.selector(Attach.class).where("did", "=", diary.getId()).findAll();
			diary.setAttaches(attachs);
		} catch (Exception e) {
		}

		return diary;
	}

	public long getOldest() {
		long result = 0l;
		String sql = "select MIN(date) from com_liuzb_moodiary_entity_DIARY";
		try {
			Cursor cur = dbUtils.execQuery(sql);
			for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
				result = cur.getLong(0);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return result;
	}

	public long getDiaryCount() {
		long result = 0l;
		try {
			result = dbUtils.selector(Diary.class).count();
		} catch (Exception e) {
		}

		return result;
	}

	public long getPhotoCount() {
		long result = 0l;
		try {
			result = dbUtils.selector(Attach.class).where(WhereBuilder.b("type", "=", "0")).count();
		} catch (Exception e) {
		}

		return result;
	}

	public long getAudioCount() {
		long result = 0l;
		try {
			result = dbUtils.selector(Attach.class).where(WhereBuilder.b("type", "=", "1")).count();
		} catch (Exception e) {
		}

		return result;
	}
}
