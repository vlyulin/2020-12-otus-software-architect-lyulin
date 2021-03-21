:loop
    c:\Utils\ab -n 50 -c 5 http://arch.homework/users
    timeout 5 > NUL
goto loop
