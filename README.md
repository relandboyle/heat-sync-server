# HeatSync
### Stay warm.

<a href="https://heat-sync.net/" target="_blank">HeatSync</a>

This application is a web client designed to interface with the HeatSync server.

The server collects time-series temperature data from IoT-enabled sensors placed in rent-stabilized apartment units throughout New York City.

It is unfortunately quite common for NYC landlords to deprive their tenants of heat and hot water during the colder months of the year. While tenants may report the condition, the behavior typically goes unpunished. City inspectors arrive on the scene days or weeks after a complaint is lodged, and if they cannot observe a hazardous condition then no violation can be issued.

HeatSync empowers tenants, tenants' associations, and their legal representatives to collect accurate, complete temperature data for any number of units. Reports and charts my be viewed in real-time or exported for use in a legal setting.

The HeatSync server is a Java / Spring Boot application, deployed and hosted at <a href="https://heroku.com" target="_blank">Heroku.com</a>. It makes use of dependencies such as Lombok, Jackson, Caffeine, Log4j, and the Postgres driver among others.
