package com.example.gsf98.notebook.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.gsf98.notebook.entity.base.Entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "com_liuzb_moodiary_entity_Tag")
public class Tag extends Entity implements Parcelable {
	@Column(name = "name")
	private String name;

	private boolean isSelect;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Tag other = (Tag) obj;
		if (this.getName().equals(other.getName()))
			return true;
		return false;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(id);
		dest.writeInt(isSelect ? 1 : 0);
	}

	public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {
		public Tag createFromParcel(Parcel in) {
			Tag tag = new Tag();
			tag.setName(in.readString());
			tag.setId(in.readInt());
			tag.setSelect(in.readInt() == 1 ? true : false);
			return tag;
		}

		public Tag[] newArray(int size) {
			return new Tag[size];
		}
	};
}
