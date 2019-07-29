package it.jwood.commons.counters;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class IntegerCounter<K> {

	private Map<K, int[]> map;

	public IntegerCounter(boolean treeMap) {
		map = treeMap ? new TreeMap<>() : new HashMap<>();
	}

	public IntegerCounter() {
		this(false);
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public void add(K key, int v) {
		int[] value = map.get(key);
		if (value == null) {
			map.put(key, new int[] {v});
			return;
		}else {
			value[0] += v;
		}
	}

	public void increment(K key) {
		int[] value = map.get(key);
		if (value == null) {
			map.put(key, new int[] {1});
			return;
		}else {
			value[0]++;
		}
	}

	public void decrement(K key) {
		int[] value = map.get(key);
		if (value == null) {
			map.put(key, new int[] {-1});
			return;
		}else {
			value[0]--;
		}
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean contains(Object key) {
		return containsKey(key);
	}

	public Integer get(Object key) {
		int[] v = map.get(key);
		return v == null ? null : v[0];
	}

	public int getOrDefault(Object key, int def) {
		int[] v = map.get(key);
		return v == null ? def: v[0];
	}

	public int obtain(Object key) {
		return map.get(key)[0];
	}

	public Integer put(K key, int value) {
		int[] v = map.put(key, new int[] {value});
		return v == null ? null : v[0];
	}

	public Integer remove(Object key) {
		int[] v = map.remove(key);
		return v == null ? null : v[0];
	}

	public void putAll(IntegerCounter<? extends K> m) {
		for (K key : m.keySet()) {
			map.put(key, new int[] {m.map.get(key)[0]});
		}
	}

	public void putAll(Map<? extends K, Integer> m) {
		for (Entry<? extends K, Integer> e : m.entrySet()) {
			map.put(e.getKey(), new int[] {e.getValue()});
		}
	}

	public void clear() {
		map.clear();
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public Collection<Integer> values() {
		return map.values().stream().map(s -> s[0]).collect(Collectors.toList());
	}

	public IntegerCounter<K> merge(IntegerCounter<? extends K> other) {
		for (Entry<? extends K, int[]> e : other.map.entrySet()) {
			K key = e.getKey();
			int[] ov = e.getValue();
			int[] v = map.get(key);
			if (v == null) {
				map.put(key, new int[] {ov[0]});
			}else {
				v[0] += ov[0];
			}
		}
		return this;
	}

	public Set<IntEntry<K>> entrySet() {
		return map.entrySet().stream().sequential().map(s -> new IntEntry<>(s.getKey(), s.getValue()))
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	public String toString(){
		return entrySet().stream().map(IntEntry::toString).collect(Collectors.joining(", ", "{", "}"));
	}

	public List<IntEntry<K>> mostCommon(int limit){
		return entrySet().stream().sorted(Comparator.<IntEntry<K>>comparingInt(IntEntry::getValue).reversed()).limit(limit).collect(Collectors.toList());
	}

	public List<IntEntry<K>> mostCommon(){
		return entrySet().stream().sorted(Comparator.<IntEntry<K>>comparingInt(IntEntry::getValue).reversed()).collect(Collectors.toList());
	}

	public List<IntEntry<K>> leastCommon(int limit){
		return entrySet().stream().sorted(Comparator.comparingDouble(IntEntry::getValue)).limit(limit).collect(Collectors.toList());
	}

	public List<IntEntry<K>> leastCommon(){
		return entrySet().stream().sorted(Comparator.comparingDouble(IntEntry::getValue)).collect(Collectors.toList());
	}



	public static class IntEntry<K> {

		private int[] value;
		private K key;

		public IntEntry(K key, int[] value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public int getValue() {
			return value[0];
		}

		public int setValue(int v) {
			int old = value[0];
			value[0] = v;
			return old;
		}

		@Override
		public String toString() {
			return new StringBuilder().append(key).append("=").append(value[0]).toString();
		}
	}

}
