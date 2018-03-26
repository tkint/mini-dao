package com.thomaskint.minidao.model;

import java.util.ArrayList;
import java.util.List;

public class MDCallStack<T> {

	private List<T> stack;

	public MDCallStack() {
		this.stack = new ArrayList<>();
	}

	public boolean isEmpty() {
		return this.stack.isEmpty();
	}

	public T push(T item) {
		this.stack.add(item);
		return item;
	}

	public T pop() {
		T item = this.stack.get(this.stack.size() - 1);
		this.stack.remove(item);
		return item;
	}

	public int search(T item) {
		int index = -1;
		int i = 0;
		while (i < stack.size() && index == -1) {
			if (stack.get(i).equals(item)) {
				index = i;
			}
			i++;
		}
		return index;
	}

	public boolean isPresent(T item) {
		return search(item) != -1;
	}
}
