# Anypoint Maven Plugin

The anypoint maven plugin allows to configure the anypoint platform components using a yaml configuration file.

The plugin define a single goal `apply`.

When the `apply` goal run the configuration defined in the yaml file will be applied to the API Manager.

## Why should I use it?

The main advantage of using the plugin is that we can store the configuration file in our version control system.
Doing that we can apply a version control on the API Manager configuration. 

## What can be configured?

The only supported components right now is the API Manager.

The plugin support the configuration of the following API elements:

- Properties
- Alerts
- Tiers
- Policies
- Contracts

## Installation

Add the plugin to the `pom.xml`

```xml
<plugin>
    <groupId>com.mariocairone.mule</groupId>
    <artifactId>anypoint-maven-plugin</artifactId>
    <version>1.0.0.alpha</version>
    <configuration>
        <server>anypoint</server>
        <organization>${organization}</organization>
        <environment>${environment}</environment>
        <configuration>apimanager.yaml</configuration>
    </configuration>
</plugin>		
```

## Plugin Configuration

### Goal
| Name  | Description                                                                              |
| ----- | ---------------------------------------------------------------------------------------- |
| apply | Apply the configuration defined in the YAML configuration file to the Anypoint Platform. |

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


> :eyeglasses: \* one of server or username and password must be configured

## Configuration file

The configuration file is written in `YAML`. 

Using YAML you may represent objects of arbitrary graph-like structures. 

To keep the configuration file clean and readable and improve the reusability the usage of anchors and aliases is encuraged.

This will allow you to define an object on the top of the documents and then refer to the same object from different parts of a document.

### API Manager 

The `apimanager` object is the root of the document and must be defined.

| Name       | Type   | Description                                           | Required |
| ---------- | ------ | ----------------------------------------------------- | -------- |
| apimanager | object | This is the configuration to apply to the API Manager | true     |

#### Environment

The `environment` arrays contains a collection of objects, each representing the configuration for a specific environment.

For each environment the `name` is used to determine the correct environment to process matching the environment name with the environment parameter defined in the plugin configuration

| Name                             | Type   | Description                                              | Required |
| -------------------------------- | ------ | -------------------------------------------------------- | -------- |
| *apimanager.* environment        | array  | A collection of environment configurations               | true     |
| *apimanager.* environment[]      | object | A single environment configuration                       | true     |
| *apimanager.environment[].* name | string | The environment name as defined in the Anypoint Platform | true     |
| *apimanager.environment[].* api  | object | The API configuration                                    | true     |

#### API

The api instance id uset to match the api in the environment.

All the other collection elements are optional and, if omitted, will be skipped from the processing by the plugin.

| Name                                       | Type    | Description                   | Required |
| ------------------------------------------ | ------- | ----------------------------- | -------- |
| *apimanager.environment[].* api            | object  | The API configuration         | true     |
| *apimanager.environment[].api.* id         | integer | The API instance id           | true     |
| *apimanager.environment[].api.* properties | object  | The API properties            | false    |
| *apimanager.environment[].api.* alerts     | array   | A collection of Alerts        | false    |
| *apimanager.environment[].api.* policies   | array   | A collection of API policies. | false    |
| *apimanager.environment[].api.* tiers      | array   | A collection of API tiers     | false    |
| *apimanager.environment[].api.* contracts  | array   | A collection of API contracts | false    |


> :warning: **If you define an empty collection all the existing resources will be deleted**: Be very careful here!

##### Properties

The `properties` is an object that contains the api configuration.
Any instance of data is allowed.

| Name                                       | Type   | Description        | Required |
| ------------------------------------------ | ------ | ------------------ | -------- |
| *apimanager.environment[].api.* properties | object | The API properties | false    |

The plugin will convert the `properties` yaml object into JSON and use it as body to :

| Method  | Resource                                                                                                 |
| ------- | -------------------------------------------------------------------------------------------------------- |
| `PATCH` | `/apimanager/api/v1/organizations/{organizationId}/environments/{environmentId}/apis/{environmentApiId}` |

for more details please refer to the [Anypoint Manager API - Documentation](https://anypoint.mulesoft.com/exchange/portals/anypoint-platform/f1e97bc6-315a-4490-82a7-23abe036327a.anypoint-platform/api-manager-api/minor/1.0/pages/home/)


##### Alerts

The `alerts` is a collection of alert objects.

| Name                                          | Type   | Description                   | Required |
| --------------------------------------------- | ------ | ----------------------------- | -------- |
| *apimanager.environment[].api.* alerts        | array  | A collection of Alerts        | false    |
| *apimanager.environment[].api.* alerts[]      | object | A single Alert configuration. | true     |
| *apimanager.environment[].api.alerts[].* name | string | The name of the Alert         | true     |


> :eyeglasses: **If you dont't want the plugin to manage the alerts you have to omit the `alerts:` array**!


> :warning: **If you define an empty collection all the existing alerts will be deleted**: Be very careful here!

When the plugin process the alerts it will:
 - created the alerts configured if they do not exist in the API MAnager
 - updated the alerts configured if they exist in the API Manager and changes are detected
 - removed the alerts if they exist in the API Manager but are not configured

The `name` of the alert is used as key to determine if it already exists and must be specified.

For the abject in the collection any instance of data is allowed.
Each object is converted to JSON and used in the following calls:

 | Method  | Resource                                                                                                   |
 | ------- | ---------------------------------------------------------------------------------------------------------- |
 | `POST`  | `/apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/alerts`          |
 | `PATCH` | `/apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/alerts/:alertId` |

for more details please refer to the [Anypoint Manager API - Documentation](https://anypoint.mulesoft.com/exchange/portals/anypoint-platform/f1e97bc6-315a-4490-82a7-23abe036327a.anypoint-platform/api-manager-api/minor/1.0/pages/home/)


##### Tiers

The `tiers` is a collection of alert objects.

| Name                                          | Type   | Description                   | Required |
| --------------------------------------------- | ------ | ----------------------------- | -------- |
| *apimanager.environment[].api.* tiers        | array  | A collection of Tiers        | false    |
| *apimanager.environment[].api.* tiers[]      | object | A single Tier configuration. | true     |
| *apimanager.environment[].api.tiers[].* name | string | The name of the Tier         | true     |

> :eyeglasses: **If you dont't want the plugin to manage the alerts you have to omit the `tiers:` array**!

> :warning: **If you define an empty collection all the existing tiers will be deleted**: Be very careful here!

When the plugin process the tiers it will:
 - created the tiers configured if they do not exist in the API MAnager
 - updated the tiers configured if they exist in the API Manager and changes are detected
 - removed the tiers if they exist in the API Manager but are not configured

The `name` of the tier is used as key to determine if it already exists and must be specified.

For the abject in the collection any instance of data is allowed.
Each object is converted to JSON and used in the following calls:

 | Method  | Resource                                                                                                   |
 | ------- | ---------------------------------------------------------------------------------------------------------- |
 | `POST`  | `/apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/tiers`          |
 | `PATCH` | `/apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/alerts/:tierId` |

for more details please refer to the [Anypoint Manager API - Documentation](https://anypoint.mulesoft.com/exchange/portals/anypoint-platform/f1e97bc6-315a-4490-82a7-23abe036327a.anypoint-platform/api-manager-api/minor/1.0/pages/home/)

##### Policies

The `policies` is a collection of policy object.

| Name                                                         | Type   | Description                   | Required |
| ------------------------------------------------------------ | ------ | ----------------------------- | -------- |
| *apimanager.environment[].api.* policies                     | array  | A collection of API policies. | false    |
| *apimanager.environment[].api.* policies[]                   | object | A single policy configuration | true     |
| *apimanager.environment[].api.policies[].* configurationData | object | The policy configuration data | true     |

> :eyeglasses: **If you dont't want the plugin to manage the policies you have to omit the `policies:` array**!

> :warning: **If you define an empty collection all the existing policies will be deleted**: Be very careful here!

When the plugin process the policies it will:
 - created the policies configured if they do not exist in the API MAnager
 - updated the policies configured if they exist in the API Manager and changes are detected
 - removed the policies if they exist in the API Manager but are not configured

For Mule 4 APIs the `assetId` of the policy is used as key to determine if it already exists and must be specified, for Mule 3 APIs the `policiTemplateId` is used instead. 

For the abject in the collection any instance of data is allowed.
Each object is converted to JSON and used in the following calls:

| Method  | Resource                                                                                                   |
 | ------- | ---------------------------------------------------------------------------------------------------------- |
 | `POST`  | `/apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/policies`          |
 | `PATCH` | `/apimanager/api/v1/organizations/:organizationId/environments/:environmentId/apis/:apiId/policies/:policyId` |

for more details please refer to the [Anypoint Manager API - Documentation](https://anypoint.mulesoft.com/exchange/portals/anypoint-platform/f1e97bc6-315a-4490-82a7-23abe036327a.anypoint-platform/api-manager-api/minor/1.0/pages/home/)


##### Contract

The `contracts` is a collection of contract object.

The `name` of the `application` is used as key to determine if it already exists and must be specified.

before configuring a contract please make sure:
- The client application alreaddy exists, the plugin will not create the client application.
- The SLA tier, if configured, exists or is defined in the `tiers` section 

| Name                                                         | Type   | Description                                         | Required |
| ------------------------------------------------------------ | ------ | --------------------------------------------------- | -------- |
| *apimanager.environment[].api.* contracts                    | array  | A collection of API contracts                       | false    |
| *apimanager.environment[].api.* contracts[]                  | object | A single API contract configuration.                | true     |
| *apimanager.environment[].api.contracts[].* application      | object | The client application associated with the contract | true     |
| *apimanager.environment[].api.contracts[].application.* name | string | The client application name                         | true     |
| *apimanager.environment[].api.contracts[].* status           | string | The contract status APPROVED/REVOKED                | true     |
| *apimanager.environment[].api.contracts[].* tier             | object | The tier to associate to the Contract               | false    |
| *apimanager.environment[].api.contracts[].tier.* name        | string | The name of the tier for the contract               | true     |

> :eyeglasses: **If you dont't want the plugin to manage the contracts you have to omit the `contracts:` array**!

> :warning: **If you define an empty collection all the existing contracts will be deleted**: Be very careful here!

When the plugin process the contracts it will:
 - created the contracts configured if they do not exist in the API MAnager
 - updated the contracts configured if they exist in the API Manager and changes are detected
 - removed the contracts if they exist in the API Manager but are not configured

#### Configuration File Example

A full example is available [here](doc/apimanager.yml)

```yaml
---
definitions:
  tiers: 
  
    - &StandardTier
      name: Standard 
      description: Standard
      limits:
      - visible: true
        maximumRequests: 20
        timePeriodInMilliseconds: 60000
      - visible: true
        maximumRequests: 5
        timePeriodInMilliseconds: 1000
      status: ACTIVE
      autoApprove: true
      
    - &InternalTier
      name: Internal Use Only
      description: Internal Use Only
      limits:
      - visible: true
        maximumRequests: 10000
        timePeriodInMilliseconds: 1000
      status: ACTIVE
      autoApprove: false
      
    - &LimitedTier
      name: Limited
      description: Limited
      limits:
      - visible: true
        maximumRequests: 3
        timePeriodInMilliseconds: 60000
      - visible: true
        maximumRequests: 20
        timePeriodInMilliseconds: 3600000
      status: ACTIVE
      autoApprove: false
  
  policies: 
 
    - &ClientIdEnforcementPolicy
      groupId: 68ef9520-24e9-4cf2-b2f5-620025690913
      assetId: client-id-enforcement
      assetVersion: 1.2.1
      configurationData:
        credentialsOriginHasHttpBasicAuthenticationHeader: customExpression
        clientIdExpression: "#[attributes.headers['client_id']]"
        clientSecretExpression: "#[attributes.headers['client_secret']]"  
        
    - &JsonThreadProtectionPolicy
      groupId: 68ef9520-24e9-4cf2-b2f5-620025690913
      assetId: json-threat-protection
      assetVersion: 1.1.3
      configurationData:
        maxContainerDepth: 10
        maxStringValueLength: 1000
        maxObjectEntryNameLength: 255
        maxObjectEntryCount: 1000
        maxArrayElementCount: 1000

        
  alerts:
  
    - &PolicyViolationAlert
      name: API HELLO WORLD Policy Violation Alert
      type: api-policy-violation
      enabled: true
      severity: Critical
      recipients:
      - type: email
        value: 246fefce.addisonlee.com@emea.teams.ms
      condition:
        resourceType: api
        aggregate: COUNT
        operator: GREATER_THAN
        value: 2
      period:
        duration:
          count: 1
          weight: MINUTES
        repeat: 3
      policyId: 437790
      policyTemplate:
        id: client-id-enforcement
        name: Client ID enforcement
        
    - &ResponseTimeAlert
      name: API HELLO WORLD Response Time Alert
      type: api-response-time
      enabled: true
      severity: Warning
      recipients:
      - type: email
        value: 246fefce.addisonlee.com@emea.teams.ms
      condition:
        resourceType: api
        aggregate: COUNT
        operator: GREATER_THAN
        value: 1
        filter:
        - property: responseTime
          operator: GREATER_THAN
          values:
            - 10000
      period:
        duration:
          count: 1
          weight: MINUTES
        repeat: 3   
        
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

## Limitation 

- Policy reorder not yet supported
- Contract tier can be changed only if status is APPROVED 