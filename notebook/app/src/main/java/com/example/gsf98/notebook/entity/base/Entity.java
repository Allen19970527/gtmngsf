package com.example.gsf98.notebook.entity.base;

import org.xutils.db.annotation.Column;

public abstract class Entity {
	@Column(name = "id", isId = true)
	protected int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
