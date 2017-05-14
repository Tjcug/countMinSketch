package com.basic.core.model;

public class CountUnit {
	public String item;
	public long count;
	public CountUnit() {
		item = null;
		count = 0;
	}
	public CountUnit(String item, long count) {
		this.item = item;
		this.count = count;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj instanceof CountUnit) {
			CountUnit cu = (CountUnit) obj;
			if (item.equals(cu.item)) {
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		int result = 15;
		result = result * 31 + item.hashCode();
		result = result * 31;
		return result;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return new String(item + ": " + count);
	}
	
}
