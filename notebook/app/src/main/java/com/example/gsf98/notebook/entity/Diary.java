package com.example.gsf98.notebook.entity;

import com.example.gsf98.notebook.bean.IconTag;
import com.example.gsf98.notebook.entity.base.Entity;
import com.example.gsf98.notebook.util.AttachHelper;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "com_liuzb_moodiary_entity_Diary")
public class Diary extends Entity {
	@Column(name = "date")
	private Date date;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

	@Column(name = "version")
	private int version;

	private List<Attach> attaches;

	private ArrayList<Tag> tags;

	private IconTag mood;

	private IconTag weather;

	private ArrayList<IconTag> iconTags;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<Attach> getAttaches() {
		return attaches;
	}

	public void setAttaches(List<Attach> attaches) {
		this.attaches = attaches;
	}

	public ArrayList<Tag> getTags() {
		return tags;
	}

	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}

	public IconTag getMood() {
		return mood;
	}

	public void setMood(IconTag mood) {
		this.mood = mood;
	}

	public IconTag getWeather() {
		return weather;
	}

	public void setWeather(IconTag weather) {
		this.weather = weather;
	}

	public ArrayList<IconTag> getIconTags() {
		return iconTags;
	}

	public void setIconTags(ArrayList<IconTag> iconTags) {
		this.iconTags = iconTags;
	}

	public String retriveTags() {
		return AttachHelper.retriveTag(getTags());
	}
}
