cd ./order-server
gradle clean build -x test
rem -Dspring.profiles.active=kubernates -x test
cd ..
