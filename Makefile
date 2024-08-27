postgres:
	docker run --name postgress16 -p 5432:5432 -e POSTGRES_USER=root -e POSTGRES_PASSWORD=secret -d postgres:16.3
createdb:
	docker exec -it postgress16 createdb --username=root --owner=root shortstop
dropdb:
	docker exec -it postgress16 dropdb shortstop

.PHONY:postgres createdb dropdb