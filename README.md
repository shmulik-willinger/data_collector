
Project Goal
---
Network bandwidth utilization is crucial for troubleshooting and capacity planning.

Current solution is to collect 15 minutes history PM via files generated by the EMS. However, this solution lacks the accurate visibility of traffic behavior. Counters are summed in 15 minutes intervals, which flattens the real peaks that may occur in duration of 0.5-1 minute.

Need a consistent, automatic, reliable way to reporting time-depth utilization and performance of large scaled WAN network  

Information should be gathered as raw data telemetry from the network elements and being complied as reports



Design Goals
---
Ease of use with minimal operations – once deployed, the manual intervention to operate the collectors should be reduced to almost zero. Aiming that network changes, addition of services, tuning of monitoring rate, all can be set automatically.

The proprietary behavior of network devices will be kept in the specific plugin in the collectors, separated from the collection method, data model and data sent mechanism.

Keep the Collectors thin with maintaining a minimal RAM. Making sure that information being read from the network devices will be streamed directly to the Analytics warehouse for storage.

Be able to work in unstable network where network devices may be unreachable due to network outage

Have a sustainable solution with easy SW upgrade, fixes and troubleshooting.

System Architecture
---
![](/readme_img/design_1.png)

Management channel:

In charge on the NEs management via the NMS and the collectors

Admin upload/update a list of all the Network NEs (Network, IP, user & pass). Next step is to subscribe to NMS queue for NE changes.
'Central Orchestrator' main service manage the list (or update API request) and sync the data to the lower Network levels (region Collectors)
Each 'region Collector' responsible to manage the regional data in aspects of performance, fault-tolerance and updates.
Every 1/2 minute (can be easily sync by 'Scheduler service' ) the Collector will start a process that will get the NEs list from the Orchestrator and will generate requests to them


Collection channel:

Each Collector service gets a list of NEs from the regional Orchestrator
Collector service prepare snmp request for each NE ,per port
An average of 50 ports per NE
Max of 100 ports per NE
Collector service send the requests to the NE
Sending one by one? / All the requests in a bulk?
NE response with the require PMs
TimeOut set to 30 seconds
No Retry mechanism
Each Collector save the data in the DB (see model draft below) as flat data (no aggregations or calculations for now)


Sync channel:

Sync Service trigger every 5 minutes.
Makes calculation on top of the row data
Calc delta between requests
PM reset cycle
Aggregation when needed
Output the data to main collection DB
Analyzer makes can perform service prediction/ maintanance on the network


Expected amount of rows per hour:
Regional NE * Ports * PMs * TPH = 20 * 50 * 20* 60= 1,200,000 data-rows per hour


Flow diagram
---
![](/readme_img/flow-diagram.png)


Non functional requirements
---
Deployment:

Micro service based environment
Spring based java services
Code reuse - Scheduler service, Security, DB (MariaDB/Mongo) from Paas

Performance:

Network bandwidth utilization is crucial. Performance must be consider cerfully
No retry mechanizm

High Availability:

Need to support High Availability (based on PaaS functionality)
Scale:

The system must be easy to scale, In various use cases:
adding new NEs in the network
enable/disable ports/ services
adding NPT network


Rest API
---
#### Device controller:
ResponseEntity<List<DevicePropertiesRequest>> listAlldevices();

ResponseEntity<?> getDevice(@PathVariable("id") long id);

ResponseEntity<?> createDevice(@RequestBody DevicePropertiesRequest devicePropertiesRequest, UriComponentsBuilder ucBuilder);

ResponseEntity<?> updateDevice(@PathVariable("id") long id, @RequestBody DevicePropertiesRequest devicePropertiesRequest);

ResponseEntity<?> deleteDevice(@PathVariable("id") long id);

ResponseEntity<?> deleteAllDevices();


#### Collectable entity controller:

ResponseEntity<List<CollectableEntityRequest>> listAllCollectableEntities();

ResponseEntity<?> getCollectableEntity(@PathVariable("id") long id);

ResponseEntity<?> createCollectableEntity(@RequestBody CollectableEntityRequest collectableEntityRequest, UriComponentsBuilder ucBuilder);

ResponseEntity<?> updateCollectableEntity(@PathVariable("id") long id, @RequestBody CollectableEntityRequest collectableEntityRequest);

ResponseEntity<?> deleteCollectableEntity(@PathVariable("id") long id);

ResponseEntity<?> deleteAllCollectableEntities();




#### Collector controller:
ResponseEntity<?> collectData();




#### Sync controller:
public void generateSync(long size);




### Examples:

Add Device :

POST http://{IP}:{port}/Collector/device

* port is currently 8090, but will be changed to 443 (https) on deployment
Request example:

{
"ip" : "10.4.61.21",
"port" : "161",
"protocol" : "udp",
"appProtocol" : "snmp",
"type" : "NPT-1200",
"version" : "6.1",
"snmpVersion" : "V2",
"snmpUser" : "",
"snmpPassword" : "",
"snmpPrivacyAlgorithm" : "DES",
"snmpPrivacyPassword" : ""
}

#### Status Codes:

* 200 Ok – Success

* 400 - Bad Request

version not Found
* 403 - Forbidden

Un-authorized request
Insufficient Permission
* 504 - Gateway Timeout (LS is not responding)

Collector service diagram:
---
![](/readme_img/collector_service.jpg)