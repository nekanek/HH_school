#!/bin/sh
createdb hh_hw_javadb
psql hh_hw_javadb < create-tables.sql