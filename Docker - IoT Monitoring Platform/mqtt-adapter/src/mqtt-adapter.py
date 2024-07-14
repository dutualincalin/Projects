import json
import os
from datetime import datetime

import paho.mqtt.client as mqtt
from dateutil import tz
from influxdb import InfluxDBClient


def on_connect(connecting_client, userdata, flags, rc):
    client.subscribe("#")


def on_message(client, userdata, msg):
    # Get message on JSON format
    try:
        jsonified_payload = json.loads(msg.payload)
    except ValueError:
        return

    location, station = msg.topic.split('/')
    database_payload = []

    # Check if json payload is ok
    if not jsonified_payload:
        return

    # Get payload timestamp if it exists or actual timestamp
    if "timestamp" in jsonified_payload:
        data_timestamp = datetime.strptime(jsonified_payload["timestamp"], '%Y-%m-%dT%H:%M:%S%z')
    else:
        data_timestamp = datetime.now(tz=tz.tzlocal())

    # Get payload fields
    for key, value in jsonified_payload.items():
        if isinstance(value, (int, float)):
            database_payload.append({
                "measurement": station + '.' + key,
                "tags": {
                    "location": location,
                    "station": station
                },

                "time": data_timestamp.strftime('%Y-%m-%dT%H:%M:%S%z'),
                "fields": {
                    "value": float(value)
                },
            })

    if len(database_payload) == 0:
        return

    # Write logs in console if DEBUG_DATA_FLOW variable is true
    if "DEBUG_DATA_FLOW" in os.environ and os.environ["DEBUG_DATA_FLOW"] == "true":
        log_timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S ")
        print(log_timestamp + "Received message by topic [" + msg.topic + "]")

        if "timestamp" in jsonified_payload:
            print(log_timestamp + "Data timestamp is: "
                  + data_timestamp.isoformat().replace("T", " "))

        else:
            print(log_timestamp + "Data timestamp is NOW")

        for key, value in jsonified_payload.items():
            if key != "timestamp" and isinstance(value, (int, float)):
                print(log_timestamp + station + '.' + location + '.' + key + " " + str(value))

        print("")

    # Write in database
    db_client.write_points(database_payload)


# Initialise InfluxDB connection
db_client = InfluxDBClient(
    host='influxdb',
    port=8086,
)
db_client.switch_database('iot_data')

# Initialise MQTT client
client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message

# Connect to MQTT server
client.connect('mosquitto', 1883, 60)
client.loop_forever()


