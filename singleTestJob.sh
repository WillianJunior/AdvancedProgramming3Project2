javac ../../*.java

output_file=$1
thread_num=$2
iterations=$3

export CRAWLER_THREADS
CRAWLER_THREADS=$thread_num

echo -n "set" >> $output_file
echo -n "$thread_num" >> $output_file
echo -n "Threads = [" >> $output_file

output=$(java -classpath ../../ includeCrawler *.y *.l *.c)
echo "time: $output"
echo -n "$output" >> $output_file
for i in $(seq 1 $iterations)
do
	echo -n ", " >> $output_file 
	output=$(java -classpath ../../ includeCrawler *.y *.l *.c)
	echo "time: $output"
	echo -n "$output" >> $output_file
done

echo -n "]" >> $output_file