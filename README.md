# Smart Home Automation Simulator (SHAS)
A prototype for a centralized control over a domestic environment.
![Smart Home Automation Simulator (SHAS)](res/Logo.png)

## Dependencies
 - [JSON Simple](https://code.google.com/archive/p/json-simple/):
   - Required for serializing data.
   - The pre-downloaded JAR file should be available in the `lib` folder.
   - Using Version `1.1.1`.

## Apparatuses
The following appliances or fixtures are available in the automation:
 - Refrigerator

### Refrigerator
The refrigerator is a continuously running appliance. Old refrigerators dating from the 1980's have around 4 cooling cycles. However, according to the analysis by (Issi, 2018), modern refrigerators consume as follows in a 24-hour period:
 - Two defrosting cycles, each lasting 15 minutes using 280 WpH.
 - Intervals of cooling processes lasting over 13 hours (each cycle separated every hour) using 35 WpH.
The refrigerator compressor rests when the refrigeration temperature is below the maximum temperature or while processing a defrost cycle.
