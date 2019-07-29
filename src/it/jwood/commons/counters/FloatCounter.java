package it.jwood.commons.counters;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class FloatCounter<K> {

	private Map<K, float[]> map;

	public FloatCounter(boolean treeMap) {
		map = treeMap ? new TreeMap<>() : new HashMap<>();
	}

	public FloatCounter() {
		this(false);
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public void add(K key, float v) {
		float[] value = map.get(key);
		if (value == null) {
			map.put(key, new float[] {v});
			return;
		}else {
			value[0] += v;
		}
	}

	public void increment(K key) {
		float[] value = map.get(key);
		if (value == null) {
			map.put(key, new float[] {1});
			return;
		}else {
			value[0]++;
		}
	}

	public void decrement(K key) {
		float[] value = map.get(key);
		if (value == null) {
			map.put(key, new float[] {-1});
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

	public Float get(Object key) {
		float[] v = map.get(key);
		return v == null ? null : v[0];
	}

	public float getOrDefault(Object key, float def) {
		float[] v = map.get(key);
		return v == null ? def: v[0];
	}

	public float obtain(Object key) {
		return map.get(key)[0];
	}

	public Float put(K key, float value) {
		float[] v = map.put(key, new float[] {value});
		return v == null ? null : v[0];
	}

	public Float remove(Object key) {
		float[] v = map.remove(key);
		return v == null ? null : v[0];
	}

	public void putAll(FloatCounter<? extends K> m) {
		for (K key : m.keySet()) {
			map.put(key, new float[] {m.map.get(key)[0]});
		}
	}

	public void putAll(Map<? extends K, Integer> m) {
		for (Entry<? extends K, Integer> e : m.entrySet()) {
			map.put(e.getKey(), new float[] {e.getValue()});
		}
	}

	public void clear() {
		map.clear();
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public Collection<Float> values() {
		return map.values().stream().map(s -> s[0]).collect(Collectors.toList());
	}


	public FloatCounter<K> merge(FloatCounter<? extends K> other) {
		for (Entry<? extends K, float[]> e : other.map.entrySet()) {
			K key = e.getKey();
			float[] ov = e.getValue();
			float[] v = map.get(key);
			if (v == null) {
				map.put(key, new float[] {ov[0]});
			}else {
				v[0] += ov[0];
			}
		}
		return this;
	}

	public Set<FloatEntry<K>> entrySet() {
		return map.entrySet().stream().map(s -> new FloatEntry<>(s.getKey(), s.getValue()))
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	public String toString(){
		return entrySet().stream().map(Objects::toString).collect(Collectors.joining(", ", "{", "}"));
	}

	public List<FloatEntry<K>> mostCommon(int limit){
		return entrySet().stream().sorted(Comparator.<FloatEntry<K>>comparingDouble(FloatEntry::getValue).reversed()).limit(limit).collect(Collectors.toList());
	}

	public List<FloatEntry<K>> mostCommon(){
		return entrySet().stream().sorted(Comparator.<FloatEntry<K>>comparingDouble(FloatEntry::getValue).reversed()).collect(Collectors.toList());
	}
	public List<FloatEntry<K>> leastCommon(int limit){
		return entrySet().stream().sorted(Comparator.comparingDouble(FloatEntry::getValue)).limit(limit).collect(Collectors.toList());
	}

	public List<FloatEntry<K>> leastCommon(){
		return entrySet().stream().sorted(Comparator.comparingDouble(FloatEntry::getValue)).collect(Collectors.toList());
	}


	public static class FloatEntry<K> {

		private float[] value;
		private K key;

		public FloatEntry(K key, float[] value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public float getValue() {
			return value[0];
		}

		public float setValue(float v) {
			float old = value[0];
			value[0] = v;
			return old;
		}

		@Override
		public String toString() {
			return new StringBuilder().append(key).append("=").append(value[0]).toString();
		}

	}

}
