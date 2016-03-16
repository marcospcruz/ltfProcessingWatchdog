echo off

set app_folder=sgmp


net stop sgmp

d:

cd \sicat\app\server\%app_folder%

rem * * * echo y | rmdir log /S
echo y | rmdir tmp /S
echo y | rmdir work /S
echo y | rmdir data /S

net start sgmp
rem * * * exit
pause