#!/bin/sh

java -server -Xms1516M -Xmx1516M -Xmn384M -XX:PermSize=192M -XX:MaxPermSize=192M -Xss256K -XX:+DisableExplicitGC -XX:SurvivorRatio=1 -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSClassUnloadingEnabled -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:SoftRefLRUPolicyMSPerMB=0 -XX:CMSInitiatingOccupancyFraction=72 -jar gameServer.jar
