package it.jwood.commons;


import it.jwood.commons.tuples.Pair;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Config {

    public static Config getConfigs(String filename) {
        return getConfigs(filename, true);
    }

    public static Config getConfigs(String filename, boolean sparse) {
        try {
            Map<String, List<String>> map = Files.lines(Paths.get(filename))
                    .map(String::trim).filter(s -> !s.isEmpty() && !s.startsWith("#"))
                    .map(s -> s.split("=", 2)).map(s -> new Pair<>(s[0], s[1]))
                    .map(s -> {
                        String second = s.getSecond();
                        if (second.startsWith("'") && second.endsWith("'"))
                            second = second.substring(1, second.length()-1);
                        if (second.startsWith("\"") && second.endsWith("\""))
                            second = second.substring(1, second.length()-1);
                        s.setSecond(second);
                        return s;
                    })
                    .collect(Collectors.groupingBy(Pair::getFirst, TreeMap::new, Collectors.mapping(Pair::getSecond, Collectors.toList())));
            return new Config(map, sparse);
        }catch(IOException e){throw new UncheckedIOException(e);}
    }

    private final Map<String, List<String>> config;
    private final boolean sparse;

    private Config(Map<String, List<String>> map, boolean sparse){
        this.config = map;
        this.sparse = sparse;
        if (sparse){
            for (Map.Entry<String, List<String>> e : map.entrySet()){
                if (e.getValue().size() > 1 ){
                    throw new IllegalArgumentException("Sparse is set but found '" + e.getKey() + "' with multiple values: " + e.getValue());
                }
            }
        }
    }

    public void set(String key, String value){
        List<String> strings = this.config.get(key);
        if (strings == null){
            strings = new ArrayList<>(1);
            this.config.put(key, strings);
        }
        strings.clear();
        strings.add(value);
    }

    public void set(String key, List<String> value){
        if (sparse && value.size() != 1){
            throw new IllegalArgumentException("Config is sparse but 'value' has multiple elements: " + value.size());
        }
        this.config.put(key, new ArrayList<>(value));
    }

    public int getAsInt(String key, int def){
        if (!sparse)
            throw new IllegalArgumentException("map must be sparse");
        List<String> v = config.get(key);
        return v == null ? def : Integer.parseInt(v.get(0));
    }

    public int getAsInt(String key){
        if (!sparse)
            throw new IllegalArgumentException("map must be sparse");
        List<String> v = config.get(key);
        return Integer.parseInt(v.get(0));
    }

    public int[] getAsInts(String key, int[] def){
        List<String> v = config.get(key);
        return v == null ? def : v.stream().mapToInt(Integer::parseInt).toArray();
    }

    public int[] getAsInts(String key){
        return getAsInts(key, null);
    }

    public double getAsDouble(String key, double def){
        if (!sparse)
            throw new IllegalArgumentException("map must be sparse");
        List<String> v = config.get(key);
        return v == null ? def : Double.parseDouble(v.get(0));
    }

    public double getAsDouble(String key){
        if (!sparse)
            throw new IllegalArgumentException("map must be sparse");
        List<String> v = config.get(key);
        return Double.parseDouble(v.get(0));
    }

    public double[] getAsDoubles(String key, double[] def){
        List<String> v = config.get(key);
        return v == null ? def : v.stream().mapToDouble(Double::parseDouble).toArray();
    }

    public double[] getAsDoubles(String key){
        return getAsDoubles(key, null);
    }

    public float getAsFloat(String key, float def){
        if (!sparse)
            throw new IllegalArgumentException("map must be sparse");
        List<String> v = config.get(key);
        return v == null ? def : Float.parseFloat(v.get(0));
    }

    public float getAsFloat(String key){
        if (!sparse)
            throw new IllegalArgumentException("map must be sparse");
        List<String> v = config.get(key);
        return Float.parseFloat(v.get(0));
    }

    public float[] getAsFloats(String key, float[] def){
        List<String> v = config.get(key);
        if (v == null)
            return def;
        float[] floats = new float[v.size()];
        int i = 0;
        for (String x : v){
            floats[i++] = Float.parseFloat(x);
        }
        return floats;
    }

    public float[] getAsFloats(String key){
        return getAsFloats(key, null);
    }

    public boolean getAsBoolean(String key, boolean def){
        if (!sparse)
            throw new IllegalArgumentException("map must be sparse");
        List<String> v = config.get(key);
        return v == null ? def : Boolean.parseBoolean(v.get(0));
    }

    public boolean getAsBoolean(String key){
        if (!sparse)
            throw new IllegalArgumentException("map must be sparse");
        List<String> v = config.get(key);
        return Boolean.parseBoolean(v.get(0));
    }

    public boolean[] getAsBooleans(String key, boolean[] def){
        List<String> v = config.get(key);
        if (v == null)
            return def;
        boolean[] b = new boolean[v.size()];
        int i = 0;
        for (String x : v)
            b[i++] = Boolean.parseBoolean(x);
        return b;
    }

    public boolean[] getAsBooleans(String key){
        return getAsBooleans(key, null);
    }


    public long getAsLong(String key, long def){
        if (!sparse)
            throw new IllegalArgumentException("map must be sparse");
        List<String> v = config.get(key);
        return v == null ? def : Long.parseLong(v.get(0));
    }

    public long getAsLong(String key){
        if (!sparse)
            throw new IllegalArgumentException("map must be sparse");
        List<String> v = config.get(key);
        return Long.parseLong(v.get(0));
    }

    public long[] getAsLongs(String key, long[] def){
        List<String> v = config.get(key);
        return v == null ? def : v.stream().mapToLong(Long::parseLong).toArray();
    }

    public long[] getAsLongs(String key){
        return getAsLongs(key, null);
    }

    public char getAsChar(String key, char def){
        if (!sparse)
            throw new IllegalArgumentException("map must be sparse");
        List<String> v = config.get(key);
        return v == null ? def : v.get(0).charAt(0);
    }

    public char getAsChar(String key){
        if (!sparse)
            throw new IllegalArgumentException("map must be sparse");
        List<String> v = config.get(key);
        return v.get(0).charAt(0);
    }

    public char[] getAsChars(String key, char[] def){
        List<String> v = config.get(key);
        if (v == null)
            return def;
        char[] b = new char[v.size()];
        int i = 0;
        for (String x : v)
            b[i++] = x.charAt(0);
        return b;
    }

    public char[] getAsChars(String key){
        return getAsChars(key, null);
    }

    public <T> T get(String key, Function<String, T> mapper, T def){
        if (!sparse)
            throw new IllegalArgumentException("map must be sparse");
        List<String> val = config.get(key);
        if (val == null)
            return def;
        return mapper.apply(val.get(0));
    }

    public <T> T get(String key, Function<String, T> mapper){
        return get(key, mapper, null);
    }

    public String get(String key){
        return get(key, Function.identity(), null);
    }

    public <T> List<T> gets(String key, Function<String, T> mapper, List<T> def){
        List<String> val = config.get(key);
        if (val == null)
            return def;
        return val.stream().map(mapper).collect(Collectors.toList());
    }

    public <T> List<T> gets(String key, Function<String, T> mapper){
        return gets(key, mapper, null);
    }

    public List<String> gets(String key){
        return gets(key, Function.identity(), null);
    }


    public String getAsString(String key, String def){
        if (!sparse)
            throw new IllegalArgumentException("map must be sparse");
        List<String> v = config.get(key);
        return v == null ? def : v.get(0);
    }

    public String getAsString(String key){
        return getAsString(key, null);
    }

    public String[] getAsStrings(String key, String[] def){
        List<String> v = config.get(key);
        return v == null ? def : v.toArray(new String[0]);
    }

    public String[] getAsStrings(String key){
        return getAsStrings(key, null);
    }

    public String toString(){
        if (sparse)
            return config.entrySet().stream().map(s -> s.getKey() + ":\t'" + s.getValue().get(0) + "'").collect(Collectors.joining("\n"));
        else
            return config.entrySet().stream().map(s -> s.getKey() + ":\t[" + s.getValue().stream().map(t -> "'" + t + "'").collect(Collectors.joining(", ")) + "]'").collect(Collectors.joining("\n"));
    }

}
