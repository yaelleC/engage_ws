# Installation

1. Install docker using Boot2Docker

	You will need docker to run Runway into a container.

	https://github.com/boot2docker/boot2docker

2. Make a volume container (only need to do this once)
	
	On the DockerVM
	
	```
	docker run -v /opt --name engage_data busybox true
	```

3. Share it using Samba (Windows file sharing)

	On the DockerVM

	```
	docker run --rm -v /usr/local/bin/docker:/docker -v /var/run/docker.sock:/docker.sock svendowideit/samba engage_data
	```

4. Mount the samba drive on linux 
	
	On the DockerVM

	```
	sudo mkdir /mnt/data
	sudo mount -t cifs //192.168.59.103/opt /mnt/data -o gid=1000,uid=1000,username=guest
	```

5. Checkout your code in your container
	
	On Windows 

	Open \\192.168.59.103\opt
	Using Tortoise, checkout trunk of WebService in \\192.168.59.103\opt\WebService

# Install MySQL

	On the DockerVM

	```
	docker run --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=mysecretpassword123 -d mysql
	```

	On windows 
	
	The best way to manage mysql is to use MySQL workbench
	http://www.mysql.com/products/workbench/


# Build the  container
	
	On the DockerVM

	```
	cd /mnt/data/WebService/
	docker build -t webservice_img .
	```

# Run the Container
	
	On the DockerVM

	Solution 1: Run without any command and display output

	```
	docker run -it -p 8080:8080 --link mysql:mysql --name=webservice webservice_img
	```


	Solution 2: Run in background (when you want to run on the server)

	```
	docker run -d -p 8080:8080 --link mysql:mysql --name=webservice webservice_img
	```


	Solution 3: Run bash shell 

	```
	docker run -d -p 8080:8080 --link mysql:mysql --name=webservice webservice_img /bin/bash
	mvn clean install exec:exec
	```

	Solution 4: Share the code between container and host 

	```
	docker run -ti -p 8080:8080 --link mysql:mysql --name=webservice --rm --volumes-from engage_data webservice_img /bin/bash
	mvn clean install exec:exec
	```