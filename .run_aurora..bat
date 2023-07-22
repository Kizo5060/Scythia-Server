@echo off
title Running Aurora!
java -server -Xmx2500m -cp bin;lib/* com.ruseps.GameServer
pause