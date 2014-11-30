#!/bin/sh
ps aux | grep 127.0.0.1 | awk 'length($2) == 5 { print }' | sort -k2r
