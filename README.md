# HeatSync
### Stay warm.

<a href="https://heat-sync.net/" target="_blank">HeatSync</a>

This application is a web client designed to interface with the HeatSync server.

The server collects time-series temperature data from IoT-enabled sensors placed in rent-stabilized apartment units throughout New York City.

This systemic failure on the part of HPD and NYC can be partially offset if the tenants of a given building are able to organize and keep accurate records of the temperature in their apartment units throughout winter. However, it is challenging for tenants to execute consistently.

HeatSync addresses the problem by automating the process of collecting consistent, accurate temperature data from any number of apartment units. As buildings and apartment units are onboarded, the application automatically adapts and flexibly reports on the new locations.

The HeatSync Client application allows a tenant to view and export the raw data from their own sensor. Additionally, a lawyer representing a building's tenants may manually onboard a new building or add apartments to the database. At this time, sensor onboarding must be completed in-person by the owner of the UbiBot sensor account.
It is unfortunately quite common for NYC landlords to deprive their tenants of heat and hot water during the colder months of the year. While tenants may report the condition, the behavior typically goes unpunished. City inspectors arrive on the scene days or weeks after a complaint is lodged, and if they cannot observe a hazardous condition then no violation can be issued.

HeatSync empowers tenants, tenants' associations, and their legal representatives to collect accurate, complete temperature data for any number of units. Reports and charts may be viewed in real-time or exported for use in a legal setting.

The HeatSync server is a Java / Spring Boot application, deployed and hosted at <a href="https://heroku.com" target="_blank">Heroku.com</a>. It makes use of dependencies such as Lombok, Jackson, Caffeine, Log4j, and the Postgres driver among others.
