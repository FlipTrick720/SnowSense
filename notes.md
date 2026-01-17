

## Local Build
**/notification-backend**$ mvn spring-boot:run
**/app**$ npm run dev
**/notification-backend**$ mvn test

## APIs
...

## H2-DB
http://localhost:8080/h2-console
**JDBC URL**: jdbc:h2:mem:snowsense
**User Name**: sa
**Password**: (leave empty)

## Render Deployment:
**"New +"** → **"Blueprint"**
auto-detect render.yaml just add:
```bash
# On your local machine
base64 -w 0 firebase-service-account.json
```
Add this to environment variable:
```bash
FIREBASE_SERVICE_ACCOUNT_BASE64=<paste-the-base64-string>
```
ALso add:
```bash
VITE_FIREBASE_API_KEY=your_api_key_here
VITE_FIREBASE_AUTH_DOMAIN=your_project.firebaseapp.com
VITE_FIREBASE_PROJECT_ID=your_project_id
VITE_FIREBASE_STORAGE_BUCKET=your_project.appspot.com
VITE_FIREBASE_MESSAGING_SENDER_ID=your_sender_id
VITE_FIREBASE_APP_ID=your_app_id
VITE_FIREBASE_VAPID_KEY=your_vapid_key
VITE_API_URL=https://snowsense.onrender.com #production URL
```
after commit to develop/deploy-for-render about 10 min i will come up on https://snowsense.onrender.com/.

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


i bekomm notifications die andere läuten gesendet HABEN MIT MEINEM FIREBASE
