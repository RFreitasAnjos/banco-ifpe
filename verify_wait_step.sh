set -e
cd /mnt/c/Users/r.freitas.dos.anjos/Documents/IFPE

if [ -f "banco-ifpe/pom.xml" ]; then
  PROJECT_DIR="banco-ifpe"
else
  PROJECT_DIR="."
fi
COMPOSE_FILE="$PROJECT_DIR/docker/docker-compose.yml"

echo "PROJECT_DIR=$PROJECT_DIR"
echo "COMPOSE_FILE=$COMPOSE_FILE"
test -s "$COMPOSE_FILE"

cd "$PROJECT_DIR"
mvn -B -DskipTests clean package >/tmp/mvn_package.log 2>&1
cd ..

mkdir -p war-artifact
cp "$PROJECT_DIR/target/banco-ifpe.war" war-artifact/banco-ifpe.war
mkdir -p "$PROJECT_DIR/target"
cp war-artifact/banco-ifpe.war "$PROJECT_DIR/target/banco-ifpe.war"

docker compose -f "$COMPOSE_FILE" down -v --remove-orphans || true
docker compose -f "$COMPOSE_FILE" build wildfly >/tmp/build_wildfly.log 2>&1
docker compose -f "$COMPOSE_FILE" up -d postgres

timeout 90 bash -c '
  until docker exec postgres_banco_ifpe pg_isready -U postgres; do
    sleep 3
  done
'

docker compose -f "$COMPOSE_FILE" up -d --no-deps wildfly
docker compose -f "$COMPOSE_FILE" ps

timeout 420 bash -c '
  until curl -sf http://localhost:8080/banco-ifpe/ -o /dev/null; do
    if ! docker ps --format "{{.Names}}" | grep -q "^wildfly_banco_ifpe$"; then
      echo "Container wildfly_banco_ifpe nao esta em execucao."
      docker compose -f "'$COMPOSE_FILE'" ps
      docker logs wildfly_banco_ifpe 2>&1 | tail -n 200 || true
      exit 1
    fi
    sleep 5
  done
'

echo "WAIT_STEP_OK"

docker compose -f "$COMPOSE_FILE" down -v --remove-orphans