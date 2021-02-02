REM https://stackoverflow.com/questions/48813286/stop-all-docker-containers-at-once-on-windows
@ECHO OFF
FOR /f "tokens=*" %%i IN ('docker ps -q') DO docker stop %%i
