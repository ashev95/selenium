chcp 65001
taskkill /f /im chromedriver.exe /fi "memusage gt 0" 2>NUL | findstr SUCCESS >NUL && if errorlevel 1 ( echo process was not killed ) else ( echo process was killed )