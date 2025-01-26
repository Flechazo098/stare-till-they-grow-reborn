# StareTillTheyGrow Changelog-en_us

## Version 3.0.0 (MC 1.20.1-47.3.0)

### Major Changes:
- Updated to Minecraft 1.20.1 and Forge 47.3.0
- Completely refactored codebase for better maintainability
- Added proper logging system using SLF4J
- Improved network packet handling
- **Added experimental "Staring at villagers" feature**: Staring at villagers and wandering traders will cause them to restock. The first restock happens immediately, and subsequent restocks require waiting for 1 minute before staring again. *Note: This feature is still experimental and not fully stable.*

### Optimizations:
- Enhanced code structure with better JavaDoc documentation
- Improved event handling system
- Added debug logging for better development support
- Optimized mod initialization process

### Bug Fixes:
- Fixed potential memory leaks in network handling
- Improved error handling and logging
- Fixed configuration loading issues

### Contributors
- Flechazo（Main developer）