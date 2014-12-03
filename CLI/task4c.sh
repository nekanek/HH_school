#!/bin/bash

# !!! execute with bash ver. >= 4.3 !!!

# draw a graph (hourly, with gnuplot) of 95% quantile of response timesto urls /resume /vacancy /user

in=log # input file, assume it is sorted
date4c="2013-01-18" # date for plotting in 4c

# creates 3 temp files (resumeT, vacancyT and userT) for graph and removes them before exit

# ======= 4c =========
echo "Drawing graph in 4c: "

function hourlyQuant2 () {
  # uses passing array by refernce (since bash 4.3)
  declare -n ar=$2
  if (( ${#ar[@]} > 0));
  then 
    # sorting only if during previous hour some values were added
    if [ "$3" = true ] ;
    then
      readarray -t ar < <(for a in "${ar[@]}"; do echo "$a"; done | sort)
    fi
    # following scary line equals these four:
      # lengthR=${#ar[@]}
      # i=$lengthR*95/100
      # temp=${ar[$i]} 
      # echo $curHour " " $temp >> $1
    echo $curHour " " ${ar[$(($((${#ar[@]}))*95/100))]}  >> $1
  fi  
}

start=true
changedR=false
changedV=false
changedU=false
arrayR=()
arrayV=()
arrayU=()
if [ -f $in ]
then
  while read date ttime log_level request_type user_id url  response_code response_time;
  do
    if [ "$date4c" = "$date" ]
    then
        done="done" # we found in file date needed
        # setting first hour
        if [ "$start" = true ] ; then
          # if relevant url
          if [[ "$url" =~ /resume[/?] ]] || [[ "$url" =~ /resume$ ]] || [[ "$url" =~ /vacancy[/?] ]] || [[ "$url" =~ /vacancy$ ]] || [[ "$url" =~ /user[/?] ]] || [[ "$url" =~ /user$ ]] ; then
              curHour=${ttime:0:2}
              start=false
          fi
        fi

        # after going to next hour, count quantiles for previous
        if (( ${ttime:0:2} > $curHour ));
        then
          hourlyQuant2 "resumeT" arrayR "$changedR"
          hourlyQuant2 "vacancyT" arrayV "$changedV"
          hourlyQuant2 "userT" arrayU "$changedU"
          
          changedR=false
          changedV=false
          changedU=false

          curHour=$(($curHour + 1))
        fi

        # add response times to arrays if relevant
        response_time=`echo $response_time | tr -d "ms"`
        if [[ "$url" =~ /resume[/?] ]] || [[ "$url" =~ /resume$ ]];
        then
          arrayR+=( "$response_time" )
          changedR=true
        fi
        if [[ "$url" =~ /vacancy[/?] ]] || [[ "$url" =~ /vacancy$ ]];
        then   
          arrayV+=( "$response_time" )
          changedV=true
        fi
        if [[ "$url" =~ /user[/?] ]] || [[ "$url" =~ /user$ ]];
        then
          arrayU+=( "$response_time" )
          changedU=true
        fi
    else
      # skip the rest of file older than required date
      if [ "$done" = "done" ] 
      then
        break
      fi
    fi
  done < $in
          hourlyQuant2 "resumeT" arrayR "$changedR"
          hourlyQuant2 "vacancyT" arrayV "$changedV"
          hourlyQuant2 "userT" arrayU "$changedU"
fi

gnuplot -p -e "plot 'resumeT' w lines, 'vacancyT' w lines, 'userT' w lines;"

rm resumeT
rm vacancyT
rm userT