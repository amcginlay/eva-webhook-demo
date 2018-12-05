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

## Run the code

If Java SDK is present:

```bash
git clone https://github.com/amcginlay/eva-webhook-demo.git
cd eva-webhook-demo
./gradlew bootRun
```

If Java SDK is not present:

```bash
wget https://github.com/amcginlay/eva-webhook-demo/releases/download/0.0.1-SNAPSHOT/eva-webhook-demo-0.0.1-SNAPSHOT.jar
java -jar eva-webhook-demo-0.0.1-SNAPSHOT.jar
```

## Exercise the endpoint

```bash
curl -XPOST -H"Content-Type: application/json" http://localhost:8080 -d "{\"NAME\": \"VALUE\"}"
```

## Build artifact and push to an internal route on Cloud Foundry



```bash
cf domains # inspect your domains
DOMAIN=apps.<YOUR_DOMAIN>

./gradlew clean build
cf push eva-webhook-demo -p ./build/libs/eva-webhook-demo-0.0.1-SNAPSHOT.jar -d ${DOMAIN}
```

## Create an Event Alerts target and subscription

```bash
cf eva-create-target demo-target webhook http://eva-webhook-demo.${DOMAIN} # not https!
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
