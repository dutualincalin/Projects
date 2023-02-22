FROM python:3.6
COPY requirements.txt /tmp
RUN pip install -U setuptools
RUN pip install -r /tmp/requirements.txt
COPY /src /app
WORKDIR /app
CMD ["python", "-u", "mqtt-adapter.py"]
