# xd-firehose

A spring-xd module that consumes messages from [doppler](https://github.com/cloudfoundry/loggregator) firehose endpoint.

## Building and installing

	   $./gradlew clean test bootRepackage
	
Deploy the jar file to spring XD:

```
 _____                           __   _______
/  ___|          (-)             \ \ / /  _  \
\ `--. _ __  _ __ _ _ __   __ _   \ V /| | | |
 `--. \ '_ \| '__| | '_ \ / _` |  / ^ \| | | |
/\__/ / |_) | |  | | | | | (_| | / / \ \ |/ /
\____/| .__/|_|  |_|_| |_|\__, | \/   \/___/
      | |                  __/ |
      |_|                 |___/
eXtreme Data
1.1.0.RELEASE | Admin Server Target: http://localhost:9393
Welcome to the Spring XD shell. For assistance hit TAB or type "help".
xd:>module upload --file <PROJECT_DIR>/build/libs/firehose-1.0.0.BUILD-SNAPSHOT.jar --name firehose --type source

```

You can now list the arguments you need to pass to the firehose:

```
module info source:firehose
Information about source module 'firehose':

  Option Name    Description                                       Default                           Type
  -------------  ------------------------------------------------  --------------------------------  --------
  dopplerUrl     Doppler WSS url                                   wss://doppler.10.244.0.34.xip.io  String
  cfAccessToken  JWT Access Token                                  ""                                String
  outputType     how this module should emit messages it produces  <none>                            MimeType

```



##Pre requisites:

You will need access to a recent CF install [Deploying CF with bosh-lite](https://github.com/cloudfoundry/bosh-lite/blob/master/docs/deploy-cf.md).

Follow the instructions on how to enable firehose access to your admin token [here](https://github.com/cloudfoundry/loggregator#adding-scope-to-a-running-cluster-via-uaac) or even a better one from [CloudCredo](http://www.cloudcredo.com/cloud-foundry-firehose-and-friends/)

Now that you have your CF token, just deploy the firehose:

```
xd:>stream create --name firehose2log --definition "firehose --cfAccessToken='bearer eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiI2NzYzNmU1Ni03MzY3LTQxYTQtOWI0ZC00MDNkNzU2YTEwODIiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbInBhc3N3b3JkLndyaXRlIiwic2NpbS53cml0ZSIsImNsaWVudHMud3JpdGUiLCJjbGllbnRzLnJlYWQiLCJzY2ltLnJlYWQiLCJ1YWEuYWRtaW4iLCJjbGllbnRzLnNlY3JldCIsImRvcHBsZXIuZmlyZWhvc2UiXSwic2NvcGUiOlsic2NpbS5yZWFkIiwidWFhLmFkbWluIiwicGFzc3dvcmQud3JpdGUiLCJzY2ltLndyaXRlIiwiY2xpZW50cy53cml0ZSIsImNsaWVudHMucmVhZCIsImNsaWVudHMuc2VjcmV0IiwiZG9wcGxlci5maXJlaG9zZSJdLCJjbGllbnRfaWQiOiJhZG1pbiIsImNpZCI6ImFkbWluIiwiYXpwIjoiYWRtaW4iLCJncmFudF90eXBlIjoiY2xpZW50X2NyZWRlbnRpYWxzIiwiaWF0IjoxNDI2NjgzNjc0LCJleHAiOjE0MjY3MjY4NzQsImlzcyI6Imh0dHBzOi8vdWFhLjEwLjI0NC4wLjM0LnhpcC5pby9vYXV0aC90b2tlbiIsImF1ZCI6WyJhZG1pbiIsInNjaW0iLCJ1YWEiLCJwYXNzd29yZCIsImNsaWVudHMiLCJkb3BwbGVyIl19.Un9D5wAP7f8XRUWhPi6HEa5lijvufJqqNTsS6zYmT0KtjYqaJdyul0Hjxcy35DLyshxAti_8V5Edu9s_Xf9lcbBbGRmZg0wX87DCpmZih4q3Wh0Iip80FnWw90YuJ9SZrC0kjEhax00k027fvGCVml-IOhr11Fab5yID0b2jsNc' | log"
Created new stream 'firehose2log'
xd:>stream deploy --name firehose2log
Deployed stream 'firehose2log'
```
Your console now should display the metrics :

```
2015-03-18 09:02:50,231 1.1.0.RELEASE DEBUG WebSocketClient@1274731094-67 handler.LoggingHandler - org.springframework.integration.handler.LoggingHandler#0 received message: GenericMessage [payload=origin: "syslog_drain_binder"
eventType: Heartbeat
heartbeat {
  sentCount: 2214
  receivedCount: 2214
  errorCount: 0
  controlMessageIdentifier {
    low: 13712409313612728631
    high: 6638444458909832046
  }
}
timestamp: 1426683770230759660
, headers={simpMessageType=MESSAGE, content-length=66, id=aefc34d2-6058-6056-530a-d189d3290bad, simpSessionAttributes={}, simpSessionId=6b79796b, timestamp=1426683770231}]
```

##Issues

It seems that only one websocket session can be active per token. If you run into errors on websocket client complaining about *Didn't switch protocol* the real issue is a 401 sent from doppler. Try deleting the token, and getting a new one

