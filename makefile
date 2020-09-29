all:
	rm nifi-postimage-nar-1.1.nar -f
	mvn clean install -DskipTests
	cp nifi-postimage-nar/target/nifi-postimage-nar-1.1.nar ./

login:
	docker exec -it --user root nifipostimageprocessor_nifi_1 /bin/bash

clean:
	docker-compose rm -f
	docker volume prune -f
	docker-compose build
	docker-compose up
