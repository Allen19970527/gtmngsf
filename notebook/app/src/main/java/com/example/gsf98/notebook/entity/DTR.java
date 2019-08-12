package com.example.gsf98.notebook.entity;

import com.example.gsf98.notebook.entity.base.Entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "com_liuzb_moodiary_entity_DTR")
public class DTR extends Entity {
	@Column(name = "did")
	private int did;

	@Column(name = "tid")
	private int tid;

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}
}
