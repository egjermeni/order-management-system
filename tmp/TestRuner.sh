#!/bin/bash

mysql=`which mysql`
mysqlslap=`which mysqlslap`
connection_string=" -h 127.0.0.1 -uedmond -pPassword1"

java_test(){
    pdir=/media/shared/dev/edd/jobs/tests/edd/JavaCore
    mvn clean test-compile -f $pdir/pom.xml
    pushd $pdir/target/test-classes
    jar -cf $pdir/target/JavaCore.jar edd
    popd

    dpath=`find $pdir -type f -name *.jar`
    dpath=`echo $dpath | sed -e "s/ /:/g"`
    dpath=".:"$dpath

    #-------------------- Jvm args ---------------------
    #java.v=08
    args=$args" -server"
    args=$args" -ea"
    args=$args" -Xms2g -Xmx4g -Xss128m"
    args=$args" -XX:+PrintCommandLineFlags"
    args=$args" -XX:+AggressiveHeap"
    args=$args" -XX:+UseParallelGC"
    #args=$args" -XX:+PrintGCDetails"
    #args=$args" -XX:+AggressiveOpts"
    #args=$args" -XX:ParallelGCThreads=2"
    #args=$args" -XX:+ParallelRefProcEnabled"
    #args=$args" -XX:+DoEscapeAnalysis"
    #args=$args" -XX:+UseParallelGC"
    #args=$args" -XX:+UseParallelOldGC"
    #args=$args" -XX:+UseParNewGC"
    #args=$args" -XX:+UseG1GC"
    #args=$args" -XX:+UseConcMarkSweepGC"
    #-------------------- Jvm args ---------------------

    runner=""
    #runner=" org.junit.runner.JUnitCore"
    #class=" edd.example.java.thread.ProducerAndConsumerTest"
    class=" edd.example.java.thread.JdbcMultithreadingTest"
    run_command="java $args -cp $dpath $runner $class $@"
    echo "-----------------------------------------"
    echo "$run_command"
    echo "-----------------------------------------"
    eval "$run_command"
}

mysql_test(){
    conn_string="$connection_string"
    #args=""
    #args="$args --no-defaults"
    #args="$args $conn_string"
    #args="$args --create-schema=edmond"
    #args="$args --no-drop"
    ##args="$args --detach=1"
    #args="$args --concurrency=300"
    #args="$args --number-of-queries=100000"
    #args="$args --iterations=1"

    args=""
    args="$args --no-defaults"
    args="$args $conn_string"
    args="$args --create-schema=edmond"
    args="$args --no-drop"
    #args="$args --detach=1"
    args="$args --concurrency=4"
    args="$args --number-of-queries=1000000"
    args="$args --iterations=1"

    args="$args --query='"
    # sql
    #args=$args"insert into ACCOUNT (id, balance) values (0, 0); "
    #args=$args"select * from ACCOUNT where id=1; "
    #args=$args"update ACCOUNT set balance=balance+1 where id=1; "
    # sql
    # store procedure
    #args=$args"call create_account(@id, @count); "
    args=$args"call update_balance(1, 222); "
    #args=$args"call increment_balance(1, 1, @balance); "
    #args=$args"call get_balance(1, @balance);"
    # store procedure
    args=$args"'"

    run_command="$mysqlslap $args"
    echo "-----------------------------------------"
    echo "$run_command"
    echo "-----------------------------------------"
    eval "$run_command"
}

mysql_update_shcema(){
    conn_string="$connection_string"
    run_command="$mysql $conn_string < `dirname $0`/schema.sql"
    echo "-----------------------------------------"
    echo "$run_command"
    echo "-----------------------------------------"
    eval "$run_command"
}

mysql_reset(){
    query=""
    query="$query truncate table ACCOUNT;"
    query="$query insert into ACCOUNT (id, balance) values (0, 0);"
    conn_string="$connection_string edmond"
    run_command="$mysql $conn_string -e '$query'"
    echo "-----------------------------------------"
    echo "$run_command"
    echo "-----------------------------------------"
    eval "$run_command"
}

mysql_drop_procs(){
    conn_string="$connection_string edmond -sN"
    query=""
    query=$query"select name from mysql.proc"
    run_command="$mysql $conn_string -e '$query'"
    procedures=`eval "$run_command"`
    procedures=`echo $procedures`
    query=""
    for proc in $procedures; do
        query="$query drop procedure $proc;"
    done
    run_command="$mysql $conn_string -e '$query'"
    #echo "-----------------------------------------"
    echo "$run_command"
    #echo "-----------------------------------------"
    eval "$run_command"
}

#------------------------------------
# mysql_update_shcema
# mysql_reset
# mysql_drop_procs
# mysql_test $@
 java_test $@
#------------------------------------


#############################################################################################
###################################### Temp Mysql ###########################################
#
# show procedure status;
# show processlist;
# show engine innodb status \G
# show binary logs;
# show variables like '%tx%';
# show global status like '%tx%';
# show global variables like '%tx%';
# select THREAD_ID, TYPE, PROCESSLIST_USER, PROCESSLIST_STATE, PROCESSLIST_INFO from performance_schema.threads;
# select * from performance_schema.events_waits_summary_by_thread_by_event_name where MAX_TIMER_WAIT > 0 order by MAX_TIMER_WAIT desc limit 50;
# select FORMAT(((1310719  - 1011886) * 16384) / power(1024,3),3) SpaceUsed;
# show variables like '%general_log%';
# SET global general_log = 1;
# show variables like '%char%';show variables like '%coll%';
# 
# show create table ACCOUNT \G
# show create edmond.procedure  update_balance \G
# drop database edmond;
# truncate table ACCOUNT;
# select count(*) from edmond.ACCOUNT;
# select * from edmond.ACCOUNT where id=1;
# select id, sum(balance) from ACCOUNT group by(id) having sum(balance) > 0;
# select name, param_list, returns, body  from mysql.proc \G
#
# reset_query="update ACCOUNT set balance =0; "
# #sql
# args="$args --query='insert into ACCOUNT (id, balance) values (0, 0);'"
# args="$args --query='select * from ACCOUNT where id=1;'"
# args="$args --query='update ACCOUNT set balance=balance+1 where id=1;'"
# #sql
# #store procedure
# args="$args --query='call create_account(@id, @count);'"
# args="$args --query='call update_balance(1, 0, @balance);'"
# args="$args --query='call increment_balance(1, 1, @balance);'"
# args="$args --query='call get_balance(1, @balance);'"
# #store procedure
#
###################################### Temp Mysql ###############################################


##################################### Temp configs Java ########################################
#-----------------------------------------------------------
#args=""
#args=$args" -server"
#args=$args" -ea"
#args=$args" -Xms512m -Xmx512m -Xss256m" # min mem
#args=$args" -Xms512m -Xmx4g -Xss1024m" # max mem
#args=$args" -Xms64m -Xmx512m -Xss64m" # max mem
#args=$args" -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:FlightRecorderOptions=defaultrecording=true" #  JFR
#-ea -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=duration=60s,filename=myrecording.jfr
#args=$args" -XX:+PrintCommandLineFlags"
#args=$args" -XX:+AggressiveOpts" # JIT
#args=$args" -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/java_pid%p.hprof" # not working
#args=$args" -XX:+UnlockDiagnosticVMOptions -XX:LogFile=/tmp/hotspot.log" # not working
#args=$args" -XX:+PrintConcurrentLocks"
#args=$args" -XX:+PrintClassHistogram"
#args=$args" -XX:+UnlockDiagnosticVMOptions"
#args=$args" -XX:+AggressiveHeap"
#args=$args" -XX:CMSExpAvgFactor=15"
#args=$args" -XX:ConcGCThreads=2"
#args=$args" -XX:+ExplicitGCInvokesConcurrent -XX:+UseConcMarkSweepGC"
#args=$args" -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly"
#args=$args" -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails"
#args=$args" -XX:+PrintGCApplicationConcurrentTime"
#args=$args" -XX:+UseConcMarkSweepGC"
#args=$args" -XX:+UseG1GC"
#args=$args" -XX:+UseParallelGC"
#args=$args" -XX:+UseSerialGC"
#args=$args" -XX:+UseSHM"
#args=$args" "
#args=$args" -d64 -server -XX:+AggressiveOpts -XX:+UseLargePages -Xmn10g  -Xms26g -Xmx26g" # Tuning for Higher Throughput
#args=$args" -d64 -XX:+UseG1GC -Xms26g Xmx26g -XX:MaxGCPauseMillis=500 -XX:+PrintGCTimeStamp" # Tuning for Lower Response Time
#args=$args" -d64 -server -XX:+AggressiveOpts -Xmn1g  -Xms4g -Xmx4g" # Tuning for Higher Throughput
#args=" -d64 -XX:+UseG1GC -Xms1g -Xmx5g -XX:MaxGCPauseMillis=500" # Tuning for Lower Response Time
#-----------------------------------------------------------
#-----------------------------------------------------------
#dpath=""
#dpath=$dpath$root_dir/lib
#dpath=$dpath" "$root_dir/target/test-classes
#dpath=`find $dpath -type f`
#dpath=`echo $dpath | sed -e "s/ /\/:/g"`
#dpath=$dpath"/*"
#echo $dpath
#java org.junit.runner.JUnitCore ProducerAndConsumerTest
#java -cp $root_dir/target/JavaCore.jar ProducerAndConsumerTest
# java -cp .:/usr/share/java/junit.jar org.junit.runner.JUnitCore ProducerAndConsumerTest [test class name]
#java -cp "Test.jar:lib/*" my.package.MainClass
#javac -cp .:junit-4.12.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore NameOfTheClass.java
#java -cp .:/$root_dir/lib/junit-4.12.jar:$root_dir/lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore ProducerAndConsumerTest
#java -cp .:/$root_dir/lib/junit-4.12.jar:$root_dir/lib/hamcrest-core-1.3.jar:$root_dir/target/test-classes/** org.junit.runner.JUnitCore  ProducerAndConsumerTest
#export CLASSPATH=".:/$root_dir/lib/junit-4.12.jar:$root_dir/lib/hamcrest-core-1.3.jar:$root_dir/target/test-classes/**"
#dpath=`find $root_dir/target/test-classes -type d | sed -e "s/^/:/g" | sed -e "s/[[:blank:]]//g"`
#export CLASSPATH=".:/$root_dir/lib/*:$root_dir/target/test-classes/*"
#java org.junit.runner.JUnitCore 'ProducerAndConsumerTest'
#find $root_dir/target/test-classes -type d | sed -e "s/.$/:/g"
#expor MAVEN_OPTS="-Xms128m -Xmx256m"
#pushd /media/shared/dev/edd/jobs/tests/edd/JavaCore
#mvn clean test -Dtest=ProducerAndConsumerTest#test_Producer_Consumer_2
#popd
#mvn test-compile
#dpath=`find $root_dir/target/test-classes -type d`
#dpath=`sed -e "s/media/:media/g" <<< $dpath`
#dpath=`find $root_dir/target/test-classes -type d | sed -e "s/^/:/g" | sed -e "s/media/xxx/g"`
#dpath=`find $root_dir/target/test-classes -type d | sed -e "s/^/:/g" | sed -e "s/.$//g"`
#dpath=`find $root_dir/target/test-classes -type d | sed -e "s/.$//g"`
#dpath=`find $root_dir/target/test-classes -type d | sed -e "s/.$/xxx/g" | sed -e "s/^/:/g" | sed -e "s/ /:/g"`
#dpath=`find $root_dir/target/test-classes -type d`
#dpath=".:"
#dpath=$dpath`find $root_dir/lib $root_dir/target/test-classes -type d` 
#dpath=`echo $dpath | sed -e "s/^/:/g"`
#JUnitCore initializationError java.lang.IllegalArgumentException: Could not find class Caused by: java.lang.ClassNotFoundException:
#sed 's/.$/<replace with text>/'
#sed 's/\x0D$/<replace with text>/'
#sed 's/\r/<replace with text>/'
#-----------------------------------------------------------
###################################### Temp configs Java ###################################
############################################################################################



############################################################################################
###################################### Test Results ########################################
#####################  Test 1  ##################################
    ##-------------------- Jvm args ---------------------
    ##java.v=08
    #args=$args" -server"
    #args=$args" -ea"
    #args=$args" -Xms2g -Xmx4g -Xss128m"
    #args=$args" -XX:+PrintCommandLineFlags"
    #args=$args" -XX:+AggressiveHeap"
    #args=$args" -XX:+UseParallelGC"
    ##-------------------- Jvm args ---------------------

		#//----------------- Configs ---------------------------
		#// common config
		#//final long nElements = 1000000000L;
		#final long nElements = 100000L;
		#final long duration = TimeUnit.MINUTES.toNanos(1);
		#final long timeout = TimeUnit.SECONDS.toMillis(10);
		#final long reportDelay = TimeUnit.SECONDS.toMillis(1);
		#final int queueSize = 500000;
		#final BlockingQueue<Account> queue = new LinkedBlockingQueue<>(queueSize);
		#//final BlockingQueue<Account> queue 		=new ArrayBlockingQueue<>(queueSize);
		#// producer configq
		#final ExecutorBuilder.ExecutorType producerExecutorType = ExecutorBuilder.ExecutorType.ARRAY_BLOCKING_QUEUE;
		#final int producerCapacityQueue = 500000;
		#final int producerCorePoolSize = 2;
		#final int producerMaximumPoolSize = 4;
		#final long producerKeepAliveTime = Long.MAX_VALUE;
		#final long produceDelay = TimeUnit.SECONDS.toMillis(0);
		#// consumer config
		#final ExecutorBuilder.ExecutorType consumerExecutorType = ExecutorBuilder.ExecutorType.ARRAY_BLOCKING_QUEUE;
		#final int consumerCapacityQueue = 500000;
		#final int consumerCorePoolSize = 100;
		#final int consumerMaximumPoolSize = 200;
		#final long consumerKeepAliveTime = Long.MAX_VALUE;
		#final long consumeDelay = TimeUnit.MILLISECONDS.toNanos(0);
		#//----------------- Configs ---------------------------

		#[mysqld] # 5.6.36
		#bind-address													=0.0.0.0
		#port																	=3306
		#user																	=davinci
		#init_connect													='SET collation_connection = utf8_unicode_ci' 
		#init_connect													='SET NAMES utf8' 
		#character-set-server									=utf8 
		#collation-server											=utf8_unicode_ci 
		#skip_name_resolve 										=1
		#disable_log_bin												=1
		#max_connections 											=501
		#innodb_stats_persistent  							=OFF			#deafult=ON
		#innodb_stats_auto_recalc  						=OFF			#deafult=ON
		#default_storage_engine								=InnoDB
		#innodb_checksum_algorithm          		=crc32		#deafult=innodb
		#query_cache_size 											=0		 		#defaul=1M, 0 to disable 
		#query_cache_type											=OFF			#deafult=OFF
		#thread_cache_size											=16				#deafult=13
		#thread_concurrency                   	=16				#deafult=10
		#table_open_cache_instances          	=16				#deafult=1
		#innodb_write_io_threads 							=16				#deafult=4
		#innodb_read_io_threads 								=8 				#deafult=4 
		#innodb_concurrency_tickets   					=10000		#default=5000
		#innodb_thread_sleep_delay      				=10   		#default=10000
		#innodb_log_file_size 									=256M 		#deafult=48M
		#innodb_buffer_pool_size 							=4G				#deafult=124M
		#innodb_flush_log_at_trx_commit 				=2				#deafult=1
		#innodb_flush_method 									=O_DIRECT	#deafult=empty
		#innodb_use_native_aio 								=1  			#deafult=1
		#innodb_log_buffer_size 								=64M 			#deafult=8M
		#innodb_thread_concurrency							=16				#default=0 no limit
		#innodb_io_capacity             				=600  		#deafult=200
		#innodb_io_capacity_max        				=3000     #deafult=2000


		#updateUsingCallableStatement(account);
		#java.util.concurrent.ThreadPoolExecutor@7f0ae716[Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 100000]
		#Queue.size [0] @[00:00:40.177.695856] 2488.98/sec
		
		#updateUsingPreparedStatement(account);
		#java.util.concurrent.ThreadPoolExecutor@5a4b617a[Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 100000]
		#Queue.size [0] @[00:00:21.027.569712] 4755.79

#####################  Test 1  ##################################

#####################  Test 2  ##################################
    ##-------------------- Jvm args ---------------------
    ##java.v=08
    #args=$args" -server"
    #args=$args" -ea"
    #args=$args" -Xms2g -Xmx4g -Xss128m"
    #args=$args" -XX:+PrintCommandLineFlags"
    #args=$args" -XX:+AggressiveHeap"
    #args=$args" -XX:+UseParallelGC"
    ##-------------------- Jvm args ---------------------

		#//----------------- Configs ---------------------------
		#// common config
		#//final long nElements = 1000000000L;
		#final long nElements = 100000L;
		#final long duration = TimeUnit.MINUTES.toNanos(1);
		#final long timeout = TimeUnit.SECONDS.toMillis(10);
		#final long reportDelay = TimeUnit.SECONDS.toMillis(1);
		#final int queueSize = 500000;
		#final BlockingQueue<Account> queue = new LinkedBlockingQueue<>(queueSize);
		#//final BlockingQueue<Account> queue 		=new ArrayBlockingQueue<>(queueSize);
		#// producer configq
		#final ExecutorBuilder.ExecutorType producerExecutorType = ExecutorBuilder.ExecutorType.ARRAY_BLOCKING_QUEUE;
		#final int producerCapacityQueue = 500000;
		#final int producerCorePoolSize = 2;
		#final int producerMaximumPoolSize = 4;
		#final long producerKeepAliveTime = Long.MAX_VALUE;
		#final long produceDelay = TimeUnit.SECONDS.toMillis(0);
		#// consumer config
		#final ExecutorBuilder.ExecutorType consumerExecutorType = ExecutorBuilder.ExecutorType.ARRAY_BLOCKING_QUEUE;
		#final int consumerCapacityQueue = 500000;
		#final int consumerCorePoolSize = 100;
		#final int consumerMaximumPoolSize = 200;
		#final long consumerKeepAliveTime = Long.MAX_VALUE;
		#final long consumeDelay = TimeUnit.MILLISECONDS.toNanos(0);
		#//----------------- Configs ---------------------------

		#[mysqld] #8.0.11
		#bind-address													=0.0.0.0
		#port																	=3306
		#user																	=davinci
		#init_connect													='SET collation_connection = utf8_unicode_ci' 
		#init_connect													='SET NAMES utf8' 
		#character-set-server									=utf8 
		#collation-server											=utf8_unicode_ci 
		#skip_name_resolve 										=1
		#disable_log_bin												=1
		#max_connections 											=501
		#innodb_dedicated_server  							=ON				#deafult=OFF
		#innodb_stats_persistent  							=OFF			#deafult=ON
		#innodb_stats_auto_recalc  						=OFF			#deafult=ON
		#default_storage_engine								=InnoDB
		#innodb_checksum_algorithm          		=crc32		#deafult=innodb
		#thread_cache_size											=16				#deafult=13
		#table_open_cache_instances          	=16				#deafult=1
		#innodb_write_io_threads 							=16				#deafult=4
		#innodb_read_io_threads 								=8 				#deafult=4 
		#innodb_concurrency_tickets   					=10000		#default=5000
		#innodb_thread_sleep_delay      				=10   		#default=10000
		#innodb_log_file_size 									=256M 		#deafult=48M
		#innodb_buffer_pool_size 							=4G				#deafult=124M
		#innodb_flush_log_at_trx_commit 				=2				#deafult=1
		#innodb_flush_method 									=O_DIRECT	#deafult=fsync, O_DIRECT_NO_FSYNC
		#innodb_use_native_aio 								=1  			#deafult=1
		#innodb_log_buffer_size 								=64M 			#deafult=8M
		#innodb_thread_concurrency							=16				#default=0 no limit
		#innodb_io_capacity             				=600  		#deafult=200
		#innodb_io_capacity_max        				=3000     #deafult=2000
		
		#updateUsingCallableStatement(account);
		#java.util.concurrent.ThreadPoolExecutor@4d8d5ac3[Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 100000]
		#Queue.size [0] @[00:01:52.077.157595] 892.24/sec

		#updateUsingPreparedStatement(account);
		#java.util.concurrent.ThreadPoolExecutor@7f0ae716[Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 100000]
		#Queue.size [0] @[00:00:59.057.719392] 1693.27/sec
		
#####################  Test 2  ##################################

###################################### Test Results ########################################
############################################################################################




############################################################################################
###################################### Errors ########################################
#2018-06-04T13:33:10.798890Z	   12 Connect	edmond@127.0.0.1 on edmond using TCP/IP
#2018-06-04T13:33:10.803626Z	   12 Query	/* mysql-connector-java-8.0.11 (Revision: 6d4eaa273bc181b4cf1c8ad0821a2227f116fedf) */SELECT  @@session.auto_increment_increment AS auto_increment_increment, @@character_set_client AS character_set_client, @@character_set_connection AS character_set_connection, @@character_set_results AS character_set_results, @@character_set_server AS character_set_server, @@collation_server AS collation_server, @@init_connect AS init_connect, @@interactive_timeout AS interactive_timeout, @@license AS license, @@lower_case_table_names AS lower_case_table_names, @@max_allowed_packet AS max_allowed_packet, @@net_write_timeout AS net_write_timeout, @@sql_mode AS sql_mode, @@system_time_zone AS system_time_zone, @@time_zone AS time_zone, @@transaction_isolation AS transaction_isolation, @@wait_timeout AS wait_timeout
#2018-06-04T13:33:10.825739Z	   12 Query	SET character_set_results = NULL
#2018-06-04T13:33:10.826488Z	   12 Query	SELECT @@session.autocommit
#2018-06-04T13:33:10.829419Z	   12 Query	SET autocommit=1
#2018-06-04T13:33:10.838950Z	   12 Query	SET sql_mode='STRICT_TRANS_TABLES'
#2018-06-04T13:33:10.839258Z	   12 Query	SHOW WARNINGS
#2018-06-04T13:33:10.853080Z	   12 Quit	
#2018-06-04T13:33:10.872316Z	   13 Connect	edmond@127.0.0.1 on edmond using TCP/IP
#2018-06-04T13:33:10.872922Z	   13 Query	/* mysql-connector-java-8.0.11 (Revision: 6d4eaa273bc181b4cf1c8ad0821a2227f116fedf) */SELECT  @@session.auto_increment_increment AS auto_increment_increment, @@character_set_client AS character_set_client, @@character_set_connection AS character_set_connection, @@character_set_results AS character_set_results, @@character_set_server AS character_set_server, @@collation_server AS collation_server, @@init_connect AS init_connect, @@interactive_timeout AS interactive_timeout, @@license AS license, @@lower_case_table_names AS lower_case_table_names, @@max_allowed_packet AS max_allowed_packet, @@net_write_timeout AS net_write_timeout, @@sql_mode AS sql_mode, @@system_time_zone AS system_time_zone, @@time_zone AS time_zone, @@transaction_isolation AS transaction_isolation, @@wait_timeout AS wait_timeout
#2018-06-04T13:33:10.874272Z	   13 Query	SET character_set_results = NULL
#2018-06-04T13:33:10.874601Z	   13 Query	SELECT @@session.autocommit
#2018-06-04T13:33:10.874961Z	   13 Query	SET autocommit=1
#2018-06-04T13:33:10.875219Z	   13 Query	SET sql_mode='STRICT_TRANS_TABLES'
#2018-06-04T13:33:10.875444Z	   13 Query	SHOW WARNINGS
#2018-06-04T13:33:10.928981Z	   13 Query	SELECT SPECIFIC_SCHEMA AS PROCEDURE_CAT, NULL AS `PROCEDURE_SCHEM`,  SPECIFIC_NAME AS `PROCEDURE_NAME`, IFNULL(PARAMETER_NAME, '') AS `COLUMN_NAME`, CASE WHEN PARAMETER_MODE = 'IN' THEN 1 WHEN PARAMETER_MODE = 'OUT' THEN 4 WHEN PARAMETER_MODE = 'INOUT' THEN 2 WHEN ORDINAL_POSITION = 0 THEN 5 ELSE 0 END AS `COLUMN_TYPE`, CASE  WHEN UPPER(DATA_TYPE)='DECIMAL' THEN 3 WHEN UPPER(DATA_TYPE)='DECIMAL UNSIGNED' THEN 3 WHEN UPPER(DATA_TYPE)='TINYINT' THEN CASE WHEN LOCATE('(1)', DTD_IDENTIFIER) != 0 THEN -7 ELSE -6 END  WHEN UPPER(DATA_TYPE)='TINYINT UNSIGNED' THEN CASE WHEN LOCATE('(1)', DTD_IDENTIFIER) != 0 THEN -7 ELSE -6 END  WHEN UPPER(DATA_TYPE)='BOOLEAN' THEN 16 WHEN UPPER(DATA_TYPE)='SMALLINT' THEN 5 WHEN UPPER(DATA_TYPE)='SMALLINT UNSIGNED' THEN 5 WHEN UPPER(DATA_TYPE)='INT' THEN 4 WHEN UPPER(DATA_TYPE)='INT UNSIGNED' THEN 4 WHEN UPPER(DATA_TYPE)='FLOAT' THEN 7 WHEN UPPER(DATA_TYPE)='FLOAT UNSIGNED' THEN 7 WHEN UPPER(DATA_TYPE)='DOUBLE' THEN 8 WHEN UPPER(DATA_TYPE)='DOUBLE UNSIGNED' THEN 8 WHEN UPPER(DATA_TYPE)='NULL' THEN 0 WHEN UPPER(DATA_TYPE)='TIMESTAMP' THEN 93 WHEN UPPER(DATA_TYPE)='BIGINT' THEN -5 WHEN UPPER(DATA_TYPE)='BIGINT UNSIGNED' THEN -5 WHEN UPPER(DATA_TYPE)='MEDIUMINT' THEN 4 WHEN UPPER(DATA_TYPE)='MEDIUMINT UNSIGNED' THEN 4 WHEN UPPER(DATA_TYPE)='DATE' THEN 91 WHEN UPPER(DATA_TYPE)='TIME' THEN 92 WHEN UPPER(DATA_TYPE)='DATETIME' THEN 93 WHEN UPPER(DATA_TYPE)='YEAR' THEN 91 WHEN UPPER(DATA_TYPE)='VARCHAR' THEN 12 WHEN UPPER(DATA_TYPE)='VARBINARY' THEN -3 WHEN UPPER(DATA_TYPE)='BIT' THEN -7 WHEN UPPER(DATA_TYPE)='JSON' THEN -1 WHEN UPPER(DATA_TYPE)='ENUM' THEN 1 WHEN UPPER(DATA_TYPE)='SET' THEN 1 WHEN UPPER(DATA_TYPE)='TINYBLOB' THEN -3 WHEN UPPER(DATA_TYPE)='TINYTEXT' THEN 12 WHEN UPPER(DATA_TYPE)='MEDIUMBLOB' THEN -4 WHEN UPPER(DATA_TYPE)='MEDIUMTEXT' THEN -1 WHEN UPPER(DATA_TYPE)='LONGBLOB' THEN -4 WHEN UPPER(DATA_TYPE)='LONGTEXT' THEN -1 WHEN UPPER(DATA_TYPE)='BLOB' THEN -4 WHEN UPPER(DATA_TYPE)='TEXT' THEN -1 WHEN UPPER(DATA_TYPE)='CHAR' THEN 1 WHEN UPPER(DATA_TYPE)='BINARY' THEN -2 WHEN UPPER(DATA_TYPE)='GEOMETRY' THEN -2 WHEN UPPER(DATA_TYPE)='UNKNOWN' THEN 1111 WHEN UPPER(DATA_TYPE)='POINT' THEN -2 WHEN UPPER(DATA_TYPE)='LINESTRING' THEN -2 WHEN UPPER(DATA_TYPE)='POLYGON' THEN -2 WHEN UPPER(DATA_TYPE)='MULTIPOINT' THEN -2 WHEN UPPER(DATA_TYPE)='MULTILINESTRING' THEN -2 WHEN UPPER(DATA_TYPE)='MULTIPOLYGON' THEN -2 WHEN UPPER(DATA_TYPE)='GEOMETRYCOLLECTION' THEN -2 WHEN UPPER(DATA_TYPE)='GEOMCOLLECTION' THEN -2 ELSE 1111 END  AS `DATA_TYPE`,  UPPER(CASE WHEN LOCATE('UNSIGNED', UPPER(DATA_TYPE)) != 0 AND LOCATE('UNSIGNED', UPPER(DATA_TYPE)) = 0 THEN CONCAT(DATA_TYPE, ' UNSIGNED') ELSE DATA_TYPE END) AS `TYPE_NAME`, NUMERIC_PRECISION AS `PRECISION`, CASE WHEN LCASE(DATA_TYPE)='date' THEN 10 WHEN LCASE(DATA_TYPE)='time' THEN 8 WHEN LCASE(DATA_TYPE)='datetime' THEN 19 WHEN LCASE(DATA_TYPE)='timestamp' THEN 19 WHEN CHARACTER_MAXIMUM_LENGTH IS NULL THEN NUMERIC_PRECISION WHEN CHARACTER_MAXIMUM_LENGTH > 2147483647 THEN 2147483647 ELSE CHARACTER_MAXIMUM_LENGTH END AS LENGTH,NUMERIC_SCALE AS `SCALE`, 10 AS RADIX,1 AS `NULLABLE`, NULL AS `REMARKS`, NULL AS `COLUMN_DEF`, NULL AS `SQL_DATA_TYPE`, NULL AS `SQL_DATETIME_SUB`, CHARACTER_OCTET_LENGTH AS `CHAR_OCTET_LENGTH`, ORDINAL_POSITION, 'YES' AS `IS_NULLABLE`, SPECIFIC_NAME FROM INFORMATION_SCHEMA.PARAMETERS WHERE SPECIFIC_SCHEMA LIKE 'edmond' AND SPECIFIC_NAME LIKE 'update_balance' AND (PARAMETER_NAME LIKE '%' OR PARAMETER_NAME IS NULL) ORDER BY SPECIFIC_SCHEMA, SPECIFIC_NAME, ROUTINE_TYPE, ORDINAL_POSITION
#2018-06-04T13:33:10.959418Z	   13 Query	call update_balance(1, 333)

#mysqlslap: [Warning] Using a password on the command line interface can be insecure. 8.0.11
#Benchmark
	#Average number of seconds to run all queries: 40.998 seconds
	#Minimum number of seconds to run all queries: 35.516 seconds
	#Maximum number of seconds to run all queries: 46.481 seconds
	#Number of clients running queries: 10
	#Average number of queries per client: 10000


#mysqlslap: [Warning] Using a password on the command line interface can be insecure. 5.6.36
#Benchmark
	#Average number of seconds to run all queries: 8.737 seconds
	#Minimum number of seconds to run all queries: 8.700 seconds
	#Maximum number of seconds to run all queries: 8.774 seconds
	#Number of clients running queries: 10
	#Average number of queries per client: 10000


#180604 23:52:09	    5 Connect	edmond@127.0.0.1 on edmond
		    #5 Query	/* mysql-connector-java-8.0.11 (Revision: 6d4eaa273bc181b4cf1c8ad0821a2227f116fedf) */SELECT  @@session.auto_increment_increment AS auto_increment_increment, @@character_set_client AS character_set_client, @@character_set_connection AS character_set_connection, @@character_set_results AS character_set_results, @@character_set_server AS character_set_server, @@collation_server AS collation_server, @@init_connect AS init_connect, @@interactive_timeout AS interactive_timeout, @@license AS license, @@lower_case_table_names AS lower_case_table_names, @@max_allowed_packet AS max_allowed_packet, @@net_write_timeout AS net_write_timeout, @@query_cache_size AS query_cache_size, @@query_cache_type AS query_cache_type, @@sql_mode AS sql_mode, @@system_time_zone AS system_time_zone, @@time_zone AS time_zone, @@tx_isolation AS transaction_isolation, @@wait_timeout AS wait_timeout
		    #5 Query	SET character_set_results = NULL
		    #5 Query	SELECT @@session.autocommit
		    #5 Query	SET autocommit=1
		    #5 Query	SET sql_mode='NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES'
		    #5 Quit	
		    #6 Connect	edmond@127.0.0.1 on edmond
		    #6 Query	/* mysql-connector-java-8.0.11 (Revision: 6d4eaa273bc181b4cf1c8ad0821a2227f116fedf) */SELECT  @@session.auto_increment_increment AS auto_increment_increment, @@character_set_client AS character_set_client, @@character_set_connection AS character_set_connection, @@character_set_results AS character_set_results, @@character_set_server AS character_set_server, @@collation_server AS collation_server, @@init_connect AS init_connect, @@interactive_timeout AS interactive_timeout, @@license AS license, @@lower_case_table_names AS lower_case_table_names, @@max_allowed_packet AS max_allowed_packet, @@net_write_timeout AS net_write_timeout, @@query_cache_size AS query_cache_size, @@query_cache_type AS query_cache_type, @@sql_mode AS sql_mode, @@system_time_zone AS system_time_zone, @@time_zone AS time_zone, @@tx_isolation AS transaction_isolation, @@wait_timeout AS wait_timeout
		    #6 Query	SET character_set_results = NULL
		    #6 Query	SELECT @@session.autocommit
		    #6 Query	SET autocommit=1
		    #6 Query	SET sql_mode='NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES'
		    #6 Query	SELECT name, type, comment FROM mysql.proc WHERE db <=> 'edmond' AND name LIKE 'update_balance' ORDER BY name, type
		    #6 Query	SHOW CREATE PROCEDURE `edmond`.`update_balance`
		    #6 Query	call update_balance(1, 333)

###################################### Errors ########################################
############################################################################################


#Benchmark
	#Average number of seconds to run all queries: 1.440 seconds
	#Minimum number of seconds to run all queries: 1.440 seconds
	#Maximum number of seconds to run all queries: 1.440 seconds
	#Number of clients running queries: 100
	#Average number of queries per client: 100 6944.44/sec

#Benchmark
	#Average number of seconds to run all queries: 109.457 seconds
	#Minimum number of seconds to run all queries: 109.457 seconds
	#Maximum number of seconds to run all queries: 109.457 seconds
	#Number of clients running queries: 50
	#Average number of queries per client: 20000 9136.00/sec

#Benchmark
	#Average number of seconds to run all queries: 137.849 seconds
	#Minimum number of seconds to run all queries: 137.849 seconds
	#Maximum number of seconds to run all queries: 137.849 seconds
	#Number of clients running queries: 100
	#Average number of queries per client: 10000 7254.31/sec

#Benchmark
	#Average number of seconds to run all queries: 130.752 seconds
	#Minimum number of seconds to run all queries: 130.752 seconds
	#Maximum number of seconds to run all queries: 130.752 seconds
	#Number of clients running queries: 100
	#Average number of queries per client: 10000 7648.06/sec


#Benchmark
	#Average number of seconds to run all queries: 97.694 seconds
	#Minimum number of seconds to run all queries: 97.694 seconds
	#Maximum number of seconds to run all queries: 97.694 seconds
	#Number of clients running queries: 50
	#Average number of queries per client: 20000  10,236.04/sec

#Benchmark
	#Average number of seconds to run all queries: 96.687 seconds
	#Minimum number of seconds to run all queries: 96.687 seconds
	#Maximum number of seconds to run all queries: 96.687 seconds
	#Number of clients running queries: 25
	#Average number of queries per client: 40000 10,342.65/sec

#Benchmark
	#Average number of seconds to run all queries: 92.269 seconds
	#Minimum number of seconds to run all queries: 92.269 seconds
	#Maximum number of seconds to run all queries: 92.269 seconds
	#Number of clients running queries: 16
	#Average number of queries per client: 62500  10,837.87/sec

#Benchmark
	#Average number of seconds to run all queries: 88.109 seconds
	#Minimum number of seconds to run all queries: 88.109 seconds
	#Maximum number of seconds to run all queries: 88.109 seconds
	#Number of clients running queries: 8
	#Average number of queries per client: 125000  11,349.57/sec

#Benchmark
	#Average number of seconds to run all queries: 79.499 seconds
	#Minimum number of seconds to run all queries: 79.499 seconds
	#Maximum number of seconds to run all queries: 79.499 seconds
	#Number of clients running queries: 4
	#Average number of queries per client: 250000   12,578.77/sec

#Benchmark
	#Average number of seconds to run all queries: 76.570 seconds
	#Minimum number of seconds to run all queries: 76.570 seconds
	#Maximum number of seconds to run all queries: 76.570 seconds
	#Number of clients running queries: 4
	#Average number of queries per client: 250000  13,059.94/sec


#Queue.size [0] @[00:05:24.149.134797] 3086.42/sec
#java.util.concurrent.ThreadPoolExecutor@75535e0[Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 1000000]

#Queue.size [0] @[00:04:57.321.72887]  3367.00/sec
#java.util.concurrent.ThreadPoolExecutor@4846f383[Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 1000000]

#Queue.size [0] @[00:04:25.190.835868] 3773.58/sec 
#java.util.concurrent.ThreadPoolExecutor@4846f383[Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 1000000]

#Queue.size [0] @[00:04:20.126.598061] 3846.15/sec thread pool [4, 8] ram 
#java.util.concurrent.ThreadPoolExecutor@75535e0[Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 1000000]

#Queue.size [0] @[00:04:40.234.714897] thread pool [4, 8] disk
#java.util.concurrent.ThreadPoolExecutor@75535e0[Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 1000000]

