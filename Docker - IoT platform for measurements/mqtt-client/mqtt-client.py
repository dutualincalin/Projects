import paho.mqtt.client as mqtt
from random import choice
from time import sleep

# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    print("Connected with result code " + str(rc))

    # Subscribing in on_connect() means that if we lose the connection and
    # reconnect then subscriptions will be renewed.
    client.subscribe("#")


# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    print(msg.topic + " " + str(msg.payload))


client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message

client.connect("localhost", 1883, 60)

# Blocking call that processes network traffic, dispatches callbacks and
# handles reconnecting.
# Other loop*() functions are available that give a threaded interface and a
# manual interface.
client.loop_start()

while True:
    battery_values = list(range(20, 90))
    humidity_values = list(range(5, 55))
    temperature_values = list(range(-5, 20))
    delay_values = [0.5, 1.0, 2.0]
    stations = ["RPI_1", "Dorinel e zeu", "Love SPRC"]

    while True:
        message = "{\"BAT\": " + str(choice(battery_values)) + ", " +\
                  "\"TEMP\": " + str(choice(temperature_values)) + ", " + \
                  "\"HUMID\": " + str(choice(humidity_values)) + "}"

        client.publish(topic="UPB/" + choice(stations), payload=message, qos=0, retain=False)
        sleep(delay_values)