# Anypoint Maven Plugin


## Configuration

### Parameters
| Name          | Type    | Description                                                  | Required |
| ------------- | ------- | ------------------------------------------------------------ | -------- |
| server        | string  | The anypoint credentials defined in the maven `settings.xml` | *false   |
| username      | string  | The anypoint username                                        | *false   |
| password      | string  | The anypoint password                                        | *false   |
| environment   | string  | The anypoint environment                                     | true     |
| organization  | string  | The anypoint business group                                  | true     |
| configuration | string  | The configuration file                                       | true     |
| skip          | boolean | When true skip the plugin execution, default to false        | false    |

  ---
  one of server or username and password must be configured

### Example

The example below shows the minimum required configuration for the plugin:

```xml
<plugin>
    <groupId>com.mariocairone.mule</groupId>
    <artifactId>anypoint-maven-plugin</artifactId>
    <version>1.0.0</version>
    <configuration>
        <server>anypoint</server>
        <organization>${organization}</organization>
        <environment>${environment}</environment>
        <configuration>apimanager.yaml</configuration>
    </configuration>
</plugin>		
```


### Configuration file


#### Global Schema
| Name                                                         | Type    | Description                                                   | Required |
| ------------------------------------------------------------ | ------- | ------------------------------------------------------------- | -------- |
| apimanager                                                   | object  | This is the configuration to apply in the API Manager         | true     |
| *apimanager.* environment                                    | array   | A collection of environment configurations                    | true     |
| *apimanager.* environment[]                                  | object  | A single environment configuration                            | true     |
| *apimanager.environment[].* name                             | string  | The environment name as defined in the Anypoint Platform      | true     |
| *apimanager.environment[].* api                              | object  | The API configuration                                         | true     |
| *apimanager.environment[].api.* id                           | integer | The API instance id                                           | true     |
| *apimanager.environment[].api.* properties                   | object  | The API properties                                            | false    |
| *apimanager.environment[].api.* alerts                       | array   | A collection of Alerts                                        | false    |
| *apimanager.environment[].api.* alerts[]                     | object  | A single Alert configuration.                                 | true     |
| *apimanager.environment[].api.alerts[].* name                | string  | The name of the Alert                                         | true     |
| *apimanager.environment[].api.* policies                     | array   | A collection of API policies.                                 | false    |
| *apimanager.environment[].api.* policies[]                   | object  | A single policy configuration                                 | true     |
| *apimanager.environment[].api.policies[].* configurationData | object  | The policy configuration data                                 | true     |
| *apimanager.environment[].api.* tiers                        | array   | A collection of API tiers                                     | false    |
| *apimanager.environment[].api.* tiers[]                      | object  | A single tier configuration.                                  | true     |
| *apimanager.environment[].api.tiers[].* name                 | string  | The name of the tier                                          | true     |
| *apimanager.environment[].api.* contracts                    | array   | A collection of API contracts                                 | false    |
| *apimanager.environment[].api.* contracts[]                  | object  | A single API contract configuration.                          | true     |
| *apimanager.environment[].api.contracts[].* application      | object  | The client application associated with the contract           | true     |
| *apimanager.environment[].api.contracts[].application.* name | string  | The client application name                                   | true     |
| *apimanager.environment[].api.contracts[].* status           | string  | The contract status APPROVED/REVOKED                          | true     |
| *apimanager.environment[].api.contracts[].* tier             | object  | *Optional* An explanation about the purpose of this instance. | false    |
| *apimanager.environment[].api.contracts[].tier.* name        | string  | The name of the tier for the contract                         | true     |

#### Resources Schema

The api configuration is splitted in different sections:

- Properties
- Alerts
- Tiers
- Policies
- Contracts

The resources above are mapped with the corresponding resources in teh API Manager API.

#### Skipping resources

To skip the processing for one resource omit the corrisponding key in the configuration file.
In the example below alerts and contracts will not be processed because are not defined.

```yaml
---
# Omitted definitions
# ...
        properties:
          assetVersion: 1.0.4
          productVersion: v1
          endpoint:
            uri: https://dev.mariocairone.com/api-hello-world/v1
            muleVersion4OrAbove: true
          instanceLabel: "Dev1"
        tiers: 
          - *StandardTier
          - *LimitedTier
          - *InternalTier
        policies:
          - *ClientIdEnforcementPolicy
          - *JsonThreadProtectionPolicy          
# ...          
```
Please note the difference between a not defined keys and and empty keys
In the example below the alerts are defined as an empty array.
In this case the proglin will process the alerts and remove all the existing alerts for the api.

```yaml
---
# Omitted definitions
# ...
        properties:
          assetVersion: 1.0.4
          productVersion: v1
          endpoint:
            uri: https://dev.mariocairone.com/api-hello-world/v1
            muleVersion4OrAbove: true
          instanceLabel: "Dev1"
        alerts:  
        tiers: 
          - *StandardTier
          - *LimitedTier
          - *InternalTier
        policies:
          - *ClientIdEnforcementPolicy
          - *JsonThreadProtectionPolicy   
# ...          
```

#### Example

A full example is available [here](doc/apimanager.yml)

```yaml
---
# Omitted definitions
# ...

apimanager:
  environment:
    - name: "Development"
      api:
        id: 15601078
        properties:
          assetVersion: 1.0.4
          productVersion: v1
          endpoint:
            uri: https://dev.mariocairone.com/api-hello-world/v1
            muleVersion4OrAbove: true
          instanceLabel: "Dev1"
        alerts: 
          - *ResponseTimeAlert 
          - *PolicyViolationAlert
        tiers: 
          - *StandardTier
          - *LimitedTier
          - *InternalTier
        policies:
          - *ClientIdEnforcementPolicy
          - *JsonThreadProtectionPolicy          
        contracts:
          - application:
              name: Hello World Client
            tier: 
              name: Standard
            status: APPROVED  
```

## Usage

1- Add the plugin in the project `pom.xml`

2- Create the configuration file

3- run the plugin
```sh
mvn anypoint:apply  -D...
```