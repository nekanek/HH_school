#!/bin/sh
# outputs size of files in bytes
grep -Ril error --include '*.log' | tee new.txt | xargs wc -c