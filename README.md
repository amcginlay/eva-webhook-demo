# eva-webhook-demo

## Pre-requisites

- PCF instance has Healthwatch installed
- PCF instance has Event Alerts installed
- CF cli has Event Alerts plugin installed

## Review the webhook code

From `src/main/java/io/pivotal/evawebhookdemo/MainController.java`

```java
@RestController
public class MainController {
    @PostMapping
    public void webhook(@RequestBody String values) {
        System.out.println("PAYLOAD: " + values);
    }
}
```

## View the available publishers, topics, targets and subscriptions

```bash
cf eva-publishers
cf eva-topics
cf eva-targets
cf eva-subscriptions
```

## Clone and test this repo

```bash
git clone https://github.com/amcginlay/eva-webhook-demo.git
cd eva-webhook-demo
./gradlew bootRun
```

```bash
curl -XPOST -H"Content-Type: application/json" http://localhost:8080 -d "{\"NAME\": \"VALUE\"}"
```

## Build artifact and push to an internal route on Cloud Foundry

```bash
cf domains # inspect your domains
DOMAIN=apps.<YOUR_DOMAIN>
HTTP_PORT=80

./gradlew clean build
cf push eva-webhook-demo -p ./build/libs/eva-webhook-demo-0.0.1-SNAPSHOT.jar -d ${DOMAIN}
```

## Create an Event Alerts target and subscription

```bash
cf eva-create-target demo-target webhook http://eva-webhook-demo.${DOMAIN}:${HTTP_PORT} # not https!
cf eva-subscribe demo-target healthwatch --topics rep.unhealthycell
```

Review these using `cf eva-targets` and `cf eva-subscriptions`

## Listen to the application logs

```bash
cf logs eva-webhook-demo | grep "PAYLOAD"
```

## Simulate an event while tailing the application logs
```bash
cf eva-sample-publish healthwatch rep.unhealthycell
```

## Using apps.internal domain (untested)

If you want to deploy on `apps.internal` you'll need to set a network policy beween the 
`event-alerts` app (in system/event-alerts) and your webhook app.
Additionally, you'll need to set the HTTP_PORT variable to 8080.

See [this](https://github.com/amcginlay/pcf-c2c-networking) for more info
