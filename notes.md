## Local Build
**/notification-backend**$ mvn spring-boot:run
**/app**$ npm run dev
**Backend Server**: Spring Boot application running on http://localhost:8080
**Frontend Server**: Vite development server running on http://localhost:5173

## Public Build
**Deployment**: https://snowsense-j7cw.onrender.com/

## H2-DB
http://localhost:8080/h2-console
**JDBC URL**: jdbc:h2:mem:snowsense
**User Name**: sa
**Password**: (leave empty)

## Avi API
### Region Codes
- `AT-07-14` - Stubai Alps
- `AT-07-16` - Ötztal Alps  
- `AT-07-17` - Silvretta
- `AT-07-08` - Central Kitzbühel Alps
- `IT-32-BZ-*` - South Tyrol regions
- `IT-32-TN-*` - Trentino regions
### Danger Levels
- `low` (1) - Generally safe
- `moderate` (2) - Caution in specific areas
- `considerable` (3) - Dangerous conditions possible
- `high` (4) - Very dangerous
- `very_high` (5) - Extraordinary avalanche situation
### Avalanche Problem Types
- `new_snow` - Fresh snow instability
- `wind_slab` - Wind-transported snow
- `persistent_weak_layers` - Deep persistent weak layers
- `wet_snow` - Wet snow avalanches
- `gliding_snow` - Gliding avalanches
