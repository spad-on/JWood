package it.jwood.commons.counters;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class DoubleCounter<K> {

	private Map<K, double[]> map;

	public DoubleCounter(boolean treeMap) {
		map = treeMap ? new TreeMap<>() : new HashMap<>();
	}

	public DoubleCounter() {
		this(false);
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public void add(K key, double v) {
		double[] value = map.get(key);
		if (value == null) {
			map.put(key, new double[] {v});
			return;
		}else {
			value[0] += v;
		}
	}

	public void increment(K key) {
		double[] value = map.get(key);
		if (value == null) {
			map.put(key, new double[] {1});
			return;
		}else {
			value[0]++;
		}
	}

	public void decrement(K key) {
		double[] value = map.get(key);
		if (value == null) {
			map.put(key, new double[] {-1});
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

	public Double get(Object key) {
		double[] v = map.get(key);
		return v == null ? null : v[0];
	}

	public double getOrDefault(Object key, double def) {
		double[] v = map.get(key);
		return v == null ? def: v[0];
	}

	public double obtain(Object key) {
		return map.get(key)[0];
	}

	public Double put(K key, double value) {
		double[] v = map.put(key, new double[] {value});
		return v == null ? null : v[0];
	}

	public Double remove(Object key) {
		double[] v = map.remove(key);
		return v == null ? null : v[0];
	}

	public void putAll(DoubleCounter<? extends K> m) {
		for (K key : m.keySet()) {
			map.put(key, new double[] {m.map.get(key)[0]});
		}
	}

	public void putAll(Map<? extends K, Integer> m) {
		for (Entry<? extends K, Integer> e : m.entrySet()) {
			map.put(e.getKey(), new double[] {e.getValue()});
		}
	}

	public void clear() {
		map.clear();
	}

	public Set<K> keySet() {
		return map.keySet();
	}

	public Collection<Double> values() {
		return map.values().stream().map(s -> s[0]).collect(Collectors.toList());
	}


	public DoubleCounter<K> merge(DoubleCounter<? extends K> other) {
		for (Entry<? extends K, double[]> e : other.map.entrySet()) {
			K key = e.getKey();
			double[] ov = e.getValue();
			double[] v = map.get(key);
			if (v == null) {
				map.put(key, new double[] {ov[0]});
			}else {
				v[0] += ov[0];
			}
		}
		return this;
	}

	public Set<DoubleEntry<K>> entrySet() {
		return map.entrySet().stream().map(s -> new DoubleEntry<>(s.getKey(), s.getValue()))
				.collect(Collectors.toCollection(LinkedHashSet::new));
	}

	public String toString(){
		return entrySet().stream().map(Objects::toString).collect(Collectors.joining(", ", "{", "}"));
	}

	public List<DoubleEntry<K>> mostCommon(int limit){
		return entrySet().stream().sorted(Comparator.<DoubleEntry<K>>comparingDouble(DoubleEntry::getValue).reversed()).limit(limit).collect(Collectors.toList());
	}

	public List<DoubleEntry<K>> mostCommon(){
		return entrySet().stream().sorted(Comparator.<DoubleEntry<K>>comparingDouble(DoubleEntry::getValue).reversed()).collect(Collectors.toList());
	}

	public List<DoubleEntry<K>> leastCommon(int limit){
		return entrySet().stream().sorted(Comparator.comparingDouble(DoubleEntry::getValue)).limit(limit).collect(Collectors.toList());
	}

	public List<DoubleEntry<K>> leastCommon(){
		return entrySet().stream().sorted(Comparator.comparingDouble(DoubleEntry::getValue)).collect(Collectors.toList());
	}



	public static class DoubleEntry<K> {

		private double[] value;
		private K key;

		public DoubleEntry(K key, double[] value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public double getValue() {
			return value[0];
		}

		public double setValue(double v) {
			double old = value[0];
			value[0] = v;
			return old;
		}

		@Override
		public String toString() {
			return new StringBuilder().append(key).append("=").append(value[0]).toString();
		}

	}

}
