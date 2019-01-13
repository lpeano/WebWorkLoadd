WebWorkLoad Performance Tool
--------------------------------


Tool to execute work load stress test with differents algorithms.

Avaialbe algorithms are:

	COSTANT_SPEED
	GEOMETRICE ACCELERATION
	
	
It allocate n client threads and a parametrized **ThreadPoolExecutore** on every Client's thread.

Following file is *configuration.properties* example file:

```
#Worker Configs
clientThreadPoolSize=10
frequency=1
#Sizing Worker Pool Size to 
# CorePoolSize (global settings)
# CorePoolSize.n (specific worker size)
CorePoolSize=1000
CorePoolSize.1=100
#Sizing MaxPoolSize Pool Size to (Optimal performance Suggested value MaxPoolSize=CorePoolSize )
# MaxPoolSize (global settings)
# MaxPoolSize.n (specific worker MaxPoolSize size) 
MaxPoolSize=1000
MaxPoolSize.1=100
#PollingAlgorit Polling algorithm type
#Possible value:
#
#		POLLING_FIXED_NUMBER: Fixed Number of work 
#			Requires 
#					HowMany : parameter as number of works for clients and
#					WaitAlgorithm : algorithm establishing frequency variation type
#		POLLING: Unlimited number of work for clients
#			Requires
#					WaitAlgorithm : algorithm establishing frequency variation type
#		ONE_SHOT: One shot execution
PollingAlgorit=MULTIWAVE_PATTERN
#HowMany: Number of polling loop
#HowMany.n: Number of polling loop for specific client
HowMany=1000
#WaitAlgorithm: Frequency variation type
#WaitAlgorithm.n: Specific Client  Frequency variation type
# Possible Values
#
#		GEO_ACCELERATE: Geometric Speed Variation Based On algorithm 
#						Vt=V0*R^(t-1)
#
#			Requires parameters:
#
#				Reason: (R) Reson of geometric series
#				Stepping: Ramping Stepping in millis ( every step is a smal ramp of t setting to 1000 t1-t0=1000)
#				POSITIV: Determine if true acceleration in false deceleration
#				MaxFrequency: Max Frequency of acceleration/deceleration
#
#		COSTANT_SPEED: Constant Speed 
#			Requires parameters:
#				frequency: Speed of calls
#
#		
WaitAlgorithm=GEO_ACCELERATE
#WaitAlgorithm=GEO_ACCELERATE
#Reason
Reason=2
#Stepping
Stepping=1000
#POSITIV
POSITIV=true
MaxFrequency=100
MaxElapsed=200000
MULTIPATTERN_PATTERNS_SEQUENCE=GEO_ACCELERATE GEO_ACCELERATE COSTANT_SPEED

MULTIPATTERN_PATTERNS.GEO_ACCELERATE.1.Elapsed=10000
MULTIPATTERN_PATTERNS.GEO_ACCELERATE.1.frequency=1
MULTIPATTERN_PATTERNS.GEO_ACCELERATE.1.Reason=3
MULTIPATTERN_PATTERNS.GEO_ACCELERATE.1.Stepping=1000
MULTIPATTERN_PATTERNS.GEO_ACCELERATE.1.POSITIV=true
MULTIPATTERN_PATTERNS.GEO_ACCELERATE.1.MaxFrequency=100
MULTIPATTERN_PATTERNS.GEO_ACCELERATE.1.MaxElapsed=5000

MULTIPATTERN_PATTERNS.GEO_ACCELERATE.2.Elapsed=10000
MULTIPATTERN_PATTERNS.GEO_ACCELERATE.2.frequency=10
MULTIPATTERN_PATTERNS.GEO_ACCELERATE.2.Reason=2
MULTIPATTERN_PATTERNS.GEO_ACCELERATE.2.Stepping=1000
MULTIPATTERN_PATTERNS.GEO_ACCELERATE.2.POSITIV=false
MULTIPATTERN_PATTERNS.GEO_ACCELERATE.2.MaxFrequency=2
MULTIPATTERN_PATTERNS.GEO_ACCELERATE.2.MaxElapsed=10000

MULTIPATTERN_PATTERNS.COSTANT_SPEED.3.Elapsed=10000000
MULTIPATTERN_PATTERNS.COSTANT_SPEED.3.frequency=continue

#########################################################
############ Client Requests Configs ####################
#########################################################
#URL : Global URL Pointing
#URL.n: Where n is client number is specific client URL
URL=https://www.tiscali.it
#setMillisConnTimeout: Global Client Connection Timeout
#setMillisConnTimeout.n: Where n is client number i specific client timeout
setMillisConnTimeout=1000
#setMillisSocketTiemout: Global Client Connection socket Timeout
#setMillisSocketTiemout.n: Where n is client number i specific client socket timeout
setMillisSocketTiemout=1000
#setConnectionRequestTimeout: Global Client Connection total request Timeout
#setConnectionRequestTimeout.n: Where n is client number i specific client total request timeout
setConnectionRequestTimeout=7200
```