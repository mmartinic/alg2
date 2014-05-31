echo %time%
set CLASSPATH=C:\dev\projects\coursera\alg2\lib\algs4.jar;C:\dev\projects\coursera\alg2\lib\stdlib.jar;.
java BurrowsWheeler - < mobydick.txt | java MoveToFront - | java Huffman - > mobydick.bw
java Huffman + < mobydick.bw | java MoveToFront + | java BurrowsWheeler + > mobydick2.txt
echo %time%