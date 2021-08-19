#pg_dump -Fc gep > /tmp/gep.db
#scp -i ~/gsd.pem /tmp/geb.db ubuntu@3.106.137.187:/tmp/
#pg_restore -d gep /tmp/geb.db
./gradlew -Pprod clean bootJar
rsync -az --delete --progress -e "ssh -C -i ~/gsd.pem" . ubuntu@3.106.137.187:/home/ubuntu/gep/
echo Finished.

