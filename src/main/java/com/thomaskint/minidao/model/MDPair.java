package com.thomaskint.minidao.model;

public class MDPair<K, V> {

	private K key;

	private V value;

	public MDPair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}
}
