# JWood Utils
Here you can find some of the utility star classes I use in my **Java** workflow. If you feel like adding to it feel free to do so. Pull request are welcome at any time. Enjoy!

# What's in it
You can find several utility classes that will speed up your everyday software development by taking care of all the nasty small details.
Here is a brief summary of its content:
- **counters**: Highly performant counters of primitive type such as *int*, *double*, *long*, *float*.
- **io**: IO utilities to read/write in both plain and compress (.bz2) format. Furthermore, it never overrides the same file but adds a progressive number.
- **streams**: Series of custom *Collectors*, *Predicates*, *Stream* such as *CStream.fromIterable()*
- **time**: Contains some ProgressBar and Timer objects to time and show current job progress.
- **tuples**: Collection of *Pair*, *Triplet* and *Poker* objects which can be very useful during stream processing.
- **concurrent**: Pool object and synchronization utilities to do **controlled** parallel processing (*parallelStream()* is not always the answer).

There are also some **beta** package that are not fully tested:
- **xml.dom**: Stream-like library to navigate through an xml file and fetch all needed information in a functional way.

## Dependencies
In order to work you will need to download [Apache Common Compress](https://commons.apache.org/proper/commons-compress/) library (v1.17) and link it to your project before building it.


## Highlights
Some classes have major roles. Here are the most important ones so you can get more comfortable with the library.

### IOUtils class
This class allows you to create BufferedReader (BufferedWriter) without worrying about the type of the file, by automatically detecting whether the file is compress.
It also appends an ID number when writing a file in case the file already exists, so you never end up writing over your work and accidentally lose hours or even days of experiments.
Finally, it also allows to perform all IO operations as *unchecked*, meaning that every IOException gets wrapped into an *UncheckedIOException* which is a RuntimeException.

```Java
BufferedWriter fout = IOUtils.newWriterByExtension("file.txt");
List<String> lst = List.of("First", "Second", "Third", "Fourth");
lst.stream().map(IOUtills::appendNewLine).forEachOrdered(IOUtils.uncheckConsumer(fout::append)); //no IOException.
IOUtils.runUnchecked(fout::close); //same. Does not throw an IOException.
```

### Pairs and Triplets
Very simple and basic classes describing a pair and a triplet of objects, respectively. They also provide some methods to perform some operations like inverting a pair or pivoting a triplet.
```Java
Pair<String, Double> pair = new Pair<>("Hello", 1.0);
Pair<Double, String> iPair = Pair.invert(pair);
//assert pair.getFirst().equals(iPair.getSecond());
//assert pair.getSecond().equals(iPair.getFirst());
```

They can be really powerful when used together with the Java stream API.
Example:
```Java
Map<Double, List<String>> map = new HashMap<>();
//... add to the map
Map<String, List<Double>> invertedMap = map.entrySet().stream()
                                      .flatMap(s -> s.getValue().stream().map( t -> new Pair<>(s.getKey(), t)))
				                              .collect(Collectors.groupingBy(Pair::getSecond, Collectors.mapping(Pair::getFirst, Collectors.toList())));
```

### Counters
The *counters* package provides some high-performace Counter objects for the main primitive numeric types, namely *int*, *double*, *long*, *float*.
Their only purpose is to count the number of occurrences of a specific object. Each Counter implements useful operations like *merge()*, *mostCommon()* and they come with a custom Collector in the *CCollectors* class.
```Java
IntegerCounter<String> wordFrequencies = IOUtils.lines("inputFile.bz2")
                                                .flatMap(s -> Arrays.stream(s.split("\\s+")))
                                                .collect(CCollectors.toIntegerCounter());
```
