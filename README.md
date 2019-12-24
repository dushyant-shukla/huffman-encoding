## HUFFMAN COMPRESSION

The program implements Huffman Encoding to compress an input file. A menu driven program allows to provide input file to compress/decompress.

### FORMAT OF THE COMPRESSED DATA
The compressed data consists of two parts:
* Header
* Huffman Encoded Data

The header consists of three parts:
* Magic bytes (2 Bytes)
* Number of different 8 bit patterns (Huffman leaf nodes) (4 Bytes)
* Huffman leaf nodes [(8 bit pattern(1 Byte) + frequency(4 Bytes)) PER node]

The compressed data will have the below structure:\
--------------------------------------------------------------------------------------------------\
| Magic Bytes | Number of Huffman Leaf Nodes | Huffman Leaf Nodes |  + Huffman Encoded Data \
------------------------------------------- Header -------------------------------------------

### BUILD THE APPLICATION
~~~
~$ mvn clean install
~~~

### RUNNING THE APPLICATION
Before running the application, please do the following:
* On Windows, create a folder named 'huffman' under C:\Users\\{user}\AppData\Local\Temp\
* On Unix based systems, create a folder 'huffman' under /tmp.

Run the application by using the below command:
~~~
~$ java -jar huffman-encoding-1.0.jar
~~~