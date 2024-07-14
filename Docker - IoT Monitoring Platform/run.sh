#! /bin/bash

if [[ -z "${SPRC_DVP}" ]]; then
	echo "Please choose the folder where you want docker volumes to be saved starting from terminal's folder or by using the absolute path (don't add '/' at the end):"
	read dvp
	export SPRC_DVP="$dvp"
else
	echo "Docker volume is set to: ${SPRC_DVP}, would you like to change it?(y/n)"
	read response
	
	if [[ "$response" == "y" ]]; then
		echo "Please choose the folder where you want docker volumes to be saved starting from terminal's folder or by using the absolute path (don't add '/' at the end):"
		read dvp
		export SPRC_DVP="$dvp"
	fi	
fi

echo "SPRC_DVP = $SPRC_DVP"
mkdir -p "${SPRC_DVP}/influxdb"
mkdir -p "${SPRC_DVP}/grafana"

chmod 777 "${SPRC_DVP}/grafana"


docker-compose -f stack.yml build
docker swarm init
docker stack deploy -c stack.yml sprc3
