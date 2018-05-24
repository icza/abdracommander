@echo off
cd "%~dp0"
set PATH=c:\Program Files (x86)\Java\jre7\bin\;c:\Program Files\Java\jre7\bin\;%PATH%
java -Xmx512m -Dcom.abdracmd.launched-with=abdracmd-win.cmd -cp lib/abdracmd.jar -splash:lib/splash.jpg com/abdracmd/AbdraCmd %*
