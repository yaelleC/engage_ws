Engage Web Services
===================

# PreRequisits 

You need to have MySQL running locally

	docker run --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=pwd -d mysql


# Build the  container

First you need to checkout the git repo
	
	git clone https://github.com/yaelleUWS/engage_ws.git

Then Build the container
	
	docker build -t yaelle/engage-ws .

# Fetch the Container

To download the latest version of the app:
	
	docker pull yaelle/engage-ws

To start the web-services

	docker run -it -p 8080:8080 --link mysql:mysql --name=webservice yaelle/engage-ws

Or run in background
	
	docker run -d -p 8080:8080 --link mysql:mysql --name=webservice yaelle/engage-ws

