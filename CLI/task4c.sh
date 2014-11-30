#!/bin/bash

# !!! execute with bash !!!

# draw a graph (hourly, with gnuplot) of 95% quantile of response timesto urls /resume /vacancy /user

in=log # input file, assume it is sorted
date4c="2013-01-18" # date for plotting in 4c

# creates 3 temp files (resumeT, vacancyT and userT) for graph and removes them before exit

# ======= 4c =========
echo "Drawing graph in 4c: "

function hourlyQuant {
  # compute 95% quantile for previous hour, put into file
  if (( ${#arrayR[@]} > 0 ));
  then
    readarray -t arrayR < <(for a in "${arrayR[@]}"; do echo "$a"; done | sort)
    # following scary line equals these four:
      # lengthR=${#arrayR[@]}
      # i=$lengthR*95/100
      # temp=${arrayR[$i]} 
      # echo $curHour " " $temp >> resumeT
    echo $curHour " " ${arrayR[$(($((${#arrayR[@]}))*95/100))]}  >> resumeT
  fi
  # same for other 2 urls 
  if ((${#arrayV[@]} > 0));
  then          
    readarray -t arrayV < <(for a in "${arrayV[@]}"; do echo "$a"; done | sort)
    echo $curHour " " ${arrayV[$(($((${#arrayV[@]}))*95/100))]}  >> vacancyT
  fi
  if ((${#arrayU[@]} > 0));
  then
    readarray -t arrayU < <(for a in "${arrayU[@]}"; do echo "$a"; done | sort)
    echo $curHour " " ${arrayU[$(($((${#arrayU[@]}))*95/100))]}  >> userT            
  fi
      
}

# head -1 $in | awk '{gsub(/:*/,""); print $2; } ' $in`
curHour=`head -1 $in | awk '{print $2;}'`
curHour=${curHour:0:2}

if [ -f $in ]
then
  while read date ttime log_level request_type user_id url  response_code response_time;
  do
  # $date $ttime $log_level $request_type $user_id $url  $response_code $response_time
    if [ "$date4c" = "$date" ]
    then
        done="done"
        # if current line time is another hour, 
        if (( ${ttime:0:2} > $curHour ));
        then
          hourlyQuant
          curHour=$(($curHour + 1))
        fi
        # add response times to arrays
        response_time=`echo $response_time | tr -d "ms"`
        if [[ "$url" =~ /resume[/?] ]] || [[ "$url" =~ /resume$ ]];
        then
          arrayR=( "${arrayR[@]}" "$response_time" )
        fi
        if [[ "$url" =~ /vacancy[/?] ]] || [[ "$url" =~ /vacancy$ ]];
        then   
          arrayV=( "${arrayV[@]}" "$response_time" )
        fi
        if [[ "$url" =~ /user[/?] ]] || [[ "$url" =~ /user$ ]];
        then
          arrayU=( "${arrayU[@]}" "$response_time" )
        fi
    else
      if [ "$done" = "done" ] 
      then
        break
      fi
    fi
  done < $in
  hourlyQuant
fi


gnuplot -p -e "plot 'resumeT' w lines, 'vacancyT' w lines, 'userT' w lines;"

rm resumeT
rm vacancyT
rm userT

