#!/bin/sh
# find 95% quantile, 99% quantile, total, average resonse time to /resume , successful (with response code 2**), during 12:00-13:00

in=log # input file

# ======= 4a =========
echo "For task 4a: "

sorted4a=`awk '$2~/^12/ && $6~/^\/resume/ && $7~/^2[0-9][0-9]$/ {gsub(/ms/,""); print $8; } ' $in | sort`

count=`echo $sorted4a | wc -w`
sum=0 
i=0
q95=$((95*$count/100))
q99=$((99*$count/100))

for ms in $sorted4a
do  
  sum=$(echo $sum + $ms | bc)
  i=$(echo $i + 1 | bc)

  if [ "$i" = "$q95" ]
  then
    echo "95% quantile: " $ms
  fi

  if [ "$i" = "$q99" ]
  then
    echo "99% quantile: " $ms
  fi

done

echo "Total response time: " $sum
echo "Average response time: " $(echo $sum/$count | bc)