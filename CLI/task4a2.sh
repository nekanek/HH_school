#!/bin/sh
# find 95% quantile, 99% quantile, total, average resonse time to /resume , successful (with response code 2**), during 12:00-13:00

in=log # input file

# ======= 4a =========
echo "For task 4a: "

sorted4a=`awk '$2~/^12/ && $6~/^\/resume/ && $7~/^2[0-9][0-9]$/ {gsub(/ms/,""); sum+=$8; count++; print $8} END {print "Total response time: ", sum > "temp";  print "Average response time: ", sum/count >> "temp"} ' $in | sort`

count=`echo $sorted4a | wc -w`
q95=$((95*$count/100))
q99=$((99*$count/100))
q95val=`echo $sorted4a | awk '{print $'$q95'}'` 
q99val=`echo $sorted4a | awk '{print $'$q99'}'` 

cat temp
rm temp
echo "95% quantile: " $q95val
echo "99% quantile: " $q99val
