package it.jwood.commons.counters;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class LongCounter<K> {

	private Map<K, long[]> map;

	public LongCounter(boolean treeMap) {
		map = treeMap ? new TreeMap<>() : new HashMap<>();
	}

	public LongCounter() {
		this(false);
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public void add(K key, long v) {
		long[] value = map.get(key);
		if (value == null) {
			map.put(key, new long[] {v});
			return;
		}else {
			value[0] += v;
		}
	}

	public void increment(K key) {
		long[] value = map.get(key);
		if (value == null) {
			map.put(key, new long[] {1});
			return;
		}else {
			value[0]++;
		}
	}

	public void decrement(K key) {
		long[] value = map.get(key);
		if (value == null) {
			map.put(key, new long[] {-1});
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

	public Long get(Object key) {
		long[] v = map.get(key);
		return v == null ? null : v[0];
	}

	public long getOrDefault(Object key, long def) {
		long[] v = map.get(key);
		return v == null ? def: v[0];
	}

	public long obtain(Object key) {
		return map.get(key)[0];
	}

	public Long put(K key, long value) {
		long[] v = map.put(key, new long[] {value});
		return v == null ? null : v[0];
	}

	public Long remove(Object key) {
		long[] v = map.remove(key);
		return v == null ? null : v[0];
	}

	public void putAll(LongCounter<? extends K> m) {
		for (K key : m.keySet()) {
			map.put(key, new long[] {m.map.get(key)[0]});
		}
	}

	public void putAll(Map<? extends K, Long> m) {
		for (Entry<? extends K, Long> e : m.entrySet()) {
			map.put(e.getKey(), new long[] {e.getValue()});
		}
	}

	public void clear() {
		map.clear();
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public Collection<Long> values() {
		return map.values().stream().map(s -> s[0]).collect(Collectors.toList());
	}

	public LongCounter<K> merge(LongCounter<? extends K> other) {
		for (Entry<? extends K, long[]> e : other.map.entrySet()) {
			K key = e.getKey();
			long[] ov = e.getValue();
			long[] v = map.get(key);
			if (v == null) {
				map.put(key, new long[] {ov[0]});
			}else {
				v[0] += ov[0];
			}
		}
		return this;
	}

	public void merge(IntegerCounter<? extends K> other) {
		for (IntegerCounter.IntEntry<? extends K> e : other.entrySet()) {
			K key = e.getKey();
			int ov = e.getValue();
			long[] v = map.get(key);
			if (v == null) {
				map.put(key, new long[] {ov});
			}else {
				v[0] += ov;
			}
		}
	}

	public Set<LongEntry<K>> entrySet() {
		return map.entrySet().stream().map(s -> new LongEntry<>(s.getKey(), s.getValue()))
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	public String toString(){
		return entrySet().stream().map(Objects::toString).collect(Collectors.joining(", ", "{", "}"));
	}

	public List<LongEntry<K>> mostCommon(int limit){
		return entrySet().stream().sorted(Comparator.<LongEntry<K>>comparingLong(LongEntry::getValue).reversed()).limit(limit).collect(Collectors.toList());
	}

	public List<LongEntry<K>> mostCommon(){
		return entrySet().stream().sorted(Comparator.<LongEntry<K>>comparingLong(LongEntry::getValue).reversed()).collect(Collectors.toList());
	}

	public List<LongEntry<K>> leastCommon(int limit){
		return entrySet().stream().sorted(Comparator.comparingDouble(LongEntry::getValue)).limit(limit).collect(Collectors.toList());
	}

	public List<LongEntry<K>> leastCommon(){
		return entrySet().stream().sorted(Comparator.comparingDouble(LongEntry::getValue)).collect(Collectors.toList());
	}



	public static class LongEntry<K> {

		private long[] value;
		private K key;

		public LongEntry(K key, long[] value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public long getValue() {
			return value[0];
		}

		public long setValue(long v) {
			long old = value[0];
			value[0] = v;
			return old;
		}

		@Override
		public String toString() {
			return new StringBuilder().append(key).append("=").append(value[0]).toString();
		}

	}

}
