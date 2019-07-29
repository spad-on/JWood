package it.jwood.commons.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

public class IOUtils {
	
	public enum FileType {
		REGULAR_FILE,
		DIRECTORY,
		SYMBOLIC_LINK,
		UNKNOWN,
		NOT_EXISTING
	}

	public static final Charset DEFAULT_CHARSET = Charset.forName("utf-8");

	private IOUtils() {}
	
	public static String getFolder(String filename) {
		return Paths.get(filename).getParent().toString();
	}
	
	public static Path getFolderPath(String filename) {
		return Paths.get(filename).getParent();
	}
	
	public static String getFilename(String filename) {
		return getFilename(filename, false);
	}
	
	public static boolean isRegularFile(String filename) {
		return Files.isRegularFile(Paths.get(filename));
	}
	
	public static boolean isDirectory(String filename) {
		return Files.isDirectory(Paths.get(filename));
	}
	
	public static boolean exists(String filename) {
		return Files.exists(Paths.get(filename));
	}
	
	public static FileType getFileType(String filename) {
		Path path = Paths.get(filename);
		if (Files.isDirectory(path))
			return FileType.DIRECTORY;
		else if (Files.isRegularFile(path))
			return FileType.REGULAR_FILE;
		else if (Files.isSymbolicLink(path))
			return FileType.SYMBOLIC_LINK;
		else if (!Files.exists(path))
			return FileType.NOT_EXISTING;
		return FileType.UNKNOWN;
	}
	
	public static String getFilename(String filename, boolean trimExtension) {
		String name = Paths.get(filename).getFileName().toString();
		if (trimExtension) {
			String ext = getExtension(filename);
			if (ext.isEmpty())
				return name;
			return name.substring(0, name.length() - ext.length() -1);
		}
		return name;
	}

	public static String getExtension(String filename){
		String name = Paths.get(filename).getFileName().toString();
		int idx = name.lastIndexOf(".");
		int s = name.indexOf("." + File.separator);
		if (idx != s && idx >= 0)
			return name.substring(0, idx);
		return "";
	}
	
	public static void readLines(String filename, Consumer<? super String> c) throws IOException {
		BufferedReader fin = newReaderByExtension(filename);
		fin.lines().forEach(c);
		fin.close();
	}

	public static String join(String...parts) {
		return String.join(File.separator, parts);
	}
		
	public static String ensureFilename(String filename) {
		int i = 0;
		String name = Paths.get(filename).getFileName().toString();
		Path pFolder = Paths.get(filename).getParent();
		String folder = pFolder == null ? "" : pFolder.toString();
		int idx = name.lastIndexOf(".");
		String ext = idx < 0 || (idx < name.length()-1 && name.charAt(idx+1) == File.separatorChar) ? "" : name.substring(idx);
		name = idx < 0 || (idx < name.length()-1 && name.charAt(idx+1) == File.separatorChar) ? name : name.substring(0, idx);
		Path pFilename = Paths.get(filename);
		if (filename.endsWith("." + File.separator))
			return filename;
		while (Files.exists(pFilename)) {
			pFilename = Paths.get(folder, String.format("%s_%05d%s", name, i++, ext));
		}
		return pFilename.toString();
	}

	public static BufferedReader newCompressedReader(String filename) throws IOException {
		return new BufferedReader(new InputStreamReader(new BZip2CompressorInputStream(new FileInputStream(filename)), DEFAULT_CHARSET));
	}

	public static BufferedReader newBufferedReader(String filename) throws IOException {
		return Files.newBufferedReader(Paths.get(filename), DEFAULT_CHARSET);
	}
	
	public static BufferedWriter newCompressedWriter(String filename) throws IOException {
		return newCompressedWriter(filename, false);
	}
	
	public static BufferedWriter newCompressedWriter(String filename, boolean append) throws IOException {
		if (!append)
			filename = ensureFilename(filename);
		Path parent = Paths.get(filename).getParent();
		if (parent != null && !Files.exists(parent))
			Files.createDirectories(parent);
		return new BufferedWriter(new OutputStreamWriter(new BZip2CompressorOutputStream(new FileOutputStream(filename, append)), DEFAULT_CHARSET));
	}

	public static BufferedWriter newBufferedWriter(String filename) throws IOException {
		return newBufferedWriter(filename, false);
	}

	public static BufferedWriter newBufferedWriter(String filename, boolean append) throws IOException {
		if (!append)
			filename = ensureFilename(filename);
		Path parent = Paths.get(filename).getParent();
		if (parent != null && !Files.exists(parent))
			Files.createDirectories(parent);
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, append), DEFAULT_CHARSET));
	}
	
	public static List<Path> listFiles(Path folder, Predicate<Path> pathFilter) throws IOException {
		Stream<Path> s = Files.list(folder);
		if (pathFilter != null)
			s = s.filter(pathFilter);
		return s.collect(Collectors.toList());
	}
	
	public static List<Path> listFiles(String folder, Predicate<Path> pathFilter) throws IOException {
		return listFiles(Paths.get(folder), pathFilter);
	}
	
	public static List<Path> listFiles(String folder) throws IOException {
		return listFiles(Paths.get(folder), null);
	}
	
	public static List<Path> listFiles(Path folder) throws IOException {
		return listFiles(folder, null);
	}
	
	public static <K, V> Map<K, List<V>> readMapping(String filename, String sep, Function<String, K> keyMapper, Function<String, V> valueMapper) throws IOException {
		return Files.lines(Paths.get(filename)).map(s -> s.split(sep))
					.collect(Collectors.toMap(s -> keyMapper.apply(s[0]),
							s -> Arrays.stream(s).skip(1).map(valueMapper).collect(Collectors.toList()),
							(List<V> l1, List<V> l2) -> { 
								l2.stream().filter(s -> !l1.contains(s)).forEach(l1::add);
								return l1;
							}
					));
	}
	
	public static  Map<String, List<String>> readMapping(String filename, String sep) throws IOException {
		return readMapping(filename, sep, s -> s, s -> s);
	}
	
	public static  Map<String, List<String>> readMapping(String filename) throws IOException {
		return readMapping(filename, "\\s+", s -> s, s -> s);
	}

	public static <K, V> Map<K, List<V>> readMapping(String filename, Function<String, K> keyMapper, Function<String, V> valueMapper) throws IOException {
		return readMapping(filename, "\\s+", keyMapper, valueMapper);
	}

	public static BufferedWriter newWriterByExtension(String filename) throws IOException {
		return newWriterByExtension(filename, false);
	}
	public static BufferedWriter newWriterByExtension(String filename, boolean append) throws IOException {
		return filename.toLowerCase().endsWith(".bz2") ? newCompressedWriter(filename, append) : newBufferedWriter(filename, append);
	}

	public static BufferedReader newReaderByExtension(String filename) throws IOException {
		return filename.toLowerCase().endsWith(".bz2") ? newCompressedReader(filename) : newBufferedReader(filename);
	}

	public static <R> Supplier<R> unchecked(IOSupplier<R> fn){
		return () -> {
			try {
				return fn.get();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		};
	}

	public static <T, R> Function<T,R> unchecked(IOFunction<T, R> fn) {
		return (T el) -> {
			try {
				return fn.apply(el);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		};
	}
	
	
	public static Runnable unchecked(IORunnable fn) {
		return () -> {
			try {
				fn.run();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		};
	}


	public static Runnable uncheckedRunnable(IORunnable fn) {
		return () -> {
			try {
				fn.run();
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		};
	}

	public static <T> Consumer<T> unchecked(IOConsumer<T> fn) {
		return (T el) -> {
			try {
				fn.apply(el);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		};
	}

	public static <T> Consumer<T> uncheckedConsumer(IOConsumer<T> fn) {
		return (T el) -> {
			try {
				fn.apply(el);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		};
	}

	public static void runUnchecked(IORunnable fn){
		try {
			fn.run();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static <T> T getUnchecked(IOSupplier<T> fn){
		try {
			return fn.get();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	public static Stream<String> lines(Path filename) throws IOException {
		return lines(filename.toString());
	}
	public static Stream<String> lines(String filename) throws IOException {
		BufferedReader fin = newReaderByExtension(filename);
		return fin.lines().onClose(unchecked(fin::close));
	}

	public static String read(Path filename) throws IOException {
		return read(filename.toString());
	}

	public static String read(String filename) throws IOException {
		return lines(filename).collect(Collectors.joining(newLine()));
	}

	public static void write(Path filename, Object obj) throws IOException {
		write(filename.toString(), obj);
	}

	public static void write(String filename, Object obj) throws IOException {
		BufferedWriter fout = newWriterByExtension(filename);
		fout.append(obj.toString());
		fout.close();
	}

	public static void writeLines(Path filename, Iterable<?> iterable) throws IOException {
		writeLines(filename.toString(), iterable);
	}

	public static void writeLines(String filename, Iterable<?> iterable) throws IOException {
		BufferedWriter fout = newWriterByExtension(filename);
		for (Object obj : iterable){
			fout.append(obj.toString());
			fout.newLine();
		}
		fout.close();
	}

	public static void writeLines(Path filename, Stream<?> stream) throws IOException {
		writeLines(filename.toString(), stream);
	}

	public static void writeLines(String filename, Stream<?> stream) throws IOException {
		BufferedWriter fout = newWriterByExtension(filename);
		stream.forEachOrdered(uncheckedConsumer(
				obj -> {
						fout.append(obj.toString());
						fout.newLine();
				})
		);
		fout.close();
	}

	public static String newLine(){
		return System.lineSeparator();
	}

	public static String appendNewLine(Object o){
		return o + newLine();
	}


}
