package com.example.gsf98.notebook.entity;

import com.example.gsf98.notebook.entity.base.Entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "com_liuzb_moodiary_entity_DIR")
public class DIR extends Entity {
	@Column(name = "did")
	private int did;

	@Column(name = "type")
	private int type; // 0:mood, 1:weather, 2:tag

	@Column(name = "content")
	private String content;

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
