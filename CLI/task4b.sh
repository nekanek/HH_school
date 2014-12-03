#!/bin/sh
# find average and mean response time to /resume with id=43, given day

in=log # input file
date="2013-01-18" # date for 4b

# ======= 4b =========
echo "\nFor task 4b: "

sorted4b=`awk -v date="$date" '$1==date  && $6~/^\/resume\?id\=43/ {gsub(/ms/,""); print $8; } ' $in | sort`

count=`echo $sorted4b | wc -w`
sum=0 
i=0
q50=$((50*$count/100))

for ms in $sorted4b
do  
  sum=$(echo $sum + $ms | bc)
  i=$(echo $i + 1 | bc)

  if [ "$i" = "$q50" ]
  then
    echo "Mean: " $ms
  fi

done

echo "Average response time: " $(echo $sum/$count | bc)
