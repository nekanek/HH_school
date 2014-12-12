#!/bin/bash

# run with $ bash netcat_serv.sh
# access http://localhost:4333/

mkfifo pipe
trap "rm -f pipe" EXIT
while true
do
    cat pipe | nc -l 4333 |(
    x=0;
    while 
        read lines[$x] && [ ${#lines[$x]} -gt 1 ];
    do 
        x=$[$x +1]
    done

    if [[ $lines[0] =~ "GET /echo" ]] 
    then
        ( IFS=$'\n'; echo "${lines[*]}"> pipe );
        break
    elif [[ $lines[0] =~ "GET /hello" ]]
    then
        index_html=$(echo '<html><body><h2>hello world</h2></body></html>')
    elif [[ $lines[0] =~ "GET / " ]]
    then
        index_html=$(echo '<html><body><h2>Netcat simple server for hh hw</h2><p>Try localhost:4333/hello and localhost:4333/echo</p></body></html>')
    else
        index_html=$(echo '<html><body><h2>404: Not Found</h2></body></html>')
        echo -en "HTTP/1.1 404 Not Found\nContent-Type: text/html;\nContent-Length: ${#index_html}\n\n$index_html" > pipe
        break    
    fi
    echo -en "HTTP/1.1 200 OK\nContent-Type: text/html;\nContent-Length: ${#index_html}\n\n$index_html" > pipe
    )
done