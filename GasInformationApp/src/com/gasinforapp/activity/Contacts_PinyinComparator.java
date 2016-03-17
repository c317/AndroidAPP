package com.gasinforapp.activity;

import java.util.Comparator;

public class Contacts_PinyinComparator implements Comparator<Contacts_SortModel> {

	public int compare(Contacts_SortModel o1, Contacts_SortModel o2) {
		if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
