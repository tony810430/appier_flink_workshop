up:
	@docker-compose up -d
	@docker-compose exec jobmanager chmod 777 /savepoints
	@docker-compose exec taskmanager chmod 777 /savepoints

down:
	@docker-compose down -v

scale:
	@docker-compose up -d --scale taskmanager=${NUM} --no-recreate taskmanager
	@docker-compose exec taskmanager chmod 777 /savepoints
