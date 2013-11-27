TopicQuestsSupport
==================

Core supporting code for TopicQuests projects

Status: *beta*<br/>
Latest edit: 20131126
## Background ##
These are simply utility routines used throughout TopicQuests projects. The project is relatively stable and has been for a long time, so it is now labeled version 1.0.0

Support is provided for:
- Loading the XML config files with ConfigPullParser
- Various Date utilities
- An LRU cache
- The LoggingPlatform and its Tracer tool
- A TextFileHandler with a variety of useful methods
- A static utility for finding where configuration files are stored; this allows you to put your config files, including logger.properties, into one of: the root directory, or a directory named "config", "conf", or "cfg".

## Changes ##
20131126 improved date handling; version 1.0.1

Added the class ConfigurationHelper and its static method findPath. <br/>
Changed the API to ConfigPullParser (could be build-breaking without paying attention) to now return a Map instead of a Hashtable<br/>
Changed the constructor and static getInstance method in LoggingPlatform to require the name of the logger.properties file. As mentioned above, it can be located in one of several places.<br/>
## ToDo ##
Mavenize the project<br/>
Create a full unit test suite

## License ##
Apache 2
