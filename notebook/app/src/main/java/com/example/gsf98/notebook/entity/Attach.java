package com.example.gsf98.notebook.entity;

import android.widget.EditText;

import com.example.gsf98.notebook.entity.base.Entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 0:photo, 1:audio, 2:gps
 */
@Table(name = "com_liuzb_moodiary_entity_Attach")
public class Attach extends Entity {
	@Column(name = "type")
	private int type;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "did")
	private int did;

	private EditText edt;

	/**
	 * 0:photo, 1:audio, 2:gps
	 */
	public int getType() {
		return type;
	}

	/**
	 * 0:photo, 1:audio, 2:gps
	 * 
	 * @return
	 */
	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDid() {
		return did;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public EditText getEdt() {
		return edt;
	}

	public void setEdt(EditText edt) {
		this.edt = edt;
	}
}
