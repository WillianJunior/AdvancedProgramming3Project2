clear

output_file=$1
iterations=$2
thread_num_beg=$3
thread_num_end=$4

rm $output_file

for i in $(seq $thread_num_beg $thread_num_end)
do
	echo "$i threads"
	sh ../../singleTestJob.sh $output_file $i $iterations
	echo ";" >> $output_file
done
