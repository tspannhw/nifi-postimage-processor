all:
	rm ./extensions/nifi-postimage-nar-1.1.nar -f
	docker run -v m2:/root/.m2/ -v ${PWD}:/data/ -it jlrigau/maven-git /usr/bin/mvn -f /data/ install -e
	cp nifi-postimage-nar/target/nifi-postimage-nar-1.1.nar ./extensions/

clean: 
	rm ./extensions/nifi-postimage-nar-1.1.nar -f
	docker run -v m2:/root/.m2/ -v ${PWD}:/data/ -it jlrigau/maven-git /usr/bin/mvn -f /data/ clean -e

docker:
	rm ./extensions/nifi-postimage-nar-1.1.nar -f
	docker run -v m2:/root/.m2/ -v ${PWD}:/data/ -it jlrigau/maven-git /usr/bin/mvn -f /data/ install -e
	cp nifi-postimage-nar/target/nifi-postimage-nar-1.1.nar ./extensions/
	chmod 777 ./extensions/ -R
	docker-compose stop
	docker-compose down
	docker-compose up
